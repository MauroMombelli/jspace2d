package jspace2d.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.dynamics.Body;

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
		v.remove(removed.id);
	}

	void addActor(CreateActor a) {
		Body body = p.add( a.blueprint.getBodyBlueprint(), a.pos, a.angle );
		ActorGui graphic = v.add( actorId, a.blueprint.getGraphicBlueprint(), a.pos, a.angle );
		actors.put( actorId, new Actor(actorId, a.blueprint, body, graphic) );
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
				
				/* just the the actual body position and angle and set it to the graphic counterpart */
				for ( Actor a:actors.values() ){
					a.graphic.pos.x = a.body.getPosition().x;
					a.graphic.pos.y = a.body.getPosition().y;

					a.graphic.angle = a.body.getAngle();
				}
			}
		}
	}
	
	@Override
	public void close() {
		if (listener != null)
			listener.eventClose();
	}
}
