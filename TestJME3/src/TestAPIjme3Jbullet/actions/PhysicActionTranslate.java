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

/**
     * @deprecated
     * This class doesn't test if two object are overlapping. Try to never use this class
     */

public class PhysicActionTranslate extends PhysicActions implements Serializable{
    float x, y, z;

    @Deprecated
    public PhysicActionTranslate(long newID, float newX, float newY, float newZ){
        super(newID);
        x = newX;
        y = newY;
        z = newZ;
    }

    @Override
    @Deprecated
    public void execute(ObjectInfo obj) {
        obj.getPhysic().setPhysicsLocation(new Vector3f(x, y, z));
    }

}
