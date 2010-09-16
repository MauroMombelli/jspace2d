package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandProcessingThread extends ProcessingThread {
	LinkedBlockingQueue<CommandObject> commandQueue;
	private boolean alive = true;	
	
	public CommandProcessingThread(LinkedBlockingQueue<CommandObject> _commandQueue) {
		super();
		commandQueue = _commandQueue;
	}

	@Override
	public void run() {
		while(alive){

			CommandObject temp = null;

			try {
				temp = commandQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			_observableFlag.alertObservers(temp);							

		}
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
