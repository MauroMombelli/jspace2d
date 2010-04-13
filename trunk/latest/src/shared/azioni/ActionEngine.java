package shared.azioni;

import java.io.Serializable;

import org.jbox2d.common.Vec2;

import shared.Oggetto2D;
import shared.PhysicWorld;

public class ActionEngine extends Action implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3137102960165321853L;

	float x=0, y=0, alpha=0;
	
	public ActionEngine(int ID, float x, float y, float alpha){
		super(ID);
		this.x = x;
		this.y = y;
		this.alpha = alpha;
	}
	
	@Override
	public boolean run(Oggetto2D p, PhysicWorld w){
		if (p != null){
			if (p.ID==ownerID){
				if (p.getBody() != null){
					System.out.println("executing action: "+x+" "+y+" "+alpha);
					System.out.println( "before: "+p.getInfoPosition() +" torque:"+ p.getBody().m_torque );
					p.getBody().applyImpulse( new Vec2(x,y), p.getBody().getWorldCenter() );
					p.getBody().applyTorque( alpha );
					System.out.println( "after: "+p.getInfoPosition() +" torque:"+ p.getBody().m_torque );
					return true;
				}else{
					System.out.println("error executing action, null body");
				}
			}else{
				System.out.println("error executing action, wrong ID");
			}
		}else{
			System.out.println("error executing action, null object");			
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ActionEngine){
			ActionEngine a = (ActionEngine)obj;
			if (a.ownerID == ownerID)
				if (a.alpha == alpha)
					if (a.x == x)
						if (a.y == y)
							return true;
		}
		return false;
	}
}
