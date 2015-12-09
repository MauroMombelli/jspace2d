package jspace2d.actor;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.common.Vec2;

public abstract class QueryAABB implements QueryCallback {

	public abstract void getAABBPoint(Vec2 pointA, Vec2 pointB);

}
