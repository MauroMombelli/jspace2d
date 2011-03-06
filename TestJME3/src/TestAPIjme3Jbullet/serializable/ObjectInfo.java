/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestAPIjme3Jbullet.serializable;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Observable;

/**
 *
 * @author mauro
 */
public class ObjectInfo extends Observable implements Externalizable{
    long ID; //unique id used for identification

    String modelName; //name of the model, used for loading graphic AND physic. MUST be one line only

    RigidBodyControl physic;//transient because RigidBodyControl is not serializable

    public ObjectInfo(long newID, String modelName) {
        ID = newID;
        this.modelName = modelName;
        loadPhysicBody();
    }

    public long getID() {
        return ID;
    }

    public RigidBodyControl getPhysic() {/*NEVER EVER USE THIS FOR MODIFICATION! USE PHYSICACTIONS INSTEAD*/
        return physic;
    }

    private void loadPhysicBody(){ //will load different physic model, based on modelName
        /** Make a ball physcial with a mass > 0.0f */
        physic = new RigidBodyControl(new SphereCollisionShape(1), 1f);
        physic.setPhysicsLocation(new Vector3f(0, 0, 0));
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        //send object ID, used for identification int client-server communications
        out.writeLong(ID);

        //write model name
        out.writeChars(modelName);

        //send the actual position
        Vector3f tempV = physic.getPhysicsLocation();
        out.writeFloat( tempV.x );
        out.writeFloat( tempV.y );
        out.writeFloat( tempV.z );

        //send the actual rotation
        Quaternion tempQ = physic.getPhysicsRotation();
        out.writeFloat( tempQ.getX() );
        out.writeFloat( tempQ.getY() );
        out.writeFloat( tempQ.getZ() );
        out.writeFloat( tempQ.getW() );
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        //reading unique id
        ID = in.readLong();

        //reading model name
        modelName=in.readLine();

        //preparing physic where info will be contained. Probabily when this class will be
        loadPhysicBody();

        physic.setPhysicsLocation( new Vector3f( in.readFloat(), in.readFloat(), in.readFloat() ) );
        
        physic.setPhysicsRotation( new Quaternion(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat()) );
    }

}
