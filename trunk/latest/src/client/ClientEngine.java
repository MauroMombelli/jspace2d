package client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.TreeMap;

import org.jbox2d.common.Vec2;

import com.jme.input.KeyBindingManager;
import com.jme.math.FastMath;

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

	//if you want all the action execute X turn after it has been trigger, put here the value. WARNIG net lag is calculated in addition of this
	private static final long actionsLag = 0;

	ServerListener server;
	
	long MAX_TURN_DURATION;
	
	PhysicWorld world = new PhysicWorld();
	
	PhysicWorld asincroniusWorld = new PhysicWorld();
	
	TreeMap<Integer, SetNode> listaAzioniGUI = new TreeMap<Integer, SetNode>();
	//START: Actually for debug purpose, take a shot of the world every 100 turn
	//HashMap< Long, HashMap<Integer, InfoBody> > mapsAtTurn = new HashMap< Long, HashMap<Integer,InfoBody>>();
	//END
	
	TreeMap< Long, ArrayList<Action> > myActions = new TreeMap<Long, ArrayList<Action> >();
	
	private InitGraphics gui;
	
	int IDmyShip=-1;
	Oggetto2D myShip;//In the asynchronous world
	
	long turnLag=-1;
	
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
			//System.out.println("\n\nClient Sincronous Turn: "+world.actualTurn+" Asinc: "+asincroniusWorld.actualTurn);
			System.out.println("\n\nClient Sincronous Turn: "+world.actualTurn);
			if ( server.isClosed() ){
				System.out.println("Server is down, closing");
				close();
			}else{
				//Synchronize box2d with server 
				long time2 = System.nanoTime();
				synchronizePhysic();
				time2 = System.nanoTime()-time2;
				System.out.println( "Synchronization time: "+time2);
				
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
		}else{
			System.out.println( "engine turn ms duration: "+time );
		}
	}
	
	//GUI PURPOISE
	private void copyWorldForPaint() {
		long time2 = System.nanoTime();
		SetNode temp;
		
		long time = System.nanoTime();
		for ( Oggetto2D t:asincroniusWorld.getOggetti().values() ){
			temp = listaAzioniGUI.get(t.ID);
			if (temp == null){
				listaAzioniGUI.put(t.ID, new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
			}else{
				temp.set( new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle() );
			}
		}
		time = System.nanoTime()-time;
		System.out.println( "GUI copy obj time: "+time);
		
		time = System.nanoTime();
		for ( Oggetto2D t:asincroniusWorld.getNewOggetti().values() ){
			temp = listaAzioniGUI.get(t.ID);
			if (temp == null){
				listaAzioniGUI.put(t.ID, new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
			}else{
				temp.set( new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle() );
			}
		}
		time = System.nanoTime()-time;
		System.out.println( "GUI copy new obj time: "+time);
		
		
		time = System.nanoTime();
		
		LinkedList<GuiAction> ris = new LinkedList<GuiAction>();
		//eventually add here other action
		ris.addAll( listaAzioniGUI.values() );
		gui.getWorldGUI().setGuiActions(ris);
		
		time = System.nanoTime()-time;
		System.out.println( "GUI preparation time: "+time);
		
		time2 = System.nanoTime()-time2;
		System.out.println( "GUI set time: "+time2);
	}
/*
	//GUI PURPOISE
	private void copyWorldForPaint() {
		long time2 = System.nanoTime();
		LinkedList<GuiAction> listaAzioniGUI = new LinkedList<GuiAction>();

		long time = System.nanoTime();
		for ( Oggetto2D t:asincroniusWorld.getOggetti().values() ){
			listaAzioniGUI.add( new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
		}
		time = System.nanoTime()-time;
		System.out.println( "GUI copy obj time: "+time);
		
		time = System.nanoTime();
		for ( Oggetto2D t:asincroniusWorld.getNewOggetti().values() ){
			listaAzioniGUI.add( new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
		}
		time = System.nanoTime()-time;
		System.out.println( "GUI copy new obj time: "+time);
		
		System.out.println( "GUIActions:"+listaAzioniGUI.size() );
		gui.getWorldGUI().setGuiActions(listaAzioniGUI);
		time2 = System.nanoTime()-time2;
		System.out.println( "GUI set time: "+time2);
	}
*/	
	//INPUT PURPOISE
	private void sendActions() {
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("escape", true)) {
			gui.close();
			System.exit(0);
        }
		
		if (myShip == null)
			return;
		
		float strenght=0, angle=0;
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_up", false)) {
			strenght -= 1f;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_down", false)) {
			strenght += 1f;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_left", false)) {
			angle += 1f;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_right", false)) {
			angle -= 1f;
        }
		if (strenght != 0 || angle != 0){
			float x = strenght*FastMath.cos( myShip.getBody().getAngle() + FastMath.PI/2 );
			float y = strenght*FastMath.sin( myShip.getBody().getAngle() + FastMath.PI/2 );
			System.out.println( "Writing action, xV:"+x+" yV:"+y+" aV"+angle );
			executeAction( new ActionEngine(myShip.ID, x, y, angle) );
		}
	}
	
	public void executeAction(Action a){
		System.out.println( "Writing action" );
		server.write(a);
		a.setExecTime(asincroniusWorld.actualTurn+turnLag+actionsLag);
		ArrayList<Action> t=myActions.get( a.getExecTime() );
		if ( t == null ){
			t = new ArrayList<Action>();
			t.add(a);
			myActions.put(a.getExecTime(), t);
		}else
			t.add(a);
	}

	//PHYSIC PURPOISE
	private void synchronizePhysic() {
		
		boolean used;
		Object o;
		
		System.out.println( "Waiting input: "+ server.inputSize() );
		long time = System.nanoTime();
		
		LinkedList<NewTurn> oldTurnToElaborate = new LinkedList<NewTurn>();
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
			
			if (o instanceof NewTurn){
				used = true;
				newTurnToElaborate.add( (NewTurn)o );
				System.out.println( "New turn received");
			}
			
			if (o instanceof AllMap){
				used = true;
				
				lastAllMap = (AllMap)o;
				
				oldTurnToElaborate.addAll(newTurnToElaborate);
				newTurnToElaborate.clear();
				System.out.println( "AllMap received" );
				//break;
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
		boolean allMapArrived = false;
		
		if (oldTurnToElaborate.size()>0){
			//execute NewTurn on synchronous world
			synchronousChanged = true;
			for (NewTurn t:oldTurnToElaborate){
				if (t.actualTurn<world.actualTurn){
					System.out.println("Wrong NewTurn");
					close();
				}
				updateWorld(t);
			}
		}
		time = System.nanoTime()-time;
		System.out.println( "First update syncronous time: "+time);
		
		time = System.nanoTime();
		long asincTurn = asincroniusWorld.actualTurn;
		if (lastAllMap!=null){
			//we have to rebuild the synchronous world
			//synchronousChanged = true;
			allMapArrived = true;
			
			if (lastAllMap.turn < world.actualTurn){
				System.out.println("Wrong allmap: "+lastAllMap.turn+" "+world.actualTurn);
				close();
			}
			
			testWorldPrecision(lastAllMap);
			
			//rebuildWorld(lastAllMap);
			
			//if (turnLag==-1)
				turnLag = asincTurn-world.actualTurn;
			//else
				//turnLag = (turnLag+asincTurn-world.actualTurn)/2;
			
			System.out.println( "Rilevated new turn LAG: "+turnLag+" to ms: "+(turnLag*MAX_TURN_DURATION) );
			
			System.out.println( "Rebuilding world from turn: "+lastAllMap.turn );
			System.out.println( "NewTurn to elaborate: "+newTurnToElaborate.size() );
			
		}
		time = System.nanoTime()-time;
		System.out.println( "TEST syncronized time: "+time);
		
		time = System.nanoTime();
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
		System.out.println( "Second update syncronous time: "+time);
		
		//remove old myAction
		for (long id : myActions.keySet() ){
			if (id <= world.actualTurn)
				myActions.remove(id);
			else
				break;
		}
		
		if (synchronousChanged){
			time = System.nanoTime();
			
			if (!allMapArrived){
				//if (turnLag==-1)
					turnLag = asincTurn-world.actualTurn;
				//else
					//turnLag = (turnLag+asincTurn-world.actualTurn)/2;
			}
			
			System.out.println( "Rilevated action LAG: "+turnLag+" to ms: "+(turnLag*MAX_TURN_DURATION) );
			
			rebuildAsichronousWorld();
			
			time = System.nanoTime()-time;
			System.out.println( "Rebuilding asyncronous time: "+time);
		}
		
		time = System.nanoTime();
		if (asincTurn<world.actualTurn){
			System.out.println( "Asinc turn was less than sync turn:"+asincTurn+" "+ world.actualTurn);
			asincTurn = world.actualTurn;
		}
		
		updateAsincronousWorld(asincTurn+1);
		
		time = System.nanoTime()-time;
		System.out.println( "Update asyncronous time: "+time);
		
		copyWorldForPaint();

	}

	private void rebuildAsichronousWorld() {
		System.out.println( "I'm rebuilding the asynchronous world!! Was "+asincroniusWorld.actualTurn +" will be "+world.actualTurn );
		asincroniusWorld.clear();
		
		Oggetto2D tempCopy;
		//Vec2 pos;
		for (Oggetto2D o:world.getOggetti().values()){
			//pos = o.getInfoPosition().getPos();
			tempCopy = asincroniusWorld.addCopy(o, 0, 0);
			tempCopy.setInfoPosition( o.getInfoPosition() );
			//System.out.println( tempCopy.getInfoPosition() );
			if (IDmyShip==tempCopy.ID)
				myShip = tempCopy;
		}
		for ( Oggetto2D o:world.getNewOggetti().values() ){
			//pos = o.getInfoPosition().getPos();
			tempCopy = asincroniusWorld.addCopy(o, 0, 0);
			tempCopy.setInfoPosition( o.getInfoPosition() );
			//System.out.println( tempCopy.getInfoPosition() );
			if (IDmyShip==tempCopy.ID)
				myShip = tempCopy;
		}
		
		asincroniusWorld.actualTurn = world.actualTurn+1;
	}

	private void updateWorld(long turn) {
		System.out.println( "I'm updating the synchronous world!! Was "+world.actualTurn +" will be "+turn );
		int i=0;
		while (world.actualTurn<turn){
			world.update();
			i++;
		}
		System.out.println( "I've updated the synchronous world!! is "+world.actualTurn +" inter "+i );
	}
	
	private void updateWorld(NewTurn t) {
		System.out.println( "I'm updating the synchronous world!! Was "+world.actualTurn +" will be "+t.actualTurn+" diff:"+(t.actualTurn-world.actualTurn)  );
		
		long time = System.nanoTime();
		
		while (world.actualTurn<t.actualTurn){
			world.update();
		}
		
		time = System.nanoTime() -time;
		System.out.println( "Step time: "+time);
		
		Oggetto2D newObj;
		//InfoBody newObjPos;
		Action newAct;
		//Vec2 pos;
		time = System.nanoTime();
		//add the new obj and set their position
		while ( (newObj=t.pollNewObj())!=null ){
			//newObjPos = t.pollPosObj();
			//pos = newObjPos.getPos();
			world.addNew(newObj);
			newObj.setInfoPosition( t.pollPosObj() );
			/*
			System.out.println( "created object:"+newObj.getInfoPosition().compare(newObjPos)+" ID "+newObj.ID );
			if (newObj.getInfoPosition().compare(newObjPos) != 0){
				System.out.println( "Error creation isn't perfect");
				close();
			}
			*/
			if (IDmyShip==newObj.ID){//executed only at the first creation time
				myShip = newObj;
				//server.write( new ActionEngine(myShip.ID, 1f, -1f, 0) );
			}
		}
		time = System.nanoTime() -time;
		System.out.println( "New obj time: "+time );
		
		time = System.nanoTime();
		//set the actions
		while ( (newAct=t.pollActions())!=null ){
			newAct.run( world.get(newAct.ID) );
			System.out.println( "action setted for object:"+newAct.ID+" at turn:"+t.actualTurn );
			if ( newAct.ID==IDmyShip ){
				ArrayList<Action> arrT = myActions.get( newAct.getExecTime() );
				if (arrT!=null)
					arrT.remove(newAct);
			}
		}
		time = System.nanoTime() -time;
		System.out.println( "Action time: "+time );
		/*
		//set the collision
		while ( (newObjPos=t.pollCollision())!=null ){
			if ( world.get(newObjPos.ID)!=null )
				world.get(newObjPos.ID).setInfoPosition(newObjPos);
			else{
				System.out.println("Error: cannot find "+newObjPos.ID);
				close();
			}
		}
		*/
		//world.update();
		
	}

	private void updateAsincronousWorld(long asincTurn) {
		System.out.println( "I'm updating the asynchronous world!! Was "+asincroniusWorld.actualTurn +" will be "+asincTurn );
		ArrayList<Action> my;
		while (asincroniusWorld.actualTurn < asincTurn){
			//update world
			asincroniusWorld.update();
			
			//execute my actions
			my = myActions.get(asincroniusWorld.actualTurn);
			if (my!= null){//if there are action
				for (Action t: my){
					t.run( asincroniusWorld.get(t.ID) );
					System.out.println( "Asin action setted for object:"+t.ID+" at turn:"+asincroniusWorld.actualTurn );
				}
			}
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
		boolean error = false;
		System.out.println("Testin precision of map");
		
		//rebuildAsichronousWorld();
		
		LinkedList<InfoBody> ris;
		InfoBody a;
		
		//DEBUG at turn
		System.out.println( "DEBUG turn: "+lastAllMap.turn);
		if (lastAllMap.turn > world.actualTurn){
			System.out.println( "test require "+(lastAllMap.turn-world.actualTurn)+" step" );
			updateWorld(lastAllMap.turn);
		}
		ris = new LinkedList<InfoBody>();
		Oggetto2D temp;
		while ( (a=lastAllMap.poll())!=null ){
			ris.add(a);
			temp = world.get(a.ID);
			if ( temp!=null ){
				//System.out.println( a.ID+" error: "+ temp.getInfoPosition().compare(a) );
				if ( temp.getInfoPosition().compare(a)!=0 ){
					if ( temp.getInfoPosition().compare(a)!=0 ){
						temp.setInfoPosition(a);
						System.out.println( "Error ID: "+a.ID+" corrected, was: "+temp.getInfoPosition().compare(a) );
					}else{
						System.out.println( "ID: "+a.ID+" error: "+temp.getInfoPosition().compare(a)+" has to be:\n"+a+" is:\n"+temp.getInfoPosition() );
						error = true;
					}
				}else{
					//System.out.println( "Every little things, gonnna be all right: "+a );
				}
			}else{
				System.out.println( a.ID+" doesn't exist!" );
				//close();
				error = true;
			}
		}
		while ( (a=ris.poll())!=null ){
			lastAllMap.add(a);
		}
		
		//CLOSE if find error
		if (error)
			close();
	}

}
