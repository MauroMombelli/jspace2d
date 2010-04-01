package client.gameState;

import com.jme.renderer.ColorRGBA;
import com.jmex.game.state.FPSGameState;

public class PosFPSGameState extends FPSGameState{
	
	public PosFPSGameState(float x, float y, float z) {
		super();
		textObject.setLocalTranslation(x, y, z);
		textObject.setDefaultColor(ColorRGBA.red);
		textObject.setTextColor(ColorRGBA.red);
	}
	
	public void setPosition(float x, float y, float z){
		textObject.setLocalTranslation(x, y, z);
	}

}
