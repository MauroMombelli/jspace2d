package server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import shared.Login;
import shared.azioni.Action;
import shared.azioni.SelectShip;
import shared.net.InputReader;
import shared.net.OutputWriter;
import shared.oggetti.Oggetto2D;

public class Player {
	Socket giocatore;
	InputReader inR;
	OutputWriter outW;
	
	Login myself=null;
	
	int activeShip = -1;
	
	int update= 0;
	
	HashMap<Integer, Oggetto2D> myPossessoin = new HashMap<Integer, Oggetto2D>();
	
	LinkedList<Action> myPendingAction = new LinkedList<Action>();
	LinkedList<Action> myExecutedAction = new LinkedList<Action>();
	//TODO: 
//	LinkedList<RemoveShip> removeOggettiActions = new LinkedList<RemoveShip>();
	//end todo
//	LinkedList<ShipRequest> createOggettiActions = new LinkedList<ShipRequest>();
	
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
	
	public void updatePendingAction(){
		for (Oggetto2D o : myPossessoin.values()){
			myPendingAction.addAll( o.getAndDestroyActions() );
		}
	}
	
	public void updateInput(){
		update++;

		//myActions.clear();
//		createOggettiActions.clear();
//		removeOggettiActions.clear();
		//read and execute client request
		
		Object t;
		Action a;
		int actionThisTurn=0;
		
		updatePendingAction();
		
		while( ( t=inR.poll() )!=null){ //until there is input
			
			actionThisTurn++;
			
			if (actionThisTurn > 5){
				System.out.println("Disconnecting player because too much actions: "+myself);
				close();
			}
			
			if (myself == null){ //first of all read login
				login(t);
			}else{
				if (t instanceof Action){						
					System.out.println("Executing action");
					a = ((Action)t);
						
					if (a.shipOwnerID == -1 || myPossessoin.containsKey( a.shipOwnerID ) ){ //if is an action on new object(actually only new obj request), or on a possesed obj 
						myPendingAction.add(a);
					}else{
						System.out.println("Player request action on object not owned: "+myself+" "+a.shipOwnerID);
						//close();
					}
				}
			}
		}
	}

	private void login(Object t) {
		if (t instanceof Login){
			myself = (Login)t;
		}else{
			//error first input wasn't a login
			System.out.println("PLAYER ERROR: Not a login, ip:"+giocatore.getRemoteSocketAddress() );
			close();
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

	public Login getLogin() {
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

	public void addOggetto(Oggetto2D s) {
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
		//send the action to client
		write( new SelectShip(iD) );
	}

	public void removeOggetto(int id) {
		myPossessoin.remove(id);
	}
/*
	public ShipRequest getCreateShip() {
		return createOggettiActions.poll();
	}
*//*
	public LinkedList<Action> getMyActions() {
		LinkedList<Action> ris = new LinkedList<Action>();
		synchronized (myExecutedAction) {
			ris.addAll(myExecutedAction);
			myExecutedAction.clear();
		}
		return ris;
	}
	*/
	public Action pollPendingActions() {
		return myPendingAction.poll();
	}
	
	public String getIP(){
		return giocatore.getRemoteSocketAddress().toString();
	}

	public void addExecutedAction(Action tAct) {
		myExecutedAction.add(tAct);
	}

}
