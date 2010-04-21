package client.gameState;

import java.util.HashMap;

import com.jme.scene.Node;

public class RemoveNode extends GuiAction {

	@Override
	public void run(HashMap<Integer, Node> visibleObject, Node rootNode, Node myself) {
		rootNode.detachChild(myself);
	}

}
