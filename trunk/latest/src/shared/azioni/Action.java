package shared.azioni;

import java.io.Serializable;
import java.util.HashMap;

import shared.Oggetto2D;
import shared.PhysicWorld;

public abstract class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4252933575712164693L;

	public int ID;
	
	public Action(int ID){
		this.ID = ID;
	}
	
	public abstract void run(Oggetto2D p, PhysicWorld w);
	
}
