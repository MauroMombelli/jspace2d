package server;

import java.util.Timer;

public class ServerMain {
	
	public static final long TURN_DURATION = 15;
	
	public static void main(String args[]){
		new ServerMain();
	}
	
	public ServerMain(){
		System.out.println("main begin");
		PortListener serverMain = new PortListener(5000);
		
		Engine motore = new Engine(serverMain);
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(motore, 0, TURN_DURATION);
		
		System.out.println("main end");
	}
	
}
