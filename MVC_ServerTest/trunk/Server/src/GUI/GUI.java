package GUI;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import view.AbstractView;

import GUI.supportComponents.ActionContainer;

public class GUI extends AbstractView{
	
	private MainFrame mainFrame;
	private ActionContainer actionContainer;
	
	public GUI(){		
		super();		
		actionContainer = new ActionContainer(this);
		actionContainer.getStartServerAction();

		try {
			
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					mainFrame = new MainFrame(actionContainer);
					mainFrame.setLocationRelativeTo(null);
					mainFrame.setVisible(true);
				}
			});
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void event(String string, ActionEvent evt) {
		_observableFlag.alertObservers(createCommandString(string, evt));		
	}

	private String createCommandString(String _type, Object object) {
		String out = null;		
		
		/*
		 * TODO switch for each command. for now just let the command pass by
		 * 
		 * */
		
		out = _type; //supposed to be the command
		return out;		
	}

}
