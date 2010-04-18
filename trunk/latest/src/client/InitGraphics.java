package client;

import java.util.LinkedList;

import client.gameState.PosFPSGameState;
import client.gameState.PosTextGameState;
import client.gameState.WorldGameState;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;

import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;

public class InitGraphics {
	
	WorldGameState guiWorld;
	PosFPSGameState gameFPS;
	PosTextGameState gameLAG;
	PosTextGameState clientTurn;
	PosTextGameState info;
	InputReader in;
	StandardGame game;
	
	
	public InitGraphics(LinkedList<ClientOggetto2D> allOggetto2D){
		game = new StandardGame("Client test");
		game.setBackgroundColor(ColorRGBA.white);
		
		//game.setConfigShowMode(ConfigShowMode.AlwaysShow);
		try {
			GameSettingsPanel.prompt(game.getSettings());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//DisplaySystem.getDisplaySystem().setMinSamples(64);
		
		// Start StandardGame, it will block until it has initialized successfully, then return
		game.start();
		
		in = new InputReader();
		
		guiWorld = new WorldGameState("AllWorld", allOggetto2D);
		
		GameStateManager.getInstance().attachChild( guiWorld );
		
		MouseInput.get().setCursorVisible(true);
		
		guiWorld.setActive(true);
		game.setConfigShowMode(ConfigShowMode.AlwaysShow);
		
		//guiWorld.addAction( new SetNode(100, "sfera.xml", new Vec2(0,0), 0) );
		//guiWorld.addAction( new SetNode(101, "sfera.xml", new Vec2(5,0), 0) );
		//System.out.println("action added");//Add FPS counter
		gameFPS = new PosFPSGameState(10, 3, 10);
		gameFPS.setText("wow");
		GameStateManager.getInstance().attachChild(gameFPS);
		gameFPS.setActive(true);
		
		gameLAG = new PosTextGameState("INITIALIZING",10, 15, 10);
		GameStateManager.getInstance().attachChild(gameLAG);
		gameLAG.setActive(true);

		clientTurn = new PosTextGameState("INITIALIZING",10, 27, 10);
		GameStateManager.getInstance().attachChild(clientTurn);
		clientTurn.setActive(true);
		
		info = new PosTextGameState("INITIALIZING",10, 34, 10);
		GameStateManager.getInstance().attachChild(info);
		info.setActive(true);
	}
	
	public WorldGameState getWorldGUI(){
		return guiWorld;
	}

	public void setCameraID(int iDmyShip) {
		guiWorld.setCameraID(iDmyShip);
	}
	
	public void close(){
		game.shutdown();
	}

	public void setLag(long lag, long allMapLag) {
		gameLAG.setText("Action turn LAG:"+lag+" AllMap turn LAG:"+allMapLag);
	}
	
	public void setTurn(long aturn, long turn, int errorNumber) {
		clientTurn.setText("AsincTurn:"+aturn+" sincTurn:"+turn+" world physic error:"+errorNumber);
	}
	
	public void setInfo(String str) {
		info.setText(str);
	}
}
