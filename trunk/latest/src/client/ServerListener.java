package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;

import shared.InputReader;
import shared.Login;
import shared.OutputWriter;
import shared.TurnDuration;

public class ServerListener extends Thread{
	Socket s;
	private InputReader inR;
	private OutputWriter outW;
	ClientEngine engine;
	
	public ServerListener(String ip, int port) {
		//InetSocketAddress proxy = new InetSocketAddress("217.160.200.51", 1080);
		//Proxy p = new Proxy(Proxy.Type.SOCKS, proxy);
		//s = new Socket(p);
		
		s = new Socket();
		try {
			InetSocketAddress ind = new InetSocketAddress(ip, port);
			s.setKeepAlive(true);
			s.setPerformancePreferences(0, 2, 1);
			s.setTcpNoDelay(true);
			s.connect(ind);
			
			inR = new InputReader( s.getInputStream() );
			outW = new OutputWriter( s.getOutputStream() );
			
			
			start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isClosed(){
		return ( s.isClosed() || s.isInputShutdown() || s.isOutputShutdown() || !inR.isAlive() || !outW.isAlive() );
	}
	
	public void run(){
		//login, just for debug
		Login l = new Login("nome", "miapassword");
		outW.write(l);
		Object in = null;
		try {
			while ( in == null && !isClosed() ){
				sleep(100);
				in = inR.poll();
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (in instanceof TurnDuration){
			Timer t = new Timer();
			TurnDuration td = (TurnDuration)in;
			engine = new ClientEngine(this, td.actualTurn, td.turnDuration);
			t.scheduleAtFixedRate(engine, 0, td.turnDuration );
		}else{
			close();
		}

	}

	public void close() {
		System.out.println( "Connection closed" );
		
		if (inR!= null)
			inR.close();
		
		if (outW!= null)
			outW.close();
		
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object poll() {
		return inR.poll();
	}

	public void addFirst(Object o) {
		inR.addFirst(o);
	}

	public int inputSize() {
		return inR.size();
	}

	public void write(Object obj) {
		outW.write(obj);
	}

}
