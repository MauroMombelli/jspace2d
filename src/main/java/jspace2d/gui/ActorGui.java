package jspace2d.gui;

import org.jbox2d.common.Vec2;

public class ActorGui {
	public final Vec2 pos;
	public float angle;
	public final Vec2 size;
	public final long id;
	
	public ActorGui(long id, Vec2 position, float angle, Vec2 size) {
		this.id = id;
		this.pos = new Vec2(position);
		this.size = new Vec2(size);
		this.angle = angle;
	}


	
}
