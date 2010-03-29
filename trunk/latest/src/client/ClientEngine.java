package client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimerTask;

import org.jbox2d.common.Vec2;

import com.jme.input.KeyBindingManager;

import client.gameState.GuiAction;
import client.gameState.SetNode;

import shared.AllMap;
import shared.InfoBody;
import shared.NewTurn;
import shared.Oggetto2D;
import shared.PhysicWorld;
import shared.azioni.Action;
import shared.azioni.ActionEngine;
import shared.specialActions.ShipRequest;

public class ClientEngine extends TimerTask{

	ServerListener server;
	
	long MAX_TURN_DURATION;
	
	PhysicWorld world = new PhysicWorld();
	
	PhysicWorld asincroniusWorld = new PhysicWorld();
	
	//START: Actually for debug purpose, take a shot of the world every 100 turn
	HashMap< Long, HashMap<Integer, InfoBody> > mapsAtTurn = new HashMap< Long, HashMap<Integer,InfoBody>>();
	//END
	
	//TreeMap< Long, ArrayList<Action> > myActions = new TreeMap<Long, ArrayList<Action> >();
	
	private InitGraphics gui;
	
	int IDmyShip=-1;
	Oggetto2D myShip;//In the asynchronous world
	
	double turnLag=-1;
	
	public ClientEngine(ServerListener serverListener, long actualTurn, long turnDuration) {
		server = serverListener;
		
		world.actualTurn = actualTurn;
		asincroniusWorld.actualTurn=actualTurn;
		
		MAX_TURN_DURATION = turnDuration;
		
		gui = new InitGraphics();
		
		server.write( new ShipRequest() );
		System.out.println("Ship request send, starting turn:"+actualTurn);
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		
		{
			System.out.println("\n\nClient Sincronous Turn: "+world.actualTurn+" Asinc: "+asincroniusWorld.actualTurn);
			
			if ( server.isClosed() ){
				System.out.println("Server is down, closing");
				close();
			}else{
				//Synchronize box2d with server 
				long time2 = System.nanoTime();
				synchronizePhysic();
				time2 = System.nanoTime()-time2;
				System.out.println( "Physic time: "+time2);
				
				//Send client action at server
				time2 = System.nanoTime();
				sendActions();
				time2 = System.nanoTime()-time2;
				System.out.println( "Send action time: "+time2);
			}
		}
		
		time = System.currentTimeMillis() - time;
		if ( time > MAX_TURN_DURATION ){
			System.out.println( "WARNING: engine turn take more execution time than requested!!! This can cause serius error/data corruption!! ms duration: "+time );
		}
	}

	//CLIENT PURPOISE
	private void copyWorldForPaint() {
		long time2 = System.nanoTime();
		LinkedList<GuiAction> listaAzioniGUI = new LinkedList<GuiAction>();
		for ( Oggetto2D t:asincroniusWorld.getOggetti().values() ){
			listaAzioniGUI.add( new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
		}
		for ( Oggetto2D t:asincroniusWorld.getNewOggetti().values() ){
			listaAzioniGUI.add( new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
		}
		System.out.println( "GUIActions:"+listaAzioniGUI.size() );
		gui.getWorldGUI().setGuiActions(listaAzioniGUI);
		time2 = System.nanoTime()-time2;
		System.out.println( "GUI copy time: "+time2);
	}
	
	//GUI PURPOISE
	private void sendActions() {
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("escape", true)) {
			gui.close();
			System.exit(0);
        }
		
		if (myShip == null)
			return;
		
		float strenght=0, angle=0;
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_up", false)) {
			strenght -= 0.01f;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_down", false)) {
			strenght += 0.01f;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_left", false)) {
			angle += 0.001f;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_right", false)) {
			angle -= 0.001f;
        }
		if (strenght != 0 || angle != 0){
			System.out.println( "Writing action" );
			double x = strenght*Math.cos( myShip.getBody().getAngle() + Math.PI/2);
			double y = strenght*Math.sin( myShip.getBody().getAngle() + Math.PI/2);
			server.write( new ActionEngine(myShip.ID, (float)x, (float)y, angle) );
		}
	}

	//PHYSIC PURPOISE
	private void synchronizePhysic() {
		
		boolean used;
		Object o;
		
		System.out.println( "Waiting input: "+ server.inputSize() );
		long time = System.nanoTime();
		
		LinkedList<NewTurn> newTurnToElaborate = new LinkedList<NewTurn>();
		AllMap lastAllMap = null;
		
		while ( (o=server.poll())!=null ){
			
			used = false;
			
			if (o instanceof ShipRequest){
				used = true;
				IDmyShip = ( (ShipRequest)o ).getID();
				gui.setCameraID(IDmyShip);
				System.out.println( "Using ship: "+ IDmyShip );
			}
			
			if (o instanceof AllMap){
				used = true;
				
				if (lastAllMap==null)
					lastAllMap = (AllMap)o;
				
				newTurnToElaborate.clear();
				System.out.println( "AllMap received" );
			}
			
			if (o instanceof NewTurn){
				used = true;
				newTurnToElaborate.add( (NewTurn)o );
				System.out.println( "New turn received");
			}
			
			if (!used){
				System.out.println("Wrong packet!: "+o);
				close();
			}

		}
		
		time = System.nanoTime()-time;
		System.out.println( "Reading input time: "+time);
		
		time = System.nanoTime();
		boolean synchronousChanged = false;
		long asincTurn = asincroniusWorld.actualTurn;
		
		if (lastAllMap!=null){
			//we have to rebuild the synchronous world
			synchronousChanged = true;
			
			if (lastAllMap.turn < world.actualTurn){
				System.out.println("Wrong allmap");
				close();
			}
			
			testWorldPrecision(lastAllMap);
			
			rebuildWorld(lastAllMap);
			
			System.out.println( "Rebuilding world from turn: "+lastAllMap.turn );
			System.out.println( "NewTurn to elaborate: "+newTurnToElaborate.size() );
			
			if (turnLag==-1)
				turnLag = asincTurn-lastAllMap.turn;
			else
				turnLag = (turnLag+asincTurn-lastAllMap.turn)/2;
			System.out.println( "Rilevated turn LAG: "+turnLag+" to ms: "+(turnLag*MAX_TURN_DURATION) );
		}
		if (newTurnToElaborate.size()>0){
			//execute NewTurn on synchronous world
			synchronousChanged = true;
			for (NewTurn t:newTurnToElaborate){
				if (t.actualTurn<world.actualTurn){
					System.out.println("Wrong NewTurn");
					close();
				}
				updateWorld(t);
			}
		}
		
		time = System.nanoTime()-time;
		System.out.println( "Updating syncronous time: "+time);
		
		time = System.nanoTime();
		if (synchronousChanged){
			rebuildAsichronousWorld();
		}
		time = System.nanoTime()-time;
		System.out.println( "Rebuilding asyncronous time: "+time);
		
		time = System.nanoTime();
		
		
		if (asincTurn<world.actualTurn){
			System.out.println( "Asinc turn was less than sync turn:"+asincTurn+" "+ world.actualTurn);
			asincTurn = world.actualTurn;
		}
		
		updateAsincronousWorld(asincTurn);
		
		time = System.nanoTime()-time;
		System.out.println( "Update asyncronous time: "+time);
		
		time = System.nanoTime();
		copyWorldForPaint();
		time = System.nanoTime()-time;
		System.out.println( "Paint copy time: "+time);

	}

	private void rebuildWorld(AllMap lastAllMap) {
		System.out.println( "I'm rebuilding the synchronous world!! Was "+world.actualTurn +" will be "+lastAllMap.turn );
		world.clear();
		InfoBody o;
		Oggetto2D nuovo;
		while ( (o=lastAllMap.poll()) != null){
			System.out.println( "Creating id: "+o.ID);
			nuovo = world.addNew( o.getOggetto2D(), o.getPos().x, o.getPos().y, o.getAngle() );
			nuovo.setInfoPosition(o);
			if (o.compare(nuovo.getInfoPosition())!=0){
				System.out.println( "Error adjusting world");
				close();
			}
		}
		world.actualTurn = lastAllMap.turn;
	}
	
	private void rebuildAsichronousWorld() {
		System.out.println( "I'm rebuilding the asynchronous world!! Was "+asincroniusWorld.actualTurn +" will be "+world.actualTurn );
		asincroniusWorld.clear();
		
		Oggetto2D tempCopy;
		for (Oggetto2D o:world.getOggetti().values()){
			tempCopy = asincroniusWorld.addCopy(o, o.getInfoPosition().getPos().x, o.getInfoPosition().getPos().y);
			tempCopy.setInfoPosition( o.getInfoPosition() );
			System.out.println( tempCopy.getInfoPosition() );
			if (IDmyShip==tempCopy.ID)
				myShip = tempCopy;
		}
		for ( Oggetto2D o:world.getNewOggetti().values() ){
			tempCopy = asincroniusWorld.addCopy(o, o.getInfoPosition().getPos().x, o.getInfoPosition().getPos().y);
			tempCopy.setInfoPosition( o.getInfoPosition() );
			System.out.println( tempCopy.getInfoPosition() );
			if (IDmyShip==tempCopy.ID)
				myShip = tempCopy;
		}
		
		asincroniusWorld.actualTurn = world.actualTurn;
	}

	private void updateWorld(NewTurn t) {
		System.out.println( "I'm updating the synchronous world!! Was "+world.actualTurn +" will be "+t.actualTurn );
		
		while (world.actualTurn<t.actualTurn){
			world.update();
		}
		Oggetto2D newObj;
		InfoBody newObjPos;
		Action newAct;
		
		//add the new obj and set their position
		while ( (newObj=t.pollNewObj())!=null ){
			newObjPos = t.pollPosObj();
			world.addNew(newObj, newObjPos.getPos().x, newObjPos.getPos().y, newObjPos.getAngle() );
			newObj.setInfoPosition(newObjPos);
			System.out.println( "created object:"+newObj.getInfoPosition().compare(newObjPos)+" ID "+newObj.ID );
			if (newObj.getInfoPosition().compare(newObjPos) != 0){
				System.out.println( "Error creation isn't perfect");
				System.exit(0);
			}
			
		}
	
		//set the actions
		while ( (newAct=t.pollActions())!=null ){
			newAct.run( world.get(newAct.ID) );
			System.out.println( "action setted for object:"+newAct.ID+" "+world.get(newAct.ID).getInfoPosition() );
		}
		
		
		//set the collision
		while ( (newObjPos=t.pollCollision())!=null ){
			if ( world.get(newObjPos.ID)!=null )
				world.get(newObjPos.ID).setInfoPosition(newObjPos);
			else{
				System.out.println("Error: cannot find "+newObjPos.ID);
				close();
			}
		}
		
		//world.update();
		
	}

	private void updateAsincronousWorld(long asincTurn) {
		System.out.println( "I'm updating the asynchronous world!! Was "+asincroniusWorld.actualTurn +" will be "+asincTurn );
		while (asincroniusWorld.actualTurn < asincTurn){
			//update world
			asincroniusWorld.update();
		
			//execute client actions
			/*
			ArrayList<Action> actions = myActions.get(asincroniusWorld.actualTurn);
			for( Action a:actions ){
				a.run( asincroniusWorld.get(a.ID) );
			}
			*/
		}
	}

	private void close() {
		System.out.println( "Closing...");
		server.close();
		gui.close();
		System.out.println( "...closing done");
		System.exit(0);
	}
	

	private void testWorldPrecision(AllMap lastAllMap) {
		System.out.println("Testin precision of map");
		rebuildAsichronousWorld();
		if (lastAllMap.turn > world.actualTurn){
			System.out.println( "test require "+(lastAllMap.turn-world.actualTurn)+" step" );
			updateAsincronousWorld(lastAllMap.turn-1);
		}
		LinkedList<InfoBody> ris = new LinkedList<InfoBody>();
		InfoBody a;
		while ( (a=lastAllMap.poll())!=null ){
			ris.add(a);
			if ( asincroniusWorld.get(a.ID)!=null ){
				System.out.println( a.ID+" error: "+ asincroniusWorld.get(a.ID).getInfoPosition().compare(a) );
				if ( asincroniusWorld.get(a.ID).getInfoPosition().compare(a)!=0 ){
					System.out.println( "has to be: "+a+"\nis "+asincroniusWorld.get(a.ID).getInfoPosition() );
					close();
				}
			}else{
				System.out.println( a.ID+" dosn't exist!" );
				//close();
			}
		}
		while ( (a=ris.poll())!=null ){
			lastAllMap.add(a);
		}
	}

}
