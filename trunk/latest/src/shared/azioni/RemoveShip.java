package shared.azioni;

import java.io.Serializable;

import server.Player;

import shared.PhysicWorld;
import shared.oggetti.Oggetto2D;

public class RemoveShip extends Action implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6173945333888939768L;
	
	public RemoveShip(int ID) {
		super(ID);
	}
	
	@Override
	public boolean run(PhysicWorld w, Player p) {
		Oggetto2D o = p.getShip(shipID); 
		if (o != null){
			System.out.println("Removing obj:"+shipID);
			w.removeBody( o.getBody(), shipID );
			p.removeOggetto(shipID);
			return true;
		}else{
			System.out.println("Player "+p.getLogin()+" tryed to delete not owned ship! "+shipID);
			//p.close();
			return false;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ActionEngine){
			ActionEngine a = (ActionEngine)obj;
			if (a.shipID == shipID)
				return true;
		}
		return false;
	}

	@Override
	public boolean run(PhysicWorld w) {
		System.out.println("Removing obj:"+shipID);
		Oggetto2D o = w.get(shipID);
		//if (o != null)
			w.removeBody( o.getBody(), shipID );
		//else
			//System.out.println("Removing obj was null");
		return true;
	}

}
