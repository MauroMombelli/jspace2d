package Components;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import Actions.actionContainer;
import Main.main;

public class splitpane {
	
	public JSplitPane splitPane;
	public tabbedpane tabbedpane;
	private JPanel canvasPanel;
	
	public splitpane(actionContainer act,JPanel c){		
		tabbedpane = new tabbedpane(act);
		splitPane = new JSplitPane();
		canvasPanel = c;		
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(tabbedpane.tabbedPane);
       	splitPane.setRightComponent(canvasPanel);
        
		splitPane.setDividerSize(10);
	}
	
}
