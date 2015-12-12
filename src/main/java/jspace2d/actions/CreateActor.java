package jspace2d.actions;

import org.jbox2d.common.Vec2;

public class CreateActor extends Action {
	
	public final long id;
	public final String blueprintName;
	public final float angle;
	public final Vec2 pos;
	public final CreateCallback callback;

	public CreateActor(long id, String blueprintName, Vec2 pos, float angle, CreateCallback createCallback) {
		this.id = id;
		this.blueprintName = blueprintName;
		this.pos = pos;
		this.angle = angle;
		this.callback = createCallback;
	}

}