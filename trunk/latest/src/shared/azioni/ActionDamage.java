package shared.azioni;

import server.Player;
import shared.PhysicWorld;
import shared.oggetti.Oggetto2D;

public class ActionDamage extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int damage;
	
	public ActionDamage(int ID, int damage) {
		super(ID);
		this.damage = damage;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ActionDamage){
			ActionDamage a = (ActionDamage)obj;
			if (a.shipID == shipID)
				if (a.damage == damage)
					return true;
		}
		return false;
	}

	@Override
	public boolean run(PhysicWorld w) {
		Oggetto2D a = w.get( shipID );
		if (a!= null){
			a.subLife(damage);
			System.out.println("Dameged "+a.ID+" for:"+damage);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean run(PhysicWorld w, Player p) {
		return run(w);
	}

}
