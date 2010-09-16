package GUI.supportComponents;

import java.util.Observable;

public class ObservableFlag extends Observable{
	private Object passing;	
	private Object owner;

	public ObservableFlag(Object _owner){		
		super();
		setOwner(_owner);
	}

	public void alertObservers(Object j){		
		passing = j;
		setChanged();
		notifyObservers(j);
	}	

	protected Object getObject(){		
		return passing;
	}

	public void setOwner(Object owner) {
		this.owner = owner;
	}

	public Object getOwner() {
		return owner;
	}
}
