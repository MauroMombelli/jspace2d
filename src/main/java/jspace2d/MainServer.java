package jspace2d;

import java.io.File;

import org.jbox2d.common.Vec2;

import jspace2d.physics.BodyBlueprint;

public class MainServer {

	public static void main(String[] args) {
		System.setProperty("java.library.path", "libs");
		System.setProperty("org.lwjgl.librarypath", new File("libs/natives").getAbsolutePath());
		new MainServer();
	}

	public MainServer(){
		System.out.println("ciao");
		ActorManager m = new ActorManager();
		
		//terrain
		BodyBlueprint terrain = new BodyBlueprint(new Vec2(0f,0f), 0, 1, 0.3f, new Vec2(0.5f, 0.1f), false);
		m.add(terrain);
		
		//falling object
		BodyBlueprint falling = new BodyBlueprint(new Vec2(0f, 1), 0f, 1, 0.3f, new Vec2(0.2f,0.2f), true);
		m.add(falling);
		
		while (true){
			m.stepPhysic();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
