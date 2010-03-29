package Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;

import Main.main;

public class shine extends AbstractAction{
	
	private main Main;
	public int value;
	
	public shine(main m){
		Main = m;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Shine");
		MaterialState temp = (MaterialState) Main.GLCanvas.impl.object.model.getRenderState(RenderState.RS_MATERIAL);
		temp.setShininess(value);
		Main.GLCanvas.impl.object.model.updateRenderState();
	}

}
