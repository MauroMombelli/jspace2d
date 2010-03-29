package Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;

import Main.main;

public class diffuseColor extends AbstractAction{
	
	private main Main;
	
	public diffuseColor(main m){
		Main = m;
	}	

	@Override
	public void actionPerformed(ActionEvent e) {
		JColorChooser temp = new JColorChooser();
		
	}

}
