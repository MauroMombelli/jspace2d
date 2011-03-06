/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test2;

import Test2.AppState.GraphicsAppState;
import Test2.AppState.AppStateBullet;
import Test2.container.ActionDB;
import Test2.container.ObjectDB;
import com.jme3.app.SimpleApplication;
/**
 *
 * @author mauro
 */
public class CustomApplication extends SimpleApplication{

    AppStateBullet bullet = new AppStateBullet();
    GraphicsAppState graphic;
    
    @Override
    public void simpleInitApp() {
        //super.simpleInitApp();
        stateManager.attach(bullet);

        graphic = new GraphicsAppState(rootNode, guiNode, settings);
        
        stateManager.attach(graphic);
        //PhysicsTestHelper.createPhysicsTestWorld( rootNode, assetManager, state.getPhysicsSpace() );

        //populate();
    }

    @Override
    public void update(){
        //cam.setLocation(new Vector3f(0, 20, 0));
        super.update();
        float tpf = timer.getTimePerFrame();

        stateManager.update(tpf);
        
        stateManager.render(renderManager);
        renderManager.render(tpf);

    }
/*
    private void populate() {
        //CREATE A 2D ARRAY OF SPHERE!
        {
            ObjectInfo temp;
            for (int x=0; x<40;x+=3){
                for (int y=0; y<40;y+=3){
                    temp = bullet.loadObject(x, 0, y);
                    
                    bullet.addObject( temp );

                    graphic.addObject( temp );
                }
            }
        }

        //CREATE THE SPHERE DESTROPIER!

        {
            ObjectInfo temp = bullet.loadObject(-1000, 0, -1000);
            bullet.addObject( temp );
            graphic.addObject( temp );

            PhysicActionImpulse impulse = new PhysicActionImpulse(temp.getID(), 100, 0, 99);
            bullet.addAction(impulse);
        }
 
    }
*/

    void setDB(ActionDB actions, ObjectDB objects) {
        bullet.setDB(actions, objects);
    }

}
