package shared.azioni;

import java.io.Serializable;

import server.Player;
import shared.GLOBAL_VARIABLE;
import shared.Oggetto2D;
import shared.PhysicWorld;
import shared.Ship;

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
		Oggetto2D o = p.getShip(shipOwnerID);
		if (o==null){
			Ship s = new Ship( w.getNextIndex() );
			if ( w.addNew( s, GLOBAL_VARIABLE.convertToPhysicEngineUnit( 0.1f ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 ), 0 ) == null ){
				System.out.println("Error, world hasn't created ship "+s.ID+" for player: "+p.getLogin().toString());
				return false;
			}
			p.addOggetto(s);
			System.out.println("Created ship "+s.ID+" for player: "+p.getLogin().toString());
			
			//set the ship as player possession
			p.setActiveShip(s.ID);
			
			shipOwnerID = s.ID;
		}else{
			System.out.println( "Ship "+shipOwnerID+" actually exist!" );
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
			if (a.shipOwnerID == shipOwnerID)
				return true;
		}
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
