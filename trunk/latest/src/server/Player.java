package server;

import java.io.IOException;
import java.net.Socket;

import shared.InputReader;
import shared.Login;
import shared.OutputWriter;
import shared.azioni.ShipRequest;

public class Player {
	Socket giocatore;
	InputReader inR;
	OutputWriter outW;
	
	Login myself=null;
	
	ShipRequest activeShip;
	
	int update= 0;
	
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
		
		//read and execute client request
		Object t;
		while( ( t=inR.poll() )!=null){ //until there is input
			
			if (myself == null){ //first of all read login
				if (t instanceof Login){
					myself = (Login)t;
				}else{
					//error first input wasn't a login
					close();
				}
				if (t instanceof ShipRequest){
					ShipRequest ship = (ShipRequest)t;
					if ( ship.isValid() ){
						activeShip = ship;
					}else{
						//Probable hacking
						close();
					}
				}
			}else{
				//TODO: read and execute action
			}
		}
	}
	
	public boolean isClosed(){
		return ( giocatore.isClosed() || giocatore.isInputShutdown() || giocatore.isOutputShutdown() || !inR.isAlive() || !outW.isAlive() );
	}

	public void close() {
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
	
	public ShipRequest getActiveShip(){
		return activeShip;
	}

}
