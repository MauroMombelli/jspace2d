package client;

import client.gameState.PosFPSGameState;
import client.gameState.WorldGameState;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

public class InitGraphics {
	
	WorldGameState guiWorld;
	PosFPSGameState gameFPS;
	InputReader in;
	public InitGraphics(){
		
		StandardGame game = new StandardGame("Client test");
		game.setBackgroundColor(ColorRGBA.white);
		
		//game.setConfigShowMode(ConfigShowMode.AlwaysShow);
		try {
			GameSettingsPanel.prompt(game.getSettings());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		DisplaySystem.getDisplaySystem().setMinSamples(64);
		
		// Start StandardGame, it will block until it has initialized successfully, then return
		game.start();
		
		in = new InputReader();
		
		guiWorld = new WorldGameState("AllWorld");
		
		GameStateManager.getInstance().attachChild( guiWorld );
		
		MouseInput.get().setCursorVisible(true);
		
		guiWorld.setActive(true);
		game.setConfigShowMode(ConfigShowMode.AlwaysShow);
		
		//guiWorld.addAction( new SetNode(100, "sfera.xml", new Vec2(0,0), 0) );
		//guiWorld.addAction( new SetNode(101, "sfera.xml", new Vec2(5,0), 0) );
		//System.out.println("action added");//Add FPS counter
		gameFPS = new PosFPSGameState(10, 15, 10);
		gameFPS.setText("wow");
		GameStateManager.getInstance().attachChild(gameFPS);
		gameFPS.setActive(true);

	}
	
	public WorldGameState getWorldGUI(){
		return guiWorld;
	}
	
}
