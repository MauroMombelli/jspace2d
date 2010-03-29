package Jme_needings;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import jmetest.effects.RenParticleEditor;

import Actions.actionContainer;
import Import_Export.Model_Importer;
import JBox.Physic_World;
import Main.main;
import Model.Model;
import Utils.Conversions;

import com.jme.light.DirectionalLight;
import com.jme.light.Light;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.ProximityShadowGate;
import com.jme.renderer.pass.RenderPass;
import com.jme.renderer.pass.ShadowedRenderPass;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.canvas.SimpleCanvasImpl;
import com.jme.util.Debug;
import com.jme.util.stat.StatCollector;
import com.jme.util.stat.StatType;
import com.jme.util.stat.graph.GraphFactory;
import com.jme.util.stat.graph.LineGrapher;
import com.jme.util.stat.graph.TabledLabelGrapher;
import com.jmex.game.state.GameStateManager;

// IMPLEMENTING THE SCENE:

public class MyImplementor extends SimpleCanvasImpl {

    /**
     * The root node of our stat graphs.
     */
    protected Node statNode;

    private TabledLabelGrapher tgrapher;
    
    private static final Logger logger = Logger
    .getLogger(RenParticleEditor.class.getName());
    
    private Quad labGraph;

	private Preferences prefs = Preferences
    .userNodeForPackage(main.class); 

	public Canvas glCanvas;

	public Node root;

	public Spatial grid;
	
	private Spatial bounds;

	public Model object;

	private actionContainer actions;

	private Physic_World phys_world;

	private BasicPassManager passManager;

	private long time_pool = 0;

	private long lastTime;

	private boolean first = true;

	private static ShadowedRenderPass shadowPass = new ShadowedRenderPass();
	
    public MyImplementor(int width, int height, Canvas c, actionContainer act, Physic_World orld) {
    	
        super(width, height);
        phys_world = orld;
        actions = act;
        setCanvas(c);
        
    }

    public void setCanvas(Canvas c){
    	glCanvas = c;
    }
    
    private Geometry createGrid() {
		final int GRID_LINES = 100;
		final float GRID_SPACING = 1f;
		
        Vector3f[] vertices = new Vector3f[GRID_LINES * 2 * 2];
        float edge = GRID_LINES / 2 * GRID_SPACING;
        for (int ii = 0, idx = 0; ii < GRID_LINES; ii++) {
            float coord = (ii - GRID_LINES / 2) * GRID_SPACING;
            vertices[idx++] = new Vector3f(-edge, 0f, coord);
            vertices[idx++] = new Vector3f(+edge, 0f, coord);
            vertices[idx++] = new Vector3f(coord, 0f, -edge);
            vertices[idx++] = new Vector3f(coord, 0f, +edge);
        }
        Geometry grid = new com.jme.scene.Line("grid", vertices, null,
                null, null) {
            private static final long serialVersionUID = 1L;
            @Override
            public void draw(Renderer r) {
                StatCollector.pause();
                super.draw(r);
                StatCollector.resume();
            }
        };
        grid.getDefaultColor().set(ColorRGBA.darkGray.clone());
        grid.setCullHint(prefs.getBoolean("showgrid", true) ? Spatial.CullHint.Dynamic
                : Spatial.CullHint.Always);
        return grid;
    }
    
    public void simpleSetup() {    	
 
    	/** Set up a basic, default light. */
        DirectionalLight light = new DirectionalLight();
        light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, .5f));
        light.setDirection(new Vector3f(1,-1,0));
        light.setShadowCaster(true);
        light.setEnabled(true);

          /** Attach the light to a lightState and the lightState to rootNode. */
        LightState lightState = getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.setGlobalAmbient(new ColorRGBA(.2f, .2f, .2f, 1f));
        lightState.attach(light);
        root = rootNode;
        root.setRenderState(lightState);	     
	        
	       

	        
	        // Finally, a stand alone node (not attached to root on purpose)
	        statNode = new Node("stat node");
	        statNode.setCullHint(Spatial.CullHint.Never);
	        
	        if (Debug.stats) {
	            setupStatGraphs();
	            setupStats();
	        }
	        
	        bounds = createBounds();
	        //bounds.setLightCombineMode(LightCombineMode.Off);
	        root.attachChild(bounds);
	        grid = createGrid();
	        grid.setLightCombineMode(LightCombineMode.Off);
	        root.attachChild(grid);
	        
	        bounds.updateRenderState();
	        grid.updateRenderState();
	        actions.cam.top.actionPerformed(null);
	        
	        ZBufferState zbuf = renderer.createZBufferState();
	        zbuf.setWritable(false);
	        zbuf.setEnabled(true);
	        zbuf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);	
	
	        statNode.updateGeometricState(0, true);
	        statNode.updateRenderState();
	        String separator = System.getProperty("file.separator");
	        File file = new File("src"+separator+"Risorse"+separator+"Mesh.mesh.xml");
	         
			object = new Model(root, file, getRenderer().createMaterialState() );			
			buildPassManager();
	        
	        logger.info("Running on: "
	                + DisplaySystem.getDisplaySystem().getAdapter()
	                + "\nDriver version: "
	                + DisplaySystem.getDisplaySystem().getDriverVersion()
	                + "\n"
	                + DisplaySystem.getDisplaySystem().getDisplayVendor()
	                + " - "
	                + DisplaySystem.getDisplaySystem().getDisplayRenderer()
	                + " - "
	                + DisplaySystem.getDisplaySystem().getDisplayAPIVersion());
	        
	        
    }  
    
    private void buildPassManager() {
        passManager = new BasicPassManager();

        
        shadowPass.add(root);
        shadowPass.addOccluder(object.model);
//        shadowPass.addOccluder(flag);
        shadowPass.setRenderShadows(true);
        shadowPass.setLightingMethod(ShadowedRenderPass.LightingMethod.Modulative);
        passManager.add(shadowPass);
    }
    
    private Spatial createBounds() {
        Node bounds = new Node();
        Box leftborder = new Box("Left", Conversions.ConvertPhysics(new Vector3f(-10,0,0), false), Conversions.ConvertPhysics(1, false),Conversions.ConvertPhysics(1, false),Conversions.ConvertPhysics(10, false));
        Box rightborder = new Box("Right", Conversions.ConvertPhysics(new Vector3f(10,0,0), false), Conversions.ConvertPhysics(1, false),Conversions.ConvertPhysics(1, false),Conversions.ConvertPhysics(10, false));
        Box bottomborder = new Box("Bottom", Conversions.ConvertPhysics(new Vector3f(0,0,-10), false), Conversions.ConvertPhysics(10, false),Conversions.ConvertPhysics(1, false),Conversions.ConvertPhysics(1, false));
        Box topborder = new Box("Top", Conversions.ConvertPhysics(new Vector3f(0,0,10), false), Conversions.ConvertPhysics(10, false),Conversions.ConvertPhysics(1, false),Conversions.ConvertPhysics(1, false));
        bounds.attachChild(leftborder);
        bounds.attachChild(rightborder);
        bounds.attachChild(bottomborder);
        bounds.attachChild(topborder);
        return bounds;
	}

	private ColorRGBA makeColorRGBA(Color color) {
        return new ColorRGBA(color.getRed() / 255f, color.getGreen() / 255f,
                color.getBlue() / 255f, color.getAlpha() / 255f);
    }
    
    public void simpleUpdate() {
    	
    	if(phys_world!=null)
    		updatePhysicalWorld();
        if (Debug.stats) {
            StatCollector.update();
            labGraph.setLocalTranslation((renderer.getWidth()-.5f*labGraph.getWidth()), (renderer.getHeight()-.5f*labGraph.getHeight()), 0);
        }
    }

    public void setPhys_World(Physic_World a){
    	phys_world = a;
    }
    
    private void updatePhysicalWorld() {
    	if(!first ){
    		time_pool = System.nanoTime()-lastTime ;    		
    		//System.out.println("Time pool: "+time_pool);
    		float fraction = 1000000000/60f;
    		int number = (int) (time_pool/fraction);
    		//System.out.println("Number: "+number);
    		for(int i = 0; i<number; i++){
	    		phys_world.world.step(1/60f, 10);
	    		lastTime = System.nanoTime();
	    		time_pool -= fraction;
    		}
    		object.update();    
    	}
    	else{
    		phys_world.world.step(1/60f, 10);
    		lastTime = System.nanoTime();
    		time_pool -= 1/60f;
    		object.update();
    		first = false;
    	}
    		
	}

	@Override
    public void simpleRender() {
		//getRenderer().clearBuffers();
		
		statNode.draw(renderer);   
		passManager.renderPasses(renderer);
    }

    /**
     * Set up which stats to graph
     *
     */
    protected void setupStats() {
    	tgrapher.addConfig(StatType.STAT_FRAMES, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.green);
    	tgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.cyan);
    	tgrapher.addConfig(StatType.STAT_QUAD_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.lightGray);
    	tgrapher.addConfig(StatType.STAT_LINE_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.red);
    	tgrapher.addConfig(StatType.STAT_POINT_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.yellow);
    	tgrapher.addConfig(StatType.STAT_GEOM_COUNT, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.gray);
    	tgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, LineGrapher.ConfigKeys.Color.name(), ColorRGBA.orange);

        tgrapher.addConfig(StatType.STAT_FRAMES, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
        tgrapher.addConfig(StatType.STAT_FRAMES, TabledLabelGrapher.ConfigKeys.Name.name(), "Frames/s:");
        tgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
        tgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Tris:");
        tgrapher.addConfig(StatType.STAT_TRIANGLE_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
        tgrapher.addConfig(StatType.STAT_QUAD_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
        tgrapher.addConfig(StatType.STAT_QUAD_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Quads:");
        tgrapher.addConfig(StatType.STAT_QUAD_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
        tgrapher.addConfig(StatType.STAT_LINE_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
        tgrapher.addConfig(StatType.STAT_LINE_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Lines:");
        tgrapher.addConfig(StatType.STAT_LINE_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
        tgrapher.addConfig(StatType.STAT_POINT_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
        tgrapher.addConfig(StatType.STAT_POINT_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Points:");
        tgrapher.addConfig(StatType.STAT_POINT_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
        tgrapher.addConfig(StatType.STAT_GEOM_COUNT, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
        tgrapher.addConfig(StatType.STAT_GEOM_COUNT, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Objs:");
        tgrapher.addConfig(StatType.STAT_GEOM_COUNT, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
        tgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
        tgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Tex binds:");
        tgrapher.addConfig(StatType.STAT_TEXTURE_BINDS, TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
    }
    
    /**
     * Set up the graphers we will use and the quads we'll show the stats on.
     *
     */
    protected void setupStatGraphs() {
        StatCollector.setSampleRate(1000L);
        StatCollector.setMaxSamples(30);
        labGraph = new Quad("labelGraph", renderer.getWidth()/3, renderer.getHeight()/3) {
            private static final long serialVersionUID = 1L;
            @Override
            public void draw(Renderer r) {
                StatCollector.pause();
                super.draw(r);
                StatCollector.resume();
            }
        };
        tgrapher = GraphFactory.makeTabledLabelGraph(renderer.getWidth()/3, renderer.getHeight()/3, labGraph);
        tgrapher.setColumns(1);
        labGraph.setLocalTranslation((renderer.getWidth()*5/6), (renderer.getHeight()*5/6), 0);
        statNode.attachChild(labGraph);
        
    }
}


