package shared.azioni;

import org.jbox2d.common.Vec2;

import shared.Oggetto2D;

public class ActionEngine extends Action{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3137102960165321853L;

	float x, y, alpha;
	
	public ActionEngine(int ID, float x, float y, float alpha){
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.alpha = alpha;
	}
	
	@Override
	public void run(Oggetto2D p){
		if (p != null){
			if (p.getBody() != null){
				p.getBody().applyImpulse( new Vec2(x,y), p.getBody().getWorldCenter() );
				p.getBody().applyTorque( alpha );
			}else{
				System.out.println("eroor executing action, null body");
			}
		}else{
			System.out.println("eroor executing action, null object");			
		}
	}
}
