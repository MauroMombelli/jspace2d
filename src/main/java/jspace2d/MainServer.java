package jspace2d;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.common.Vec2;

import jspace2d.actions.CreateActor;
import jspace2d.actions.CreateCallback;
import jspace2d.actor.ActorManager;
import jspace2d.actor.Blueprint;
import jspace2d.gui.GraphicBlueprint;
import jspace2d.physics.BodyBlueprint;

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
		
		//m.setListener(this);
		
		//terrain
		final Vec2 size = new Vec2(50, 1);
		BodyBlueprint terrainBody = new BodyBlueprint(size, false, 0.5f, 1, 0.3f);
		GraphicBlueprint terrainGraphic = new GraphicBlueprint( size );
		Blueprint terrain = new Blueprint(terrainBody, terrainGraphic);
		CreateActor createTerrain = new CreateActor(-1, terrain, new Vec2(1f,1f), (float)Math.toRadians(0), null);
		
		m.add(createTerrain);
		
		//falling object
		final Vec2 size2 = new Vec2(2, 2);
		BodyBlueprint fallingBody = new BodyBlueprint(size2, true, 0.5f, 1, 0.3f);
		GraphicBlueprint fallingGraphic = new GraphicBlueprint( size2 );
		Blueprint falling = new Blueprint(fallingBody, fallingGraphic);
		CreateActor createFallingBox = new CreateActor(-1, falling, new Vec2(0f, 100f), (float)Math.toRadians(40), new CreateCallback(){
			@Override
			public void created(long id){
				m.setCamera(id);
			}
		});
		
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
