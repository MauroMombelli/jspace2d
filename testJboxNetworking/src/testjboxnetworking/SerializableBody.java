/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testjboxnetworking;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Daniel Boy
 */
class SerializableBody {

    Vec2 pos;
    float a;
    SerializableBody(Body body) {
        pos = body.getPosition().clone();
        a = body.getAngle();
    }

    //return true if check is ok
    boolean check(Body body) {
        if ( !pos.equals(body.getPosition()) ){
            System.out.println( "POS FAIL:"+body.getPosition()+" "+pos );
            return false;
        }
        if ( a!= body.getAngle() ){
            System.out.println( "ANGLE FAIL:"+body.getAngle()+" "+a );
            return false;
        }
        return true;
    }
    
}
