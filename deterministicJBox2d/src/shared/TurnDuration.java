package shared;

import java.io.Serializable;

public class TurnDuration implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6918813186905071539L;
	
	
	public long turnDuration;
	public long actualTurn;
	
	public TurnDuration(long t, long actualT){
		turnDuration = t;
		actualTurn = actualT;
	}
}
