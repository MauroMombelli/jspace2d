package shared.oggetti;

import java.io.Serializable;

import shared.GLOBAL_VARIABLE;

public class SharedShape implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected float radius;
	protected float density;
	protected float friction;
	protected float restitution = 0.8f;
	
	public enum Forme { CIRCLE, RECTANGLE }
	protected Forme forma = Forme.CIRCLE; //0=circle,  

	public SharedShape(float realRadius, Forme forma, float density, float friction){
		 radius=GLOBAL_VARIABLE.convertToPhysicEngineUnit( realRadius );
		 
		 this.density = density;
		 this.friction = friction;
	}
}
