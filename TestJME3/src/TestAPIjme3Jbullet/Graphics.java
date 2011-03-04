/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TestAPIjme3Jbullet;

import TestAPIjme3Jbullet.serializable.ObjectInfo;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import java.util.LinkedList;

/**
 *
 * @author mauro
 */

/** Prepare the Physics Application State (jBullet) */

public class Graphics extends SimpleApplication {

    /** Activate custom rendering of shadows */
    BasicShadowRenderer bsr;
    BulletAppState bulletAppState;
    LinkedList<ObjectInfo> pendingObjToLoad = new LinkedList<ObjectInfo>();
    
    public void addBulletAppState(BulletAppState bulletAppState){
        if (stateManager!=null){
            System.out.println("ok state manager");
            stateManager.attach(bulletAppState);
        }else{
            System.out.println("KO state manager");
            this.bulletAppState=bulletAppState;
        }
        //bulletAppState.getPhysicsSpace().addCollisionListener(new CollisionList());
    }


    public void initFloor() {
        Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
        TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.png");
        key3.setGenerateMips(true);
        Texture tex3 = assetManager.loadTexture(key3);
        tex3.setWrap(WrapMode.Repeat);
        floor_mat.setTexture("ColorMap", tex3);

        Box floor = new Box(Vector3f.ZERO, 10f, 0.1f, 5f);
        floor.scaleTextureCoordinates(new Vector2f(3, 6));
        Geometry floor_geo = new Geometry("Floor", floor);
        floor_geo.setMaterial( floor_mat );
        floor_geo.setShadowMode(ShadowMode.Receive);
        floor_geo.setLocalTranslation(0, -1, 0);
        this.rootNode.attachChild(floor_geo);
    }
    
    @Override
    public void simpleInitApp() {
        if (bulletAppState!=null){
            System.out.println("ok bullet state");
            stateManager.attach(bulletAppState);
        }

        /** Configure cam to look at scene */
        cam.setLocation(new Vector3f(0, 20, 0));
        cam.lookAt(Vector3f.ZERO, new Vector3f(0, 1, 0));
        cam.setFrustumFar(105);

        /** Add InputManager action: Left click triggers shooting. */
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(myActionListener, "shoot");
        /** Initialize the scene, materials, and physics space */
        //initMaterials();
        //initWall();
        //initFloor();
        initCrossHairs();
        initShadows();

        loadFPSText();
    }

    /** Activate shadow casting and light direction */
    private void initShadows() {
        bsr = new BasicShadowRenderer(assetManager, 256);
       // bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(bsr);
        // Default mode is Off -- Every node declares own shadow mode!
        rootNode.setShadowMode(ShadowMode.Off);
    }

    /** A plus sign used as crosshairs to help the player with aiming.*/
    protected void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+");        // fake crosshairs :)
        ch.setLocalTranslation( // center
        settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
        settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

    /**
   * Every time the shoot action is triggered, a new cannon ball is produced.
   * The ball is set up to fly from the camera position in the camera direction.
   */
    private ActionListener myActionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("shoot") && !keyPressed) {
                //makeCannonBall();
            }
        }
    };

    @Override
    public void update(){
        super.update();
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
System.out.println("cam vector"+cam.getLocation());
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

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");

        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        mat.setTexture("ColorMap", tex2);

        /** Create a cannon ball geometry and attach to scene graph. */
        Geometry ball_geo = new Geometry("sfera", new Sphere(32, 32, 1f, true, false) );
        
       // mat.setColor("m_Color", ColorRGBA.Blue);
        ball_geo.setMaterial(mat);
        return ball_geo;
    }
}
