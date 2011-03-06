/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test2.container;

import TestAPIjme3Jbullet.serializable.ObjectInfo;
import java.util.Observable;
import java.util.TreeMap;

/**
 *
 * @author mauro
 */
public class ObjectDB extends Observable{
    TreeMap<Long, ObjectInfo> objectInWorld = new TreeMap<Long, ObjectInfo>();
    long ID = 0;
    
    public long getNewID(){
        return ID++;
    }

    public ObjectInfo get(long targetID) {
        return objectInWorld.get(targetID);
    }

    public void put(ObjectInfo node) {
        setChanged();
        objectInWorld.put(node.getID(), node);
    }
}
