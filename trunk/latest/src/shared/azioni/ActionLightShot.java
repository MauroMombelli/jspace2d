package shared.azioni;

import com.jme.math.FastMath;

import server.Player;
import shared.PhysicWorld;
import shared.oggetti.Oggetto2D;
import shared.oggetti.OggettoBullet;

public class ActionLightShot extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float bulletSpeed=5f;

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
		
		return createShot(o, w, null);
	}

	@Override
	public boolean run(PhysicWorld w, Player p) {
		if (p!=null){
			Oggetto2D o = p.getShip(shipOwnerID);
			return createShot(o, w, p);
		}else{
			System.out.println("Error, non existent player tryed to shot");
			return false;
		}
		
	}
	
	private boolean createShot(Oggetto2D o, PhysicWorld w, Player p){
		if (o != null){
			OggettoBullet b = new OggettoBullet( w.getNextIndex() );
			float x, y, a, sinA, cosA;
			
			
			a = o.getBody().getAngle();
			sinA = FastMath.sin(a + FastMath.PI/2);
			cosA = FastMath.cos(a + FastMath.PI/2);

			x = o.getBody().getPosition().x - ( o.getRadius()+b.getRadius()+0.0001f ) *cosA;
			y = o.getBody().getPosition().y - ( o.getRadius()+b.getRadius()+0.0001f ) *sinA;
			System.out.println("rad1:"+o.getRadius()+" rad2:"+b.getRadius());
			
			if (w.addNew(b, x, y, a) != null){
				if (p!=null)
					p.addOggetto(b);
				x = -bulletSpeed*cosA+o.getBody().getLinearVelocity().x;
				y = -bulletSpeed*sinA+o.getBody().getLinearVelocity().y;
				ActionEngine tAct = new ActionEngine(b.ID, x, y, a);
				if ( ! tAct.run(w) ){
					/*
					if (p!=null)
						p.addAction(tAct);
					*/
					b.addAction(tAct);
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
