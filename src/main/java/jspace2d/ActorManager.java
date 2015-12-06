package jspace2d;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jbox2d.dynamics.Body;

import jspace2d.gui.ActorGui;
import jspace2d.gui.Visualizer;
import jspace2d.gui.VisualizerListener;
import jspace2d.physics.BodyBlueprint;
import jspace2d.physics.Physic;

public class ActorManager implements VisualizerListener{
	private final Visualizer v = new Visualizer();
	private final Physic p = new Physic();
	
	private final AtomicBoolean toUpdate = new AtomicBoolean();
	private final List<Actor> actors = new ArrayList<>();
	
	public ActorManager(){
		v.setListener(this);
		new Thread(v).start();
	}
	
	public void stepPhysic(){
		synchronized(toUpdate){
			p.step();
			toUpdate.set(true);
		}
	}
	
	@Override
	public void preRender() {
		synchronized(toUpdate){
			if (toUpdate.getAndSet(false)){
				
				/*  
				 * here we try to update instead of cloning the array and contents
				 * to try to create less garbage.
				 * As all the value get updated, the order of the order is not important
				 */
				List<ActorGui> actorGui = v.getActorGui();
				//List<ActorGui> actorGui = s.getActorGui();
				ActorGui a;
				Actor b;
				
				/* first we add and initialize missing actorGui */
				for (int i = actorGui.size(); i < actors.size(); i++){
					b = actors.get(i);
					actorGui.add( new ActorGui( b.body.getPosition(), b.body.getAngle(), b.blue.getSize() ) );
				}
				
				/* now we just have to update the old one */
				for (int i = 0; i < actorGui.size(); i++){
					a = actorGui.get(i);
					b = actors.get(i);
					
					a.pos.x = b.body.getPosition().x;
					a.pos.y = b.body.getPosition().y;
					
					a.angle = b.body.getAngle();
					
					a.size.x = b.blue.getSize().x;
					a.size.y = b.blue.getSize().y;
				}
			}
		}
	}

	public void add(BodyBlueprint b) {
		Body z = p.add(b);
		actors.add( new Actor(b, z) );
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
