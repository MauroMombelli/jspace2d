/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test2;

import com.jme3.system.AppSettings;

/**
 *
 * @author mauro
 */
public class Main {

    public static void main(String args[]){
        new Main();
    }

    public Main(){

        GameLogic mess = new GameLogic();

        mess.populate();
        
    }

}
