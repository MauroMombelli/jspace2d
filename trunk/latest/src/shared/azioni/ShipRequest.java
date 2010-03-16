package shared.azioni;

import java.io.Serializable;
import java.util.LinkedList;

import shared.Ship;


public class ShipRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ship getShip(int id, LinkedList<Action>allChanges) {
		return new Ship(id, allChanges);
	}

	public boolean isValid() {
		// TODO check if this constructor is ok
		return true;
	}

}
