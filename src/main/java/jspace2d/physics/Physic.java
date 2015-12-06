package jspace2d.physics;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
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

	public Body add(BodyBlueprint e) {
		
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.position.set(e.getPos().x, e.getPos().y);
		bodyDef.angle = e.getAngle();
		
		if ( e.isDynamic() ){
			bodyDef.type = BodyType.DYNAMIC;
		}else{
			bodyDef.type = BodyType.STATIC;
		}
		
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(e.getSize().x/2, e.getSize().y/2);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape=dynamicBox;
		fixtureDef.density=e.getDensity();
		fixtureDef.friction=e.getFriction();
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		
		log.log(Level.INFO, "Created body");
		
		return body;
	}

	public void step(){
		world.step(timeStep, velocityIterations, positionIterations);
	}
}
