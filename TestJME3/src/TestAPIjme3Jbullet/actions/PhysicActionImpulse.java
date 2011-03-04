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
public class PhysicActionImpulse extends PhysicActions implements Serializable{
    float impulsePowerX;
    float impulsePowerY;
    float impulsePowerZ;

    public PhysicActionImpulse(long newID, float pwX, float pwY, float pwZ){
        super(newID);
        impulsePowerX = pwX;
        impulsePowerY = pwY;
        impulsePowerZ = pwZ;
    }

    @Override
    public void execute(ObjectInfo obj) {
        obj.getPhysic().applyImpulse( new Vector3f(impulsePowerX, impulsePowerY, impulsePowerZ), new Vector3f() );
        obj.getPhysic();
    }


}
