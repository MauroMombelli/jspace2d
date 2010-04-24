package shared.oggetti;

import java.io.Serializable;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.dynamics.Body;

import shared.GLOBAL_VARIABLE;

public class Ship extends Oggetto2D implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Ship(int id) {
		super(id);
		modelName="Cone";
		forma.clear();
		forma.add( new SharedShape(4, SharedShape.Forme.CIRCLE, 20, 0) );
	}
	
	public Ship(Ship o) {
		super(o);
		modelName="Cone";
		forma.clear();
		forma.add( new SharedShape(4, SharedShape.Forme.CIRCLE, 20, 0) );
	}

	@Override
	public void createBody(Body body) {
		super.createBody(body);
        
        CircleDef radar = new CircleDef();
        radar.radius = GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 );
        radar.isSensor = true;
        bodyContainer.myBody.createShape(radar);
	}
}
