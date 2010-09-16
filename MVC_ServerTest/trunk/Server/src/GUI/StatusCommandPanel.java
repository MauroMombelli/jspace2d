package GUI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import GUI.supportComponents.ActionContainer;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class StatusCommandPanel extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1811844285121315830L;



	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JButton jButton1;
	private AbstractAction stopServerAction;
	private AbstractAction startServerAction;
	private JButton jButton2;
	private JSeparator jSeparator1;	
	private ActionContainer actionContainer;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new StatusCommandPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public StatusCommandPanel() {
		super();
		initGUI();
		/*
	 	instanciate after the EDT has finished.
		*/
	}
	
	public StatusCommandPanel(ActionContainer actionContainer) {
		super();
		setActionContainer(actionContainer);
		initGUI();
		/*
	 	instanciate after the EDT has finished.
		*/
	}

	private void initGUI() {
		try {
			{
				this.setBorder(BorderFactory.createTitledBorder("Commands"));
				GroupLayout thisLayout = new GroupLayout((JComponent)this);
				this.setLayout(thisLayout);
				{
					jButton1 = new JButton();
					jButton1.setToolTipText("Start the server");
					jButton1.setBounds(14, 32, 143, 28);
					jButton1.setAction(getStartServerAction());
				}
				{
					jSeparator1 = new JSeparator();
					jSeparator1.setBounds(20, 66, 131, 10);
				}
				{
					jButton2 = new JButton();
					jButton2.setText("Stop server");
					jButton2.setToolTipText("Stop the server");
					jButton2.setBounds(14, 76, 143, 28);
					jButton2.setAction(getStopServerAction());
				}
					thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
						.add(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.RELATED)
						.add(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
						.add(jButton2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
					thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
					.add(GroupLayout.LEADING, thisLayout.createParallelGroup()
					    .add(GroupLayout.LEADING, jButton2, 0, 143, Short.MAX_VALUE)
					    .add(GroupLayout.LEADING, jButton1, 0, 143, Short.MAX_VALUE))
					.add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
					    .addPreferredGap(jButton2, jSeparator1, LayoutStyle.INDENT)
					    .add(jSeparator1, 0, 131, Short.MAX_VALUE)
					    .addContainerGap()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private AbstractAction getStartServerAction() {
		if(startServerAction == null) {
			startServerAction = actionContainer.getStartServerAction();
		}
		return startServerAction;
	}

	private AbstractAction getStopServerAction() {
		if(stopServerAction == null) {
			stopServerAction = actionContainer.getStopServerAction();
		}
		return stopServerAction;
	}

	public void setActionContainer(ActionContainer actionContainer) {
		this.actionContainer = actionContainer;
	}

	public ActionContainer getActionContainer() {
		return actionContainer;
	}
}
