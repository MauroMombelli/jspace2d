package jspace2d.physics;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.FixtureDef;

public abstract class FixtureBlueprint {
	
	private final float restitution;
	public final float density;
	public final float friction;
	
	public final boolean isSensor;
	
	public FixtureBlueprint(boolean isSensor, float restitution, float density, float friction){
		this.isSensor = isSensor;
		this.restitution = restitution;
		this.density = density;
		this.friction = friction;
	}

	public FixtureDef getFixture() {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape= getShape();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		fixtureDef.isSensor = isSensor;
		
		return fixtureDef;
	}
	
	abstract Shape getShape();
}
