package shared.azioni;

import java.util.LinkedList;

import com.jme.math.FastMath;

import shared.Oggetto2D;
import shared.OggettoBullet;
import shared.PhysicWorld;

public class ActionLightShot extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7184373712240566017L;

	public ActionLightShot(int ID) {
		super(ID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ActionLightShot)
			if ( ((ActionLightShot)obj).ownerID == ownerID )
				return true;
		return false;
	}

	@Override
	public boolean run(Oggetto2D p, PhysicWorld w, LinkedList<Action> a) {
		float x, y, alpha, speed = 1, sinA, cosA;
		
		alpha = p.getBody().getAngle();
		sinA = FastMath.sin(alpha);
		cosA = FastMath.cos(alpha);
		x = p.getBody().getPosition().x+0.001f*sinA;
		y = p.getBody().getPosition().y+0.001f*cosA;
		
		
		OggettoBullet b = new OggettoBullet( w.getNextIndex() );
		
		w.addNew(b, x, y, alpha);
		
		x = speed*sinA;
		y = speed*cosA;
		
		ActionEngine act = new ActionEngine(b.ID, x, y, alpha);

		act.run(b, w, a);
		if (a==null)
			a = new LinkedList<Action>();
		a.add(act);
		
		return true;
	}

}
