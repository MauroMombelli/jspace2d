package shared.azioni;

import java.io.Serializable;

import shared.Oggetto2D;

public abstract class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4252933575712164693L;

	public int ownerID;
	long time;
	
	public Action(int ID){
		this.ownerID = ID;
	}
	
	public abstract boolean run(Oggetto2D p); //return true if action is executed with success

	public void setExecTime(long turn) {
		time = turn;
	}

	public long getExecTime() {
		return time;
	}
	
	public abstract boolean equals(Object obj);
	/*{
		
		if (obj instanceof Action)
			if ( ((Action)obj).ownerID == ownerID ){
				return true;
			}
				
		return false;
	}
	*/
	public int hashCode(){
		return ownerID;
	}
}
