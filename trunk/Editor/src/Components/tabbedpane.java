package Components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Actions.actionContainer;

public class tabbedpane {
	
	public JTabbedPane tabbedPane;
	private actionContainer actions;
	public materialTab matpanel;
	public lightsTab lightspanel;
	
	public tabbedpane(actionContainer act){
		actions = act;
		tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(200,300));
		lightspanel = new lightsTab(actions);
		matpanel = new materialTab(actions);
		tabbedPane.addTab("Material", matpanel.panel);
		tabbedPane.validate();
	}
	
	public void materialMode(){				
		
	}
	
	public void testMode(){		
		
	}
}
