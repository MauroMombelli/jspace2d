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
		modelName="Cone";
		radius=GLOBAL_VARIABLE.convertToPhysicEngineUnit( 4 );
		density = 20;
	}
	
	public Ship(Ship o) {
		super(o);
		modelName="Cone";
		radius=GLOBAL_VARIABLE.convertToPhysicEngineUnit( 4 );
		density = 20;
	}

	@Override
	public void createBody(Body body) {
		super.createBody(body);
        
        CircleDef radar = new CircleDef();
        radar.radius = GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 );
        radar.userData = this;
        radar.isSensor = true;
        bodyContainer.myBody.createShape(radar);
	}
}
