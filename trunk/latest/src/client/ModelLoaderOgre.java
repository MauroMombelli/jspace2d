package client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jmex.model.ModelFormatException;
import com.jmex.model.ogrexml.MaterialLoader;
import com.jmex.model.ogrexml.MeshCloner;
import com.jmex.model.ogrexml.OgreLoader;

public class ModelLoaderOgre {
	static OgreLoader loader = new OgreLoader();
	static MaterialLoader matLoader = new MaterialLoader();
	
	static HashMap<String, Node> loadedModel = new HashMap<String, Node>();
	
	static int i=0;
	
	public static Node loadModelOgre(String name){
		
		System.out.println("loading: "+name);
		
		Node loadedNode=loadedModel.get(name);
		
		if (loadedNode == null){

			File sk = new File(name+".mesh.xml");
            URL meshURL = null;
			try {
				meshURL = sk.toURI().toURL();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("loading from filesystem: "+name);
			
			if (meshURL == null)
                throw new IllegalStateException( "Required runtime resource missing: " + name);
            
            try {

            	loadedNode = (Node) loader.loadModel(meshURL);
				System.out.println("Numero vertici"+((TriMesh)loadedNode.getChild(0)).getVertexCount());
            	loadedNode.setLocalScale(0.2f);
				
            	loadedNode.setLocalTranslation(0, 0, 0);
				
				loadedModel.put(name, loadedNode);
				
			}  catch (ModelFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Node res =new Node();
		
		loadedNode = MeshCloner.cloneMesh(loadedNode);
		Quaternion m4 = new Quaternion();
		m4.fromAngleAxis( (float)(Math.PI/2), new Vector3f(1, 0, 0));
		loadedNode.setLocalRotation(m4);
		
		loadedNode.setModelBound( new BoundingBox() );
		loadedNode.updateModelBound();
		loadedNode.updateRenderState();
		
		res.attachChild(loadedNode);
		
		return res;
	}

/*
	public static Node loadModelOgre(String modelName) {
		Node ris = new Node();
		ris.attachChild( new Box("Ship", new Vector3f(0,0,0), 0.2f, 0.2f, 0.2f) );
		return ris;
	}
*/
}
