package shared;

import java.io.Serializable;
import java.util.LinkedList;

import shared.azioni.Action;

public class NewTurn implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long actualTurn;
	
	LinkedList<Oggetto2D> nuoviOggetti = new LinkedList<Oggetto2D>();
	LinkedList<InfoBody> posizioneOggetti = new LinkedList<InfoBody>();
	
	LinkedList<Action> nuoveAzioni = new LinkedList<Action>();
	
	LinkedList<InfoBody> collisioneOggetti = new LinkedList<InfoBody>();
	
	public NewTurn(long actualTurn){
		this.actualTurn = actualTurn;
	}

	public void add(Oggetto2D t, InfoBody infoPosition) {
		nuoviOggetti.add(t);
		posizioneOggetti.add(infoPosition);
	}

	public void addAll(LinkedList<Action> allChanges) {
		nuoveAzioni.addAll(allChanges);
	}
	
	public void addAllCollision(LinkedList<InfoBody> allCollisions) {
		collisioneOggetti.addAll(allCollisions);
	}
	
	public Oggetto2D pollNewObj(){
		return nuoviOggetti.poll();
	}
	
	public InfoBody pollPosObj(){
		return posizioneOggetti.poll();
	}
	
	public Action pollActions(){
		return nuoveAzioni.poll();
	}
	
	public InfoBody pollCollision(){
		return collisioneOggetti.poll();
	}

	public int actionsSize() {
		return nuoveAzioni.size();
	}

	public int newObjSize() {
		return nuoviOggetti.size();
	}
	
	public int newCollisionSize() {
		return collisioneOggetti.size();
	}
}
