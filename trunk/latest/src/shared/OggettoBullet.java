package shared;

import shared.azioni.RemoveShip;

public class OggettoBullet extends Oggetto2D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5735099940575927022L;

	public OggettoBullet(int id) {
		super(id);
		modelName="Bullet1";
		radius=GLOBAL_VARIABLE.convertToPhysicEngineUnit( 2f );
		density=20;
	}

	public OggettoBullet(OggettoBullet o) {
		super(o);
		modelName="Bullet1";
		radius=GLOBAL_VARIABLE.convertToPhysicEngineUnit( 2f );
		density=20;
	}
	
	@Override
	public void collide(Oggetto2D obj1) {
		if (obj1!=null) //null == wall
			System.out.println("Bullet "+ID+" is colliding with:"+obj1.ID);
		//addAction( new ActionLightShot(ID) );
		addAction( new RemoveShip(ID) );
	}
	
	@Override
	public void continueCollide(Oggetto2D obj1) {
		if (obj1!=null) //null == wall
			System.out.println("Bullet "+ID+" is continuing colliding with:"+obj1.ID);
		//addAction( new ActionLightShot(ID) );
		addAction( new RemoveShip(ID) );
	}

}
