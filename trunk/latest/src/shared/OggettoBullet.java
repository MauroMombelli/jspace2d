package shared;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.dynamics.Body;


public class OggettoBullet extends Oggetto2D {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5735099940575927022L;

	public OggettoBullet(int id) {
		super(id);
		//modelName="Bullet";
		//radius=0.4f;
	}

	public OggettoBullet(OggettoBullet o) {
		super(o);
		//modelName="Bullet";
		//radius=0.4f;
	}
	
	@Override
	public void createBody(Body body) {
		bodyContainer.myBody = body;
		CircleDef hull = new CircleDef();
        hull.restitution = 0.8f;
        hull.friction = 0;
        hull.radius = GLOBAL_VARIABLE.convertToPhysicEngineUnit( radius );
        hull.density = 20;
        hull.userData = this;
        bodyContainer.myBody.createShape(hull);
        
        bodyContainer.myBody.setMassFromShapes();
	}

}
