/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testjboxnetworking;

import java.util.LinkedList;

/**
 *
 * @author Daniel Boy
 */
class WorldCopy {

    LinkedList<SerializableBody> list = new LinkedList<SerializableBody>();
    private final int stepN;

    WorldCopy(int actualStep) {
        stepN = actualStep;
    }
    
    void add(SerializableBody info) {
        list.add(info);
    }
    
    SerializableBody poll(){
        return list.poll();
    }

    int getStepNumber() {
        return stepN;
    }
    
}
