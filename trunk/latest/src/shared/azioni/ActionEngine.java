package shared.azioni;

import java.io.Serializable;

import org.jbox2d.common.Vec2;

import server.Player;
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
	public boolean run(Oggetto2D o, PhysicWorld w){
		if (o != null){
			if (o.ID==ownerID){
				if (o.getBody() != null){
					System.out.println("executing action: "+x+" "+y+" "+alpha);
					System.out.println( "before: "+o.getInfoPosition() +" torque:"+ o.getBody().m_torque );
					o.getBody().applyImpulse( new Vec2(x,y), o.getBody().getWorldCenter() );
					o.getBody().applyTorque( alpha );
					System.out.println( "after: "+o.getInfoPosition() +" torque:"+ o.getBody().m_torque );
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

	@Override
	public boolean run(Oggetto2D o, PhysicWorld w, Player p) {
		return run(o, w);
	}
}
