package server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import shared.AllMap;
import shared.Login;
import shared.NewTurn;
import shared.azioni.Action;
import shared.azioni.RemoveShip;
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
	
	HashMap<Integer, Oggetto2D> myPossession = new HashMap<Integer, Oggetto2D>();
	
	LinkedList<Action> myPendingAction = new LinkedList<Action>();
	LinkedList<Action> myExecutedAction = new LinkedList<Action>();

	private HashMap<Integer, Oggetto2D> myNewPossession = new HashMap<Integer, Oggetto2D>();
	
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
		for (Oggetto2D o : myPossession.values()){
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
						
					if (a.shipID == -1 || myPossession.containsKey( a.shipID ) ){ //if is an action on new object(actually only new obj request), or on a possesed obj 
						myPendingAction.add(a);
					}else{
						System.out.println("Player request action on object not owned: "+myself+" "+a.shipID);
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
		Oggetto2D old = myPossession.put(s.ID, s);
		myNewPossession.put(s.ID, s);
		if (old!=null){
			System.out.println("PLAYER ERROR: creating a new existent ship");
			//myPendingAction.add( new RemoveShip(old.ID) );
			close();
		}
	}

	public Oggetto2D getShip(int iD) {
		return myPossession.get(iD);
	}

	public void setActiveShip(int iD) {
		activeShip = iD;
		//send the action to client
		write( new SelectShip(iD) );
	}

	public void removeOggetto(int id) {
		myPossession.remove(id);
	}
	
	public void writeNewTurn(long actualTurn){
		NewTurn n = new NewTurn(actualTurn);
		
		n.addAll(myExecutedAction);//personal actions
		myExecutedAction.clear();
		
		
		for ( Oggetto2D myShip : myNewPossession.values() ){
			if (activeShip == myShip.ID){
				n.add( myShip, myShip.getInfoPosition() );
				System.out.println("New obj in my possession, id: "+myShip.ID);
			}
		}
		/*
		if (activeShip!=-1)
			n.add(myPossession.get(activeShip), myPossession.get(activeShip).getInfoPosition());
		*/
		myNewPossession.clear();
		
		
		for ( Oggetto2D myShip : myPossession.values() ){
			if (activeShip == myShip.ID)
				n.addAll( myShip.getActions() );
			
			
			for ( Oggetto2D o : myShip.getOutOfRadar() ){
				if ( o.getLife()>0 && !myPossession.containsKey(o.ID) )
					n.add( new RemoveShip(o.ID) );
			}
			
			for ( Oggetto2D o : myShip.getNewOnRadar() ){
				n.add( o, o.getInfoPosition() );
				System.out.println("New obj on radar, id: "+o.ID);
			}
			/*
			for ( Oggetto2D o : myShip.getRadar() ){
				n.addAll( o.getActions() );
			}
			*/
			myShip.clearActions();
			myShip.updateRadar();
		}
		if ( !n.isEmpty() )
			write(n);
	}
	
	public Action pollPendingActions() {
		return myPendingAction.poll();
	}
	
	public String getIP(){
		return giocatore.getRemoteSocketAddress().toString();
	}

	public void addExecutedAction(Action tAct) {
		myExecutedAction.add(tAct);
		
	}

	public void setExecutedActionToOggetti() {
		Oggetto2D o = null;
		Action tAct = null;
		LinkedList<Action> personalAction = new LinkedList<Action>();
		while ( (tAct=myExecutedAction.poll())!=null ){
			o= myPossession.get( tAct.shipID );
			if (o!=null){
				o.addAction(tAct);
			}else{
				personalAction.add(tAct);
			}
		}
		myExecutedAction.addAll(personalAction);
	}

	public void writeAllMap(long actualTurn) {
		AllMap n = new AllMap(actualTurn);
		
		for ( Oggetto2D myShip : myPossession.values() ){
			n.add( myShip.getInfoPosition() );
			
			for ( Oggetto2D o : myShip.getRadar() ){
				n.add( o.getInfoPosition() );
			}
		}
		write(n);

	}

}
