package shared.azioni;

import java.io.Serializable;

import org.jbox2d.common.Vec2;

import server.Player;
import shared.PhysicWorld;
import shared.oggetti.Oggetto2D;

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
	public boolean run(PhysicWorld w){
		Oggetto2D o = w.get(shipOwnerID);
		if (o != null){
			if (o.getBody() != null){
				System.out.println("executing impulse: "+x+" "+y+" "+alpha);
				//System.out.println( "before: "+o.getInfoPosition() +" torque:"+ o.getBody().m_torque );
				o.getBody().applyImpulse( new Vec2(x,y), o.getBody().getWorldCenter() );
				o.getBody().applyTorque( alpha );
				//System.out.println( "after: "+o.getInfoPosition() +" torque:"+ o.getBody().m_torque );
				return true;
			}else{
				System.out.println("error executing action, null body");
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
			if (a.shipOwnerID == shipOwnerID)
				if (a.alpha == alpha)
					if (a.x == x)
						if (a.y == y)
							return true;
		}
		return false;
	}

	@Override
	public boolean run(PhysicWorld w, Player p) {
		Oggetto2D o = p.getShip(shipOwnerID);
		if (o != null){
			if (o.getBody() != null){
				System.out.println("executing impulse: "+x+" "+y+" "+alpha);
				//System.out.println( "before: "+o.getInfoPosition() +" torque:"+ o.getBody().m_torque );
				o.getBody().applyImpulse( new Vec2(x,y), o.getBody().getWorldCenter() );
				o.getBody().applyTorque( alpha );
				//System.out.println( "after: "+o.getInfoPosition() +" torque:"+ o.getBody().m_torque );
				return true;
			}else{
				System.out.println("error executing action, null body");
			}
		}else{
			System.out.println("error executing action, null object");			
		}
		return false;
	}
}
