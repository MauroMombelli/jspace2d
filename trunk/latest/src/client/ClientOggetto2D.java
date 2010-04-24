package client;

import java.util.HashMap;

import org.jbox2d.common.Vec2;

import com.jme.scene.Node;

import client.gameState.SetNode;
import shared.InfoBody;
import shared.oggetti.Oggetto2D;

public class ClientOggetto2D {
	Oggetto2D obj;
	SetNode graphicPosition;
	Node myNode;
	
	public ClientOggetto2D(Oggetto2D obj){
		this.obj = obj;
		graphicPosition = new SetNode( obj.ID, obj.getModelName(), new Vec2( obj.getBody().getPosition() ) , obj.getBody().getAngle() );
	}

	public void update() {
		if (graphicPosition != null){ //obj has been deleted
			graphicPosition.set( new Vec2( obj.getBody().getPosition() ) , obj.getBody().getAngle() );
		}else{
			graphicPosition = null;
		}
	}

	public void run(HashMap<Integer, Node> visibleObject, Node rootNode) {
		if (graphicPosition != null){
			if (myNode==null)
				myNode = visibleObject.get(obj.ID);
			
			graphicPosition.run(visibleObject, rootNode, myNode);
		}
	}

	public void set(Oggetto2D o) {
		obj.setOggetto2D(o);
	}

	public void setInfoPosition(InfoBody infoPosition) {
		obj.setInfoPosition(infoPosition);
	}

	public int getID() {
		return obj.ID;
	}
}
