package jspace2d.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

public class FixtureCircle extends FixtureBlueprint {

	private final float radius;
	
	@SuppressWarnings("unused") //needed fo jackson serialization
	private FixtureCircle(){
		radius = 0;
	}

	public FixtureCircle(boolean isSensor, float restitution, float density, float friction, float radius) {
		super(isSensor, restitution, density, friction);
		
		this.radius = radius;
	}

	@Override
	Shape getShape() {
		CircleShape s = new CircleShape();
		s.setRadius(radius);
		return s;
	}

}
