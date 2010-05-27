package shared;

import java.io.Serializable;
import java.util.TreeSet;

public class AllMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2363308013051184926L;

	public long turn;
	TreeSet<InfoBody> maps = new TreeSet<InfoBody>();
	
	public AllMap(long actualTurn) {
		turn = actualTurn;
	}

	public void add(InfoBody infoPosition) {
		maps.add(infoPosition);
	}

	public InfoBody poll() {
		return maps.pollLast();
	}

	public int size() {
		return maps.size();
	}

}
