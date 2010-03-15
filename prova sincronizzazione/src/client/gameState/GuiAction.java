package client.gameState;

import java.util.HashMap;

import com.jme.scene.Node;

public abstract class GuiAction {

	public abstract void run(HashMap<Integer, Node> visibleObject, Node rootNode);

}
