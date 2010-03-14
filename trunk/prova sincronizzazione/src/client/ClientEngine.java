package client;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimerTask;

import org.jbox2d.common.Vec2;

import client.gameState.GuiAction;
import client.gameState.SetNode;

import shared.AllMap;
import shared.InfoBody;
import shared.NewTurn;
import shared.Oggetto2D;
import shared.PhysicWorld;
import shared.azioni.Action;

public class ClientEngine extends TimerTask{

	ServerListener server;
	
	long actualEngineTurn;
	long MAX_TURN_DURATION;
	
	long lastServerTurn;
	
	PhysicWorld world = new PhysicWorld();
	HashMap<Integer, Oggetto2D> allOggetto2D = new HashMap<Integer, Oggetto2D>();
	
	PhysicWorld asincroniusWorld = new PhysicWorld();
	LinkedList<Oggetto2D> asincronousOggetto2D = new LinkedList<Oggetto2D>();
	
	//START: Actually for debug purpose, take a shot of the world every 100° turn
	HashMap< Long, HashMap<Integer, InfoBody> > mapsAtTurn = new HashMap< Long, HashMap<Integer,InfoBody>>();

	private InitGraphics gui;
	//LinkedList< AllMap > allMapNotElaborated = new LinkedList< AllMap >();
	//END
	
	public ClientEngine(ServerListener serverListener, long actualTurn, long turnDuration) {
		server = serverListener;
		this.actualEngineTurn = actualTurn;
		this.lastServerTurn = actualTurn;
		MAX_TURN_DURATION = turnDuration;
		gui = new InitGraphics();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
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
				synchronizePhysic();
			}
		}
		
		time = System.currentTimeMillis() - time;
		if ( time > MAX_TURN_DURATION ){
			System.out.println( "WARNING: engine turn take more execution time than requested!!! This can cause serius error/data corruption!! duration: "+time );
		}
	}

	private void copyWorldForPaint(Collection<Oggetto2D> collection) {
		LinkedList<GuiAction> listaAzioni = new LinkedList<GuiAction>();
		for (Oggetto2D t:collection){
			listaAzioni.add( new SetNode(t.ID, t.getModelName(), new Vec2( t.getBody().getPosition() ) , t.getBody().getAngle()) );
		}
		System.out.println( "GUIActions:"+listaAzioni.size() );
		gui.getWorldGUI().addGuiActions(listaAzioni);
	}

	private void synchronizePhysic() {
		Object o;
		//LinkedList<NewTurn> n = new LinkedList<NewTurn>();
		boolean arrivedNewTurn = false; 
		NewTurn temp;
		//boolean elaborated = false;
		while ( (o=server.poll())!=null ){
			if (o instanceof NewTurn){
				temp =(NewTurn)o; 
				//n.add(temp);
				System.out.println( "New turn received: "+ temp.actualTurn );
				System.out.println( "Contains "+temp.actionsSize()+" actions and "+temp.newObjSize()+" new obj" );
				//if (elaborated && temp.actualTurn>=actualTurn && server.inputSize() <= 1){
				//	System.out.println( "NewTurn is for the next turn" );
				//	server.addFirst(o);
				//	break;
				//}else{
				arrivedNewTurn = true;
					elaborate(temp);
					
					//System.out.println( "executed" );
				//	elaborated = true;
				//}
				//execute();
				//actualTurn--;
				
			}else{
				if (o instanceof AllMap){
					//allMapNotElaborated.add((AllMap)o);
					
					AllMap tempAM = (AllMap)o;
					HashMap<Integer, InfoBody> clientData = mapsAtTurn.get(tempAM.turn);
					if (clientData!=null){
						//if (clientData.){
							System.out.println("\n\nI'm debuggin turn: "+tempAM.turn);
							InfoBody a;
							InfoBody b;
							double diffSum=0;
							while ( (a=tempAM.poll())!=null ){
								b = clientData.get(a.ID);
								if (b == null){
									System.out.println("DEBUG client dosn't have obj: "+a.ID);
								}else{
									if (a.compare(b)!=0)
										System.out.println("DEBUG difference obj: "+a.ID+"\n\tdiff: "+a.compare(b)+"\n"+a+"\n"+b+"\n" );
									diffSum+=a.compare(b);
								}
							}
							if (diffSum != 0){
								System.out.println("ERROR IN DEBUG, PHYSIC IS NOT PERFECTLY SYNCRONIZED!");
								System.exit(0);
							}
							mapsAtTurn.remove(clientData);
						//}
					}else{
						System.out.println("DEBUG SERVER IS FASTER");
					}
				}else{
					System.out.println("Wrong packet!");
					server.close();
				}
			}
		}

		if (lastServerTurn < actualEngineTurn){
			if (arrivedNewTurn)
				copyWorldForPaint( crateAndUpdateAsincroniusWorld(actualEngineTurn-lastServerTurn, true) );
			else
				copyWorldForPaint( crateAndUpdateAsincroniusWorld(1, false) ); //just 1 step
		}else{
			copyWorldForPaint( allOggetto2D.values() );
			crateAndUpdateAsincroniusWorld(0, true);
		}
		
		if (lastServerTurn < actualEngineTurn){
			System.out.println( "Server is slow! I'm "+ (actualEngineTurn - lastServerTurn) +" turn ahead, ms:"+((actualEngineTurn - lastServerTurn)*MAX_TURN_DURATION) );
			//System.exit(0);// FOR DEBUG PURPOISE
		}

		if (lastServerTurn > actualEngineTurn){
			System.out.println( "Server is faster! jumped "+ (lastServerTurn - actualEngineTurn) +" turn, ms:"+((lastServerTurn-actualEngineTurn)*MAX_TURN_DURATION) );
			actualEngineTurn=lastServerTurn;
		}
	}
	
	
	
	private LinkedList<Oggetto2D> crateAndUpdateAsincroniusWorld(long l, boolean clear) {
		if (clear){
			asincroniusWorld.clear();
			Oggetto2D tempCopy;
			for (Oggetto2D o:allOggetto2D.values()){
				tempCopy = asincroniusWorld.addCopy(o, o.getInfoPosition().getPos().x, o.getInfoPosition().getPos().y);
				tempCopy.setInfoPosition( o.getInfoPosition() );
				asincronousOggetto2D.add(tempCopy);
			}
		}
		for (int i =0; i < l; i++){
			asincroniusWorld.update();
		}
		System.out.println( "Asincronous turn calculated: "+l+" cleared: "+clear);
		return asincronousOggetto2D;
	}

	private void elaborate(NewTurn tempTurn) {
		if (tempTurn.actualTurn < lastServerTurn){
			System.out.println( "Error, arrived a packed already sincronized: "+tempTurn.actualTurn+" "+lastServerTurn);
			System.exit(0);
		}
		
		if (tempTurn.actualTurn == lastServerTurn){
			//ready to elaborate!
			execute(tempTurn);
			System.out.println( "Elaborated: "+tempTurn.actualTurn+" "+lastServerTurn);
			return;
		}
		
		if (tempTurn.actualTurn > lastServerTurn){
			System.out.println( "FastElaboration from: "+lastServerTurn+" to: "+tempTurn.actualTurn );
			while (lastServerTurn<tempTurn.actualTurn-1){
				execute();
			}
			execute(tempTurn);
			System.out.println( " lastServerTurn:"+lastServerTurn);
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
		}
	
		//set the actions
		while ( (newAct=toDo.pollActions())!=null ){
			System.out.println( "action after: "+allOggetto2D.get(newAct.ID).getInfoPosition().getPosVel() );
			newAct.run( allOggetto2D.get(newAct.ID) );
			System.out.println( "action before: "+allOggetto2D.get(newAct.ID).getInfoPosition().getPosVel() );
		}
		
		execute();

	}
	
	public void execute(){
		
		world.update();
		
		System.out.println( "Executing:"+lastServerTurn);
		if (lastServerTurn%100==0){
			System.out.println( "Saving for debug: "+lastServerTurn);
			HashMap<Integer, InfoBody> data = new HashMap<Integer, InfoBody>();
			for ( Oggetto2D obj:allOggetto2D.values() ){
				data.put(obj.ID, obj.getInfoPosition());
			}
			mapsAtTurn.put(lastServerTurn, data);
		}
		
		lastServerTurn++;
	}

}
