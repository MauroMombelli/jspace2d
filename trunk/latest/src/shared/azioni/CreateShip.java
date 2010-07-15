package shared.azioni;

import java.io.Serializable;

import server.Player;
import shared.GLOBAL_VARIABLE;
import shared.PhysicWorld;
import shared.oggetti.Oggetto2D;
import shared.oggetti.Ship;

public class CreateShip extends Action implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CreateShip() {
		super(-1);
	}
	
	public CreateShip(int ID) {
		super(ID);
	}

	@Override
	public boolean run(PhysicWorld w, Player p) {
		Oggetto2D o = null;
		if (shipID!= -1)
			o = p.getShip(shipID);
		else
			shipID = w.getNextIndex();
		
		if (o==null){
			Ship s = new Ship( shipID );
			if ( w.addNew( s, GLOBAL_VARIABLE.convertToPhysicEngineUnit( 0.1f ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 ), 0 ) == null ){
				System.out.println("Error, world hasn't created ship "+s.ID+" for player: "+p.getLogin().toString());
				return false;
			}
			p.addOggetto(s);
			System.out.println("Created ship "+s.ID+" for player: "+p.getLogin().toString());
			
			//set the ship as player possession
			p.setActiveShip(s.ID);
			
			shipID = s.ID;
		}else{
			System.out.println( "Ship "+shipID+" actually exist!" );
			return false;
			/*
			if ( p.getShip(shipOwnerID) != null ){
				p.setActiveShip(shipOwnerID);
			}else{
				//player is trying to take control over an Oggetto2d not possessed, sure hacking
				System.out.println( "Ship "+shipOwnerID+" is not owned by player: "+p.getLogin().toString() );
				p.close();
				return false;
			}
			*/
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ActionEngine){
			ActionEngine a = (ActionEngine)obj;
			if (a.shipID == shipID)
				return true;
		}
		return false;
	}

	@Override
	public boolean run(PhysicWorld w) {
		Oggetto2D o = w.get(shipID);
		if (o==null){
			Ship s = new Ship( shipID );
			w.addNew( s, GLOBAL_VARIABLE.convertToPhysicEngineUnit( 0.1f ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 ), 0 );
		}else{
			System.out.println( "Ship "+shipID+" actually exist!" );
		}
		return true;
	}
	
}
