package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class PortListener extends Thread{
	
	ServerSocket server;
	LinkedList<Player> newPlayer = new LinkedList<Player>();
	
	public PortListener(int port) {
		try {
			server = new ServerSocket(port);
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		Socket t;
		server.setPerformancePreferences(0, 2, 1);
		try {
			while (true){
				t=server.accept();
				
				System.out.println( "Connection request from: "+t.getRemoteSocketAddress() );
				
				synchronized (newPlayer) {
					newPlayer.add( new Player(t) );
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Player poll() {
		// TODO Auto-generated method stub
		synchronized (newPlayer) {
			return newPlayer.poll();
		}
	}

}
