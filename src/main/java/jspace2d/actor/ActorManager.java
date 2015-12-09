package jspace2d.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import jspace2d.actions.Action;
import jspace2d.actions.CreateActor;
import jspace2d.actions.MoveActor;
import jspace2d.actions.RemoveActor;
import jspace2d.gui.ActorGui;
import jspace2d.gui.ActorGuiRectangle;
import jspace2d.gui.Camera;
import jspace2d.gui.Visualizer;
import jspace2d.physics.Physic;

public class ActorManager {

	private static final Logger log = Logger.getLogger(ActorManager.class.getName());

	private long actorId = 0;
	private final Map<Long, Actor> actors = new HashMap<>();

	private final Physic p = new Physic();
	private final TreeMap<Long, List<Action>> actions = new TreeMap<>();

	private volatile long timeTick = 0;

	private boolean needSync = false;
	private final AtomicBoolean toUpdate = new AtomicBoolean();

	private final Visualizer visualizer = new Visualizer("test");

	public void setCamera(long id) {
		setNeedSync(true);
		Actor a = actors.get(id);
		if (a == null) {
			log.log(Level.WARNING, "Requested not existing actor " + actorId + " as camera");
			return;
		}
		visualizer.setCamera(new Camera(this, a));

		log.info("Set camera to "+a.id);
		if (!visualizer.runnig) {
			new Thread(visualizer).start();
			log.info("Graphic started");
		}
	}

	public void stepPhysic() {

		if (timeTick >= Long.MAX_VALUE) {
			timeTick = 0;
		}
		timeTick++;

		executeActions();

		// consume executed actions
		actions.remove(timeTick);

		if (needSync) {
			synchronized (toUpdate) {
				p.step();

				toUpdate.set(true);
			}
		} else {
			p.step();
		}

	}

	public void addActor(CreateActor a) {
		if (a.id < 0) {
			a.id = actorId;
			actorId++;
		}
		Body body = p.add(a.blueprint.getBodyBlueprint(), a.pos, a.angle);
		ActorGui graphic = new ActorGuiRectangle(a.id, a.pos, a.angle, a.blueprint.getGraphicBlueprint().getSize());
		Actor actor = new Actor(a.id, a.blueprint, body, graphic);
		body.m_userData = actor;
		actors.put(a.id, actor);
		
		log.log(Level.INFO, "Created actor id " + a.id);
		
		if (a.callback != null) {
			a.callback.created(a.id);
		}
	}

	public void add(Action a) {
		if (a.executionTime < 0)
			a.executionTime = timeTick + 1; // next iteration

		List<Action> actionListAtTick = actions.get(a.executionTime);
		if (actionListAtTick == null) {
			actionListAtTick = new ArrayList<Action>();
			actions.put(a.executionTime, actionListAtTick);
		}
		actionListAtTick.add(a);
	}

	private final AABB queryAABB = new AABB();
	private final Vec2 pointA = new Vec2();
	private final Vec2 pointB = new Vec2();

	public void queryAABB(QueryAABB q) {
		synchronized (toUpdate) {

			q.getAABBPoint(pointA, pointB);

			queryAABB.lowerBound.set(pointA.x, pointA.y);
			queryAABB.upperBound.set(pointB.x, pointB.y);

			p.queryAABB(q, queryAABB);

		}
	}
	
	public long getTick() {
		return timeTick;
	}

	private void setNeedSync(boolean needSync) {
		this.needSync = needSync;
		toUpdate.set(true);
	}

	private void executeActions() {
		List<Action> actionsThisTick = actions.get(timeTick);
		if (actionsThisTick == null) {
			return;
		}

		for (Action a : actionsThisTick) {
			if (a instanceof CreateActor) {
				addActor((CreateActor) a);
			} else if (a instanceof RemoveActor) {
				removeActor((RemoveActor) a);
			} else if (a instanceof MoveActor) {
				moveActor((MoveActor) a);
			}
		}
	}

	private void moveActor(MoveActor a) {
		Actor actor = actors.get(a.id);
		if (actor == null) {
			log.log(Level.WARNING, "Requested to move unexisting actor with id " + a.id);
			return;
		}
		actor.body.applyLinearImpulse(a.impulseCenter, a.impulse);
	}

	private void removeActor(RemoveActor a) {
		Actor removed = actors.remove(a.id);

		if (removed == null) {
			log.log(Level.WARNING, "Requested to delete actor with id " + a.id + " but is not in list!");
			return;
		}

		p.remove(removed.body);
	}
}
