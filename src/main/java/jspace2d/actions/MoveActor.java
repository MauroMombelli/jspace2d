package jspace2d.actions;

import org.jbox2d.common.Vec2;

public class MoveActor extends Action {

	public final long id;
	public final Vec2 impulse;
	public final Vec2 impulseCenter;
	
	public MoveActor(long id, Vec2 impulseCenter, Vec2 impulse){
		this.id = id;
		this.impulse = new Vec2(impulse);
		this.impulseCenter = new Vec2(impulseCenter);
	}

}