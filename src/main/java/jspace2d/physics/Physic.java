package jspace2d.physics;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Physic {
	
	private static final Logger log = Logger.getLogger( Physic.class.getName() );
	
	private float timeStep = 1.0f / 60.0f;
	private int velocityIterations = 6;
	private int positionIterations = 2;
	
	private final World world;
	
	public Physic() {
		Vec2 gravity = new Vec2(0, -10);
		world = new World(gravity);
		// Setup world
		
	}

	public Body add(BodyBlueprint e, Vec2 pos, float angle) {
		
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.position.set(pos.x, pos.y);
		bodyDef.angle = angle;
		
		if ( e.isDynamic() ){
			bodyDef.type = BodyType.DYNAMIC;
		}else{
			bodyDef.type = BodyType.STATIC;
		}
		
		Body body = world.createBody(bodyDef);
		
		for (FixtureBlueprint f : e.getFixture()){
			body.createFixture( f.getFixture() );
		}
		
		log.log(Level.INFO, "Created body");
		
		return body;
	}

	public void step(){
		world.step(timeStep, velocityIterations, positionIterations);
	}

	public void remove(Body body) {
		world.destroyBody(body);
	}

	public void queryAABB(QueryCallback callback, AABB aabb) {
		world.queryAABB(callback, aabb);
	}
}
