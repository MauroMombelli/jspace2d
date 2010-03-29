package Components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jmex.model.ogrexml.anim.AnimationChannel;
import com.jmex.model.ogrexml.anim.MeshAnimationController;

import Actions.actionContainer;
import Actions.materialMode;
import Actions.quit;
import Actions.testMode;
import Main.main;

public class window implements WindowListener,KeyListener{
	public JFrame frame;	
	public menubar menubar;
	public modepanel modepanel;
	public basepanel basepanel;
	public underpanel underpanel;
	public splitpane splitpane;
	public tabbedpane tabbedpane;
	private actionContainer actions;
	private JPanel canvasPanel;
	private main Main;
	private boolean rightturret;
	private boolean leftturret;
	
	public window(actionContainer act, JPanel c, main m){
		leftturret = false;
		rightturret = false;
		Main = m;
		canvasPanel = c;
		canvasPanel.addKeyListener(this);
		actions = act;
				
		//basic setup
		startup();
		JMenuBar();
		addPanels();
		
		frame.validate();
		frame.repaint();
	}	
	
	public void testMode() {
		splitpane.tabbedpane.testMode();
		frame.validate();
		frame.repaint();
	}

	public void materialMode() {
		splitpane.tabbedpane.materialMode();
		splitpane.tabbedpane.matpanel.button.addKeyListener(this);
		splitpane.tabbedpane.matpanel.panel.addKeyListener(this);
		frame.validate();
		frame.repaint();
	}
	
	private void addSplitPane() {
		splitpane = new splitpane(actions,canvasPanel);
		splitpane.splitPane.addKeyListener(this);
		splitpane.tabbedpane.tabbedPane.addKeyListener(this);		
		underpanel.panel.add(splitpane.splitPane);		
	}
	
	private void addPanels() {
		addBasePanel();
		addModePanel();
		addUnderPanel();	
		addSplitPane();
	}

	private void addUnderPanel() {
		underpanel = new underpanel();
		underpanel.panel.addKeyListener(this);
		underpanel.panel.setLayout(new BoxLayout(underpanel.panel, BoxLayout.LINE_AXIS));
		basepanel.panel.add(underpanel.panel,BorderLayout.CENTER);
	}

	private void addBasePanel() {
		basepanel = new basepanel();
		basepanel.panel.addKeyListener(this);
		basepanel.panel.setLayout(new BorderLayout());
		frame.add(basepanel.panel);
	}

	private void addModePanel() {
		modepanel = new modepanel(actions);
		modepanel.panel.addKeyListener(this);
		modepanel.buildButton.addKeyListener(this);
		modepanel.testButton.addKeyListener(this);
		basepanel.panel.add(modepanel.panel,BorderLayout.NORTH);
	}

	private void startup() {
		GraphicsEnvironment env =
		     GraphicsEnvironment.getLocalGraphicsEnvironment();		
		frame = new JFrame("Editor");
		frame.setMaximizedBounds(env.getMaximumWindowBounds());
		frame.setPreferredSize(new Dimension(800,600));		
		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE); 		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
		frame.setVisible(true);		
		frame.addWindowListener(this);	
		frame.addKeyListener(this);
	}
	
	private void JMenuBar() {
		menubar = new menubar(actions);
		frame.setJMenuBar(menubar.menubar);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		actions.quit.actionPerformed(null);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void refreshAll() {
		frame.validate();
		frame.repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getComponent().equals(canvasPanel.getComponent(0))){
			manageCanvasKeys(arg0);
		}
		
	}

	private void manageCanvasKeys(KeyEvent arg0) {
		if(arg0.getID()==arg0.KEY_PRESSED){
			if(arg0.getKeyCode()==103){
				actions.cam.top.actionPerformed(null);
			}
			
			if(arg0.getKeyCode()==97){
				actions.cam.front.actionPerformed(null);
			}
			
			if(arg0.getKeyCode()==99){
				actions.cam.side.actionPerformed(null);
			}
		}
			if(arg0.getKeyChar()=='k'){
				
				if(!leftturret&&arg0.getID()==arg0.KEY_PRESSED){
					System.out.println("animating left");
					MeshAnimationController animControl = (MeshAnimationController) Main.GLCanvas.impl.object.model.getController(0);			
					AnimationChannel left = animControl.getAnimationChannel("Left");	        
					left.addFromRootBone("Turret_L");
			        animControl.setAnimation(left, "Left Turret");
			        leftturret = true;
				}
				if(arg0.getID()==arg0.KEY_RELEASED){	
						System.out.println("releasing k");
						MeshAnimationController animControl = (MeshAnimationController) Main.GLCanvas.impl.object.model.getController(0);			
						AnimationChannel left = animControl.retrieveAnimationChannel("Left");
				        animControl.setAnimation(left, "<bind>");
				        leftturret = false;
				}
			}
			
			if(arg0.getKeyChar()=='l'){

				if(!rightturret&&arg0.getID()==arg0.KEY_PRESSED ){
					System.out.println("animating right");
					MeshAnimationController animControl = (MeshAnimationController) Main.GLCanvas.impl.object.model.getController(0);			
					AnimationChannel right = animControl.getAnimationChannel("Right");
			        right.addFromRootBone("Turret_R");
			        animControl.setAnimation(right, "Right Turret");
		        	rightturret = true;
				}
				if(arg0.getID()==arg0.KEY_RELEASED){	
					System.out.println("releasing l");
					MeshAnimationController animControl = (MeshAnimationController) Main.GLCanvas.impl.object.model.getController(0);			
					AnimationChannel right = animControl.retrieveAnimationChannel("Right");
			        animControl.setAnimation(right, "<bind>");		   
			        rightturret = false;
				}
			}
		
			
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getComponent().equals(canvasPanel.getComponent(0))){
			manageCanvasKeys(arg0);
		}		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void placeMode(boolean s) {
		if(s){
			s=!s;
			modepanel.panel.setEnabled(s);
			modepanel.buildButton.setEnabled(s);
			modepanel.testButton.setEnabled(s);					
		}
		
	}
	
}
