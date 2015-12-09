package jspace2d.gui;

import org.jbox2d.common.Vec2;

public abstract class ActorGui {
	public final Vec2 pos;
	public float angle;
	public final long id;
	
	public ActorGui(long id, Vec2 position, float angle) {
		this.id = id;
		this.pos = new Vec2(position);
		this.angle = angle;
	}

	public abstract void draw();
	
}
