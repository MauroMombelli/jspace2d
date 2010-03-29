package Camera_Positions;

import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;

import Main.main;

import com.jme.math.Vector3f;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;

public class Front_View extends AbstractAction{
	private main Main;

	public Front_View(main m) {
		Main = m;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {	
		Main.GLCanvas.camhand.recenterCamera();
		
		Main.GLCanvas.impl.getRenderer().getCamera().setLocation(new Vector3f(0,0,50));		
		Main.GLCanvas.impl.getRenderer().getCamera().lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
		
		Callable<Void> exe = new Callable<Void>() {
	        public Void call() {
	        	Main.GLCanvas.impl.getRenderer().getCamera().setLocation(new Vector3f(0,0,50));		
	    		Main.GLCanvas.impl.getRenderer().getCamera().lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
				 return null;
	        }
	    };
	    GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER)
	            .enqueue(exe);
	}
}
