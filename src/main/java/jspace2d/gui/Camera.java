package jspace2d.gui;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

import jspace2d.actor.Actor;
import jspace2d.actor.ActorManager;
import jspace2d.actor.QueryAABB;

public class Camera extends QueryAABB{
	
	private final List<ActorGui> toPrint = new ArrayList<>();
	private final Actor actor;
	
	private final Vec2 center = new Vec2();
	private float angle;
	
	private Vec2 halfSize = new Vec2(500, 500);
	private long lastUpdate = -1; //force first update

	
	private final ActorManager actorManager;
	
	
	public Camera(ActorManager actorManager, Actor actor) {
		this.actor = actor;
		
		this.actorManager = actorManager;
	}
	
	@Override
	public boolean reportFixture(Fixture fixture) {
		Actor a = (Actor) fixture.m_body.m_userData;
/*
		tmp.x = a.body.getPosition().x - center.x;
		tmp.y = a.body.getPosition().y - center.y;
		
		sin = MathUtils.sin(angle);
		cos = MathUtils.cos(angle);
		
		a.graphic.pos.x = tmp.x * cos + tmp.y * sin;
		a.graphic.pos.y = tmp.x * -sin + tmp.y * cos;
*/
		a.graphic.pos.x = a.body.getPosition().x - center.x;
		a.graphic.pos.y = a.body.getPosition().y - center.y;
		
		a.graphic.angle = a.body.getAngle() - angle;
		
		toPrint.add(a.graphic);
		
		return true;
	}

	public List<ActorGui> getToPrint() {
		final long t = actorManager.getTick();
		if (t == lastUpdate){
			return toPrint;
		}
		lastUpdate = t;
		toPrint.clear();
		actorManager.queryAABB(this);
		return toPrint;
	}

	@Override
	public void getAABBPoint(Vec2 pointA, Vec2 pointB) {
		
		center.x = actor.body.getPosition().x;
		center.y = actor.body.getPosition().y;
		
		angle = actor.body.getAngle();
		
		pointA.x = center.x - halfSize.x;
		pointA.y = center.y - halfSize.y;
		
		pointB.x = center.x + halfSize.x;
		pointB.y = center.y + halfSize.y;
		
		if (pointA.x > pointB.x){
			float swap = pointA.x;
			pointA.x = pointB.x;
			pointB.x = swap;
		}
		if (pointA.y > pointB.y){
			float swap = pointA.y;
			pointA.y = pointB.y;
			pointB.y = swap;
		}
	}

}
