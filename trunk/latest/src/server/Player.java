package server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import shared.InputReader;
import shared.Login;
import shared.Oggetto2D;
import shared.OutputWriter;
import shared.Ship;
import shared.azioni.Action;
import shared.specialActions.RemoveShip;
import shared.specialActions.ShipRequest;

public class Player {
	Socket giocatore;
	InputReader inR;
	OutputWriter outW;
	
	Login myself=null;
	
	int activeShip = -1;
	
	int update= 0;
	
	HashMap<Integer, Oggetto2D> myPossessoin = new HashMap<Integer, Oggetto2D>();
	
	HashMap< Long, LinkedList<Action> > myActions = new HashMap< Long, LinkedList<Action> >();
	//TODO: 
	LinkedList<RemoveShip> removeOggettiActions = new LinkedList<RemoveShip>();
	//end todo
	LinkedList<ShipRequest> createOggettiActions = new LinkedList<ShipRequest>();
	
	public Player(Socket t) {
		giocatore = t;
		try {
			t.setKeepAlive(true);
			t.setTcpNoDelay(true);
			t.setPerformancePreferences(0, 2, 1);
			
			inR = new InputReader( t.getInputStream() );
			outW = new OutputWriter( t.getOutputStream() );
			
			System.out.println( "Connection ok with: "+t.getRemoteSocketAddress() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
	}
	
	public void update(){
		update++;

		myActions.clear();
		createOggettiActions.clear();
		removeOggettiActions.clear();
		//read and execute client request
		Object t;
		while( ( t=inR.poll() )!=null){ //until there is input
			
			if (myself == null){ //first of all read login
				if (t instanceof Login){
					myself = (Login)t;
				}else{
					//error first input wasn't a login
					System.out.println("PLAYER ERROR: Not a login, ip:"+giocatore.getRemoteSocketAddress() );
					close();
				}
			}else{
				
				if (activeShip != -1){ //don't accept action without ship
					if (t instanceof Action){
						System.out.println("Executing action");
						Action a = ((Action)t);
						if ( a.run( myPossessoin.get(a.ownerID) ) ){
							synchronized (myActions) {
								LinkedList<Action> actionAtTurn = myActions.get( a.getExecTime() );
								if (actionAtTurn!=null){
									actionAtTurn.add(a);
								}else{
									actionAtTurn = new LinkedList<Action>();
									actionAtTurn.add(a);
									myActions.put(a.getExecTime(), actionAtTurn);
								}
							}
						}else{
							//hacking?!?!
							close();
						}
					}
				}else{
					if (t instanceof ShipRequest){
						System.out.println("Ship request arrived");
						createOggettiActions.add( (ShipRequest)t );
						break; //SPECIAL CASE: Don't try to execute any other action before shipRequest has been accepted
					}else{
						//Action without a ship, probably an hacking attempt
						System.out.print("PLAYER ERROR: Player:"+myself+" send action without ship selected, ip:"+giocatore.getRemoteSocketAddress() );
						close();
					}
				}
			}
		}
	}
	
	public boolean isClosed(){
		return ( giocatore.isClosed() || giocatore.isInputShutdown() || giocatore.isOutputShutdown() || !inR.isAlive() || !outW.isAlive() );
	}

	public void close() {
		//TODO: for every ship create a delete action 
		
		System.out.println( "Connection closed with: "+giocatore.getRemoteSocketAddress() );
		
		if (inR!= null)
			inR.close();
		
		if (outW!= null)
			outW.close();
		
		try {
			giocatore.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Object getLogin() {
		return myself;
	}

	public int getUpdate() {
		return update;
	}

	public void write(Object obj) {
		if (obj!=null)
			outW.write(obj);
	}
	
	public int getActiveShip(){
		return activeShip;
	}

	public void addShip(Ship s) {
		Oggetto2D old = myPossessoin.put(s.ID, s);
		if (old!=null){
			System.out.println("PLAYER ERROR: creating a new existent ship");
			//removeOggettiActions.add( new RemoveShip(old.ID) );
			close();
		}
	}

	public Oggetto2D getShip(int iD) {
		return myPossessoin.get(iD);
	}

	public void setActiveShip(int iD) {
		activeShip = iD;
	}

	public void removeOggetto(int id) {
		myPossessoin.remove(id);
	}

	public ShipRequest getCreateShip() {
		return createOggettiActions.poll();
	}

	public LinkedList<Action> getMyActions(long turn) {
		//Return the action of this turn and the old one
		/*
		LinkedList<Action> ris = new LinkedList<Action>();
		synchronized (myActions) {
			for (long id : myActions.keySet() ){
				if (id <= turn){
					ris.addAll(myActions.get(id));
					//myActions.remove(id);
				}
			}
			myActions.values().removeAll(ris);
		}
		return ris;
		*/
		LinkedList<Action> ris = new LinkedList<Action>();
		synchronized (myActions) {
			for ( LinkedList<Action> t:myActions.values() ){
				//if (id <= turn){
					ris.addAll(t);
					//myActions.remove(id);
				//}
			}
			myActions.values().removeAll(ris);
		}
		return ris;
	}

}
