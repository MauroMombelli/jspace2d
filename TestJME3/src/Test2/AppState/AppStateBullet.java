/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test2.AppState;

import Test2.container.ActionDB;
import Test2.container.ObjectDB;
import TestAPIjme3Jbullet.actions.PhysicActions;
import TestAPIjme3Jbullet.actions.PhysicObjectCreate;
import TestAPIjme3Jbullet.serializable.ObjectInfo;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;

/**
 *
 * @author mauro
 */
public class AppStateBullet extends BulletAppState{
    //BulletAppState bullet = new BulletAppState();

    ObjectDB objects;
    ActionDB actions;

    public AppStateBullet(){
        super();
        
    }

    @Override
    public void startPhysics() {
        super.startPhysics();
        //bullet.startPhysics();
        
        /* TRY to remain deterministic, it works until fps < 1, i hope :-S */
        getPhysicsSpace().setMaxSubSteps(60);

        /* NO GRAVITY IN SPACE */
        getPhysicsSpace().setGravity( new Vector3f(0, 0, 0) );

        CollisionList l = new CollisionList();
        getPhysicsSpace().addCollisionListener( l );
    }

    @Override
    public void update(float tpf){
        
        super.update(tpf);
        PhysicActions tempAction;
        
        while ( (tempAction=actions.pollWaiting())!=null ){
            if ( executeAction(tempAction) ){
                actions.addExecuted(tempAction);
            }
        }

        objects.notifyObservers();
        //super.update(tpf);
    }

    private boolean executeAction(PhysicActions tempAction) {
        
        ObjectInfo data =  objects.get( tempAction.getTargetID() );
        
        if (data==null){
            if (tempAction instanceof PhysicObjectCreate){
                data = loadObject(tempAction.getTargetID());
            }else{
                System.out.println("ID is INVALID!");
                return false;
            }
        }
        
        tempAction.execute( data );
        return true;
    }

    public void setDB(ActionDB actions, ObjectDB objects) {
        this.actions = actions;
        this.objects = objects;
    }

    public ObjectInfo loadObject(long id){
        ObjectInfo ris = new ObjectInfo( id, "Sphere" );
        ris.getPhysic().setPhysicsSpace(getPhysicsSpace());
        return ris;
    }

    public ObjectInfo loadObject(long id, int x, int y, int z) {
        ObjectInfo ris = loadObject(id);
        ris.getPhysic().setPhysicsLocation(new Vector3f(x, y, z));
        return ris;
    }
}
