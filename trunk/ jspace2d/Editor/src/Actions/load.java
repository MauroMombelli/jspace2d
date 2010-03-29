package Actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import Import_Export.Model_Importer;
import Main.main;
import Model.Model;

public class load extends AbstractAction{
	
	private main Main;
	
	public load(main m){
		Main = m;
		putValue(SHORT_DESCRIPTION, "Load");
		putValue(NAME, "Load model...");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("loading...");	
		
		//Create a file chooser
		final JFileChooser fc = new JFileChooser();		
		
		int returnVal = fc.showOpenDialog(Main.finestra.frame);
		
		if(returnVal==fc.APPROVE_OPTION){			
			File file = fc.getSelectedFile();
				
			if(Main.GLCanvas.impl.object!=null){
				Main.GLCanvas.impl.object.release();
			}
				Main.GLCanvas.impl.object = new Model(Main.GLCanvas.impl.root, file, Main.GLCanvas.impl.getRenderer().createMaterialState() );					
			
		}
	}
}
