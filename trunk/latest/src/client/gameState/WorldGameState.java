package client.gameState;

import java.util.HashMap;
import java.util.LinkedList;

import com.jme.input.KeyBindingManager;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.BasicGameState;

public class WorldGameState extends BasicGameState{

	HashMap<Integer, Node> visibleObject = new HashMap<Integer, Node>();
	
	LinkedList<GuiAction> objMod = new LinkedList<GuiAction>();

	LightState lightState;
	PointLight light;
	private Camera camera;
	
	private float cameraZoom=20, cameraX=0, cameraY=0;

	private Integer cameraID=-1;
	
	public WorldGameState(String arg0) {
		super(arg0);
		
		// Lighting
        /** Set up a basic, default light. */
        light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        light.setLocation( new Vector3f( 0, 0, 10 ) );
        light.setEnabled( true );

        /** Attach the light to a lightState and the lightState to rootNode. */
        lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled( true );
        lightState.attach( light );
        rootNode.setRenderState( lightState );
        
        camera=DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        camera.setLocation( new Vector3f(cameraX, cameraY, cameraZoom) );
        
        Box borderLeft = new Box("Left", new Vector3f(-41,0,0), 1, 50, 1);
        rootNode.attachChild(borderLeft);
        
        Box borderRight = new Box("Right", new Vector3f(41,0,0), 1, 50, 1);
        rootNode.attachChild(borderRight);
        
        Box borderUp = new Box("Up", new Vector3f(0,41,0), 50, 1, 1);
        rootNode.attachChild(borderUp);
        
        Box borderDown = new Box("Down", new Vector3f(0,-41,0), 50, 1, 1);
        rootNode.attachChild(borderDown);
        
        rootNode.updateRenderState();
        rootNode.updateWorldBound();
        rootNode.updateGeometricState(0.0f, true);
	}
	
	public void addAction(GuiAction a){
		synchronized (objMod) {
			objMod.add(a);
		}
	}
	
	public void setCameraID(int id){
		synchronized (cameraID) {
			cameraID = id;
		}
	}

	@Override
	public void update(float tpf) {
		//System.out.println( "GUI update");
		synchronized (objMod) {
			super.update(tpf);
			GuiAction currentAction;
			while( (currentAction=objMod.poll())!=null ){
				currentAction.run( visibleObject, rootNode );
			}
			Node t=null;
			synchronized (cameraID) {
				t = visibleObject.get(cameraID);
			}
			if (t!=null){
				cameraX = t.getLocalTranslation().x;
				cameraY = t.getLocalTranslation().y;
				camera.setLocation( new Vector3f(cameraX, cameraY, cameraZoom) );
			}
				
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_up", true)) {
			cameraY++;
			camera.setLocation( new Vector3f(cameraX, cameraY, cameraZoom) );
        }
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_down", true)) {
			cameraY--;
			camera.setLocation( new Vector3f(cameraX, cameraY, cameraZoom) );
        }
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_left", true)) {
			cameraX--;
			camera.setLocation( new Vector3f(cameraX, cameraY, cameraZoom) );
        }
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("move_right", true)) {
			cameraX++;
			camera.setLocation( new Vector3f(cameraX, cameraY, cameraZoom) );
        }
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("less_zoom", true)) {
			cameraZoom++;
			camera.setLocation( new Vector3f(cameraX, cameraY, cameraZoom) );
        }
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("more_zoom", true)) {
			cameraZoom--;
			if (cameraZoom<10)
				cameraZoom = 10;
			camera.setLocation( new Vector3f(cameraX, cameraY, cameraZoom) );
        }
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("escape", true)) {
			System.exit(0);
        }
		
		rootNode.updateRenderState();
		rootNode.updateGeometricState(tpf, true);
	}
/*	
	@Override
	public void render(float tpf) {
		synchronized (visibleObject) {
			super.render(tpf);
		}
	}
*/
/*
	public void addGuiActions(LinkedList<GuiAction> listaAzioni) {
		synchronized (objMod) {
			objMod.addAll(listaAzioni);
		}
	}
*/
	public void setGuiActions(LinkedList<GuiAction> listaAzioni) {
		synchronized (objMod) {
			objMod = listaAzioni;
		}
	}
}
