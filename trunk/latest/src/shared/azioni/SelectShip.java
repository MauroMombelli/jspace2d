package shared.azioni;

import java.io.Serializable;

import server.Player;
import shared.Oggetto2D;
import shared.PhysicWorld;

public class SelectShip extends Action implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectShip(int ID) {
		super(ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SelectShip){
			if ( ((SelectShip) obj).shipOwnerID==shipOwnerID )
				return true;
		}
		return false;
	}

	@Override
	public boolean run(PhysicWorld w) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean run(PhysicWorld w, Player p) {
		Oggetto2D a = p.getShip(shipOwnerID);
		if (a != null){
			p.setActiveShip(shipOwnerID);
			return true;
		}
		System.out.println("Error:"+p.getLogin()+" selected a ship not owned or unexistent");
		return false;
	}

}
