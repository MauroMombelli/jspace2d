package model;

import GUI.supportComponents.ObservableFlag;

public abstract class AbstractModel {
public ObservableFlag _observableFlag;
	
	public AbstractModel(){
		_observableFlag = new ObservableFlag(this);
	}	
}
