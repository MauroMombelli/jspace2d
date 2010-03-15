package client.gameState;

import java.util.HashMap;

import org.jbox2d.common.Vec2;

import client.ModelLoaderOBJ;
import client.ModelLoaderXML;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

public class SetNode extends GuiAction{

	//Node model;
	int ID;
	String modelName;
	Quaternion rotation;
	Vec2 position;
	
	public SetNode(int id, String name, Vec2 pos, float rotZ){
		ID = id;
		
		modelName = name;
		
		rotation = new Quaternion();
		rotation.fromAngleAxis(rotZ, new Vector3f(0,0,1));
		
		position = pos;
		
	}
	
	@Override
	public void run(HashMap<Integer, Node> visibleObject, Node rootNode) {
		
		Node myself = visibleObject.get(ID);
		if (myself!=null){
			myself.setLocalTranslation( new Vector3f(position.x, position.y, 0) );
			myself.setLocalRotation(rotation);
		}else{
			myself = ModelLoaderXML.loadModelXML(modelName);
			//myself = (Node)ModelLoaderOBJ.loadModel("test5");
			myself.setLocalTranslation( new Vector3f(position.x, position.y, 0) );
			myself.setLocalRotation(rotation);
			visibleObject.put(ID, myself);
			rootNode.attachChild(myself);
			System.out.println("New model created, ID:"+ID);
		}
	}
	

}
