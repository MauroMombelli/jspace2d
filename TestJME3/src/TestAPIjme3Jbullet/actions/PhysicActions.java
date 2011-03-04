/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestAPIjme3Jbullet.actions;

import TestAPIjme3Jbullet.serializable.ObjectInfo;
import java.io.Serializable;

/**
 *
 * @author mauro
 */
public abstract class PhysicActions implements Serializable{
    long ID;
    
    public PhysicActions(long newID){
        ID = newID;
    }

    public long getTargetID() {
        return ID;
    }

    public abstract void execute(ObjectInfo obj);

}
