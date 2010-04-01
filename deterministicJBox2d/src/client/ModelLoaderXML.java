package client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.util.export.xml.XMLImporter;

public class ModelLoaderXML {
	static XMLImporter xmlImporter = XMLImporter.getInstance();
	
	static HashMap<String, Node> loadedModel = new HashMap<String, Node>();
	
	static int i=0;
	
	public static Node loadModelXML(String name){
		
		Node loadedNode=null;//loadedModel.get(name);
		
		if (loadedNode == null){
			
			URL f = null;
			try {
				f = new URL("file:"+name);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("loading: "+name+" "+f);
			try {
				loadedNode=(Node) xmlImporter.load( f );
				loadedNode.setLocalScale(0.25f); //to box2d scale
				loadedNode.setName(name);
				Quaternion m4 = new Quaternion();
				m4.fromAngleAxis( (float)(Math.PI/2), new Vector3f(1, 0, 0));
				loadedNode.setLocalTranslation(0, 0, 0);
				loadedNode.setLocalRotation(m4);
				loadedNode.setModelBound( new BoundingBox() );
				loadedNode.updateModelBound();
				loadedNode.updateRenderState();
				loadedModel.put(name, loadedNode);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//SharedNode shaNode = new SharedNode(name+" model:"+i, loadedNode);
		Node ris = new Node("copy");
		ris.attachChild(loadedNode);
		return ris;
	}
}
