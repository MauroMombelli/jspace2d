package client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Node;
import com.jme.util.export.xml.XMLImporter;

public class ModelLoaderXML {
	static XMLImporter xmlImporter = XMLImporter.getInstance();
	
	static HashMap<String, Node> loadedModel = new HashMap<String, Node>();
	
	static int i=0;
	
	public static Node loadModelXML(String name){
		
		Node loadedNode=null;// = loadedModel.get(name);
		
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
				loadedNode.setLocalScale(0.5f); //to box2d scale
				loadedNode.setName(name);
				loadedNode.setLocalTranslation(0, 0, 0);
				loadedNode.setModelBound( new BoundingBox() );
				loadedNode.updateModelBound();
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

		return loadedNode;//new SharedNode("Shared1", loadedNode);
	}
}
