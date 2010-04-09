package client;

import java.util.HashMap;

import org.jbox2d.common.Vec2;

import com.jme.scene.Node;

import client.gameState.SetNode;
import shared.Oggetto2D;

public class ClientOggetto2D {
	Oggetto2D obj;
	SetNode graphicPosition;
	
	public ClientOggetto2D(Oggetto2D obj){
		this.obj = obj;
		graphicPosition = new SetNode( obj.ID, obj.getModelName(), new Vec2( obj.getBody().getPosition() ) , obj.getBody().getAngle() );
	}

	public void update() {
		graphicPosition.set( new Vec2( obj.getBody().getPosition() ) , obj.getBody().getAngle() );
	}

	public void run(HashMap<Integer, Node> visibleObject, Node rootNode) {
		graphicPosition.run(visibleObject, rootNode);
	}
}
