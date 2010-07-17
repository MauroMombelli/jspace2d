package shared;

import java.io.Serializable;

import org.jbox2d.common.Mat22;
import org.jbox2d.common.Sweep;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;

import shared.oggetti.Oggetto2D;


public class InfoBody implements Serializable, Comparable<InfoBody>{

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

	private float xF;
	private float yF;

	private float[][] r;

	private float sA;
	private float sA0;
	private float sCx;
	private float sCy;
	private float sC0x;
	private float sC0y;
	private float sLx;
	private float sLy;
	private float t0;
	
	private int life;
	
	public InfoBody(Oggetto2D o, int id, XForm xForm, Vec2 linearVelocity, float angle, float angularVelocity, float torque, Vec2 force, Sweep s) {
		this.o = o;
		this.ID = id;
		
		r=new float[2][2];
		r[0][0]=xForm.R.col1.x;
		r[0][1]=xForm.R.col1.y;
		r[1][0]=xForm.R.col2.x;
		r[1][1]=xForm.R.col2.y;
		
		sA = s.a;
		sA0 = s.a0;
		sCx = s.c.x;
		sCy = s.c.y;
		sC0x = s.c0.x;
		sC0y = s.c0.y;
		sLx = s.localCenter.x;
		sLy = s.localCenter.y;
		t0 = s.t0;
		
		x = xForm.position.x;
		y = xForm.position.y;
		xV = linearVelocity.x;
		yV = linearVelocity.y;
		alfa = angle;
		alfaV = angularVelocity;
		this.torque = torque;
		xF = force.x;
		yF = force.y;
		
		life = o.getLife();
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
	
	public int getLife() {
		return life;
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
		float diffT=torque-b.torque;
		//System.out.println("diff t:"+diffT);
		float diffXF=xF-b.xF;
		//System.out.println("diff xF:"+diffXF);
		float diffYF=yF-b.yF;
		//System.out.println("diff yF:"+diffYF);
		float diffR1=r[0][0]-b.r[0][0];
		//System.out.println("diff R1:"+diffR1);
		float diffR2=r[0][1]-b.r[0][1];
		//System.out.println("diff R2:"+diffR2);
		float diffR3=r[1][0]-b.r[1][0];
		//System.out.println("diff R3:"+diffR3);
		float diffR4=r[1][1]-b.r[1][1];
		//System.out.println("diff R4:"+diffR4);
		
		float diffSa = sA-b.sA;
		float diffSa0 = sA0-b.sA0;
		float diffScx = sCx-b.sCx;
		float diffScy = sCy-b.sCy;
		float diffSc0x = sC0x-b.sC0x;
		float diffSc0y = sC0y-b.sC0y;
		float diffSlx = sLx-b.sLx;
		float diffSly = sLy-b.sLy;
		float diffT0 = t0-b.t0;
		
		return diffA+diffAV+diffX+diffXV+diffY+diffYV+diffT+diffXF+diffYF+diffR1+diffR2+diffR3+diffR4+diffSa+diffSa0+diffSc0x+diffSc0y+diffScx+diffScy+diffSlx+diffSly+diffT0;
	}

	public String toString(){
		return "x: "+x+" y: "+y+" xV: "+xV+" yV: "+yV+" aV: "+alfaV+" a: "+alfa+" t: "+torque+" xF:"+xF+" yF:"+yF+" rMat:"+getR();
	}

	public float getAngle() {
		return alfa;
	}

	public Mat22 getR(){
		return new Mat22(r[0][0], r[1][0], r[1][0], r[1][1]);
	}
	
	public Sweep getSweep(){
		Sweep ris = new Sweep();
		ris.a = sA;
		ris.a0 = sA0;
		ris.c.x = sCx;
		ris.c.y = sCy;
		ris.c0.x = sC0x;
		ris.c0.y = sC0y;
		ris.localCenter.x = sLx;
		ris.localCenter.y = sLy;
		ris.t0 = t0;
		return ris;
	}

	@Override
	public int compareTo(InfoBody arg0) {
		return arg0.ID-ID;
	}

	public Vec2 getForce() {
		return new Vec2(xF, yF);
	}
}
