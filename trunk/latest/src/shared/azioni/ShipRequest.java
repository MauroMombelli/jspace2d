package shared.azioni;

import java.io.Serializable;

import server.Player;
import shared.GLOBAL_VARIABLE;
import shared.Oggetto2D;
import shared.PhysicWorld;
import shared.Ship;

public class ShipRequest extends Action implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ShipRequest() {
		super(-1);
	}
	
	public ShipRequest(int ID) {
		super(ID);
	}

	@Override
	public boolean run(PhysicWorld w, Player p) {
		Oggetto2D o = p.getShip(shipOwnerID);
		if (o==null){
			Ship s = new Ship( w.getNextIndex() );
			w.addNew( s, GLOBAL_VARIABLE.convertToPhysicEngineUnit( 0.1f ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 ), 0 );
			p.addShip(s);
			System.out.println("Created ship "+s.ID+" for player: "+p.getLogin().toString());
			//notify client of ship creation
			p.write( new ShipRequest(s.ID) );
			p.setActiveShip(s.ID);
		}else{
			if ( p.getShip(shipOwnerID) != null ){
				p.setActiveShip(shipOwnerID);
			}else{
				//player is trying to take control over an Oggetto2d not possessed, sure hacking
				System.out.println( "Ship "+shipOwnerID+" is not owned by player: "+p.getLogin().toString() );
				p.close();
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean run(PhysicWorld w) {
		Oggetto2D o = w.get(shipOwnerID);
		if (o==null){
			Ship s = new Ship( w.getNextIndex() );
			w.addNew( s, GLOBAL_VARIABLE.convertToPhysicEngineUnit( 0.1f ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 ), 0 );
		}
		return true;
	}
	
}
