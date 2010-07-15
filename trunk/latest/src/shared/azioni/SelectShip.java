package shared.azioni;

import java.io.Serializable;

import server.Player;
import shared.PhysicWorld;
import shared.oggetti.Oggetto2D;

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
			if ( ((SelectShip) obj).shipID==shipID )
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
		Oggetto2D a = p.getShip(shipID);
		if (a != null){
			p.setActiveShip(shipID);
			System.out.println(p.getLogin()+" selected ship:"+shipID);
			return true;
		}
		System.out.println("Error:"+p.getLogin()+" selected a ship not owned or unexistent");
		return false;
	}

}
