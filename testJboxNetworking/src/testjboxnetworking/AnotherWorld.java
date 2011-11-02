/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testjboxnetworking;

import java.util.Collections;
import testjboxnetworking.creationActions.CreationAction;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 *
 * @author Daniel Boy
 */
public class AnotherWorld extends Thread{

    float hz = 60;
    World mondo2d;
    float timeStep = hz > 0f ? 1f / hz : 0;
    
    List<ActionsContainer> actionList = Collections.synchronizedList(new LinkedList<ActionsContainer>());
    List<WorldCopy> worldCopy = Collections.synchronizedList(new LinkedList<WorldCopy>());
    
    LinkedBlockingQueue<Object> waitToken = new LinkedBlockingQueue<Object>();
    
    public AnotherWorld() {
        mondo2d = new World(new Vec2(1.0f, 0), true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                waitToken.take();
                if (actionList.size()>0){
                    ActionsContainer container = actionList.get(0);
                    actionList.remove(0);
                    addActionAndStep(container);
                }
                if (worldCopy.size()>0){
                    WorldCopy c = worldCopy.get(0);
                    worldCopy.remove(0);
                    checkWorld(c);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(AnotherWorld.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void add(ActionsContainer cont) {
        actionList.add(cont);
    }

    void add(WorldCopy copy) {
        worldCopy.add(copy);
    }

    private void checkWorld(WorldCopy c) {
        WorldCopy copy = c;
        SerializableBody b;
        System.out.println("W2 checking:");
        System.out.println( "W2 body count:"+mondo2d.getBodyCount() );
        for (Body body = mondo2d.getBodyList(); body != null; body = body.getNext()) {
            b = copy.poll();
            if (b==null){
                System.out.println("W2 determinism FAIL!!, b is null");
            }else if ( !b.check(body) ){
                System.out.println("W2 determinism FAIL!!");
            }
            //System.out.println( "corpo:"+body.getPosition()+" "+body.getAngle() );
        }
        System.out.println("W2 END check");
    }

    private void addActionAndStep(ActionsContainer container) {
        LinkedList<CreationAction> cA = container.getCreationsAction();
        for (CreationAction c : cA) {
            c.make(mondo2d);
        }
        if (container.getStep()){
            mondo2d.step(hz, 3, 8);
        }
    }

    void wakeUp() {
        waitToken.offer(new Object());
    }
}
