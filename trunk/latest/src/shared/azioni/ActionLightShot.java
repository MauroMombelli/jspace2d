package shared.azioni;

import com.jme.math.FastMath;

import server.Player;
import shared.Oggetto2D;
import shared.OggettoBullet;
import shared.PhysicWorld;

public class ActionLightShot extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float bulletSpeed=0.01f;

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
			sinA = FastMath.sin(a) + FastMath.PI/2;
			cosA = FastMath.cos(a) + FastMath.PI/2;
			
			x = o.getBody().getWorldCenter().x + ( o.getRadius()+b.getRadius() ) *cosA;
			y = o.getBody().getWorldCenter().y + ( o.getRadius()+b.getRadius() ) *sinA;
			
			if (w.addNew(b, x, y, a) != null){
				x = bulletSpeed*cosA;
				y = bulletSpeed*sinA;
				ActionEngine tAct = new ActionEngine(b.ID, x, y, a);
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
			sinA = FastMath.sin(a) + FastMath.PI/2;
			cosA = FastMath.cos(a) + FastMath.PI/2;

			x = o.getBody().getPosition().x + ( o.getRadius()+b.getRadius() ) *cosA;
			y = o.getBody().getPosition().y + ( o.getRadius()+b.getRadius() ) *sinA;
			
			if (w.addNew(b, x, y, a) != null){
				p.addOggetto(b);
				x = bulletSpeed*cosA;
				y = bulletSpeed*sinA;
				ActionEngine tAct = new ActionEngine(b.ID, x, y, a);
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
