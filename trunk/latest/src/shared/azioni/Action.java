package shared.azioni;

import java.io.Serializable;

import server.Player;
import shared.PhysicWorld;

public abstract class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4252933575712164693L;

	public int shipID;
	long time;
	
	public Action(int ID){
		this.shipID = ID;
	}
	
	public abstract boolean run(PhysicWorld w); //return true if action is executed with success
	
	public abstract boolean run(PhysicWorld w, Player p); //return true if action is executed with success

	public void setExecTime(long turn) {
		time = turn;
	}

	public long getExecTime() {
		return time;
	}
	
	public abstract boolean equals(Object obj);

	public int hashCode(){
		return shipID;
	}
}
