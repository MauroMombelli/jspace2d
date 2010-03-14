package client;

import client.gameState.WorldGameState;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.input.MouseInput;

import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

public class InitGraphics {
	
	WorldGameState guiWorld;
	
	public InitGraphics(){
		StandardGame game = new StandardGame("Client test");

		try {
			GameSettingsPanel.prompt(game.getSettings());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Start StandardGame, it will block until it has initialized successfully, then return
		game.start();
		
		guiWorld = new WorldGameState("AllWorld");
		
		GameStateManager.getInstance().attachChild( guiWorld );
		
		MouseInput.get().setCursorVisible(true);
		
		guiWorld.setActive(true);
		game.setConfigShowMode(ConfigShowMode.AlwaysShow);
		
		//guiWorld.addAction( new SetNode(100, "sfera.xml", new Vec2(0,0), 0) );
		//guiWorld.addAction( new SetNode(101, "sfera.xml", new Vec2(5,0), 0) );
		//System.out.println("action added");
		
	}
	
	public WorldGameState getWorldGUI(){
		return guiWorld;
	}
	
}
