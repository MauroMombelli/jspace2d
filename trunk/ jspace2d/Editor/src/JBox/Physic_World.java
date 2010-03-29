package JBox;

import java.awt.Shape;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.jme.math.FastMath;
import com.jme.scene.TriMesh;

import Main.main;

public class Physic_World {
	
	public World world;
	public Body model;
	private main Main;
	
	public Physic_World(main m){
		Main = m;
		Vec2 lowerBound = new Vec2(-10000,-10000);
		Vec2 upperBound = new Vec2(10000,10000);
		AABB bounds = new AABB(lowerBound,upperBound);
		Vec2 gravity = new Vec2(0,0);
		world = new World(bounds, gravity, true);
		createWalls();
		createModel();
	}

	private void createWalls() {
		BodyDef temp = new BodyDef();
		Body tempB = world.createBody(temp);
		PolygonDef alignedBoxDef = new PolygonDef();
		float hx = 1.0f; // half-width
		float hy = 10.0f; // half-height		
		alignedBoxDef.setAsBox(hx, hy, new Vec2(-10,0), 0);
		alignedBoxDef.density=0;
		alignedBoxDef.restitution = 1f;
		tempB.createShape(alignedBoxDef);
		
		temp = new BodyDef();
		tempB = world.createBody(temp);
		alignedBoxDef = new PolygonDef();
		hx = 1.0f; // half-width
		hy = 10.0f; // half-height
		alignedBoxDef.setAsBox(hx, hy, new Vec2(0,-10), FastMath.PI/2);
		alignedBoxDef.density=0;
		alignedBoxDef.restitution = 1f;
		tempB.createShape(alignedBoxDef);
		
		temp = new BodyDef();
		tempB = world.createBody(temp);
		alignedBoxDef = new PolygonDef();
		hx = 1.0f; // half-width
		hy = 10.0f; // half-height		
		alignedBoxDef.setAsBox(hx, hy, new Vec2(10,0), 0);
		alignedBoxDef.density=0;
		alignedBoxDef.restitution = 1f;
		tempB.createShape(alignedBoxDef);
		
		temp = new BodyDef();
		tempB = world.createBody(temp);
		alignedBoxDef = new PolygonDef();
		hx = 1.0f; // half-width
		hy = 10.0f; // half-height		
		alignedBoxDef.setAsBox(hx, hy, new Vec2(0,10), FastMath.PI/2);
		alignedBoxDef.density=0;
		alignedBoxDef.restitution = 1f;
		tempB.createShape(alignedBoxDef);
	}

	private void createModel() {	
		model = world.createBody(Main.GLCanvas.impl.object.getBodyDef());		
		Main.GLCanvas.impl.object.createShapes(model);		
	}
	
}
