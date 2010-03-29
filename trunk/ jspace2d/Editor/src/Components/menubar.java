package Components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import Actions.actionContainer;
import Actions.quit;

public class menubar {
	
	public JMenuBar menubar;
	public JMenu menu;
	public JMenuItem loadItem;
	public JMenuItem openItem;
	public JMenuItem quitItem;
	private actionContainer actions;
	
	public menubar(actionContainer act){
		actions = act;
		menubar = new JMenuBar();
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);		
		menubar.add(menu);		
		addItems();
	}


	private void addItems() {
		loadItem = new JMenuItem(actions.load);
		menu.add(loadItem);
		quitItem = new JMenuItem(actions.quit);		
		menu.add(quitItem);
	}
	

}
