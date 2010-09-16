package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import GUI.supportComponents.GUIEvent;
import GUI.supportComponents.ObservableFlag;


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
public class CopyOfClientCommandPanel extends javax.swing.JPanel implements FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -471088501702354849L;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JButton jButton4;
	private JTextField jTextField1;
	private JButton jButton3;
	private JButton jButton1;
	private AbstractAction broadcastAction;
	private AbstractAction startPrivateChatAction;
	private AbstractAction banAction;
	private AbstractAction kickAction;
	private JSeparator jSeparator1;
	private JButton jButton2;
	private JSeparator jSeparator2;
	private JSeparator jSeparator3;
	public ObservableFlag _observableFlag;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new CopyOfClientCommandPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public CopyOfClientCommandPanel() {
		super();
		initGUI();
		_observableFlag = new ObservableFlag(this);
	}
	
	private void initGUI() {
		try {
			{
				GroupLayout thisLayout = new GroupLayout((JComponent)this);
				this.setLayout(thisLayout);
				this.setBorder(BorderFactory.createTitledBorder("Commands"));
				{
					jButton4 = new JButton();
					jButton4.setText("BROADCAST");
					jButton4.setBounds(14, 192, 143, 28);
					jButton4.setToolTipText("Send a short message to all clients (appears ingame)");
					jButton4.setAction(getBroadcastAction());
				}
				{
					jTextField1 = new JTextField();
					jTextField1.setText("Enter message...");
					jTextField1.setBounds(14, 164, 143, 28);
					jTextField1.addActionListener(broadcastAction);
					jTextField1.addFocusListener(this);
				}
				{
					jSeparator3 = new JSeparator();
					jSeparator3.setBounds(20, 154, 131, 10);
				}
				{
					jButton3 = new JButton();
					jButton3.setText("START PVT CHAT");
					jButton3.setBounds(14, 120, 143, 28);
					jButton3.setToolTipText("Open a new chat window with the selected client");
					jButton3.setAction(getStartPrivateChatAction());
				}
				{
					jSeparator2 = new JSeparator();
					jSeparator2.setBounds(20, 110, 131, 10);
				}
				{
					jButton2 = new JButton();
					jButton2.setText("BAN PLAYER");
					jButton2.setBounds(14, 76, 143, 28);
					jButton2.setToolTipText("Ban the selected clients IP (permanently or temporarily)");
					jButton2.setAction(getBanAction());
				}
				{
					jSeparator1 = new JSeparator();
					jSeparator1.setBounds(20, 66, 131, 10);
				}
				{
					jButton1 = new JButton();
					jButton1.setText("KICK PLAYER");
					jButton1.setBounds(14, 32, 143, 28);
					jButton1.setToolTipText("Kick the client from the game");
					jButton1.setAction(getKickAction());
				}
					thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
						.add(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.RELATED)
						.add(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
						.add(jButton2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.RELATED)
						.add(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
						.add(jButton3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.RELATED)
						.add(jSeparator3, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
						.add(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.add(jButton4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
					thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
						.add(GroupLayout.LEADING, jButton4, 0, 143, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, jTextField1, 0, 143, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, jButton3, 0, 143, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, jButton2, 0, 143, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, jButton1, 0, 143, Short.MAX_VALUE)
						.add(thisLayout.createSequentialGroup()
						    .addPreferredGap(jButton4, jSeparator3, LayoutStyle.INDENT)
						    .add(thisLayout.createParallelGroup()
						        .add(GroupLayout.LEADING, jSeparator3, 0, 131, Short.MAX_VALUE)
						        .add(GroupLayout.LEADING, jSeparator2, 0, 131, Short.MAX_VALUE)
						        .add(GroupLayout.LEADING, jSeparator1, 0, 131, Short.MAX_VALUE))
						    .add(6)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		if(arg0.getComponent().equals(jTextField1)){
			jTextField1.setSelectionStart(0);
			jTextField1.setSelectionEnd(jTextField1.getText().length());
		}		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		/*
		if(arg0.getComponent().equals(jTextField1)){
			jTextField1.setText("Enter message...");
		}
		*/
	}

	public void clientSelected(int i) {
		if(i!=1){
			banAction.setEnabled(false);
			startPrivateChatAction.setEnabled(false);
		}else{
			kickAction.setEnabled(true);
			banAction.setEnabled(true);
			startPrivateChatAction.setEnabled(true);
		}
	}
	
	private AbstractAction getKickAction() {
		if(kickAction == null) {
			kickAction = new AbstractAction("Kick player", null) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 4764161490349297108L;

				public void actionPerformed(ActionEvent evt) {
					//System.out.println("Passing from client command panel: "+evt.getActionCommand());
					_observableFlag.alertObservers(createGUIEvent(evt));
				}
			};
			kickAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Kick the client from the game");
		}
		//kickAction.setEnabled(false);
		return kickAction;
	}
	
	private AbstractAction getBanAction() {
		if(banAction == null) {
			banAction = new AbstractAction("Ban player", null) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 7311111719876941067L;

				public void actionPerformed(ActionEvent evt) {
					//System.out.println("Passing from client command panel: "+evt.getActionCommand());
					_observableFlag.alertObservers(createGUIEvent(evt));
				}
			};
			banAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Ban the selected clients IP (permanently or temporarily)");
		}
		//banAction.setEnabled(false);
		return banAction;
	}
	
	protected GUIEvent createGUIEvent(ActionEvent evt) {		
		return new GUIEvent(evt);
	}

	private AbstractAction getStartPrivateChatAction() {
		if(startPrivateChatAction == null) {
			startPrivateChatAction = new AbstractAction("Start private chat", null) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -1686949094033196879L;

				public void actionPerformed(ActionEvent evt) {
					//System.out.println("Passing from client command panel: "+evt.getActionCommand());
					_observableFlag.alertObservers(createGUIEvent(evt));
				}
			};
			startPrivateChatAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Open a new chat window with the selected client");
		}
		//startPrivateChatAction.setEnabled(false);
		return startPrivateChatAction;
	}
	
	private AbstractAction getBroadcastAction() {
		if(broadcastAction == null) {
			broadcastAction = new AbstractAction("Broadcast", null) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 2663868059302355900L;

				public void actionPerformed(ActionEvent evt) {
					if(evt.getSource().equals(jTextField1)){
						jTextField1.transferFocus();
					}
					//System.out.println("Passing from client command panel: "+evt.getActionCommand());
					_observableFlag.alertObservers(createGUIEvent(evt));
				}
			};
			broadcastAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Send a short message to all clients (appears ingame)");
		}
		return broadcastAction;
	}

}
