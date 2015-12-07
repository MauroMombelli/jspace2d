package jspace2d.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import jspace2d.actions.Action;
import jspace2d.actions.CreateActor;
import jspace2d.actions.MoveActor;
import jspace2d.actions.RemoveActor;
import jspace2d.gui.ActorGui;
import jspace2d.gui.Visualizer;
import jspace2d.gui.VisualizerListener;
import jspace2d.physics.Physic;

public class ActorManager implements VisualizerListener{
	
	private static final Logger log = Logger.getLogger( ActorManager.class.getName() );
	
	private final Visualizer v = new Visualizer();
	private final Physic p = new Physic();
	
	private final AtomicBoolean toUpdate = new AtomicBoolean();
	private final Map<Long, Actor> actors = new HashMap<>();
	
	private long actorId = 0;
	
	private final TreeMap<Long, List<Action>> actions = new TreeMap<>();
	
	private long timeTick = 0;

	private ActorManagerListener listener;
	
	private final AABB queryAABB = new AABB();
	CameraCallback queryCallback = new CameraCallback();
	private Actor camera;

	private float half_camera_height = 50;
	private float half_camera_width = 50;
	
	public ActorManager(){
		v.setListener(this);
		new Thread(v).start();
	}

	public void setListener(ActorManagerListener l){
		listener = l;		
	}
	
	public void stepPhysic(){
		synchronized(toUpdate){
			if (timeTick >= Long.MAX_VALUE){
				timeTick = 0;
			}
			timeTick++;
			
			executeActions();
			
			p.step();
			
			toUpdate.set(true);
		}
	}
	
	private void executeActions() {
		List<Action> actionsThisTick = actions.remove(timeTick);
		if (actionsThisTick == null){
			return;
		}
		
		for (Action a:actionsThisTick){
			if (a instanceof CreateActor){
				addActor( (CreateActor)a);
			}else if(a instanceof RemoveActor){
				removeActor((RemoveActor)a);
			}else if(a instanceof MoveActor){
				moveActor((MoveActor)a);
			}
		}
	}

	private void moveActor(MoveActor a) {
		Actor actor = actors.get( a.id );
		if (actor == null){
			log.log(Level.WARNING, "Requested to move unexisting actor with id "+a.id);
			return;
		}
		actor.body.applyLinearImpulse(a.impulseCenter, a.impulse);
	}

	private void removeActor(RemoveActor a) {
		Actor removed = actors.remove( a.id );
		
		if (removed == null){
			log.log(Level.WARNING, "Requested to delete actor with id "+a.id+" but is not in list!");
			return;
		}
		
		p.remove(removed.body);
	}

	void addActor(CreateActor a) {
		Body body = p.add( a.blueprint.getBodyBlueprint(), a.pos, a.angle ); 
		ActorGui graphic = new ActorGui( a.pos, a.angle, a.blueprint.getGraphicBlueprint().getSize() );
		Actor actor = new Actor(actorId, a.blueprint, body, graphic);
		body.m_userData = actor;
		actors.put( actorId, actor );
		actorId++;
	}
	
	public void add(Action a) {
		if (a.executionTime < 0)
			a.executionTime = timeTick+1; //next iteration
		
		List<Action> actionListAtTick = actions.get(a.executionTime);
		if (actionListAtTick == null){
			actionListAtTick = new ArrayList<Action>();
			actions.put(a.executionTime, actionListAtTick);
		}
		actionListAtTick.add(a);
	}

	@Override
	public void preRender() {
		synchronized(toUpdate){
			if (toUpdate.getAndSet(false)){
				Vec2 center;
				float angle;
				if (camera != null){
					center = camera.body.getPosition();
					angle = camera.body.getAngle();
				}else{
					center = new Vec2(0,0);
					angle = 0;
				}
				
				queryAABB.lowerBound.set(center.x - half_camera_width, center.y - half_camera_height);
                queryAABB.upperBound.set(center.x + half_camera_width, center.y + half_camera_height);
                
                queryCallback.reset( center, angle );
                
				p.queryAABB(queryCallback, queryAABB);
				
				v.set(queryCallback.getToPrint());
			}
		}
	}
	
	@Override
	public void close() {
		if (listener != null)
			listener.eventClose();
	}
	
	private class CameraCallback implements QueryCallback{

		private Vec2 center = new Vec2();
		private float angle;
		private final List<ActorGui> toPrint = new ArrayList<>();

		@Override
		public boolean reportFixture(Fixture fixture) {
			Actor a = (Actor)fixture.m_body.m_userData;
			a.graphic.pos.x = a.body.getPosition().x - center.x;
			a.graphic.pos.y = a.body.getPosition().y - center.y;

			a.graphic.angle = a.body.getAngle() - angle;
			
			toPrint.add(a.graphic);
			return true;
		}

		public List<ActorGui> getToPrint() {
			return toPrint;
		}

		public void reset(Vec2 position, float angle) {
			this.center  = position;
			this.angle = angle;
			toPrint.clear();
		}
		
	}
}
