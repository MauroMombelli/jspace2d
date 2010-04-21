package shared.azioni;

import java.io.Serializable;

import server.Player;

import shared.Oggetto2D;
import shared.PhysicWorld;

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
		Oggetto2D o = p.getShip(shipOwnerID); 
		if (o != null){
			System.out.println("Removing obj:"+shipOwnerID);
			w.removeBody( o.getBody(), shipOwnerID );
			p.removeOggetto(shipOwnerID);
			return true;
		}else{
			System.out.println("Player "+p.getLogin()+" tryed to delete not owned ship, disconnectiong!");
			p.close();
			return false;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ActionEngine){
			ActionEngine a = (ActionEngine)obj;
			if (a.shipOwnerID == shipOwnerID)
				return true;
		}
		return false;
	}

	@Override
	public boolean run(PhysicWorld w) {
		System.out.println("Removing obj:"+shipOwnerID);
		Oggetto2D o = w.get(shipOwnerID);
		w.removeBody( o.getBody(), shipOwnerID );
		return true;
	}

}
