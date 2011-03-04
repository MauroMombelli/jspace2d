/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestAPIjme3Jbullet;

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
        System.out.println("Collision found: "+event.getNodeA().getName()+" and "+event.getNodeB().getName());
    }

}
