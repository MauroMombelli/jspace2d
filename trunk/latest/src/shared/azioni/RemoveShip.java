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
			w.removeBody( o.getBody(), shipOwnerID );
			p.removeOggetto(o.ID);
			return true;
		}else{
			System.out.println("Player "+p.getLogin()+" tryed to delete not owned ship, disconnectiong!");
			p.close();
			return false;
		}
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean run(PhysicWorld w) {
		Oggetto2D o = w.get(shipOwnerID);
		w.removeBody( o.getBody(), shipOwnerID );
		return true;
	}

}
