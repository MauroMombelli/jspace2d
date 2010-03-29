package Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import Main.main;

public class testMode extends AbstractAction{
	
	private materialMode mod;
	private main Main;
	
	public testMode(main main){
		Main = main;
		putValue(SHORT_DESCRIPTION, "Test Mode");
		putValue(NAME, "TEST");
	}
	
	public void setOther(materialMode arg){
		mod = arg;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("TEST MODE");
		setEnabled(false);
		mod.setEnabled(true);
		Main.finestra.testMode();
		Main.testMode();
	}
}
