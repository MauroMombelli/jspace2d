package shared;

import java.io.Serializable;
import java.util.LinkedList;

import shared.azioni.Action;
import shared.oggetti.Oggetto2D;

public class NewTurn implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long actualTurn;
	
	LinkedList<Oggetto2D> nuoviOggetti = new LinkedList<Oggetto2D>();
	LinkedList<InfoBody> posizioneOggetti = new LinkedList<InfoBody>();
	
	LinkedList<Action> nuoveAzioni = new LinkedList<Action>();

	private boolean empty = true;
	
	public NewTurn(long actualTurn){
		this.actualTurn = actualTurn;
	}

	public void add(Oggetto2D t, InfoBody infoPosition) {
		nuoviOggetti.add(t);
		posizioneOggetti.add(infoPosition);
		empty=false;
	}

	public void addAll(LinkedList<Action> allChanges) {
		nuoveAzioni.addAll(allChanges);
		if (allChanges.size()>0)
			empty=false;
	}
	
	public void add(Action act) {
		nuoveAzioni.add(act);
		empty=false;
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

	public int actionsSize() {
		return nuoveAzioni.size();
	}

	public int newObjSize() {
		return nuoviOggetti.size();
	}

	public boolean isEmpty() {
		return empty ;
	}
}
