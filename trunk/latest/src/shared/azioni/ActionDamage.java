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
			if (a.shipOwnerID == shipOwnerID)
				if (a.damage == damage)
					return true;
		}
		return false;
	}

	@Override
	public boolean run(PhysicWorld w) {
		Oggetto2D a = w.get( shipOwnerID );
		if (a!= null){
			a.subLife(damage);
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
