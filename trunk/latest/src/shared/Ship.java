package shared;

import java.util.LinkedList;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.dynamics.Body;

import shared.azioni.Action;

public class Ship extends Oggetto2D{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Ship(int id, LinkedList<Action> allChanges) {
		super(id, allChanges);
		modelName="astronave-jme.xml";
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void createBody(Body body) {
		t.myBody = body;
		CircleDef hull = new CircleDef();
        hull.restitution = 0.8f;
        hull.friction = 0.2f;
        hull.radius = GLOBAL_VARIABLE.convertToPhysicEngineUnit( 10 );
        hull.density = 1;
        hull.userData = this;
        t.myBody.createShape(hull);
        
        CircleDef radar = new CircleDef();
        radar.radius = GLOBAL_VARIABLE.convertToPhysicEngineUnit( 10 );
        radar.userData = this;
        radar.isSensor = true;
        t.myBody.createShape(radar);
	}
}
