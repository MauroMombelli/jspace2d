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

	float x, y, alpha;
	
	public ActionEngine(int ID, float x, float y, float alpha){
		super(ID);
		this.x = x;
		this.y = y;
		this.alpha = alpha;
	}
	
	@Override
	public void run(Oggetto2D p, PhysicWorld w){
		if (p != null){
			if (p.getBody() != null){
				p.getBody().applyImpulse( new Vec2(x,y), p.getBody().getWorldCenter() );
				p.getBody().applyTorque( alpha );
			}else{
				System.out.println("error executing action, null body");
			}
		}else{
			System.out.println("error executing action, null object");			
		}
	}
}
