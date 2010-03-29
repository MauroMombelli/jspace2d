package Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import Main.main;

public class refresh extends AbstractAction{
	
	private main Main;
	
	public refresh(main m){
		Main = m;
		putValue(SHORT_DESCRIPTION, "Refresh");
		putValue(NAME, "Refresh");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("refreshing");
		Main.finestra.refreshAll();
		
	}

}
