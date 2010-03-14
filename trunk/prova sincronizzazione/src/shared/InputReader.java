package shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;

public class InputReader extends Thread{
	InputStream tempStream;
	
	ObjectInputStream inputStream;
	boolean connesso = false;
	LinkedList<Object> input = new LinkedList<Object>();
	
	public InputReader(InputStream inputStream) throws IOException {
		// TODO Auto-generated constructor stub
		tempStream = inputStream;
		start();
	}

	@Override
	public void run(){
		Object t;
		try {
			
			inputStream = new ObjectInputStream(tempStream);
			connesso = true;
			System.out.println("Input ok");
			
			while(connesso){
				t = inputStream.readObject();
				synchronized (input) {
					input.add( t );
				}
			}
		} catch (IOException e) {
			if (connesso)
				e.printStackTrace();
		} catch (ClassNotFoundException e) {
			if (connesso)
				e.printStackTrace();
		}finally{
			connesso = false;
		}
		System.out.println("Input chiuso");
	}
	
	public void close() {
		// TODO Auto-generated method stub
		connesso = false;
		interrupt();
		if (inputStream!=null)
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public Object poll() {
		if (size() > 10)
			System.out.println( "pending input: "+size() );
		synchronized (input) {
			return input.poll();
		}
	}

	public void addFirst(Object o) {
		synchronized (input) {
			input.addFirst(o);
		}
	}
	
	public int size(){
		return input.size();
	}
}
