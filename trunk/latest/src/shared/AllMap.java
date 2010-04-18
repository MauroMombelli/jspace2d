package shared;

import java.io.Serializable;
import java.util.LinkedList;


public class AllMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2363308013051184926L;

	public long turn;
	LinkedList<InfoBody> maps = new LinkedList<InfoBody>();
	
	public AllMap(long actualTurn) {
		turn = actualTurn;
	}

	public void add(InfoBody infoPosition) {
		maps.add(infoPosition);
	}

	public InfoBody poll() {
		return maps.poll();
	}

	public int size() {
		return maps.size();
	}

}
