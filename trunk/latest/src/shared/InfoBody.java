package shared;

import java.io.Serializable;

import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;

public class InfoBody implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int ID;
	private float alfaV;
	private float yV;
	private float xV;
	private float y;
	private float x;

	private float alfa;
	private float torque;
	Oggetto2D o;
	
	public InfoBody(Oggetto2D o, int id, XForm xForm, Vec2 linearVelocity, float angle, float angularVelocity, float torque) {
		this.o = o;
		this.ID = id;
		x = xForm.position.x;
		y = xForm.position.y;
		xV = linearVelocity.x;
		yV = linearVelocity.y;
		alfa = angle;
		alfaV = angularVelocity;
		this.torque = torque;
	}
	
	public Oggetto2D getOggetto2D(){
		return o;
	}

	public Vec2 getPos() {
		return new Vec2(x, y);
	}

	public float getAngAcc() {
		return alfaV;
	}

	public float getTorque() {
		return torque;
	}
	
	public Vec2 getPosVel() {
		return new Vec2(xV, yV);
	}

	public float compare(InfoBody b) {
		float diffX=x-b.x;
		//System.out.println("diff x:"+diffX);
		float diffY=y-b.y;
		//System.out.println("diff y:"+diffY+" ya: "+y+" yb: "+b.y);
		float diffXV=xV-b.xV;
		//System.out.println("diff xV:"+diffXV);
		float diffYV=yV-b.yV;
		//System.out.println("diff yV:"+diffYV+" yVa: "+yV+" yVb: "+b.yV);
		float diffA=alfa-b.alfa;
		//System.out.println("diff A:"+diffA);
		float diffAV=alfaV-b.alfaV;
		//System.out.println("diff AV:"+diffAV);
		return diffA+diffAV+diffX+diffXV+diffY+diffYV;
	}

	public String toString(){
		return "x: "+x+" y: "+y+" xV: "+xV+" yV: "+yV+" aV: "+alfaV+" a: "+alfa;
	}

	public float getAngle() {
		return alfa;
	}
}
