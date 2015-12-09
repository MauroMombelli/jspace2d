package jspace2d.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

public class FixtureRectangle extends FixtureBlueprint {

	private final Vec2 size;

	public FixtureRectangle(boolean isSensor, float restitution, float density, float friction, Vec2 size) {
		super(isSensor, restitution, density, friction);
		
		this.size = size;
	}

	@Override
	Shape getShape() {
		PolygonShape s = new PolygonShape();
		s.setAsBox(size.x/2, size.y/2);
		return s;
	}

}
