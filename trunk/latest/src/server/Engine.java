package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TimerTask;

import shared.AllMap;
import shared.GLOBAL_VARIABLE;
import shared.NewTurn;
import shared.Oggetto2D;
import shared.PhysicWorld;
import shared.TurnDuration;
import shared.azioni.Action;
import shared.azioni.ShipRequest;

public class Engine extends TimerTask{

	private static final int TIMEOUT_LOGIN = 1000;
	private static final int MAX_TOTAL_PLAYER = 20;
	
	PortListener serverMain;
	
	static final int MAX_NEW = 1;
	LinkedList<Player> unloggedPlayer = new LinkedList<Player>();
	ArrayList<Player> observerPlayer = new ArrayList<Player>();

	ArrayList<Player> players = new ArrayList<Player>();
	
	LinkedList<Player> newObserver = new LinkedList<Player>();
	LinkedList<Player> removedObserver = new LinkedList<Player>();
	
	//long actualTurn;
	
	PhysicWorld world = new PhysicWorld();

	//HashMap<Integer, Oggetto2D> allOggetto2D = new HashMap<Integer, Oggetto2D>();
	//ArrayList<Oggetto2D> newOggetti2D = new ArrayList<Oggetto2D>();
	
	
	LinkedList<Action> allChanges = new LinkedList<Action>();
	
	
	
	
	EngineListener collListener = new EngineListener();
	
	public Engine(PortListener serverMain) {
		this.serverMain = serverMain;
		
		world.setCollisionListener(collListener);
		
		//put 10 obj in world
		Oggetto2D t;
		for (int i=0; i < 10; i++){
			for (int a=0; a < 10; a++){
				t = new Oggetto2D(world.getNextIndex());
				world.addNew( t, GLOBAL_VARIABLE.convertToPhysicEngineUnit( i*10 ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( a*10 ), 0 );
				
				/*
				Action az = new ActionEngine(t.ID, GLOBAL_VARIABLE.convertToPhysicEngineUnit( (float)Math.random()*6-3 ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( (float)Math.random()*6-3 ), 0);
				az.run(t, world);
				allChanges.add(az);
				*/
			}
		}
	}

	/*
	 * HOW ENGINE WORKS:
	 * 1. READ THE LOGIN
	 * 2. LOG AS OBSERVER, and send him all the object. for debug purpose the player will also get all the action in world
	 * 3. TODO: LOG AS PLAYER, if the observer request a ship
	 */
	
	@Override
	public void run() { //new turn
		long time = System.currentTimeMillis();
		
		System.out.println( "Turn "+world.actualTurn);
		
		/*
		 * DEBUG PURPOISE
		 */
		/*
		
		if (actualTurn>=2000){
			if ( actualTurn%500==0 ){//&& allOggetto2D.get(0).getBody().getLinearVelocity().y==0){
				for (int i=0; i < 50; i++){
					Oggetto2D t=allOggetto2D.get(i);
					Action a = new ActionEngine(t.ID, GLOBAL_VARIABLE.convertToPhysicEngineUnit( (float)Math.random()*6-3 ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( (float)Math.random()*6-3 ), 0);
					a.run(t);
					allChanges.add(a);
				}
			}
		}
		*/
		
		{
			//accept MAX_NEW new client
			acceptNewPlayer();
			
			//step
			world.update();
			
			//read and execute new action
			updatePlayer();
			
			//write the executed action and collision
			writeNewTurn();
			
			//every 1000 turn send all maps, otherwise send NewTurn
			if (world.actualTurn%1000==0)
				writeAllMaps();
			
			//add the new observer to observerPlayer
			observerPlayer.addAll(newObserver);
			newObserver.clear();
			
			//clear actual turn actions
			allChanges.clear();
		}
		
		time = System.currentTimeMillis() - time;
		if ( time > ServerMain.TURN_DURATION ){
			System.out.println( "WARNING: engine turn take more execution time than requested!!! This can cause serius error/data corruption!! duration: "+time );
			System.out.println( "unlogged player: "+unloggedPlayer.size()+" observer Player: "+observerPlayer.size()+" new observer:"+newObserver.size()+" players: "+players.size() );
		}
	}

	private void writeAllMaps() {
		AllMap n = new AllMap(world.actualTurn);
		
		
		for ( Oggetto2D t:world.getOggetti() ){
			n.add( t.getInfoPosition() );
		}
		
		for (Player p:observerPlayer){
			p.write(n);
		}
		
		//re-elaborate to send only radar map
		for (Player p:players){
			p.write(n);
		}
	}

	private void writeNewTurn() {
		//add all map to new observer NewTurn
		NewTurn nNewObserver = new NewTurn(world.actualTurn);
		
		for ( Oggetto2D t:world.getOggetti() ){
			nNewObserver.add( t, t.getInfoPosition() );
		}
		
		//add all new object to observer and new observer NewTurn
		NewTurn n = new NewTurn(world.actualTurn);
		for ( Oggetto2D t:world.getNewOggetti() ){
			nNewObserver.add( t, t.getInfoPosition() );
			n.add( t, t.getInfoPosition() );
		}
		
		//add all changes to observer and new observer
		nNewObserver.addAll( allChanges );
		n.addAll( allChanges );
		
		//send new turn if there are actions or new object
		if (n.actionsSize() != 0 || n.newObjSize() != 0){
		
			//send NewTurn to all observer, if there are changes
			for (Player p:observerPlayer){
				p.write(n);
			}
			
			///* THIS IS NOT NECESSARY, PLAYER USE RADAR OF THEIR SHIP
			//send NewTurn to all player
			for (Player p:players){
				p.write(n);
			}
			//*/
		}
		
		//send NewTurn + allWorld to all new observer
		for (Player p:newObserver){
			p.write(nNewObserver);
		}
		
	}

	private void updatePlayer() {
		Player tP;
		
		/*
		 * control if the login is arrived for the unlogged player.
		 */
		for (int i =0; i < unloggedPlayer.size(); i++){
			tP = unloggedPlayer.poll();
			if ( !tP.isClosed() ){
				tP.update(world, allChanges);
				if (tP.getLogin() == null){
					if (tP.getUpdate() < TIMEOUT_LOGIN  ){
						unloggedPlayer.add(tP);
					}else{
						System.out.println( "Timeout for login: "+tP.getIP() );
						tP.close();
					}
				}else{
					System.out.println( "new player logged: "+tP.myself );
					tP.write( new TurnDuration(ServerMain.TURN_DURATION, world.actualTurn) );
					newObserver.add(tP);
				}
			}else{
				System.out.println( "Connection lost: "+tP.getIP() );
				tP.close();
			}
		}
		
		/*
		 * control if a ship request is arrived for the unlogged player.
		 */
		ShipRequest objToCreate;
		for (Player t:observerPlayer){
			if ( t.isClosed() ){
				t.close();
				removedObserver.add(t);
			}else{
				t.update(world, allChanges);
				if ( (objToCreate=t.getCreateShip()) != null ){
					System.out.println("creating ship1");
					removedObserver.add(t);
					if (objToCreate.shipOwnerID == -1){
						objToCreate.run(world, t);
						players.add(t);
					}else{
						System.out.println( "Error observer request an existing ship, disconnecting! "+t.getLogin() );
						t.close();
					}
				}
			}
		}
		// remove disconnected or player from observer
		observerPlayer.removeAll(removedObserver);
		
		
		/*
		 * control if action requests is arrived for the player and execute it. Also delete disconnected player
		 */
		LinkedList<Player> removedPlayer = new LinkedList<Player>();
		LinkedList<Action> tempAct;
		for (Player t:players){
			if ( t.isClosed() ){
				t.close();
				removedPlayer.add(t);
			}else{
				t.update(world, allChanges);
				tempAct = t.getMyActions();
				for (Action tA:tempAct){
					if ( tA.run(world, t) ){
						allChanges.add(tA);
					}else{
						System.out.println("Error executing request ation, disconnecting!");
						t.close();
					}
				}
			}
		}
		players.removeAll(removedPlayer);
		
		//System.out.println("");
	}

	/*
	 * get some player from the server pool
	 */
	private void acceptNewPlayer() {
		if ( getTotalConnectedPlayerSize() < MAX_TOTAL_PLAYER ){
			Player tP;
			tP = serverMain.poll();
			if (tP!=null)
				unloggedPlayer.add(tP);
		}
	}
	
	private int getTotalConnectedPlayerSize(){
		return unloggedPlayer.size()+observerPlayer.size()+newObserver.size()+players.size();
	}

}
