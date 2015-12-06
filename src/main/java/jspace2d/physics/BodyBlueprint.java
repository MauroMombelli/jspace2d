package jspace2d.physics;

import org.jbox2d.common.Vec2;

public class BodyBlueprint {

	private final float density;
	private final float friction;
	private final Vec2 size;
	private final boolean isDynamic;
	private final float restitution;
	
	public BodyBlueprint(Vec2 size, boolean isDynamic, float restitution, float density, float friction){
		this.density = density;
		this.friction = friction;
		this.size = size;
		this.isDynamic = isDynamic;
		this.restitution = restitution;
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

	public float getRestitution() {
		return restitution;
	}

}
