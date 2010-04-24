package client.gameState;

import java.util.HashMap;

import com.jme.scene.Node;

public class RemoveNode extends GuiAction {

	Node remove;
	int id;
	public RemoveNode(Node myself, int ID){
		remove = myself;
		id = ID;
	}
	
	@Override
	public void run(HashMap<Integer, Node> visibleObject, Node rootNode, Node myself) {
		if (myself!=null){
			rootNode.detachChild(myself);
			visibleObject.remove(id);
		}
		if (remove!=null){
			rootNode.detachChild(remove);
			visibleObject.remove(id);
		}
	}

}
