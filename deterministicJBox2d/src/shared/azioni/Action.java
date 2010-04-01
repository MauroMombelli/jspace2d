package shared.azioni;

import java.io.Serializable;

import shared.Oggetto2D;

public abstract class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4252933575712164693L;

	public int ID;
	
	public Action(int ID){
		this.ID = ID;
	}
	
	public abstract boolean run(Oggetto2D p); //return true if action is executed with success
	
}
