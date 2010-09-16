package controller;

import GUI.supportComponents.ObservableFlag;

public abstract class ProcessingThread implements Runnable {
	public ObservableFlag _observableFlag;
	
	public ProcessingThread(){
		_observableFlag = new ObservableFlag(this);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
