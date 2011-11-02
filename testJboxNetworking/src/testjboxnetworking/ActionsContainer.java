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
    private final int stepN;

    ActionsContainer(boolean b, int actualStep) {
        step= b;
        stepN = actualStep;
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

    int getStepNumber() {
        return stepN;
    }
    
}
