/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test2.AppState;

import TestAPIjme3Jbullet.actions.PhysicActions;
import TestAPIjme3Jbullet.serializable.ObjectInfo;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.texture.Texture;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.system.AppSettings;
import java.util.LinkedList;

/**
 *
 * @author mauro
 */
public class GraphicsAppState extends AbstractAppState {

    /** Activate custom rendering of shadows */
    BasicShadowRenderer bsr;
    LinkedList<ObjectInfo> pendingObjToLoad = new LinkedList<ObjectInfo>();
    Node rootNode;
    Node guiNode;
    protected BitmapFont guiFont;
    AppSettings settings;
    Application app;

    public GraphicsAppState(Node rootNode, Node guiNode, AppSettings settings){
        this.rootNode = rootNode;
        this.guiNode = guiNode;
        this.settings = settings;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        this.app=app;

        initCrossHairs();
        initShadows();

        //app.loadFPSText();

        app.getCamera().setLocation(new Vector3f(0, 20, 0));
        app.getCamera().lookAt(Vector3f.ZERO, new Vector3f(0, 1, 0));
        app.getCamera().setFrustumFar(105);
    }

    /** A plus sign used as crosshairs to help the player with aiming.*/
    protected void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+");        // fake crosshairs :)
        ch.setLocalTranslation( settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

    /** Activate shadow casting and light direction */
    private void initShadows() {
        bsr = new BasicShadowRenderer(app.getAssetManager(), 256);
       // bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        app.getViewPort().addProcessor(bsr);
        // Default mode is Off -- Every node declares own shadow mode!
        
        rootNode.setShadowMode(ShadowMode.Off);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        ObjectInfo name;
        synchronized(pendingObjToLoad){
            while ( (name=pendingObjToLoad.poll() ) != null ){
                addObject( loadObject(name), name.getPhysic() );
            }
        }
    }

    public void addObject(ObjectInfo i){
        synchronized(pendingObjToLoad){
            pendingObjToLoad.add(i);
        }
    }

    private void addObject( Geometry graphicsRapresentation, RigidBodyControl physicRapresentation ){

        rootNode.attachChild(graphicsRapresentation);
        /** Position the object **/
        //graphicsRapresentation.setLocalTranslation(cam.getLocation());
        System.out.println("cam vector"+app.getCamera().getLocation());
        /** activate shadows **/
        graphicsRapresentation.setShadowMode(ShadowMode.CastAndReceive);

        /** Add physical object to graphics space. */
        if (physicRapresentation!=null){
            Vector3f t = physicRapresentation.getPhysicsLocation();
            graphicsRapresentation.setLocalTranslation(t);
            graphicsRapresentation.addControl( physicRapresentation );
            if ( t.equals(physicRapresentation.getPhysicsLocation()) ){
                System.out.println("ok vector");
            }else{
                System.out.println("KO vector was: "+t+" now: "+physicRapresentation.getPhysicsLocation());
                physicRapresentation.setPhysicsLocation(t);
            }

            //physicRapresentation.setSpatial(graphicsRapresentation);
        }else{
            graphicsRapresentation.setLocalTranslation(0,0,0);
        }

    }

    private Geometry loadObject(ObjectInfo name){

        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/SimpleTextured.j3md");

        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = app.getAssetManager().loadTexture(key2);
        mat.setTexture("ColorMap", tex2);

        /** Create a cannon ball geometry and attach to scene graph. */
        Geometry ball_geo = new Geometry("sfera", new Sphere(32, 32, 1f, true, false) );

       // mat.setColor("m_Color", ColorRGBA.Blue);
        ball_geo.setMaterial(mat);
        return ball_geo;
    }
}
