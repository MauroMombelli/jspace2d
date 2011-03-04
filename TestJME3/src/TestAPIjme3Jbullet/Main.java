/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestAPIjme3Jbullet;

//import com.jme3.bullet.control.PhysicsControl;

import TestAPIjme3Jbullet.actions.PhysicActionImpulse;
import TestAPIjme3Jbullet.actions.PhysicActionTranslate;
import TestAPIjme3Jbullet.serializable.ObjectInfo;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author mauro
 */
public class Main {

    public static void main(String args[]) {
        Main a = new Main();
    }

    public Main(){
        loadThings();
        
    }

    private void loadThings() {
        /*START PHYSIC*/
        Physics physic = new Physics();
        physic.startPhysics();
        //physic.getPhysicsSpace().addCollisionListener(new CollisionList() );
        
        /*CREATE PHYSIC OBJECT*/
        ObjectInfo temp = physic.loadObject();

        /*ADD PHYSIC OBJECT TO WORLD*/
        physic.addObject( temp );

        /*CREATE GRAPHIC*/
        Graphics graphics = new Graphics();
        graphics.start();
        graphics.addBulletAppState( physic );

        //graphics.addObject( new ObjectInfo(null) );
        
        /*ADD GRAPHIC OBJECT*/
        graphics.addObject( temp );


        {
            /*CREATE ANOTHER OBJECT*/
            ObjectInfo temp2 = physic.loadObject();

            /*ADD PHYSIC OBJECT TO WORLD*/
            physic.addObject( temp2 );

            /*ADD GRAPHIC OBJECT*/
            graphics.addObject( temp2 );


            /*create action translate*/
            PhysicActionTranslate actionTranslate = new PhysicActionTranslate(temp2.getID(), 0, 1, 10);

            /*create action impulse*/
            PhysicActionImpulse actionForce = new PhysicActionImpulse(temp2.getID(), 0, 0, -2);

            /* ADD ACTIONS TO ENGINE */
            physic.addAction(actionTranslate);
            //physic.addAction(actionForce);
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("GOooo");
        PhysicActionImpulse actionImpulse = new PhysicActionImpulse(temp.getID(), 0, 0, 2f);
        physic.addAction(actionImpulse);
        {
            ObjectInfo temp3 = physic.loadObject();

            /*ADD PHYSIC OBJECT TO WORLD*/
            physic.addObject( temp3 );

            /*ADD GRAPHIC OBJECT*/
            graphics.addObject( temp3 );
        }
    }
}
