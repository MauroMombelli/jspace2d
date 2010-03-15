package shared;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class OutputWriter extends Thread{
	OutputStream tempStream;
	
	ObjectOutputStream outputStream;
	boolean connesso = false;
	LinkedBlockingQueue<Object> output = new LinkedBlockingQueue<Object>();
	
	public OutputWriter(OutputStream outputStream) throws IOException {
		// TODO Auto-generated constructor stub
		tempStream = outputStream;
		start();
	}

	public void write(Object o){
		if (output.size() > 0)
			System.out.println("Pending obj: "+output.size());
		output.add(o);
	}
	
	@Override
	public void run(){
		Object t;
		try {
			outputStream = new ObjectOutputStream(tempStream);
			connesso = true;
			System.out.println("Output ok");
			
			while (connesso){
				t = output.take();
				outputStream.writeObject(t);
				outputStream.flush();
			}
		} catch (InterruptedException e) {
			if (connesso)
				e.printStackTrace();
		} catch (IOException e) {
			if (connesso)
				e.printStackTrace();
		}finally{
			connesso = false;
			close();
		}
		System.out.println("Output chiuso");
	}

	public void close() {
		// TODO Auto-generated method stub
		connesso = false;
		interrupt();
		if (outputStream!=null)
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
