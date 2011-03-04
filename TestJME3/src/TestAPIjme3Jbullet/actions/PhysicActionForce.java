/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestAPIjme3Jbullet.actions;

import TestAPIjme3Jbullet.serializable.ObjectInfo;
import com.jme3.math.Vector3f;
import java.io.Serializable;

/**
 *
 * @author mauro
 */
public class PhysicActionForce extends PhysicActions implements Serializable{
    float fX;
    float fY;
    float fZ;

    public PhysicActionForce(long newID, float fx, float fy, float fz){
        super(newID);
        fX=fx;
        fY=fy;
        fZ=fz;
    }

    @Override
    public void execute(ObjectInfo obj) {
        obj.getPhysic().applyCentralForce( new Vector3f(fX, fY, fZ) );
    }

}
