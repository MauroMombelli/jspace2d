package shared;

import java.util.TreeMap;

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
	float minX =-50, minY=-50, maxX=50, maxY=50;
	
	TreeMap<Integer, Oggetto2D> allOggetto2D = new TreeMap<Integer, Oggetto2D>();
	TreeMap<Integer, Oggetto2D> newOggetti2D = new TreeMap<Integer, Oggetto2D>();
	
	public long actualTurn=0;
	
	public PhysicWorld(){
		AABB worldSize = new AABB(new Vec2(minX, minY), new Vec2(maxX,maxY));
		Vec2 worldGravity = new Vec2();
		physicWorld = new World(worldSize, worldGravity, true);
		//physicWorld.setContinuousPhysics(true);
		createBorder();
	}
	
	private void createBorder(){
		//create border
		Body ground = null;
		
		//up
		BodyDef bd = new BodyDef();
		bd.position.set(minX, minY);
		ground = physicWorld.createBody(bd);

		PolygonDef sd = new PolygonDef();
		sd.setAsBox(maxX-minX, 10);
		sd.friction=0.02f;
		ground.createShape(sd);
		
		//down
		bd = new BodyDef();
		bd.position.set(minX, maxY);
		ground = physicWorld.createBody(bd);

		sd = new PolygonDef();
		sd.setAsBox(maxX-minX, 10);
		sd.friction=0.02f;
		ground.createShape(sd);
		
		//left
		bd = new BodyDef();
		bd.position.set(minX, minY);
		sd.friction=0.02f;
		ground = physicWorld.createBody(bd);

		sd = new PolygonDef();
		sd.setAsBox(10, maxY-minY);
		ground.createShape(sd);

		//right
		bd = new BodyDef();
		bd.position.set(maxX, minY);
		sd.friction=0.02f;
		ground = physicWorld.createBody(bd);

		sd = new PolygonDef();
		sd.setAsBox(10, maxY-minY);
		ground.createShape(sd);
	}
	
	public void update() {
		long time = System.nanoTime();
		actualTurn++;
		physicWorld.step(TIMESTEP, 10);
		
		time = System.nanoTime()-time;
		System.out.println("step time: "+time);
		//add the new object to the object
		allOggetto2D.putAll(newOggetti2D);
		newOggetti2D.clear();
		
	}
	
	public void addNew(Oggetto2D t) {
		addNew(t, 0, 0, 0);
	}
	
	public Oggetto2D addNew(Oggetto2D t, float x, float y, float rotation) {
		if ( t.isValid() ){
			
			BodyDef bd = new BodyDef();
			bd.position.x = x;
			bd.position.y = y;
			bd.angle = rotation;
            bd.allowSleep=true;	
            bd.angularDamping=0.005f;
            
            t.createBody( physicWorld.createBody(bd) );
			
			t.getBody().setMassFromShapes();
			
			newOggetti2D.put(t.ID, t);
			
			System.out.println("elements in world:"+physicWorld.getBodyCount());
			return t;
		}
		return null;
	}

	public void addNew(Oggetto2D newObj, float x, float y) {
		addNew(newObj, x, y, 0);
	}

	public void clear() {
		allOggetto2D.clear();
		newOggetti2D.clear();
		
		Body t = physicWorld.getBodyList();
		while (t!=null){
			physicWorld.destroyBody(t);
			t = t.getNext();
		}
		createBorder();
	}

	public Oggetto2D addCopy(Oggetto2D o, float x, float y) {
		Oggetto2D copy = null;
		if (o instanceof Oggetto2D)
			copy = new Oggetto2D(o);
		
		if (o instanceof Ship)
			copy = new Ship( (Ship)o );
		
		if ( copy!=null && copy.isValid() ){
			
			BodyDef bd = new BodyDef();
			bd.position.x = x;
			bd.position.y = y;
            bd.allowSleep=true;	
            bd.angularDamping=0.005f;
            
            copy.createBody( physicWorld.createBody(bd) );
			
			copy.getBody().setMassFromShapes();
			
			newOggetti2D.put(copy.ID, copy);
			
			//System.out.println("elements in world:"+physicWorld.getBodyCount());
			return copy;
		}
		return null;
	}
	
	public void setCollisionListener(ContactListener l){
		//Used by server
		physicWorld.setContactListener(l);
	}

	public void removeBody(Body body) {
		physicWorld.destroyBody(body);
	}

	public TreeMap<Integer, Oggetto2D> getOggetti() {
		return allOggetto2D;
	}

	public TreeMap<Integer, Oggetto2D> getNewOggetti() {
		// TODO Auto-generated method stub
		return newOggetti2D;
	}

	public Oggetto2D get(int iD) {
		Oggetto2D ris =allOggetto2D.get(iD);
		if (ris == null)
			ris = newOggetti2D.get(iD);
		return ris;
	}
	
}
