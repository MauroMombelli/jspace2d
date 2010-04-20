package shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.dynamics.Body;


public class Oggetto2D implements Serializable, Comparable<Oggetto2D> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6591598803708118223L;
	
	
	//UniqueData is the body, because is not serializable, it will be created from scratch
	UniqueData bodyContainer = new UniqueData();//this class will not be serializated
	
	public int ID;
	String modelName="Sphere";
	protected float radius=GLOBAL_VARIABLE.convertToPhysicEngineUnit( 4 );
	protected float density = 1;
	protected float friction = 0;
	protected float restitution = 0.8f;
	
	public Oggetto2D(int id){
		this.ID = id;
	}
	
	public Oggetto2D(Oggetto2D obj){
		setOggetto2D(obj);
	}
	
	public void setOggetto2D(Oggetto2D obj) {
		this.ID = obj.ID;
		this.modelName = obj.modelName;
		this.radius = obj.radius;
		this.density = obj.density;
		this.friction = obj.friction;
		this.restitution = obj.restitution;
		setInfoPosition(obj.getInfoPosition());
	}

	public boolean isValid() {
		//TODO:implement a control
		return true;
	}

	public void createBody(Body body) {
		bodyContainer.myBody = body;
		CircleDef sd = new CircleDef();
        sd.restitution = restitution;
        sd.friction = friction ;
        sd.radius = radius;
        sd.density = density ;
        sd.userData = this;
        bodyContainer.myBody.createShape(sd);
	}

	public Body getBody() {
		return bodyContainer.myBody;
	}
	
	public InfoBody getInfoPosition() {
		return new InfoBody(this, ID, bodyContainer.myBody.getXForm(), bodyContainer.myBody.getLinearVelocity(), bodyContainer.myBody.getAngle(), bodyContainer.myBody.getAngularVelocity(), bodyContainer.myBody.m_torque, bodyContainer.myBody.m_force, bodyContainer.myBody.m_sweep );
	}
	
	public void setInfoPosition(InfoBody ad){
		bodyContainer.myBody.setXForm( ad.getPos(), ad.getAngle() );
		bodyContainer.myBody.setAngularVelocity( ad.getAngAcc() );
		bodyContainer.myBody.setLinearVelocity( ad.getPosVel() );
		bodyContainer.myBody.applyTorque( ad.getTorque()-bodyContainer.myBody.m_torque );
		bodyContainer.myBody.m_sweep = ad.getSweep();
		//bodyContainer.myBody.m_torque=ad.getTorque();
	}
	
	public float getRadius(){
		return radius;
	}

	public String getModelName() {
		return modelName;
	}

	public void see(Oggetto2D obj1) {
		// TODO Vedo obj1
		
	}
	
	public void isSee(Oggetto2D obj1) {
		// TODO Sono visto da obj1
		
	}

	public void notSee(Oggetto2D obj1) {
		// TODO Non vedo più obj1
		
	}
	
	public void isNotSee(Oggetto2D obj1) {
		// TODO Non sono più visto da obj1
		
	}

	public void collide(Oggetto2D obj1) {
		// TODO Collido con obj1
		
	}

	public void continueCollide(Oggetto2D obj1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(Oggetto2D o) {
		return ID-o.ID;
	}

}

class UniqueData implements Serializable{
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
