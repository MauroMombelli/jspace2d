package GUI;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

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
public class ChatDialog extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8357708044938608332L;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JSplitPane jSplitPane1;
	private JPanel jPanel1;
	private JButton jButton1;
	private JEditorPane jEditorPane2;
	private JEditorPane jEditorPane1;
	private JPanel jPanel2;

	/**
	 * Auto-generated main method to display this JDialog
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				ChatDialog inst = new ChatDialog(frame);
				inst.setVisible(true);
			}
		});
	}

	public ChatDialog(JFrame frame) {
		super(frame);
		initGUI();
	}

	private void initGUI() {
		try {
			{
				jSplitPane1 = new JSplitPane();
				getContentPane().add(jSplitPane1, BorderLayout.CENTER);
				jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
				jSplitPane1.setDividerLocation(300);
				jSplitPane1.setPreferredSize(new java.awt.Dimension(642, 168));
				{
					jPanel1 = new JPanel();
					GroupLayout jPanel1Layout = new GroupLayout((JComponent)jPanel1);
					jPanel1.setLayout(jPanel1Layout);
					jSplitPane1.add(jPanel1, JSplitPane.BOTTOM);
					jPanel1.setBorder(BorderFactory.createTitledBorder(null, "Player chat", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));
					{
						jButton1 = new JButton();
						jButton1.setText("Send");
						jButton1.setFont(new java.awt.Font("SansSerif",1,20));
						jButton1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					}
					{
						jEditorPane2 = new JEditorPane();
						jEditorPane2.setText("Type message here...");
						jEditorPane2.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					}
					jPanel1Layout.setHorizontalGroup(jPanel1Layout.createSequentialGroup()
							.addComponent(jEditorPane2, 0, 517, Short.MAX_VALUE)
							.addComponent(jButton1, 0, 123, Short.MAX_VALUE));
					jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup()
							.addComponent(jButton1, GroupLayout.Alignment.LEADING, 0, 93, Short.MAX_VALUE)
							.addComponent(jEditorPane2, GroupLayout.Alignment.LEADING, 0, 93, Short.MAX_VALUE));
				}
				{
					jPanel2 = new JPanel();
					GroupLayout jPanel2Layout = new GroupLayout((JComponent)jPanel2);
					jPanel2.setLayout(jPanel2Layout);
					jSplitPane1.add(jPanel2, JSplitPane.LEFT);
					jPanel2.setBorder(BorderFactory.createTitledBorder("Chatting with..."));
					{
						jEditorPane1 = new JEditorPane();
						jEditorPane1.setText("Chat goes here...");
						jEditorPane1.setEditable(false);
						jEditorPane1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					}
					jPanel2Layout.setHorizontalGroup(jPanel2Layout.createSequentialGroup()
							.addComponent(jEditorPane1, 0, 640, Short.MAX_VALUE));
					jPanel2Layout.setVerticalGroup(jPanel2Layout.createSequentialGroup()
							.addComponent(jEditorPane1, 0, 299, Short.MAX_VALUE));
				}
			}
			this.setSize(658, 440);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
