package client.gameState;

import com.jme.renderer.ColorRGBA;
import com.jmex.game.state.TextGameState;

public class PosTextGameState extends TextGameState{

	public PosTextGameState(String arg0, float x, float y, float z) {
		super(arg0);
		textObject.setLocalTranslation(x, y, z);
		textObject.setDefaultColor(ColorRGBA.red);
		textObject.setTextColor(ColorRGBA.red);
	}
	
	public void setPosition(float x, float y, float z){
		textObject.setLocalTranslation(x, y, z);
	}

}

