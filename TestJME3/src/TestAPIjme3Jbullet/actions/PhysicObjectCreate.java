/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestAPIjme3Jbullet.actions;

import TestAPIjme3Jbullet.serializable.ObjectInfo;
import com.jme3.bullet.collision.PhysicsSweepTestResult;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import java.util.List;

/**
 *
 * @author mauro
 */
public class PhysicObjectCreate extends PhysicActions{

    ObjectInfo obj;

    public PhysicObjectCreate(long newID){
        super(newID);
        //obj = loadObject();
    }

    public PhysicObjectCreate(long newID, int x, int y, int z){
        super(newID);
        //obj = loadObject(x, y, z);
    }

    public PhysicObjectCreate(long newID, ObjectInfo info){
        super(newID);
        obj=info;
    }

    public PhysicObjectCreate(long newID, ObjectInfo info, int x, int y, int z){
        super(newID);
        obj=info;
        obj.getPhysic().setPhysicsLocation(new Vector3f(x, y, z));
    }

    @Override
    public void execute(ObjectInfo obj2) {

        /*
         * TEST IF OBJ ALREADY EXIST
         */
        if (obj!=null){
            if (obj2.getID() == obj.getID() ){
                obj2 = obj;
            }else{
                System.out.println("ERROR: CREATION WITH DIFFERENT ID");
            }
        }
        System.out.println("space: "+obj2.getPhysic().getPhysicsSpace());


        /**
        *   TEST IF THE PLACE IS VOID, IF THE PLACE IS NOT VOID, FIND A VOID PLACE
        */
        int i=0;
        Vector3f temp = obj2.getPhysic().getPhysicsLocation();
        System.out.println("test n°: 1" );
        while(testIfFreePosition( obj2.getPhysic() ) > 0 && i<2){
            i++;
            System.out.println("test n°: "+(i+1) );
            temp.x+=1;
            temp.y+=1;
            temp.z+=1;
            obj2.getPhysic().setPhysicsLocation(temp);
        }

        System.out.println( "Number of test: "+i+" new Position: "+obj2.getPhysic().getPhysicsLocation() );

        /**
        *   ADD THE OBJECT TO WORLD
        */
        obj2.getPhysic().getPhysicsSpace().add(obj2.getPhysic());

        //objectInWorld.put(obj.getID(), obj);
    }

    private int testIfFreePosition(RigidBodyControl physic) {
        int numCollidingObj=0;
        Transform actualPosition = new Transform(physic.getPhysicsLocation(), physic.getPhysicsRotation() );
        Transform nextFakePosition = actualPosition.clone();

        //basic test
        nextFakePosition.setTranslation(actualPosition.getTranslation().x+1, actualPosition.getTranslation().y+1, actualPosition.getTranslation().z+1);
        System.out.println("testing "+actualPosition+" "+nextFakePosition );
        
        List<PhysicsSweepTestResult> ris1 = physic.getPhysicsSpace().sweepTest(physic.getCollisionShape(), actualPosition, nextFakePosition);
        numCollidingObj+=ris1.size();
        System.out.println("ris1: "+ris1.size());

        //meybe we was INSIDE an object and we was moving away, so test in the other direction. This will also work if actualPosition is in the center of the other colliding object
        List<PhysicsSweepTestResult> ris2 = physic.getPhysicsSpace().sweepTest(physic.getCollisionShape(), nextFakePosition, actualPosition );
        numCollidingObj+=ris2.size();
        System.out.println("ris2: "+ris2.size());

        return numCollidingObj;
    }


}
