package jspace2d.physics;

import org.jbox2d.common.Vec2;

public class BodyBlueprint {

	private final Vec2 pos;
	private final float angle;
	private final float density;
	private final float friction;
	private final Vec2 size;
	private final boolean isDynamic;
	
	public BodyBlueprint(Vec2 pos, float angle, float density, float friction, Vec2 size, boolean isDynamic){
		this.pos = pos;
		this.angle = angle;
		this.density = density;
		this.friction = friction;
		this.size = size;
		this.isDynamic = isDynamic;
	}
	
	public Vec2 getPos() {
		return pos;
	}

	public boolean isDynamic() {
		return isDynamic;
	}

	public Vec2 getSize() {
		return size;
	}

	public float getDensity() {
		return density;
	}

	public float getFriction() {
		return friction;
	}

	public float getAngle() {
		return angle;
	}

}
