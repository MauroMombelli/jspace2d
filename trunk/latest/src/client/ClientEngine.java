package client;

import java.util.LinkedList;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jbox2d.common.Vec2;

import client.gameState.GuiAction;
import client.gameState.RemoveNode;

import com.jme.input.KeyBindingManager;
import com.jme.math.FastMath;

import shared.AllMap;
import shared.InfoBody;
import shared.NewTurn;
import shared.PhysicWorld;
import shared.azioni.Action;
import shared.azioni.ActionEngine;
import shared.azioni.ActionLightShot;
import shared.azioni.CreateShip;
import shared.azioni.SelectShip;
import shared.oggetti.Oggetto2D;

public class ClientEngine extends TimerTask{

	//if you want all the action execute X turn after it has been trigger, put here the value. WARNIG net lag is calculated in addition of this
	private static final long fixedActionLAG = 0;

	ServerListener server;
	
	long MAX_TURN_DURATION;
	
	PhysicWorld world = new PhysicWorld();
	
	PhysicWorld asincroniusWorld = new PhysicWorld();
	//PhysicWorldListener asincList = new PhysicWorldListener(); this will be useful with lag
	
	LinkedList<ClientOggetto2D> allOggetto2D = new LinkedList<ClientOggetto2D>();
	LinkedList<ClientOggetto2D> tempAllOggetto2D = new LinkedList<ClientOggetto2D>();
	LinkedList<GuiAction> indipendetActions = new LinkedList<GuiAction>();
	
	TreeMap< Long, LinkedList<Action> > myActions = new TreeMap<Long, LinkedList<Action> >();
	long lastMyActionClear;
	
	private InitGraphics gui;
	
	int IDmyShip=-1;
	Oggetto2D myShip;//In the asynchronous world
	
	long actionLag=0;
	long allMapLag=0;
	long realClientTurn=0;

	private int errorNumber=0;
	
	public ClientEngine(ServerListener serverListener, long actualTurn, long turnDuration) {
		server = serverListener;
		
		world.actualTurn = actualTurn;

		asincroniusWorld.actualTurn=actualTurn;
		//asincroniusWorld.setCollisionListener(asincList);
		lastMyActionClear = actualTurn;
		
		MAX_TURN_DURATION = turnDuration;
		
		gui = new InitGraphics( allOggetto2D );
		
		server.write( new CreateShip() );
		System.out.println("Creation ship request send, starting turn:"+actualTurn);
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		
		{
			realClientTurn++;
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
		
		synchronized (allOggetto2D) {
			if (tempAllOggetto2D.size()!=0){
				allOggetto2D.clear();
				allOggetto2D.addAll(tempAllOggetto2D);
				//tempAllOggetto2D.clear();
			}
				
			for (ClientOggetto2D temp:allOggetto2D)
				temp.update();
		}
		gui.setActions(indipendetActions);
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
	
	//INPUT PURPOISE
	private void sendActions() {
		boolean actionExecuted = false;
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("escape", true)) {
			gui.close();
			System.exit(0);
        }
		
		if (myShip == null)
			return;
		
		//movement
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
		System.out.println( "forza:"+strenght+" a:"+angle );
		if (strenght != 0 || angle != 0){
			float x = strenght*FastMath.cos( myShip.getBody().getAngle() + FastMath.PI/2 );
			float y = strenght*FastMath.sin( myShip.getBody().getAngle() + FastMath.PI/2 );
			System.out.println( "Writing action, xV:"+x+" yV:"+y+" aV"+angle );
			executeAction( new ActionEngine(myShip.ID, x, y, angle) );
			actionExecuted = true;
		}
		
		//shoot
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("shot_light", false)) {
			executeAction( new ActionLightShot(myShip.ID) );
			actionExecuted = true;
        }
		
		if (actionExecuted){
			//long t = asincroniusWorld.actualTurn;
			//rebuildAsichronousWorld();
			//updateAsincronousWorld(t);
		}
	}
	
	public void executeAction(Action a){
		System.out.println( "Writing action" );
		server.write(a);
		a.setExecTime(asincroniusWorld.actualTurn-actionLag+fixedActionLAG);
		/*
		synchronized (myActions) {
			LinkedList<Action> t=myActions.get( a.getExecTime() );
			if ( t == null ){
				t = new LinkedList<Action>();
				t.add(a);
				myActions.put(a.getExecTime(), t);
			}else
				t.add(a);
		}
		*/
		
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
			
			if (o instanceof SelectShip){
				used = true;
				IDmyShip = ((SelectShip)o).shipOwnerID;
				gui.setCameraID(IDmyShip);
				myShip = world.get(IDmyShip);
				System.out.println( "\tSelect ship received, id:"+IDmyShip);
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
			
			allMapLag = asincTurn-world.actualTurn;
			//gui.setLag(allMapLag*MAX_TURN_DURATION);
			
			System.out.println( "\tRilevated new turn LAG: "+allMapLag+" to ms: "+(allMapLag*MAX_TURN_DURATION) );
			
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
		
		/*
		time = System.nanoTime();
		//remove old myAction
		LinkedList<Action> ris = new LinkedList<Action>();
		synchronized (myActions) {
			for (long id : myActions.keySet() ){
				if (id <= world.actualTurn){
					ris.addAll(myActions.get(id));
				}
			}
			myActions.values().removeAll(ris);
		}
		time = System.nanoTime()-time;
		System.out.println( "\tRemoving old action time: "+time);
		*/
		
		if (synchronousChanged || allMapArrived){
			time = System.nanoTime();
			
			gui.setLag(actionLag, allMapLag);
			
			if (!allMapArrived){
				actionLag = asincTurn-world.actualTurn;
			}
			
			System.out.println( "\tRilevated action LAG: "+actionLag+" to ms: "+(actionLag*MAX_TURN_DURATION) );
			
			rebuildAsichronousWorld();
			
			time = System.nanoTime()-time;
			System.out.println( "\tRebuilding asyncronous time: "+time);
		}
		
		time = System.nanoTime();
		
		if (asincTurn<world.actualTurn){
			System.out.println( "\tAsinc turn was less than sync turn:"+asincTurn+" "+ world.actualTurn);
			asincTurn = world.actualTurn;
		}
		
		if (!synchronousChanged){
			updateAsincronousWorld(asincTurn+1);
		}
		
		time = System.nanoTime()-time;
		System.out.println( "\tUpdate asyncronous time: "+time);
		
		gui.setTurn(asincroniusWorld.actualTurn, world.actualTurn, errorNumber);
		
		copyWorldForPaint();
		if (myShip!=null)
			gui.setLife( "Life: "+myShip.getLife() );
	}

	private void rebuildAsichronousWorld() {
		System.out.println( "\t\tI'm rebuilding the asynchronous world!! Was "+asincroniusWorld.actualTurn +" will be "+world.actualTurn );
		
		long time = System.nanoTime();
		//asincroniusWorld.clear();
		
		time = System.nanoTime() - time;
		System.out.println( "Clear time: "+time );
		
		time = System.nanoTime();
		Oggetto2D tempCopy;
		Vec2 pos;
		
		LinkedList<Oggetto2D> tempW = new LinkedList<Oggetto2D>();
		tempW.addAll( world.getOggetti() );

		gui.setInfo("Number of obj in world:"+tempW.size());
		
		ClientOggetto2D a;
		LinkedList<ClientOggetto2D> all = new LinkedList<ClientOggetto2D>();
		Oggetto2D o;
		boolean deleteThisObj = false;
		LinkedList<ClientOggetto2D> removedOggetto = new LinkedList<ClientOggetto2D>();
		while( (o = tempW.poll()) != null ){
			
			a = tempAllOggetto2D.poll();
			
			
			while ( a!=null && a.getID() < o.ID){
				asincroniusWorld.removeBody(a.obj.getBody(), a.obj.ID);
				removedOggetto.add(a);
				a = tempAllOggetto2D.poll();
			}
			
			if ( a!=null && !deleteThisObj ){
				//updating old obj
				System.out.println("Same id:"+o.ID);
				
				a.set( o );
				a.setInfoPosition(o.getInfoPosition());
				
				all.add(a);
				
				if (a.getID() == IDmyShip){
					myShip = a.obj;
				}
			}else{
			
				if (a == null){
					//creatin new obj
					System.out.println("Created id:"+o.ID);
					pos = o.getInfoPosition().getPos();
				
					tempCopy = asincroniusWorld.addCopy( o, pos.x, pos.y, o.getInfoPosition().getAngle() );
					tempCopy.setInfoPosition( o.getInfoPosition() );
			
					a = new ClientOggetto2D(tempCopy);
					
					all.add( a );
					
					if (a.getID() == IDmyShip){
						myShip = a.obj;
					}
				}else{
					if (!deleteThisObj){
						System.out.println("Unexpected error: "+o.ID+" "+a.getID());
						close();
					}
				}
			}
		}
		
		while( (a = tempAllOggetto2D.poll() ) != null ){
			//remove inexistent obj
			removedOggetto.add(a);
			asincroniusWorld.removeBody(a.obj.getBody(), a.obj.ID);
		}
		for (ClientOggetto2D obj:removedOggetto){
			indipendetActions.add( new RemoveNode(obj.myNode, obj.getID()) );
		}

		tempAllOggetto2D.addAll(all);
		
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
		
		updateWorld(t.actualTurn);
		
		time = System.nanoTime() -time;
		System.out.println( "\t\tStep time: "+time);
		
		Oggetto2D newObj;
		InfoBody newObjPos;
		Action newAct;
		Vec2 pos;
		time = System.nanoTime();
		//add the new obj and set their position
		while ( (newObj=t.pollNewObj())!=null ){
			newObjPos = t.pollPosObj();
			pos = newObjPos.getPos();
			world.addNew( newObj, pos.x, pos.y, newObjPos.getAngle() );
			newObj.setInfoPosition( newObjPos );
			
			System.out.println( "created object:"+newObj.getInfoPosition().compare(newObjPos)+" ID "+newObj.ID );
			if (newObj.getInfoPosition().compare(newObjPos) != 0){
				System.out.println( "Error creation isn't perfect");
				close();
			}
			
			if (IDmyShip==newObj.ID){//executed only at the first creation time
				myShip = newObj;
			}
		}
		time = System.nanoTime() -time;
		System.out.println( "New obj time: "+time );
		
		time = System.nanoTime();
		//set the actions
		LinkedList<Action> myActionDefinetlyExecuted = new LinkedList<Action>();
		while ( (newAct=t.pollActions())!=null ){
			newAct.run( world );
			System.out.println( "action setted for object:"+newAct.shipOwnerID+" at turn:"+t.actualTurn );
			
			if ( newAct.shipOwnerID==IDmyShip ){
				myActionDefinetlyExecuted.add(newAct);
			}
			
			/* TODO: create a object ActivateShip instead of using CreateShip!!!!
			if (newAct instanceof CreateShip){
				IDmyShip = ( (CreateShip)newAct ).shipOwnerID;
				gui.setCameraID(IDmyShip);
				System.out.println( "\tUsing ship: "+ IDmyShip );
			}
			*/
			
		}
		
		synchronized (myActions) {
			/*
			for (long id:myActions.keySet()){
				if ( myActions.get(id).contains(myActionDefinetlyExecuted.get(0)) ){
					System.out.println( "Real Lag:"+(id-world.actualTurn) );
				}
			}
			*/
			for ( LinkedList<Action> tA:myActions.values() )
				tA.removeAll(myActionDefinetlyExecuted);
		}
		
		time = System.nanoTime() -time;
		System.out.println( "\t\tAction time: "+time );		
	}

	private void updateAsincronousWorld(long asincTurn) {
		System.out.println( "\t\tI'm updating the asynchronous world!! Was "+asincroniusWorld.actualTurn +" will be "+asincTurn );
		LinkedList<Action> my;
		Action t;
		LinkedList<Action> temp;
		while (asincroniusWorld.actualTurn < asincTurn){
			//update world
			asincroniusWorld.update();
			//execute world action
			//time2 = System.nanoTime();
			executeAsincAct();
			//time2 = System.nanoTime()-time2;
			//System.out.println( "Asinc action time: "+time2);
			//execute my actions
			synchronized (myActions) {
				my = myActions.get(asincroniusWorld.actualTurn);
				if (my!= null){//if there are action
					temp = new LinkedList<Action>();
					for (int i=0; i < my.size(); i++){
						t = my.poll();
						t.run( asincroniusWorld );
						System.out.println( "\t\tAsin action setted for object:"+t.shipOwnerID+" at turn:"+asincroniusWorld.actualTurn );
						temp.add(t);
					}
					my.addAll(temp);
				}
			}
		}
	}
	
	private void executeAsincAct() {
		TreeSet<Oggetto2D> t = new TreeSet<Oggetto2D>( asincroniusWorld.getOggetti() );
		for (Oggetto2D o:t){
			for ( Action a:o.getAndDestroyActions() ){
				a.run(asincroniusWorld);
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
		
		System.out.println("\t\tTestin precision of map");
		
		InfoBody a;
		
		TreeSet<Oggetto2D> tempWord = world.getOggetti();
		System.out.println( "\t\tSize of client world: "+tempWord.size()+" size of server world: "+lastAllMap.size() );
		int i=0;
		for (Oggetto2D t:tempWord){
			i++;
			a=lastAllMap.poll();
			if (a == null){
				System.out.println( "\t\t\tClient world has more obj than server! first occurence id:"+t.ID );
				close();
				break;
			}
			if (t.ID == a.ID){
				t.setInfoPosition(a);
			}else{
				System.out.println( "\t\t\tExpected ID: "+a.ID+" found: "+t.ID+" iteration number:"+i );
				close();
			}
		}
		
		a=lastAllMap.poll();
		if (a != null){
			System.out.println( "\t\t\tServer world has more obj than client! ids:"+a.ID );
			while ( (a=lastAllMap.poll()) != null )
				System.out.println( a.ID );
			close();
		}
		world.actualTurn = lastAllMap.turn;
		/*
		boolean error = false;
		//DEBUG at turn
		System.out.println( "\t\tDEBUG turn: "+lastAllMap.turn);
		if (lastAllMap.turn > world.actualTurn){
			System.out.println( "\t\ttest require "+(lastAllMap.turn-world.actualTurn)+" step" );
			updateWorld(lastAllMap.turn);
		}		
		
		TreeSet<Oggetto2D> tempWord = world.getOggetti();

		for (Oggetto2D t:tempWord){
			a=lastAllMap.poll();
			if (a == null){
				System.out.println( "\t\t\tClient world has more obj than server! first occurence id:"+t.ID );
				error = true;
				close();
				break;
			}
			if (t.ID == a.ID){
				if ( t.getInfoPosition().compare(a)!=0 ){
					System.out.println( "\t\t\tCorrecting ID: "+a.ID+" error: "+t.getInfoPosition().compare(a)+" has to be:\n"+a+" is:\n"+t.getInfoPosition() );
					t.setInfoPosition(a);
					error = true;
				}//else everything is all right
			}else{
				System.out.println( "\t\t\tExpected ID: "+a.ID+" found: "+t.ID );
				error = true;
			}
		}
		
		a=lastAllMap.poll();
		if (a != null){
			System.out.println( "\t\t\tServer world has more obj than client! ids:"+a.ID );
			while ( (a=lastAllMap.poll()) != null )
				System.out.println( a.ID );
			error = true;
			close();
		}

		if (error){
			errorNumber++;
			System.out.println("\t\t\tFound unexpected world error!");
		}else{
			System.out.println("\t\tTest OK!");
		}
		*/
		/*
		//CLOSE if find error
		if (errorNumber>0){
			close();
		}
		*/
			
	}

}
