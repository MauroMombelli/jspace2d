package Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Actions.actionContainer;
import Actions.materialMode;
import Actions.testMode;

public class modepanel {
	public JPanel panel;
	public JButton buildButton;
	public JButton testButton;
	private actionContainer actions;
	
	public modepanel(actionContainer act){
		actions = act; 
		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 5, 5));	
		panel.setPreferredSize(new Dimension(100,50));
		addButtons();
	}

	private void addButtons() {
		buildButton = new JButton(actions.materialmode);
		//buildButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		panel.add(buildButton);
		testButton = new JButton(actions.testmode);
		//testButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		panel.add(testButton);		
	}
}
