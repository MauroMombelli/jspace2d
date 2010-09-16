package GUI;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import GUI.supportComponents.ClientTableModel;
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
public class CopyOfMainFrame extends javax.swing.JFrame implements FocusListener, ListSelectionListener, Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7375850437139062005L;

	{
		//Set Look & Feel
		try {			
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			javax.swing.ToolTipManager.sharedInstance().setInitialDelay(1000);
			javax.swing.ToolTipManager.sharedInstance().setReshowDelay(200);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JMenuBar jMenuBar1;
	private AbstractAction exitAction;
	private JMenuItem jMenuItem1;
	private JMenu jMenu1;
	private int lastSelected = -1;
	private JScrollPane jScrollPane2;
	private StatusCommandPanel statusCommandPanel1;
	private JScrollPane jScrollPane4;
	private JScrollPane jScrollPane1;
	private JPanel jPanel4;
	private JPanel jPanel3;
	private JPanel statusViewerPanel;
	private ClientTable jTable1;
	private JScrollPane jScrollPane3;
	private ClientCommandPanel clientCommandPanel1;
	private JPanel jPanel2;
	private JPanel jPanel1;
	private JPanel clientManagerPanel;
	private JTabbedPane jTabbedPane1;
	public ObservableFlag _observableFlag;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CopyOfMainFrame inst = new CopyOfMainFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				System.out.println("hey finished");
			}
		});
	}
	
	public CopyOfMainFrame() {
		super();
		initGUI();
		/*
		 	instanciate after the EDT has finished.
		 */
		_observableFlag = new ObservableFlag(this);
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setPreferredSize(new java.awt.Dimension(688, 344));
			this.setMinimumSize(new java.awt.Dimension(400, 200));
			this.setVisible(false);
			this.setTitle("JavaSpace2D server");			
			getContentPane().add(getJTabbedPane1(), BorderLayout.CENTER);
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("File");
					{
						jMenuItem1 = new JMenuItem();
						jMenu1.add(jMenuItem1);
						jMenuItem1.setText("jMenuItem1");
						jMenuItem1.setAction(getExitAction());
					}
				}
			}
			pack();
			this.setSize(688, 344);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private AbstractAction getExitAction() {
		if(exitAction == null) {
			exitAction = new AbstractAction("Exit", null) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -1226178302931423268L;

				public void actionPerformed(ActionEvent evt) {
					exit();
				}
			};
		}
		return exitAction;
	}
	
	protected void exit() {
		dispose();		
	}

	@Override
	public void focusGained(FocusEvent arg0) {		
				
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		int currentSelected = jTable1.getSelectedRow();
		
		if(!arg0.getValueIsAdjusting()){			
			if(currentSelected!=-1){				
				if(currentSelected!=lastSelected){					
					clientCommandPanel1.clientSelected(1);
				}else{					
					jTable1.getSelectionModel().removeIndexInterval(currentSelected, currentSelected);
					clientCommandPanel1.clientSelected(0);
				}
			}else{
				clientCommandPanel1.clientSelected(0);
			}			
			lastSelected = currentSelected;		
		}
		
		
	}

	private boolean setTabEnabled( Component component, boolean state){
		int index = jTabbedPane1.indexOfComponent(component);
		jTabbedPane1.setEnabledAt(index, state);		
		return state;
	}	
	
	private JTabbedPane getJTabbedPane1() {
		if(jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			jTabbedPane1.addTab("Server status", null, getStatusViewerPanel(), null);
			jTabbedPane1.addTab("Client manager", null, getClientManagerPanel(), null);
			//setTabEnabled(getClientManagerPanel(), false);
		}
		return jTabbedPane1;
	}
	
	private JPanel getClientManagerPanel() {
		if(clientManagerPanel == null) {
			clientManagerPanel = new JPanel();
			GridBagLayout clientManagerPanelLayout = new GridBagLayout();
			clientManagerPanel.setLayout(clientManagerPanelLayout);
			clientManagerPanel.add(getJPanel1(), new GridBagConstraints(0, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			clientManagerPanel.add(getJPanel2(), new GridBagConstraints(1, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			clientManagerPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			clientManagerPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			clientManagerPanelLayout.columnWeights = new double[] {1.0, 0.0};
			clientManagerPanelLayout.columnWidths = new int[] {450, 7};
		}
		return clientManagerPanel;
	}
	
	private JPanel getJPanel1() {
		if(jPanel1 == null) {
			jPanel1 = new JPanel();
			GroupLayout jPanel1Layout1 = new GroupLayout((JComponent)jPanel1);
			jPanel1.setLayout(jPanel1Layout1);
			jPanel1Layout1.setHorizontalGroup(jPanel1Layout1.createSequentialGroup()
				.addComponent(getJScrollPane3(), 0, 666, Short.MAX_VALUE));
			jPanel1Layout1.setVerticalGroup(jPanel1Layout1.createSequentialGroup()
				.addComponent(getJScrollPane3(), 0, 244, Short.MAX_VALUE));
		}
		return jPanel1;
	}
	
	private JPanel getJPanel2() {
		if(jPanel2 == null) {
			jPanel2 = new JPanel();
			GroupLayout jPanel2Layout = new GroupLayout((JComponent)jPanel2);
			jPanel2.setLayout(jPanel2Layout);
			jPanel2.setMinimumSize(new java.awt.Dimension(218, 10));
			jPanel2Layout.setHorizontalGroup(jPanel2Layout.createSequentialGroup()
				.addComponent(getJScrollPane2(), 0, 240, Short.MAX_VALUE));
			jPanel2Layout.setVerticalGroup(jPanel2Layout.createSequentialGroup()
				.addComponent(getJScrollPane2(), 0, 244, Short.MAX_VALUE));
		}
		return jPanel2;
	}	
	
	private JScrollPane getJScrollPane2() {
		if(jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getClientCommandPanel1());
		}
		return jScrollPane2;
	}
	
	private ClientCommandPanel getClientCommandPanel1() {
		if(clientCommandPanel1 == null) {
			clientCommandPanel1 = new ClientCommandPanel();
			clientCommandPanel1._observableFlag.addObserver(this);
			clientCommandPanel1.setPreferredSize(new java.awt.Dimension(197, 240));
		}
		return clientCommandPanel1;
	}
	
	private JScrollPane getJScrollPane3() {
		if(jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setBounds(0, 0, 333, 244);
			jScrollPane3.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			jScrollPane3.setViewportView(getJTable1());
		}
		return jScrollPane3;
	}	
	
	private ClientTable getJTable1() {
		if(jTable1 == null) {
			TableModel jTable1Model = new ClientTableModel();
			jTable1 = new ClientTable();
			jTable1.setModel(jTable1Model);
		}
		return jTable1;
	}
	
	private JPanel getStatusViewerPanel() {
		if(statusViewerPanel == null) {
			statusViewerPanel = new JPanel();
			GridBagLayout statusViewerPanelLayout = new GridBagLayout();
			statusViewerPanel.setLayout(statusViewerPanelLayout);
			statusViewerPanel.add(getJPanel3(), new GridBagConstraints(0, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			statusViewerPanel.add(getJPanel4(), new GridBagConstraints(1, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			statusViewerPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			statusViewerPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			statusViewerPanelLayout.columnWeights = new double[] {1.0, 0.0};
			statusViewerPanelLayout.columnWidths = new int[] {450, 7};
		}
		return statusViewerPanel;
	}
	
	private JPanel getJPanel3() {
		if(jPanel3 == null) {
			jPanel3 = new JPanel();
			GroupLayout jPanel3Layout = new GroupLayout((JComponent)jPanel3);
			jPanel3.setLayout(jPanel3Layout);
			jPanel3.setOpaque(false);
			jPanel3Layout.setHorizontalGroup(jPanel3Layout.createSequentialGroup()
				.addComponent(getJScrollPane4(), 0, 487, Short.MAX_VALUE));
			jPanel3Layout.setVerticalGroup(jPanel3Layout.createSequentialGroup()
				.addComponent(getJScrollPane4(), 0, 246, Short.MAX_VALUE));
		}
		return jPanel3;
	}
	
	private JPanel getJPanel4() {
		if(jPanel4 == null) {
			jPanel4 = new JPanel();
			GroupLayout jPanel4Layout = new GroupLayout((JComponent)jPanel4);
			jPanel4.setLayout(jPanel4Layout);
			jPanel4.setSize(218, 246);
			jPanel4.setMinimumSize(new java.awt.Dimension(218, 10));
			jPanel4Layout.setHorizontalGroup(jPanel4Layout.createSequentialGroup()
				.addComponent(getJScrollPane1(), 0, 492, Short.MAX_VALUE));
			jPanel4Layout.setVerticalGroup(jPanel4Layout.createSequentialGroup()
				.addComponent(getJScrollPane1(), 0, 245, Short.MAX_VALUE));
		}
		return jPanel4;
	}
	
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getStatusCommandPanel1());
		}
		return jScrollPane1;
	}
	
	private JScrollPane getJScrollPane4() {
		if(jScrollPane4 == null) {
			jScrollPane4 = new JScrollPane();
		}
		return jScrollPane4;
	}
	
	private StatusCommandPanel getStatusCommandPanel1() {
		if(statusCommandPanel1 == null) {
			statusCommandPanel1 = new StatusCommandPanel();
			statusCommandPanel1._observableFlag.addObserver(this);
			statusCommandPanel1.setSize(240, 240);
		}
		return statusCommandPanel1;
	}

	@Override
	public synchronized void update(Observable arg0, Object arg1) {
		//System.out.println("Passing from mainFrame: "+((GUIEvent)arg1).getEvent().getActionCommand());		
		_observableFlag.alertObservers(arg1);
	}

}
