package Components;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Actions.actionContainer;

public class materialTab {
	
	public JPanel panel;
	actionContainer actions;
	public JButton button;
	
	public materialTab(actionContainer act){
		actions = act;
		panel = new JPanel();
		button = new JButton(actions.refresh);		
		panel.add(button);		
	}
}
