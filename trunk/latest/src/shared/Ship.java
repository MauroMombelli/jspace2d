package shared;

import java.io.Serializable;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.dynamics.Body;

public class Ship extends Oggetto2D implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Ship(int id) {
		super(id);
		modelName="astronave 2.xml";
	}
	
	@Override
	public void createBody(Body body) {
		bodyContainer.myBody = body;
		CircleDef hull = new CircleDef();
        hull.restitution = 0.8f;
        hull.friction = 0.2f;
        hull.radius = GLOBAL_VARIABLE.convertToPhysicEngineUnit( 12 );
        hull.density = 1;
        hull.userData = this;
        bodyContainer.myBody.createShape(hull);
        
        CircleDef radar = new CircleDef();
        radar.radius = GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 );
        radar.userData = this;
        radar.isSensor = true;
        bodyContainer.myBody.createShape(radar);
	}
}
