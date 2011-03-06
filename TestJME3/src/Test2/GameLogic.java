/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test2;

import Test2.container.ActionDB;
import Test2.container.ObjectDB;
import Test2.container.PlayerDB;
import TestAPIjme3Jbullet.actions.PhysicActions;
import TestAPIjme3Jbullet.actions.PhysicObjectCreate;
import com.jme3.system.AppSettings;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author mauro
 */
public class GameLogic implements Observer{
    CustomApplication app;
    
    PlayerDB players = new PlayerDB();
    ActionDB actions = new ActionDB();
    ObjectDB objects = new ObjectDB();
    
    Communication comm;

    public GameLogic(){
        /*STARTING APP*/
        app = new CustomApplication();

        actions.addObserver(this);
        app.setDB(actions, objects);

        AppSettings settings = new AppSettings(true);

        settings.setTitle("WOWOWOWOOW");
        app.setSettings( settings );

        app.start();

    }

    public void update(Observable o, Object arg) {

    }

    public void populate() {
        for (int x=0; x < 4; x+=3){
            for (int z=0; z < 4; z+=3){
                actions.addWaiting(new PhysicObjectCreate(objects.getNewID(), x, 0, z));
            }
        }
    }

}
