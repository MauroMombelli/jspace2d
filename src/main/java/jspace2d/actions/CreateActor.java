package jspace2d.actions;

import org.jbox2d.common.Vec2;

import jspace2d.actor.Blueprint;

public class CreateActor extends Action {
	
	public final Blueprint blueprint;
	public final float angle;
	public final Vec2 pos;
	public long id;
	public final CreateCallback callback;

	public CreateActor(long id, Blueprint b, Vec2 pos, float angle, CreateCallback createCallback) {
		this.id = id;
		this.blueprint = b;
		this.pos = pos;
		this.angle = angle;
		this.callback = createCallback;
	}

}