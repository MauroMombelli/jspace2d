package jspace2d;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.common.Vec2;

import jspace2d.actions.CreateActor;
import jspace2d.actions.CreateCallback;
import jspace2d.actor.ActorManager;
import jspace2d.actor.Blueprint;
import jspace2d.gui.GraphicBlueprint;
import jspace2d.physics.BodyBlueprint;
import jspace2d.physics.FixtureBlueprint;
import jspace2d.physics.FixtureCircle;
import jspace2d.physics.FixtureRectangle;

public class MainServer{
	private static final Logger log = Logger.getLogger(MainServer.class.getName());

	public static void main(String[] args) {
		System.setProperty("java.library.path", "libs");
		System.setProperty("org.lwjgl.librarypath", new File("libs/natives").getAbsolutePath());
		new MainServer();
	}

	private volatile boolean run = true;

	public MainServer(){
		System.out.println("ciao");
		final ActorManager m = new ActorManager();

		
		//terrain
		final Vec2 size = new Vec2(50, 1);
		FixtureBlueprint f = new FixtureRectangle(false, 0.5f, 1, 0.3f, size);
		BodyBlueprint terrainBody = new BodyBlueprint(false, false, Arrays.asList(f).toArray(new FixtureBlueprint[1]) );
		GraphicBlueprint terrainGraphic = new GraphicBlueprint( size );
		Blueprint terrain = new Blueprint(terrainBody, terrainGraphic);
		
		CreateActor createTerrain = new CreateActor(-1, terrain, new Vec2(1f,1f), (float)Math.toRadians(1), new CreateCallback(){
			@Override
			public void created(long id){
				m.setCamera(id);
			}
		});
		
		m.add(createTerrain);
		
		//falling object
		FixtureBlueprint f2 = new FixtureCircle(false, 0.5f, 1, 0.3f, 2f); 
		BodyBlueprint fallingBody = new BodyBlueprint(true, false, Arrays.asList(f2).toArray(new FixtureBlueprint[1]) );
		GraphicBlueprint fallingGraphic = new GraphicBlueprint( new Vec2(2, 2) );
		Blueprint falling = new Blueprint(fallingBody, fallingGraphic);
		CreateActor createFallingBox = new CreateActor(-1, falling, new Vec2(0f, 100f), (float)Math.toRadians(40), null);
		
		m.add(createFallingBox);
		
		long startMs = System.currentTimeMillis();
		long loopCount = 0;
		while (run){
			if (System.currentTimeMillis() - startMs >= 1000){
				startMs = System.currentTimeMillis();
				log.log(Level.INFO, "FPS MAIN: "+loopCount);
				loopCount = 0;
			}
			loopCount++;
			
			m.stepPhysic();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
