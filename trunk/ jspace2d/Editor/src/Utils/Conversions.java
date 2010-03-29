package Utils;

import java.awt.Point;

import org.jbox2d.common.Vec2;

import com.jme.math.Vector3f;

public final class Conversions {
	public static float physicConv = 1f;
	
	public static Point VectorToPoint(Vector3f a,int suppress){
		if(suppress==1){
			Point asd = new Point();
			asd.setLocation(a.y, a.z);
			return asd;
		}
		if(suppress==2){
			Point asd = new Point();
			asd.setLocation(a.x, a.z);
			return asd;
		}
		if(suppress==3){
			Point asd = new Point();
			asd.setLocation(a.x, a.y);
			return asd;
		}
		return null;
	}
	
	public static Vec2 ConvertPhysics (Vec2 vec, boolean from){
		if(from)
			return new Vec2(vec.x*physicConv,vec.y*physicConv);
		else
			return new Vec2(vec.x/physicConv,vec.y/physicConv);
	}
	
	public static float ConvertPhysics (float in, boolean from){
		if(from)
			return in*physicConv;
		else
			return in/physicConv;
	}

	public static Vector3f ConvertPhysics(Vector3f newLoc, boolean from) {
		if(from)
			return new Vector3f(newLoc.x*physicConv,newLoc.y*physicConv,newLoc.z*physicConv);
		else
			return new Vector3f(newLoc.x/physicConv,newLoc.y/physicConv,newLoc.z/physicConv);
		
	}
}
