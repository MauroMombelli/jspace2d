package client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.TreeMap;

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
	
	long actualEngineTurn;
	long MAX_TURN_DURATION;
	
	long nextServerTurn;
	
	int objectInWrongPos=0;
	double biggestPositionError=0;
	
	PhysicWorld world = new PhysicWorld();
	TreeMap<Integer, Oggetto2D> allOggetto2D = new TreeMap<Integer, Oggetto2D>();
	
	PhysicWorld asincroniusWorld = new PhysicWorld();
	LinkedList<Oggetto2D> asincronousOggetto2D = new LinkedList<Oggetto2D>();
	
	//START: Actually for debug purpose, take a shot of the world every 100° turn
	HashMap< Long, HashMap<Integer, InfoBody> > mapsAtTurn = new HashMap< Long, HashMap<Integer,InfoBody>>();
	//END
	
	private InitGraphics gui;
	
	int IDmyShip=-1;
	Oggetto2D myShip;//In the asynchronous world
	
	int turnAsincCalculated=0;
	
	public ClientEngine(ServerListener serverListener, long actualTurn, long turnDuration) {
		server = serverListener;
		this.actualEngineTurn = actualTurn;
		this.nextServerTurn = actualTurn;
		MAX_TURN_DURATION = turnDuration;
		gui = new InitGraphics();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		server.write( new ShipRequest() );
		System.out.println("Ship request send, starting turn:"+actualTurn);
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		
		{
			actualEngineTurn++;
			System.out.println("Turn: "+actualEngineTurn);
			if ( server.isClosed() ){
				System.out.println("Disconnecting");
				server.close();
				this.cancel();
			}else{
				//	Synchronize box2d with server 
				long time2 = System.nanoTime();
				synchronizePhysic();
				time2 = System.nanoTime()-time2;
				System.out.println( "Physic time: "+time2);
				
				time2 = System.nanoTime();
				sendActions();
				time2 = System.nanoTime()-time2;
				System.out.println( "Send action time: "+time2);
			}
		}
		
		time = System.currentTimeMillis() - time;
		if ( time > MAX_TURN_DURATION ){
			System.out.println( "WARNING: engine turn take more execution time than requested!!! This can cause serius error/data corruption!! duration: "+time );
		}
	}

	private void sendActions() {
		if (myShip == null)
			return;
		
		
		float strenght=0, angle=0;
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_up", true)) {
			strenght -= 1;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_down", true)) {
			strenght += 1;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_left", true)) {
			angle -= 1;
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_right", true)) {
			angle += 1;
        }
		if (strenght != 0 || angle != 0){
			System.out.println( "Writing action" );
			double x = strenght*Math.sin( myShip.getBody().getAngle() );
			double y = strenght*Math.cos( myShip.getBody().getAngle() );
			server.write( new ActionEngine(myShip.ID, (float)x, (float)y, angle) );
		}
	}

	private void copyWorldForPaint(LinkedList<Oggetto2D> collection) {
		long time2 = System.nanoTime();
		LinkedList<GuiAction> listaAzioni = new LinkedList<GuiAction>();
		for (Oggetto2D t:collection){
			listaAzioni.add( new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
		}
		System.out.println( "GUIActions:"+listaAzioni.size() );
		gui.getWorldGUI().setGuiActions(listaAzioni);
		time2 = System.nanoTime()-time2;
		System.out.println( "GUI copy time: "+time2);
	}

	private void synchronizePhysic() {
		Object o;
		
		NewTurn temp;
		boolean used = false;
		long time2 = System.nanoTime();
		boolean arrivedNewTurn = false;
		boolean arrivedAllMap = false;
		System.out.println( "Waiting input: "+ server.inputSize() );
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
				arrivedNewTurn = true;
				temp =(NewTurn)o; 

				System.out.println( "New turn received: "+ temp.actualTurn );
				System.out.println( "Contains "+temp.actionsSize()+" actions, "+temp.newObjSize()+" new obj and "+temp.newCollisionSize()+" collision" );

				elaborate(temp);

			}
			
			if (o instanceof AllMap){
				used = true;
				
				arrivedAllMap=true;
				AllMap tempAM = (AllMap)o;
				
				//There was no actions, so we can update synchronous world to tempAM.turn
				System.out.println( "DEBUG FastElaboration from: "+nextServerTurn+" to: "+tempAM.turn );
				while (nextServerTurn<=tempAM.turn){
					execute();
				}
				//System.out.println( " lastServerTurn:"+nextServerTurn);
				boolean errorFound = false;
				HashMap<Integer, InfoBody> clientData = mapsAtTurn.get(tempAM.turn);
				if (clientData!=null){
					//if (clientData.){
						System.out.println("\n\nI'm debuggin turn: "+tempAM.turn);
						InfoBody a;
						InfoBody b;
						double diffSum=0;
						int tested=0;
						
						LinkedList<InfoBody> map = new LinkedList<InfoBody>();
						while ( (a=tempAM.poll())!=null ){
							b = clientData.get(a.ID);
							if (b == null){
								System.out.println("DEBUG client dosn't have obj: "+a.ID);
							}else{
								if (a.compare(b)!=0){
									objectInWrongPos++;
									System.out.println("DEBUG difference obj: "+a.ID+"\n\tdiff: "+a.compare(b)+"\n"+a+"\n"+b+"\nwrong obj:"+objectInWrongPos );
									if (a.compare(b)>biggestPositionError)
										biggestPositionError=a.compare(b);
									
									//allOggetto2D.get(a.ID).setInfoPosition(a);
								}
								//else
									//System.out.println("DEBUG no difference: "+a.ID);
								diffSum+=a.compare(b);
							}
							map.add(a);
							tested++;
							if (objectInWrongPos>100){
								System.out.println("Too much error, biggest:"+biggestPositionError);
							}
						}
						if (diffSum != 0){
							System.out.println("ERROR IN DEBUG, PHYSIC IS NOT PERFECTLY SYNCRONIZED!");
							//errorFound=true;
							recreateSyncronousWorld(map, tempAM.turn);
						}else{
							System.out.println("DEBUG: EVERY LITTLE THINGS, IS GONNA BE ALL RIGHT :-): "+tested);
						}
						mapsAtTurn.remove(clientData);
					//}
				}else{
					System.out.println("DEBUG SERVER IS FASTER: "+tempAM.turn+" "+nextServerTurn);
				}
				if (errorFound)
					System.exit(0);
				
			}
			
			if (!used){
				System.out.println("Wrong packet!: "+o);
				server.close();
			}
			
			//if ( System.nanoTime()-time2 > 10000000 ) // if there is too much time for input take a break
			//	break;
		}
		time2 = System.nanoTime()-time2;
		System.out.println( "Reading input time: "+time2);
		
		time2 = System.nanoTime();
		//if (nextServerTurn < actualEngineTurn){
		if (arrivedAllMap){
			copyWorldForPaint( crateAndUpdateAsincroniusWorld(turnAsincCalculated, true) );
			turnAsincCalculated=0;
		}else
			if (arrivedNewTurn){
				//copyWorldForPaint( crateAndUpdateAsincroniusWorld(0, true) );
				copyWorldForPaint( crateAndUpdateAsincroniusWorld(turnAsincCalculated, true) );
				turnAsincCalculated=0;
			}else{
				copyWorldForPaint( crateAndUpdateAsincroniusWorld(1, false) ); //just 1 step
				turnAsincCalculated++;
			}
		//}else{
		//	copyWorldForPaint( allOggetto2D.values() );
		//	crateAndUpdateAsincroniusWorld(0, true);
		//}
		time2 = System.nanoTime()-time2;
		System.out.println( "Paint copy time: "+time2+" ainc calculated: "+turnAsincCalculated);
				
		if (nextServerTurn < actualEngineTurn){
			System.out.println( "Server is slow! I'm "+ (actualEngineTurn - nextServerTurn) +" turn ahead, ms:"+((actualEngineTurn - nextServerTurn)*MAX_TURN_DURATION) );
			//System.exit(0);// FOR DEBUG PURPOISE
		}

		if (nextServerTurn > actualEngineTurn){
			System.out.println( "Server is faster! jumped "+ (nextServerTurn - actualEngineTurn) +" turn, ms:"+((nextServerTurn-actualEngineTurn)*MAX_TURN_DURATION) );
			actualEngineTurn=nextServerTurn-1;
		}
	}

	private void recreateSyncronousWorld(LinkedList<InfoBody> map, long turn) {
		System.out.println( "I'm rebuilding the syncronous world!!" ); 
		for (InfoBody o:map){
			allOggetto2D.get(o.ID).setInfoPosition(o);
			if (o.compare(allOggetto2D.get(o.ID).getInfoPosition())!=0){
				System.out.println( "Error adjusting world");
				System.exit(0);
			}
		}
		nextServerTurn=turn+1;
	}

	private LinkedList<Oggetto2D> crateAndUpdateAsincroniusWorld(long l, boolean clear) {
		long time2 = System.nanoTime();
		if (clear){
			
			long time3 = System.nanoTime();
			//asincroniusWorld.clear();
			asincroniusWorld = new PhysicWorld();
			asincronousOggetto2D.clear();
			time3 = System.nanoTime()-time3;
			System.out.println( "Asinc clearing time: "+time3);
			
			Oggetto2D tempCopy;
			for (Oggetto2D o:allOggetto2D.values()){
				tempCopy = asincroniusWorld.addCopy(o, o.getInfoPosition().getPos().x, o.getInfoPosition().getPos().y);
				tempCopy.setInfoPosition( o.getInfoPosition() );
				asincronousOggetto2D.add(tempCopy);
				if (IDmyShip==tempCopy.ID)
					myShip = tempCopy;
			}
		}
		time2 = System.nanoTime()-time2;
		System.out.println( "Asinc creation time: "+time2);
		
		time2 = System.nanoTime();
		for (int i =0; i < l; i++){
			asincroniusWorld.update();
		}
		System.out.println( "Asincronous turn calculated: "+l+" cleared: "+clear);
		time2 = System.nanoTime()-time2;
		System.out.println( "Asinc update time: "+time2);
		
		return asincronousOggetto2D;
	}

	private void elaborate(NewTurn tempTurn) {
		if (tempTurn.actualTurn < nextServerTurn){
			System.out.println( "Error, arrived a packed already sincronized: "+tempTurn.actualTurn+" "+nextServerTurn);
			System.exit(0);
		}
		
		if (tempTurn.actualTurn == nextServerTurn){
			//ready to elaborate!
			execute(tempTurn);
			System.out.println( "Elaborated: "+tempTurn.actualTurn+" "+nextServerTurn);
			return;
		}
		
		if (tempTurn.actualTurn > nextServerTurn){
			System.out.println( "FastElaboration from: "+nextServerTurn+" to: "+tempTurn.actualTurn );
			while (nextServerTurn<tempTurn.actualTurn){
				execute();
			}
			execute(tempTurn);
			System.out.println( " lastServerTurn:"+nextServerTurn);
		}
	}

	public void execute(NewTurn toDo){
		Oggetto2D newObj;
		InfoBody newObjPos;
		Action newAct;
		
		//add the new obj and set their position
		while ( (newObj=toDo.pollNewObj())!=null ){
			newObjPos = toDo.pollPosObj();
			world.addNew(newObj, newObjPos.getPos().x, newObjPos.getPos().y, newObjPos.getAngle() );
			newObj.setInfoPosition(newObjPos);
			allOggetto2D.put(newObj.ID, newObj);
			System.out.println( "created object:"+newObj.getInfoPosition().compare(newObjPos)+"\n"+newObj.getInfoPosition()+"\n"+newObjPos );
			if (newObj.getInfoPosition().compare(newObjPos) != 0){
				System.out.println( "Error creation isn't perfect");
				System.exit(0);
			}
			/*if (newObj.ID == 10){
				System.out.println( "created object model:"+newObj.getModelName());
				System.exit(0);
			}*/
			
		}
	
		//set the actions
		while ( (newAct=toDo.pollActions())!=null ){
			//System.out.println( "action after: "+allOggetto2D.get(newAct.ID).getInfoPosition().getPosVel() );
			newAct.run( allOggetto2D.get(newAct.ID) );
			System.out.println( "action setted for object:"+newAct.ID );
			//System.out.println( "action before: "+allOggetto2D.get(newAct.ID).getInfoPosition().getPosVel() );
			//System.out.println( "data: "+newAct.ID+" "+allOggetto2D.get(newAct.ID).getInfoPosition() );
		}
		
		//set the collision
		while ( (newObjPos=toDo.pollCollision())!=null ){
			allOggetto2D.get(newObjPos.ID).setInfoPosition(newObjPos);
		}
		
		execute();

	}
	
	public void execute(){
		
		world.update();

		//System.out.println( "Executing:"+nextServerTurn);
		if (nextServerTurn%100==0){
			System.out.println( "Saving for debug: "+nextServerTurn);
			HashMap<Integer, InfoBody> data = new HashMap<Integer, InfoBody>();
			for ( Oggetto2D obj:allOggetto2D.values() ){
				data.put(obj.ID, obj.getInfoPosition());
			}
			mapsAtTurn.put(nextServerTurn, data);
		}
		nextServerTurn++;
		
	}

}
