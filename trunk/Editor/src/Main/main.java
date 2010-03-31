
package Main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import Actions.actionContainer;
import Actions.materialMode;
import Actions.testMode;
import Components.canvasRenderer;
import Components.window;
import JBox.Physic_World;

import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.JMECanvas;
import com.jme.system.canvas.SimpleCanvasImpl;
import com.jme.system.lwjgl.LWJGLSystemProvider;
import com.jme.util.Debug;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jme.util.stat.StatCollector;
import com.jme.util.stat.StatType;
import com.jme.util.stat.graph.GraphFactory;
import com.jme.util.stat.graph.LineGrapher;
import com.jme.util.stat.graph.TabledLabelGrapher;
import com.jmex.awt.lwjgl.LWJGLAWTCanvasConstructor;
import com.jmex.effects.particles.ParticleSystem;
import com.jmex.game.state.GameStateManager;

 public class main implements KeyListener, ActionListener{
      public static void main(String[] args) { 
    	  try {
  			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  		} catch (ClassNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (InstantiationException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IllegalAccessException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (UnsupportedLookAndFeelException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
    			new main();
    }
    
    //variables
	public window finestra;
	private materialMode build_mode;
	private testMode test_mode;
	private actionContainer actions;
	public canvasRenderer GLCanvas;
	private JPanel canvasPanel;
	private Physic_World phys_world;
	private Timer timer;
	
    public main() {
    	setActions();    	
    	createJME();
        createwindow();       
    }
    
	private void createJME() {
		GLCanvas = new canvasRenderer(100, 100, actions, phys_world);		
		canvasPanel = new JPanel();
		canvasPanel.setLayout(new BorderLayout());
        canvasPanel.add(GLCanvas.canvas, BorderLayout.CENTER);
	}

	private void setActions() {
		actions = new actionContainer(this);
		actions.Intersecate();
	}

	private void createwindow() {		
		finestra = new window(actions, canvasPanel, this);
		GLCanvas.canvas.addKeyListener(finestra);		

		//start with BUILD MODE
		actions.materialmode.actionPerformed(null);
	}
	
	public void testMode(){
		
		phys_world = new Physic_World(this);
		GLCanvas.impl.setPhys_World(phys_world);
		timer = new Timer(50/3, this);
		timer.setInitialDelay(0);
		timer.start(); 

	}
	
	public void materialMode(){
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("main pressed");
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		phys_world.world.step(1/60f, 10);
		GLCanvas.impl.object.update();
	}
}