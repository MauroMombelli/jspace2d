/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testjboxnetworking;

import testjboxnetworking.creationActions.CreationAction;
import java.util.LinkedList;

/**
 *
 * @author Daniel Boy
 * one action container rapresent one step in physical world
 */
class ActionsContainer {
    LinkedList<CreationAction> cA = new LinkedList<CreationAction>();
    private final boolean step;
    
    public ActionsContainer(boolean shouldStep){
        step= shouldStep;
    }
    
    LinkedList<CreationAction> getCreationsAction() {
        return cA;
    }

    void add(CreationAction temp) {
        cA.add(temp);
    }

    boolean getStep() {
        return step;
    }
    
}
