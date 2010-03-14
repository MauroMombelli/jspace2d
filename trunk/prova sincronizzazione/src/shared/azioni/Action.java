package shared.azioni;

import java.io.Serializable;

import shared.Oggetto2D;

public abstract class Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4252933575712164693L;

	public int ID;
	
	public abstract void run(Oggetto2D p);
	
}
