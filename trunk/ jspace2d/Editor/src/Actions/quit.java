package Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import Main.main;

public class quit extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private main Main;
	
	public quit(main m){
		Main = m;
		putValue(SHORT_DESCRIPTION, "Quit");
		putValue(NAME, "Quit");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(JOptionPane.showConfirmDialog(null, "Are you sure? All unsaved changes will be lost.")==0){
			System.exit(0);
		}		
	}

}
