package client.gameState;

import java.util.HashMap;

import org.jbox2d.common.Vec2;

import client.ModelLoaderOgre;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.model.ogrexml.anim.AnimationChannel;
import com.jmex.model.ogrexml.anim.MeshAnimationController;

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
			//myself.setLocalTranslation( new Vector3f(GLOBAL_VARIABLE.convertFromPhysicEngineUnit( position.x ), GLOBAL_VARIABLE.convertFromPhysicEngineUnit( position.y ), 0) );
			myself.setLocalTranslation( new Vector3f( position.x, position.y, 0) );
			myself.setLocalRotation(rotation);
			
			if (ID ==0 ){
				//for (Spatial s:myself.getChildren()){
					System.out.println( "\t controller:"+myself.getControllerCount() );
					for (int i=0; i<myself.getControllerCount();i++){
						MeshAnimationController animControl = (MeshAnimationController) myself.getController(i);
						animControl.setAnimation("<bind>");
						AnimationChannel lower = animControl.getAnimationChannel();
						lower.addFromRootBone("Bone_L");
						animControl.setAnimation(lower, "Left Cube");
						System.out.println( "\t azone eseguita" );
					}
				//}
			}
			
		}else{
			myself = ModelLoaderOgre.loadModelOgre(modelName);
			//myself = (Node)ModelLoaderOBJ.loadModel("test5");
			//myself.setLocalTranslation( new Vector3f( GLOBAL_VARIABLE.convertFromPhysicEngineUnit( position.x ), GLOBAL_VARIABLE.convertFromPhysicEngineUnit( position.y ), 0) );
			myself.setLocalTranslation( new Vector3f( position.x, position.y, 0) );
			myself.setLocalRotation(rotation);
			visibleObject.put(ID, myself);
			rootNode.attachChild(myself);
			System.out.println("New model created, ID:"+ID+" "+modelName);
			//if (ID==10){
			//	System.exit(0);
			//}
		}
	}
	

}
