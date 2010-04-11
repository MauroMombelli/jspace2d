package client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.TreeMap;

import com.jme.input.KeyBindingManager;
import com.jme.math.FastMath;

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
	
	//HashMap<Integer, SetNode> listaAzioniGUI = new HashMap<Integer, SetNode>();
	LinkedList<ClientOggetto2D> allOggetto2D = new LinkedList<ClientOggetto2D>();
	//START: Actually for debug purpose, take a shot of the world every 100 turn
	//HashMap< Long, HashMap<Integer, InfoBody> > mapsAtTurn = new HashMap< Long, HashMap<Integer,InfoBody>>();
	//END
	
	boolean firstCorrection = true;
	
	//SortedMap< Long, ArrayList<Action> > myActions = Collections.synchronizedSortedMap( new TreeMap<Long, ArrayList<Action> >() );
	TreeMap< Long, ArrayList<Action> > myActions = new TreeMap<Long, ArrayList<Action> >();
	long lastMyActionClear;
	
	private InitGraphics gui;
	
	int IDmyShip=-1;
	Oggetto2D myShip;//In the asynchronous world
	
	long turnLag=-1;
	
	public ClientEngine(ServerListener serverListener, long actualTurn, long turnDuration) {
		server = serverListener;
		
		world.actualTurn = actualTurn;
		asincroniusWorld.actualTurn=actualTurn;
		lastMyActionClear = actualTurn;
		
		MAX_TURN_DURATION = turnDuration;
		
		gui = new InitGraphics( allOggetto2D );
		
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
			System.out.println( "WARNING: engine turn take more execution time than requested!!! ms duration: "+time );
		}else{
			System.out.println( "engine turn ms duration: "+time );
		}
	}
	
	//GUI PURPOISE
	private void copyWorldForPaint() {
		long time2 = System.nanoTime();
		
		
		long time = System.nanoTime();
		/*
		SetNode temp;
		for ( Oggetto2D t:asincroniusWorld.getOggetti().values() ){
			//temp = listaAzioniGUI.get(t.ID);
			//if (temp == null){
				listaAzioniGUI.put(t.ID, new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
			//}else{
			//	temp.set( new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle() );
			//}
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
		*/
		
		synchronized (allOggetto2D) {
			for (ClientOggetto2D temp:allOggetto2D)
				temp.update();
		}
		time = System.nanoTime()-time;
		System.out.println( "GUI update obj time: "+time);
		
		/*
		time = System.nanoTime();
		
		LinkedList<GuiAction> ris = new LinkedList<GuiAction>();
		//eventually add here other action
		//ris.addAll( listaAzioniGUI.values() );
		gui.getWorldGUI().setGuiActions(ris);
		
		time = System.nanoTime()-time;
		System.out.println( "GUI preparation time: "+time);
		*/
		
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
		synchronized (myActions) {
			ArrayList<Action> t=myActions.get( a.getExecTime() );
			if ( t == null ){
				t = new ArrayList<Action>();
				t.add(a);
				myActions.put(a.getExecTime(), t);
			}else
				t.add(a);
		}
	}

	//PHYSIC PURPOISE
	private void synchronizePhysic() {
		
		boolean used;
		Object o;
		
		System.out.println( "\tWaiting input: "+ server.inputSize() );
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
				System.out.println( "\tUsing ship: "+ IDmyShip );
			}
			
			if (o instanceof NewTurn){
				used = true;
				newTurnToElaborate.add( (NewTurn)o );
				System.out.println( "\tNew turn received");
			}
			
			if (o instanceof AllMap){
				used = true;
				
				lastAllMap = (AllMap)o;
				
				oldTurnToElaborate.addAll(newTurnToElaborate);
				newTurnToElaborate.clear();
				System.out.println( "\tAllMap received" );
				//break;
			}
			
			if (!used){
				System.out.println("\tWrong packet!: "+o);
				close();
			}

		}
		
		time = System.nanoTime()-time;
		System.out.println( "\tReading input time: "+time);
		
		time = System.nanoTime();
		boolean synchronousChanged = false;
		boolean allMapArrived = false;
		
		if (oldTurnToElaborate.size()>0){
			//execute NewTurn on synchronous world
			synchronousChanged = true;
			for (NewTurn t:oldTurnToElaborate){
				if (t.actualTurn<world.actualTurn){
					System.out.println("\tWrong NewTurn");
					close();
				}
				updateWorld(t);
			}
		}
		time = System.nanoTime()-time;
		System.out.println( "\tFirst update syncronous time: "+time);
		
		time = System.nanoTime();
		long asincTurn = asincroniusWorld.actualTurn;
		if (lastAllMap!=null){
			//we have to rebuild the synchronous world
			//synchronousChanged = true;
			allMapArrived = true;
			
			if (lastAllMap.turn < world.actualTurn){
				System.out.println("\tWrong allmap: "+lastAllMap.turn+" "+world.actualTurn);
				close();
			}
			
			testWorldPrecision(lastAllMap);
			
			//rebuildWorld(lastAllMap);
			
			//if (turnLag==-1)
				turnLag = asincTurn-world.actualTurn;
				gui.setLag(turnLag*MAX_TURN_DURATION);
			//else
				//turnLag = (turnLag+asincTurn-world.actualTurn)/2;
			
			System.out.println( "\tRilevated new turn LAG: "+turnLag+" to ms: "+(turnLag*MAX_TURN_DURATION) );
			
			System.out.println( "\tRebuilding world from turn: "+lastAllMap.turn );
			System.out.println( "\tNewTurn to elaborate: "+newTurnToElaborate.size() );
			
		}
		time = System.nanoTime()-time;
		System.out.println( "\tTEST syncronized time: "+time);
		
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
		System.out.println( "\tSecond update syncronous time: "+time);
		
		//remove old myAction
		time = System.nanoTime();
		LinkedList<Action> ris = new LinkedList<Action>();
		synchronized (myActions) {
			for (long id : myActions.keySet() ){
				if (id <= world.actualTurn){
					ris.addAll(myActions.get(id));
					//myActions.remove(id);
				}
			}
			myActions.values().removeAll(ris);
		}
		time = System.nanoTime()-time;
		System.out.println( "\tRemoving old action time: "+time);
		/*
		for (; lastMyActionClear<=world.actualTurn;lastMyActionClear++){
			myActions.remove(lastMyActionClear);
		}
		*/
		if (synchronousChanged){
			time = System.nanoTime();
			
			if (!allMapArrived){
				//if (turnLag==-1)
					turnLag = asincTurn-world.actualTurn;
					gui.setLag(turnLag*MAX_TURN_DURATION);
				//else
					//turnLag = (turnLag+asincTurn-world.actualTurn)/2;
			}
			
			System.out.println( "\tRilevated action LAG: "+turnLag+" to ms: "+(turnLag*MAX_TURN_DURATION) );
			
			rebuildAsichronousWorld();
			
			time = System.nanoTime()-time;
			System.out.println( "\tRebuilding asyncronous time: "+time);
		}
		
		time = System.nanoTime();
		if (asincTurn<world.actualTurn){
			System.out.println( "\tAsinc turn was less than sync turn:"+asincTurn+" "+ world.actualTurn);
			asincTurn = world.actualTurn;
		}
		
		updateAsincronousWorld(asincTurn+1);
		
		time = System.nanoTime()-time;
		System.out.println( "\tUpdate asyncronous time: "+time);
		
		copyWorldForPaint();

	}

	private void rebuildAsichronousWorld() {
		System.out.println( "\t\tI'm rebuilding the asynchronous world!! Was "+asincroniusWorld.actualTurn +" will be "+world.actualTurn );
		
		long time = System.nanoTime();
		asincroniusWorld.clear();
		allOggetto2D.clear();
		time = System.nanoTime() - time;
		System.out.println( "Clear time: "+time );
		
		time = System.nanoTime();
		Oggetto2D tempCopy;
		//Vec2 pos;
		Collection<Oggetto2D> temp = world.getOggetti().values();
		for (Oggetto2D o:temp){
			//pos = o.getInfoPosition().getPos();
			tempCopy = asincroniusWorld.addCopy(o, 0, 0);
			tempCopy.setInfoPosition( o.getInfoPosition() );
			
			synchronized (allOggetto2D) {
				allOggetto2D.add( new ClientOggetto2D(tempCopy) );
			}
			
			//System.out.println( tempCopy.getInfoPosition() );
			if (IDmyShip==tempCopy.ID)
				myShip = tempCopy;
		}
		
		temp = world.getNewOggetti().values();
		for ( Oggetto2D o:temp ){
			//pos = o.getInfoPosition().getPos();
			tempCopy = asincroniusWorld.addCopy(o, 0, 0);
			tempCopy.setInfoPosition( o.getInfoPosition() );
			
			synchronized (allOggetto2D) {
				allOggetto2D.add( new ClientOggetto2D(tempCopy) );
			}
			
			//System.out.println( tempCopy.getInfoPosition() );
			if (IDmyShip==tempCopy.ID)
				myShip = tempCopy;
		}
		time = System.nanoTime() - time;
		System.out.println( "Population time: "+time );
		asincroniusWorld.actualTurn = world.actualTurn+1;
	}

	private void updateWorld(long turn) {
		System.out.println( "\t\tI'm updating the synchronous world!! Was "+world.actualTurn +" will be "+turn );
		int i=0;
		while (world.actualTurn<turn){
			world.update();
			i++;
		}
		System.out.println( "\t\tI've updated the synchronous world!! is "+world.actualTurn +" inter "+i );
	}
	
	private void updateWorld(NewTurn t) {
		System.out.println( "\t\tI'm updating the synchronous world!! Was "+world.actualTurn +" will be "+t.actualTurn+" diff:"+(t.actualTurn-world.actualTurn)  );
		
		long time = System.nanoTime();
		
		while (world.actualTurn<t.actualTurn){
			world.update();
		}
		
		time = System.nanoTime() -time;
		System.out.println( "\t\tStep time: "+time);
		
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
				synchronized (myActions) {
					ArrayList<Action> arrT = myActions.get( newAct.getExecTime() );
					if (arrT!=null)
						arrT.remove(newAct);
				}
			}
		}
		time = System.nanoTime() -time;
		System.out.println( "\t\tAction time: "+time );
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
		System.out.println( "\t\tI'm updating the asynchronous world!! Was "+asincroniusWorld.actualTurn +" will be "+asincTurn );
		ArrayList<Action> my;
		while (asincroniusWorld.actualTurn < asincTurn){
			//update world
			asincroniusWorld.update();
			
			//execute my actions
			synchronized (myActions) {
				my = myActions.get(asincroniusWorld.actualTurn);
				if (my!= null){//if there are action
					for (Action t: my){
						t.run( asincroniusWorld.get(t.ID) );
						System.out.println( "\t\tAsin action setted for object:"+t.ID+" at turn:"+asincroniusWorld.actualTurn );
					}
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
		System.out.println("\t\tTestin precision of map");
		
		//rebuildAsichronousWorld();
		
		//LinkedList<InfoBody> ris;
		InfoBody a;
		
		//DEBUG at turn
		System.out.println( "\t\tDEBUG turn: "+lastAllMap.turn);
		if (lastAllMap.turn > world.actualTurn){
			System.out.println( "test require "+(lastAllMap.turn-world.actualTurn)+" step" );
			updateWorld(lastAllMap.turn);
		}
		//ris = new LinkedList<InfoBody>();
		Oggetto2D temp;
		while ( (a=lastAllMap.poll())!=null ){
			//ris.add(a);
			temp = world.get(a.ID);
			if ( temp!=null ){
				//System.out.println( a.ID+" error: "+ temp.getInfoPosition().compare(a) );
				if ( temp.getInfoPosition().compare(a)!=0 ){
					if ( firstCorrection ){ //here you can set the max acceptable error
						System.out.println( "Correcting ID: "+a.ID+" error: "+temp.getInfoPosition().compare(a)+" has to be:\n"+a+" is:\n"+temp.getInfoPosition() );
						temp.setInfoPosition(a);
					}else{
						System.out.println( "ID: "+a.ID+" error: "+temp.getInfoPosition().compare(a)+" has to be:\n"+a+" is:\n"+temp.getInfoPosition() );
						error = true;
					}
				}else{
					System.out.println( a.ID+": Every little things, gonna be all right");//+a );
				}
			}else{
				System.out.println( "\t\t"+a.ID+" doesn't exist!" );
				//close();
				error = true;
			}
		}
		firstCorrection = false;
		/*
		while ( (a=ris.poll())!=null ){
			lastAllMap.add(a);
		}
		*/
		//CLOSE if find error
		if (error)
			close();
	}

}
