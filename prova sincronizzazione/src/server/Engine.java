package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimerTask;

import shared.AllMap;
import shared.NewTurn;
import shared.Oggetto2D;
import shared.PhysicWorld;
import shared.TurnDuration;
import shared.azioni.Action;
import shared.azioni.ActionEngine;

public class Engine extends TimerTask{

	private static final int TIMEOUT_LOGIN = 1000;
	private static final int MAX_TOTAL_PLAYER = 20;
	
	PortListener serverMain;
	
	static final int MAX_NEW = 1;
	LinkedList<Player> unloggedPlayer = new LinkedList<Player>();
	ArrayList<Player> observerPlayer = new ArrayList<Player>();

	LinkedList<Player> newObserver = new LinkedList<Player>();
	LinkedList<Player> removedObserver = new LinkedList<Player>();
	
	long actualTurn;
	
	PhysicWorld world = new PhysicWorld();
	HashMap<Integer, Oggetto2D> allOggetto2D = new HashMap<Integer, Oggetto2D>();
	ArrayList<Oggetto2D> newOggetti2D = new ArrayList<Oggetto2D>();
	LinkedList<Action> allChanges = new LinkedList<Action>();
	int objIndex;
	
	public Engine(PortListener serverMain) {
		this.serverMain = serverMain;
		
		//put 10 obj in world
		Oggetto2D t;
		for (int i=0; i < 10; i++){
			t = new Oggetto2D(objIndex++, allChanges);
			allOggetto2D.put(t.ID, t);
			world.addNew( t, i*10, 0, 0 );
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
		
		/*
		 * DEBUG PURPOISE
		 */
		/*
		int r = (int)(Math.random()*100);
		for (int i=0; i < r; i++){
			allChanges.add( new ActionEngine(-1, 0, 0, 0) );
		}
		*/
		actualTurn++;
		
		if (actualTurn==100 ){//&& allOggetto2D.get(0).getBody().getLinearVelocity().y==0){
			Oggetto2D t=allOggetto2D.get(0);
			Action a = new ActionEngine(t.ID, 1, 0, 0.01f);
			a.run(allOggetto2D.get(0));
			allChanges.add(a);
		}
		
		{
			
		
			//accept MAX_NEW new client
			acceptNewPlayer();
		
			//read and execute new action
			updatePlayer();
			
			//send new turn
			writeNewTurn();
			
			//step
			world.update();
			
			//every 100 turn send all maps, actually for debug purpoise
			if (actualTurn%100==0)
				writeAllMaps();
		}
		
		time = System.currentTimeMillis() - time;
		if ( time > ServerMain.TURN_DURATION ){
			System.out.println( "WARNING: engine turn take more execution time than requested!!! This can cause serius error/data corruption!! duration: "+time );
			System.out.println( "unlogged player: "+unloggedPlayer.size()+" observer Player: "+observerPlayer.size() );
		}
	}

	private void writeAllMaps() {
		AllMap n = new AllMap(actualTurn);
		for ( Oggetto2D t:allOggetto2D.values() ){
			n.add( t.getInfoPosition() );
		}
		
		for (Player p:observerPlayer){
			p.write(n);
		}
	}

	private void writeNewTurn() {
		//add all map to new observer NewTurn
		NewTurn nNewObserver = new NewTurn(actualTurn);
		
		for ( Oggetto2D t:allOggetto2D.values() ){
			nNewObserver.add( t, t.getInfoPosition() );
		}
		
		//add all new object to observer and new observer NewTurn
		NewTurn n = new NewTurn(actualTurn);
		for ( Oggetto2D t:newOggetti2D ){
			nNewObserver.add( t, t.getInfoPosition() );
			n.add( t, t.getInfoPosition() );
		}
		
		//add all changes to observer and new observer
		nNewObserver.addAll( allChanges );
		n.addAll( allChanges );
		
		//send NewTurn to all observer, if there are changes
		for (Player p:observerPlayer){
			if (n.actionsSize() != 0 || n.newObjSize() != 0 || actualTurn%100==0)
				p.write(n);
		}
		
		//send NewTurn to all new observer
		for (Player p:newObserver){
			p.write(nNewObserver);
		}
		
		//add the new observer to observerPlayer
		observerPlayer.addAll(newObserver);
		newObserver.clear();
		newOggetti2D.clear();
		
		//clear sended actions
		allChanges.clear();
	}

	private void updatePlayer() {
		Player tP;
		
		/*
		 * control if the login is arrived for the unlogged player.
		 */
		for (int i =0; i < unloggedPlayer.size(); i++){
			tP = unloggedPlayer.poll();
			if ( !tP.isClosed() ){
				tP.update();
				if (tP.getLogin() == null){
					if (tP.getUpdate() < TIMEOUT_LOGIN  )
						unloggedPlayer.add(tP);
					else
						tP.close();
				}else{
					System.out.println("new player logged: "+tP.myself);
					tP.write( new TurnDuration(ServerMain.TURN_DURATION, actualTurn) );
					newObserver.add(tP);
				}
			}else{
				tP.close();
			}
		}
		
		/*
		 * control if a ship request is arrived for the unlogged player.
		 */
		for (Player t:observerPlayer){
			if ( t.isClosed() ){
				t.close();
				removedObserver.add(t);
			}else{
				t.update();
				//TODO: if observer request a ship and everything is right, set him as player
			}
		}
		// remove disconnected player
		observerPlayer.removeAll(removedObserver);
		
		/*
		 * TODO: control if action requests is arrived for the player (all handled by update() method.
		 */
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
		//TODO: add player when they will be added
		return unloggedPlayer.size()+observerPlayer.size();
	}

}
