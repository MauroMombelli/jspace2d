package shared.azioni;

import com.jme.math.FastMath;

import server.Player;
import shared.GLOBAL_VARIABLE;
import shared.Oggetto2D;
import shared.OggettoBullet;
import shared.PhysicWorld;

public class ActionLightShot extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float bulletSpeed=0.1f;

	public ActionLightShot(int ID) {
		super(ID);
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
		
		Oggetto2D o = w.get(shipOwnerID);
		if (o != null){
			float x, y, a, sinA, cosA;
			OggettoBullet b = new OggettoBullet( w.getNextIndex() );
			
			a = o.getBody().getAngle();
			sinA = FastMath.sin(a);
			cosA = FastMath.cos(a);
			
			//x = o.getBody().getPosition().x + ( o.getRadius()+b.getRadius() )*sinA;
			//y = o.getBody().getPosition().y + ( o.getRadius()+b.getRadius() )*cosA;
			x = o.getBody().getPosition().x + GLOBAL_VARIABLE.convertToPhysicEngineUnit( o.getRadius()+b.getRadius() )*sinA;
			y = o.getBody().getPosition().y + GLOBAL_VARIABLE.convertToPhysicEngineUnit ( o.getRadius()+b.getRadius() )*cosA;
			
			if (w.addNew(b, x, y, a) != null){
				x = bulletSpeed+sinA;
				y = bulletSpeed+cosA;
				ActionEngine tAct = new ActionEngine(b.ID, x, y, 0);
				if ( ! tAct.run(w) ){
					System.out.println("error moving the bullet!");
					return false;
				}
				return true;
			}else{
				System.out.println("error creating the bullet in the world");
			}
		}else{
			System.out.println("error executing action, null object");			
		}
		return false;
	}

	@Override
	public boolean run(PhysicWorld w, Player p) {
		Oggetto2D o = p.getShip(shipOwnerID);
		if (o != null){
			float x, y, a, sinA, cosA;
			OggettoBullet b = new OggettoBullet( w.getNextIndex() );
			
			a = o.getBody().getAngle();
			sinA = FastMath.sin(a);
			cosA = FastMath.cos(a);
			
			x = o.getBody().getPosition().x + GLOBAL_VARIABLE.convertToPhysicEngineUnit( o.getRadius()+b.getRadius()*sinA );
			y = o.getBody().getPosition().y + GLOBAL_VARIABLE.convertToPhysicEngineUnit ( o.getRadius()+b.getRadius()*cosA );
			//x = o.getBody().getPosition().x + 1*sinA;
			//y = o.getBody().getPosition().y + 1*cosA;
			
			if (w.addNew(b, x, y, a) != null){
				p.addOggetto(b);
				x = bulletSpeed+cosA;
				y = bulletSpeed+sinA;
				ActionEngine tAct = new ActionEngine(b.ID, x, y, 0);
				if ( ! tAct.run(w) ){
					p.addAction(tAct);
					System.out.println("error moving the bullet!");
					return false;
				}
				return true;
			}else{
				System.out.println("error creating the bullet in the world");
			}
		}else{
			System.out.println("error executing action, null object");			
		}
		return false;
	}

}
