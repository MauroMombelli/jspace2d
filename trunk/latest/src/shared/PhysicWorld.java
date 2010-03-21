package shared;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;


public class PhysicWorld {
	private static final float TIMESTEP = 1.0f/60.0f;
	World physicWorld;
	
	public PhysicWorld(){
		float minX =-50, minY=-50, maxX=50, maxY=50;
		AABB worldSize = new AABB(new Vec2(minX, minY), new Vec2(maxX,maxY));
		Vec2 worldGravity = new Vec2();
		physicWorld = new World(worldSize, worldGravity, true);
		//create border
		Body ground = null;
		
		//up
		BodyDef bd = new BodyDef();
		bd.position.set(minX, minY);
		ground = physicWorld.createBody(bd);

		PolygonDef sd = new PolygonDef();
		sd.setAsBox(maxX-minX, 10);
		ground.createShape(sd);
		
		//down
		bd = new BodyDef();
		bd.position.set(minX, maxY);
		ground = physicWorld.createBody(bd);

		sd = new PolygonDef();
		sd.setAsBox(maxX-minX, 10);
		ground.createShape(sd);
		
		//left
		bd = new BodyDef();
		bd.position.set(minX, minY);
		ground = physicWorld.createBody(bd);

		sd = new PolygonDef();
		sd.setAsBox(10, maxY-minY);
		ground.createShape(sd);

		//right
		bd = new BodyDef();
		bd.position.set(maxX, minY);
		ground = physicWorld.createBody(bd);

		sd = new PolygonDef();
		sd.setAsBox(10, maxY-minY);
		ground.createShape(sd);
	}
	
	public void update() {
		physicWorld.step(TIMESTEP, 10);
	}
	
	public void addNew(Oggetto2D t) {
		addNew(t, 0, 0, 0);
	}
	
	public void addNew(Oggetto2D t, float x, float y, float rotation) {
		if ( t.isValid() ){
			
			BodyDef bd = new BodyDef();
			bd.position.x = x;
			bd.position.y = y;
			bd.angle = rotation;
            bd.allowSleep=true;	
            bd.angularDamping=0.005f;
            
            t.createBody( physicWorld.createBody(bd) );
			
			t.getBody().setMassFromShapes();
			
			System.out.println("elements in world:"+physicWorld.getBodyCount());
		}
	}

	public void addNew(Oggetto2D newObj, float x, float y) {
		addNew(newObj, x, y, 0);
	}

	public void clear() {
		Body t = physicWorld.getBodyList();
		Body next;
		for (int i = 0; i < physicWorld.getBodyCount();i++){
			next = t.getNext();
			physicWorld.destroyBody(t);
			t = next;
		}
	}

	public Oggetto2D addCopy(Oggetto2D o, float x, float y) {
		Oggetto2D copy = new Oggetto2D(o);
		if ( copy.isValid() ){
			
			BodyDef bd = new BodyDef();
			bd.position.x = x;
			bd.position.y = y;
            bd.allowSleep=true;	
            bd.angularDamping=0.005f;
            
            copy.createBody( physicWorld.createBody(bd) );
			
			copy.getBody().setMassFromShapes();
			
			//System.out.println("elements in world:"+physicWorld.getBodyCount());
			return copy;
		}
		return null;
	}
	
	public void setCollisionListener(ContactListener l){
		//Used by server
		physicWorld.setContactListener(l);
	}
	
}
