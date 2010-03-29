package Import_Export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import jmetest.ogrexml.TestMeshLoading;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.xml.XMLImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jmex.model.ModelFormatException;
import com.jmex.model.ogrexml.OgreLoader;
import com.jmex.model.ogrexml.anim.AnimationChannel;
import com.jmex.model.ogrexml.anim.MeshAnimationController;

public class Model_Importer {
	
	private Node rootNode;
	private XMLImporter xmlImporter;
	private InputStream modelUrl;
	public Node loadedSpatial;
	
	
	public Model_Importer(Node n, File f){
		OgreLoader loader = new OgreLoader();
		
		rootNode = n;
		xmlImporter = new XMLImporter();		
		try {
			modelUrl = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    // May also be called during update() loop to add to scene.
	    loadedSpatial = null;	    
	    
	    /*
	    try {
	        loadedSpatial = (Node) xmlImporter.load(modelUrl);
	    } catch (Exception e) {
	        throw new IllegalArgumentException(
	                "Failed to load URL: " + modelUrl, e);
	    }
	    */
	    
	    URL meshURL = null;
		try {
			meshURL = f.toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    System.out.println(meshURL);
	    try {
			loadedSpatial = (Node) loader.loadModel(meshURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModelFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	        
	    loadedSpatial.setModelBound(new BoundingBox());
	    
	}
    
}
