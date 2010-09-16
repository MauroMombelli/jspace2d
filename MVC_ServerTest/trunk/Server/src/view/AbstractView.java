package view;

import GUI.supportComponents.ObservableFlag;


public abstract class AbstractView {
	public ObservableFlag _observableFlag;
	
	public AbstractView(){
		_observableFlag = new ObservableFlag(this);
	}		
	
}
