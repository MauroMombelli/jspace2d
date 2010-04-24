package shared.oggetti;

import org.jbox2d.dynamics.Body;

import shared.azioni.ActionDamage;
import shared.azioni.RemoveShip;

public class OggettoBullet extends Oggetto2D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5735099940575927022L;
	private int bulletDamage = 5;

	public OggettoBullet(int id) {
		super(id);
		
		modelName="Bullet1";
		
		SharedShape basic = new SharedShape(2, SharedShape.Forme.CIRCLE, 20, 0);
		forma.clear();
		forma.add( basic );
		globalRadius = basic.radius;
	}
	
	public OggettoBullet(OggettoBullet o) {
		super(o);
	}
	
	@Override
	public void createBody(Body body) {
		super.createBody(body);
		body.setBullet(true);
	}
	
	@Override
	public void collide(Oggetto2D obj1) {
		if (obj1!=null){ //null == wall
			System.out.println("Bullet "+ID+" is colliding with:"+obj1.ID);
			addAction( new ActionDamage(obj1.ID, bulletDamage) );
			//obj1.subLife(bulletDamage);
		}
		//addAction( new ActionLightShot(ID) );
		addAction( new RemoveShip(ID) );
	}
/*	
	@Override
	public void continueCollide(Oggetto2D obj1) {
		if (obj1!=null) //null == wall
			System.out.println("Bullet "+ID+" is continuing colliding with:"+obj1.ID);
		//addAction( new ActionLightShot(ID) );
		addAction( new RemoveShip(ID) );
	}
*/
}
