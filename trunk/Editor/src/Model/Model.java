package Model;

import java.io.File;
import java.util.HashMap;

import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import com.jme.math.Quaternion;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jmex.model.ogrexml.anim.OgreMesh;

import Import_Export.Model_Importer;
import Utils.Conversions;

public class Model {	
	Node parent;
	File file;
	Model_Importer importer;
	public Node model;
	TriMesh mesh;
	OgreMesh body_mesh;
	Body fisical;
	private Integer counter;
	private Triangle[] verts;
	public Node bodymodel;
	private MaterialState materialState;
	
	public Model(Node n, File f, MaterialState s){
		materialState = s;
		counter = 0;
		parent = n;
		file = f;

		importer = new Model_Importer(parent, file );
		model = (Node) importer.loadedSpatial;
		mesh = (TriMesh) model.getChild("Mesh");
		//get the body		
		String bodystring = file.getParent();
		//bodystring = bodystring.concat("\\Body.mesh.xml");
		String separator = System.getProperty("file.separator");
		File bodyfile = new File("src"+separator+"Risorse"+separator+"Body.mesh.xml");
		importer = new Model_Importer(parent, bodyfile );
		bodymodel = (Node) importer.loadedSpatial;		
		body_mesh = (OgreMesh) bodymodel.getChild("BodyMesh000");

	    parent.attachChild(model);
	    //parent.attachChild(bodymodel);
	    setMaterial();
	    model.updateModelBound();	   
	    parent.updateRenderState();
	}
	
	private void setMaterial() {
		
		//materialState.setAmbient(new ColorRGBA(0,0,0,0.5f));
		//materialState.setEmissive(new ColorRGBA(0,0.75f,0,0.5f));
		materialState.setDiffuse(new ColorRGBA(0.5f,0.5f,0.1f,0.1f));		
		materialState.setShininess(100);
		materialState.setColorMaterial(ColorMaterial.Specular);
	        materialState.setEnabled(true);
	        model.setRenderState(materialState);
	        model.updateRenderState();
	        System.out.println("material setted");
	}

	public void updatePosition(Vector3f newLoc){
		newLoc.z = -newLoc.z;
		model.setLocalTranslation(newLoc);
	}

	public void release() {
		model.detachAllChildren();		
	}

	public BodyDef getBodyDef() {
		BodyDef bodyDef = new BodyDef();		
		return bodyDef;
	}
	
	public void createShapes(Body b){
		fisical = b;
		
		for(int i = 0; i<body_mesh.getTriangleCount();i++){
			PolygonDef shape = new PolygonDef();
			shape.density = 0.9f;
			shape.friction = 0.1f
			;
			shape.restitution = 1;
			verts = new Triangle[body_mesh.getTriangleCount()];
			System.out.println(body_mesh.getColorBuffer());
			body_mesh.getMeshAsTriangles(verts);
			//System.out.println("Triangle: "+i);
			for(int j = 2; j>=0; j--){
				
				Vec2 tempVert = new Vec2(verts[i].get(j).x,verts[i].get(j).z);
				
				shape.addVertex(Conversions.ConvertPhysics(tempVert, false));
				System.out.println("Triangle: "+i+" Vertex: "+verts[i].get(j));
				//System.out.println("Shape: "+i+" Vertex: "+shape.getVertexArray()[j]);
			}
			
			b.createShape(shape); 
		}
		b.setLinearVelocity(new Vec2(4,0.5f));
		b.setMassFromShapes();
	}

	public void update() {
		
		Vector3f newLoc = new Vector3f(fisical.getPosition().x,0,fisical.getPosition().y);
		
		model.setLocalTranslation(Conversions.ConvertPhysics(newLoc, true));
		Quaternion rot = new Quaternion();
		rot.fromAngleNormalAxis(-fisical.getAngle(), new Vector3f(0,1,0));
		model.setLocalRotation(rot);
	}

	public void reset() {
		Vector3f newLoc = Vector3f.ZERO;		
		model.setLocalTranslation(Conversions.ConvertPhysics(newLoc, true));
		Quaternion rot = new Quaternion();
		rot.fromAngleNormalAxis(0, new Vector3f(0,1,0));
		model.setLocalRotation(rot);
		
	}
}
