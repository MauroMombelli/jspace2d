/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test2.container;

import TestAPIjme3Jbullet.actions.PhysicActions;
import java.util.LinkedList;
import java.util.Observable;

/**
 *
 * @author mauro
 */
public class ActionDB extends Observable{
    LinkedList<PhysicActions> waitingActions = new LinkedList<PhysicActions>();
    LinkedList<PhysicActions> executedActions = new LinkedList<PhysicActions>();


    public void addWaiting(PhysicActions tempAction) {
        synchronized(waitingActions){
            waitingActions.add(tempAction);
        }
    }

    public PhysicActions pollWaiting() {
        synchronized(waitingActions){
            return waitingActions.poll();
        }
    }


    public void addExecuted(PhysicActions tempAction) {
        synchronized(executedActions){
            executedActions.add(tempAction);
        }
    }

    public PhysicActions pollExecuted() {
        synchronized(executedActions){
            return executedActions.poll();
        }
    }

}
