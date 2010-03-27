package client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.model.ModelFormatException;
import com.jmex.model.ogrexml.MaterialLoader;
import com.jmex.model.ogrexml.OgreLoader;

public class ModelLoaderOgre {
	static OgreLoader loader = new OgreLoader();
	static MaterialLoader matLoader = new MaterialLoader();
	
	//static HashMap<String, Node> loadedModel = new HashMap<String, Node>();
	
	static int i=0;
	
	public static Node loadModelOgre(String name){
		
		Node loadedNode=null;//loadedModel.get(name);
		
		if (loadedNode == null){

			File sk = new File(name+".mesh.xml");
            URL meshURL = null;
			try {
				meshURL = sk.toURI().toURL();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("loading: "+name);
			
			if (meshURL == null)
                throw new IllegalStateException( "Required runtime resource missing: " + name);
            
            try {

				loadedNode = (Node) loader.loadModel(meshURL);
				
				loadedNode.setLocalScale(0.2f);
				
				loadedNode.setLocalTranslation(0, 0, 0);
				
				Quaternion m4 = new Quaternion();
				m4.fromAngleAxis( (float)(Math.PI/2), new Vector3f(1, 0, 0));
				loadedNode.setLocalRotation(m4);
				
				loadedNode.setModelBound( new BoundingBox() );
				loadedNode.updateModelBound();
				loadedNode.updateRenderState();
				//loadedModel.put(name, loadedNode);

			}  catch (ModelFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		/*
		SharedNode shaNode=null;
		if (loadedNode!=null)
			shaNode = new SharedNode(name+" model:"+i, loadedNode);
		else{
			throw new RuntimeException("Oggetto caricato nullo");
		}
		*/
		Node mod = new Node();
		mod.attachChild(loadedNode);
		return mod;
	}
}
