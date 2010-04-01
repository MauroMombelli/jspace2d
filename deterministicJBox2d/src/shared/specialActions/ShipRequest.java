package shared.specialActions;

import java.io.Serializable;

import server.Player;
import shared.GLOBAL_VARIABLE;
import shared.PhysicWorld;
import shared.Ship;

public class ShipRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int ID=-1;
	
	public ShipRequest() {
		setID(-1);
	}
	
	public ShipRequest(int ID) {
		this.setID(ID);
	}

	public void run(PhysicWorld w, int objIndex, Player p) {
		if (getID()==-1){
			Ship s = new Ship(objIndex);
			w.addNew( s, GLOBAL_VARIABLE.convertToPhysicEngineUnit( 0.1f ), GLOBAL_VARIABLE.convertToPhysicEngineUnit( 100 ), 0 );
			p.addShip(s);
			System.out.println("Created ship "+s.ID+" for player: "+p.getLogin().toString());
			//notify client of ship creation
			p.write( new ShipRequest(s.ID) );
			p.setActiveShip(s.ID);
		}else{
			if ( p.getShip(getID()) != null ){
				p.setActiveShip(getID());
			}else{
				//player is trying to take control over an Oggetto2d not possessed, sure hacking
				System.out.println( "Ship "+ID+" is not owned by player: "+p.getLogin().toString() );
				p.close();
			}
		}
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getID() {
		return ID;
	}
	
}
