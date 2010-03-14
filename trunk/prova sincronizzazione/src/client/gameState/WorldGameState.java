package client.gameState;

import java.util.HashMap;
import java.util.LinkedList;

import com.jme.input.KeyBindingManager;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.BasicGameState;

public class WorldGameState extends BasicGameState{

	HashMap<Integer, Node> visibleObject = new HashMap<Integer, Node>();
	
	LinkedList<GuiAction> objMod = new LinkedList<GuiAction>();

	LightState lightState;
	PointLight light;
	private Camera camera;

	private float cameraZoom;
	
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
        
        rootNode.updateRenderState();
        rootNode.updateWorldBound();
        rootNode.updateGeometricState(0.0f, true);
        
        camera=DisplaySystem.getDisplaySystem().getRenderer().getCamera();
        camera.setLocation( new Vector3f(20, 0, 50) );
	}
	
	public void addAction(GuiAction a){
		synchronized (objMod) {
			objMod.add(a);
		}
	}

	@Override
	public void update(float tpf) {
		System.out.println( "GUI update");
		synchronized (objMod) {
			super.update(tpf);
			GuiAction currentAction;
			while( (currentAction=objMod.poll())!=null ){
				currentAction.run( visibleObject, rootNode );
			}
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("less_zoom", true)) {
			cameraZoom++;
			if (cameraZoom>100)
				cameraZoom=100;
			camera.setLocation( new Vector3f(0, 0, cameraZoom) );
        }
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("more_zoom", true)) {
			cameraZoom--;
			if (cameraZoom<10)
				cameraZoom=10;
			camera.setLocation( new Vector3f(0, 0, cameraZoom) );
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

	public void addGuiActions(LinkedList<GuiAction> listaAzioni) {
		synchronized (objMod) {
			objMod.addAll(listaAzioni);
		}
	}
}
