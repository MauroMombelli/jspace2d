/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testjboxnetworking;

/**
 *
 * @author Daniel Boy
 */
class CommunicationLayer {

    AnotherWorld world2 = new AnotherWorld();
    
    public CommunicationLayer(){
        world2.start();
    }
    
    void write(ActionsContainer cont) {
        world2.add(cont);
        world2.wakeUp();
    }

    void write(WorldCopy copy) {
        world2.add(copy);
        world2.wakeUp();
    }
    
}
