package shared.specialActions;

import java.util.HashMap;
import java.util.LinkedList;

import server.Player;
import shared.Oggetto2D;
import shared.PhysicWorld;

public class RemoveShip {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6173945333888939768L;

	int ID;
	
	public RemoveShip(int ID) {
		this.ID = ID;
	}
	
	public void run(PhysicWorld w, HashMap<Integer, Oggetto2D> allObj, Player p, LinkedList<Oggetto2D> removedOggetti2D) {
		Oggetto2D delete = allObj.remove(ID);
		if (delete!=null){
			w.removeBody( delete.getBody() );
			p.removeOggetto(ID);
			removedOggetti2D.add(delete);
		}
	}

}
