package client;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.jme.bounding.BoundingSphere;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.ShadeState;
import com.jme.system.DisplaySystem;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

public class ModelLoaderOBJ {

	//static TreeMap<String, Spatial> preLoadedObject = new TreeMap<String, Spatial>();
	
	public static Spatial loadModel(String modelPath) {
		/*
		if ( preLoadedObject.containsKey(modelPath) ){
			System.out.println("PRELoaded Model" + modelPath);
			return preLoadedObject.get(modelPath);
		}
		*/
		if (modelPath == null){
			System.out.println("null modelPath");
			System.exit(0);
		}
		
		if ( modelPath.equals("Bullet1") ){
			Sphere s = new Sphere("Sphere", 30, 30, 0.25f);
			
			//to jbox unit
			s.setLocalScale(0.5f);
			
			s.setModelBound( new BoundingSphere() );
			
			s.updateModelBound();
			return s;
		}
		
		Spatial risObj3D;
		
		File obj = new File(modelPath+".obj");
		URL model = null;
		try {
			model = obj.toURI().toURL();
		} catch (MalformedURLException e) {
			System.out.println("Damn exceptions! O_o \n" + e);
			e.printStackTrace();
			System.exit(0);
		}
		FormatConverter converter=new ObjToJme();
		converter.setProperty("mtllib", model);

		final ShadeState smoothShadeState = DisplaySystem.getDisplaySystem().getRenderer().createShadeState();
        smoothShadeState.setShadeMode( ShadeState.ShadeMode.Smooth );
        
		if (model != null){
			ByteArrayOutputStream BO=new ByteArrayOutputStream();
			try {
				converter.convert(model.openStream(), BO);
				risObj3D =(Spatial) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
	 
				//to jbox unit
				risObj3D.setLocalScale(0.5f);
				
				risObj3D.setRenderState( smoothShadeState );
				
				//risObj3D.setModelBound(new BoundingBox());
				risObj3D.setModelBound( new BoundingSphere() );
				risObj3D.updateModelBound();
				
				System.out.println("Loaded Model" + modelPath);
//				Spatial copy = new (risObj3D);
//				preLoadedObject.put(modelPath, risObj3D);
				
				return risObj3D;
			} catch (Exception e) {
				System.out.println("Damn exceptions! O_o \n" + e);
				e.printStackTrace();
				System.exit(0);
			}
		}

		return null;
	}

}
