package jspace2d.gui;

import org.jbox2d.common.Vec2;

public class ActorGui {
	public final Vec2 pos = new Vec2();
	public float angle;
	public final Vec2 size = new Vec2();
	
	public ActorGui(Vec2 position, float angle, Vec2 size) {
		this.pos.x = position.x;
		this.pos.y = position.y;
		this.angle = angle;
	}
	
}
