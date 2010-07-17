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
	boolean visible = false;
	boolean deleted = false;
	
	public ClientOggetto2D(Oggetto2D obj){
		this.obj = obj;
		graphicPosition = new SetNode( obj.ID, obj.getModelName(), new Vec2( obj.getBody().getPosition() ) , obj.getBody().getAngle() );
	}

	public void update() {
		graphicPosition.set( new Vec2( obj.getBody().getPosition() ) , obj.getBody().getAngle() );
	}

	public void run(HashMap<Integer, Node> visibleObject, Node rootNode) {
		
		if (visible){
			deleted = false;
			if (myNode==null)
				myNode = visibleObject.get(obj.ID);
			
			graphicPosition.run(visibleObject, rootNode, myNode);
		}else{
			if (!deleted){
				rootNode.detachChild(myNode);
				visibleObject.remove(obj.ID);
				myNode=null;
				deleted = true;
			}
		}
	}

	public void set(Oggetto2D o) {
		obj=o;
	}

	public void setInfoPosition(InfoBody infoPosition) {
		obj.setInfoPosition(infoPosition);
	}

	public int getID() {
		return obj.ID;
	}

	public void setVisible(boolean b) {
		visible=b;
	}
}
