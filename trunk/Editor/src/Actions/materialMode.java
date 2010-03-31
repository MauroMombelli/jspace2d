package Actions;

import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.lwjgl.LWJGLCamera;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.JMECanvas;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;

import Components.window;
import Main.main;

public class materialMode extends AbstractAction{
	
	private testMode mod;
	private main Main;
	private LWJGLCamera camOrtho;
	
	public materialMode(main main){
		Main = main;
		putValue(SHORT_DESCRIPTION, "Build Mode");
		putValue(NAME, "BUILD");
	}
	
	public void setOther(testMode arg){
		mod = arg;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {	
				System.out.println("MATERIAL MODE");
				setEnabled(false);
				mod.setEnabled(true);
				Main.finestra.materialMode();		
				Main.GLCanvas.impl.setPhys_World(null);
				if(Main.GLCanvas.impl.object!=null)
					Main.GLCanvas.impl.object.reset();
				Main.GLCanvas.impl.root.attachChild(Main.GLCanvas.impl.grid);
				
	}

}
