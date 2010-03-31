package Components;

import javax.swing.JButton;
import javax.swing.JPanel;

import Actions.actionContainer;

public class lightsTab {
	
	public JPanel panel;
	actionContainer actions;
	public JButton button;
	
	public lightsTab(actionContainer act){
		actions = act;
		panel = new JPanel();
		button = new JButton(actions.refresh);		
		panel.add(button);
	}
}
