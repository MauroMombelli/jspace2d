package jspace2d.physics;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.FixtureDef;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@classType")
public abstract class FixtureBlueprint {
	
	private final float restitution;
	public final float density;
	public final float friction;
	
	public final boolean isSensor;
	
	@SuppressWarnings("unused") //needed fo jackson serialization
	FixtureBlueprint(){
		restitution = 0;
		density = 0;
		friction = 0;
		isSensor = false;
	}
	
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
