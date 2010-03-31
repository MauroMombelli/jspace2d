package Components;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

import Actions.actionContainer;
import JBox.Physic_World;
import Jme_needings.CameraHandler;
import Jme_needings.MyImplementor;
import Main.main;

import com.jme.input.AbsoluteMouse;
import com.jme.input.InputSystem;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Geometry;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.JMECanvas;
import com.jme.system.lwjgl.LWJGLSystemProvider;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.stat.StatCollector;
import com.jmex.awt.lwjgl.LWJGLAWTCanvasConstructor;
import com.jmex.game.state.GameStateManager;

public class canvasRenderer {
	
	public Canvas canvas;
	public MyImplementor impl;
	private int width;
	private int height;
	private Preferences prefs = Preferences
    .userNodeForPackage(main.class); 
	public CameraHandler camhand;
	private actionContainer actions;
	private Physic_World phys_world;

	
	public void forceUpdateToSize() {
        // force a resize to ensure proper canvas size.
        canvas.setSize(canvas.getWidth(), canvas.getHeight() + 1);
        canvas.setSize(canvas.getWidth(), canvas.getHeight() - 1);
    }
	
	public void doResize() {
        if (impl != null) {
            impl.resizeCanvas(canvas.getWidth(), canvas.getHeight());
            if (impl.getCamera() != null) {
                Callable<Void> exe = new Callable<Void>() {
                    public Void call() {
                        impl.getCamera().setFrustumPerspective(
                                45.0f,
                                (float) canvas.getWidth()
                                        / (float) canvas.getHeight(), 1,
                                10000);
                        return null;
                    }
                };
                GameTaskQueueManager.getManager()
                        .getQueue(GameTaskQueue.RENDER).enqueue(exe);
            }
        }
    }
	
	public canvasRenderer(int w, int h, actionContainer act, Physic_World orld){
		phys_world = orld;
		actions = act;
		canvas = new Canvas();
		
		width = w;
		height = h;
		// -------------GL STUFF------------------

        // make the canvas:
    	DisplaySystem display = DisplaySystem.getDisplaySystem(LWJGLSystemProvider.LWJGL_SYSTEM_IDENTIFIER);
    	display.registerCanvasConstructor("AWT", LWJGLAWTCanvasConstructor.class);
        canvas = (Canvas)display.createCanvas(width, height);
        canvas.setMinimumSize(new Dimension(100, 100));

        // add a listener... if window is resized, we can do something about
        // it.
       
        canvas.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent ce) {            	
                doResize();
            }
        });       
       

        
        // Important! Here is where we add the guts to the canvas:
        impl = new MyImplementor(width, height, canvas, actions, phys_world);        
        ((JMECanvas) canvas).setImplementor(impl);
        
        
        
        camhand = new CameraHandler(impl);

        canvas.addMouseWheelListener(camhand);
        canvas.addMouseListener(camhand);
        canvas.addMouseMotionListener(camhand);
        
        
        
        
        // -----------END OF GL STUFF-------------

        Callable<Void> exe = new Callable<Void>() {
            public Void call() {
                forceUpdateToSize();
                ((JMECanvas) canvas).setTargetRate(60);                
                return null;
            }
        };
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).enqueue(exe);
        
    }	
	
}
