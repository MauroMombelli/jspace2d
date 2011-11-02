/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testjboxnetworking.creationActions;

import java.util.Vector;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author Daniel Boy
 */
public class Domino implements CreationAction{
    private BodyType BodyType;
    
    Vec2 position;
    boolean horizontal;
    
    public Domino( Vec2 pos, boolean hori){
        position=pos;
        horizontal=hori;
    }
    
    public void makeDomino(World mondo2d) {
        PolygonShape sd = new PolygonShape();
        sd.setAsBox(.5f * .20f, .5f * 1.0f);
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        fd.density = 10f;
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        fd.friction = 0.1f;
        fd.restitution = 0.65f;
        bd.position = position;
        bd.angle = horizontal ? (float) (Math.PI / 2.0) : 0f;
        Body myBody = mondo2d.createBody(bd);
        myBody.createFixture(fd);
    }

    @Override
    public void make(World mondo2d) {
        makeDomino(mondo2d);
    }
    
}
