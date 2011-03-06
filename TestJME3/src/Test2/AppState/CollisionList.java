/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test2.AppState;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;

/**
 *
 * @author mauro
 */
class CollisionList implements PhysicsCollisionListener{

    public CollisionList() {
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        //System.out.println("Collision found: "+event.getNodeA().getName()+" and "+event.getNodeB().getName()+" impulse: "+event.getAppliedImpulse()+" distance: "+event.getDistance1());
    }

}
