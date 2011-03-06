/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import TestAPIjme3Jbullet.actions.PhysicActions;
import TestAPIjme3Jbullet.serializable.ObjectInfo;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsSweepTestResult;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
/**
 *
 * @author mauro
 */
public class Physics extends BulletAppState{
    LinkedList<ObjectInfo> waitingPhysic = new LinkedList<ObjectInfo>();
    LinkedList<PhysicActions> waitingActions = new LinkedList<PhysicActions>();

    TreeMap<Long, ObjectInfo> objcectInWorld = new TreeMap<Long, ObjectInfo>();
    long ID = 0;

    CollisionList l;

    @Override
    public void startPhysics(){
        super.startPhysics();
        /* TRY to remain deterministic, it works until fps < 1, i hope :-S */
        getPhysicsSpace().setMaxSubSteps(60);

        /* NO GRAVITY IN SPACE */
        getPhysicsSpace().setGravity( new Vector3f(0, 0, 0) );

    }

    public void addObject(ObjectInfo node){
        synchronized(waitingPhysic){
            waitingPhysic.add(node);
        }
    }

    public void addAction(PhysicActions act){
        synchronized(waitingActions){
            waitingActions.add(act);
        }
    }
/*
    @Override
    public void render(RenderManager rm){
        System.out.println( "Render");
        if (l==null){
            l=new CollisionList();
            getPhysicsSpace().addCollisionListener(l);
        }
        super.render(rm);
    }
*/
    @Override
    public void update(float tpf){
/*
        if (l == null){
            l = new CollisionList();
            getPhysicsSpace().addCollisionListener(l);
            getPhysicsSpace().setContactAddedCallback();
        }
*/
        ObjectInfo tempInfo;
        PhysicActions tempAction;
        synchronized(waitingPhysic){
            while ( (tempInfo=waitingPhysic.poll())!=null ){
                addObjectSync(tempInfo);
            }
        }
        synchronized(waitingActions){
            while ( (tempAction=waitingActions.poll())!=null ){
                executeAction(tempAction);
            }
        }
        super.update(tpf);
    }

    private void addObjectSync(ObjectInfo node) {

        
        /**
        *   TEST IF THE PLACE IS VOID, IF THE PLACE IS NOT VOID, FIND A VOID PLACE
        */
        int i=0;
        Vector3f temp = node.getPhysic().getPhysicsLocation();
        while(testIfFreePosition( node.getPhysic() ) > 0){
            i++;
            temp.x+=1;
            temp.y+=1;
            temp.z+=1;
            node.getPhysic().setPhysicsLocation(temp);
        }
        
        System.out.println( "Number of test: "+i+" new Position: "+node.getPhysic().getPhysicsLocation() );

        /**
        *   ADD THE OBJECT TO WORLD
        */
        getPhysicsSpace().add(node.getPhysic());

        objcectInWorld.put(node.getID(), node);
    }

    private void executeAction(PhysicActions tempAction) {
        tempAction.execute( objcectInWorld.get( tempAction.getTargetID() ) );
    }

    public ObjectInfo loadObject(){

        ObjectInfo info = new ObjectInfo( getNewID(), "Sphere" );

        return info;
    }

    private long getNewID(){
        return ID++;
    }

    private int testIfFreePosition(RigidBodyControl physic) {
        int numCollidingObj=0;
        Transform actualPosition = new Transform(physic.getPhysicsLocation(), physic.getPhysicsRotation() );
        Transform nextFakePosition = actualPosition.clone();

        //basic test
        nextFakePosition.setTranslation(actualPosition.getTranslation().x+1, actualPosition.getTranslation().y+1, actualPosition.getTranslation().z+1);
        List<PhysicsSweepTestResult> ris = getPhysicsSpace().sweepTest(physic.getCollisionShape(), actualPosition, nextFakePosition);
        numCollidingObj+=ris.size();

        //meybe we was INSIDE an object and we was moving away, so test in the other direction. This will also work if actualPosition is in the center of the other colliding object
        ris = getPhysicsSpace().sweepTest(physic.getCollisionShape(), nextFakePosition, actualPosition );
        numCollidingObj+=ris.size();

        return numCollidingObj;
    }
}
