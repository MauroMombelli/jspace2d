/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testjboxnetworking;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import testjboxnetworking.creationActions.Domino;

/**
 *
 * @author Daniel Boy
 */
public strictfp class TestJboxNetworking {

    float hz=60;
    World mondo2d;
    float timeStep = hz > 0f ? 1f / hz : 0;
    int actualStep=0;
    CommunicationLayer commLink = new CommunicationLayer();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new TestJboxNetworking();
    }

    public TestJboxNetworking() {
        mondo2d = new World(new Vec2(1.0f, 0), true);
        System.out.println("staring");
        populateWorld();
        System.out.println("world entries:"+mondo2d.getBodyCount());
        checkWorld();
        
        while (true){
        System.out.println("RUN");
        runWorld();
        System.out.println("end");
        //printWorld();
        
        checkWorld();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestJboxNetworking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void populateWorld() {
        ActionsContainer cont = new ActionsContainer(false, actualStep);
        Domino temp;
        for (int x=0;x<10;x++){
            for (int y=0;y<10;y++){
                temp = new Domino(new Vec2(x, y), false);
                temp.make(mondo2d);
                cont.add(temp);
            }
        }
        write(cont);
    }

    private void runWorld() {
        actualStep++;
        System.out.println("W1 actualStep:"+actualStep);
        mondo2d.step(timeStep, 3, 8);
        write(new ActionsContainer(true, actualStep));
    }

    private void printWorld() {
        for (Body body = mondo2d.getBodyList(); body != null; body = body.getNext()) {
            System.out.println( "corpo:"+body.getPosition()+" "+body.getAngle() );
        }
    }

    private void write(ActionsContainer cont) {
        commLink.write(cont);
    }

    private void checkWorld() {
        WorldCopy copy = new WorldCopy(actualStep);
        SerializableBody info;
        for (Body body = mondo2d.getBodyList(); body != null; body = body.getNext()) {
            info = new SerializableBody(body);
            copy.add(info);
            //
        }
        System.out.println( "body count:"+mondo2d.getBodyCount() );
        write(copy);
    }

    private void write(WorldCopy copy) {
        commLink.write(copy);
    }
}
