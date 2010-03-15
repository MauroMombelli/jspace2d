package shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.dynamics.Body;

import shared.azioni.Action;


public class Oggetto2D implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6591598803708118223L;
	
	//private Body myBody;
	Test t = new Test();
	
	public int ID;
	LinkedList<Action> allChanges;
	
	String modelName="astronave 2.xml";
	
	public Oggetto2D(int id, LinkedList<Action> allChanges){
		this.ID = id;
		this.allChanges = allChanges;
	}
	
	public boolean isValid() {
		//TODO:implement a control
		return true;
	}

	public void createBody(Body body) {
		t.myBody = body;
		CircleDef sd = new CircleDef();
        sd.restitution = 0.8f;
        sd.friction = 0.2f;
        sd.radius = GLOBAL_VARIABLE.convertToPhysicEngineUnit( 10 );
        sd.density = 1;
        sd.userData = this;
        t.myBody.createShape(sd);
	}

	public Body getBody() {
		return t.myBody;
	}
	
	public InfoBody getInfoPosition() {
		return new InfoBody( ID, t.myBody.getXForm(), t.myBody.getLinearVelocity(), t.myBody.getAngle(), t.myBody.getAngularVelocity() );
	}
	
	public void setInfoPosition(InfoBody ad){
		t.myBody.setXForm( ad.getPos(), ad.getAngle() );
		t.myBody.setAngularVelocity( ad.getAngAcc() );
		t.myBody.setLinearVelocity( ad.getPosVel() );
	}

	public String getModelName() {
		return modelName;
	}

}

class Test implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6222995190883047154L;
	
	public Body myBody;
	
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
	}
	
	private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
    }

}
