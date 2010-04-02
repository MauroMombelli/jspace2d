package server;

import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import shared.Oggetto2D;

public class EngineListener implements ContactListener {
	
	@Override
	public void add(ContactPoint point) {
		if ( point.shape1.isSensor() && !point.shape2.isSensor() ){
			//radar della shape 1 vede shape 2
			Oggetto2D obj1 = (Oggetto2D)point.shape1.getUserData();
			Oggetto2D obj2 = (Oggetto2D)point.shape2.getUserData();
			if (obj1!=null && obj2!=null){
				obj1.see(obj2);
				obj2.isSee(obj1);
				System.out.println(obj1.ID+" see "+obj2.ID);
			}else{
				//wall collision
			}
		}
		if ( point.shape2.isSensor() && !point.shape1.isSensor() ){
			//radar della shape 2 vede shape 1
			Oggetto2D obj1 = (Oggetto2D)point.shape1.getUserData();
			Oggetto2D obj2 = (Oggetto2D)point.shape2.getUserData();
			if (obj1!=null && obj2!=null){
				obj2.see(obj1);
				obj1.isSee(obj2);
				System.out.println(obj2.ID+" see "+obj1.ID);
			}else{
				//wall collision
			}
		}
		
		if ( !point.shape1.isSensor() && !point.shape2.isSensor() ){
			//collisione tra shape1 e shape2
			Oggetto2D obj1 = (Oggetto2D)point.shape1.getUserData();
			Oggetto2D obj2 = (Oggetto2D)point.shape2.getUserData();
			if (obj1!=null && obj2!=null){
				obj1.collide(obj2);
				obj2.collide(obj1);
				System.out.println(obj1.ID+" collide "+obj2.ID);
				//allPhisicCollision.add(obj1.getInfoPosition());
				//allPhisicCollision.add(obj2.getInfoPosition());
			}else{
				//wall collision
			}
		}
	}

	@Override
	public void persist(ContactPoint point) {
		if ( !point.shape1.isSensor() && !point.shape2.isSensor() ){
			//continua la collisione tra shape1 e shape2
			Oggetto2D obj1 = (Oggetto2D)point.shape1.getUserData();
			Oggetto2D obj2 = (Oggetto2D)point.shape2.getUserData();
			if (obj1!=null && obj2!=null){
				obj1.continueCollide(obj2);
				obj2.continueCollide(obj1);
				//System.out.println(obj1.ID+" continue to collide "+obj2.ID);
				//allPhisicCollision.add(obj1.getInfoPosition());
				//allPhisicCollision.add(obj2.getInfoPosition());
			}else{
				//wall collision
			}
		}
	}

	@Override
	public void remove(ContactPoint point) {
		if ( point.shape1.isSensor() && !point.shape2.isSensor() ){
			//radar della shape 1 non vede più shape 2
			Oggetto2D obj1 = (Oggetto2D)point.shape1.getUserData();
			Oggetto2D obj2 = (Oggetto2D)point.shape2.getUserData();
			if (obj1!=null && obj2!=null){
				obj1.notSee(obj2);
				obj2.isNotSee(obj1);
				System.out.println(obj1.ID+" not see "+obj2.ID);
			}else{
				//end wall collision
			}
		}
		if ( point.shape2.isSensor() && !point.shape1.isSensor() ){
			//radar della shape 2 non vede più shape 1
			Oggetto2D obj1 = (Oggetto2D)point.shape1.getUserData();
			Oggetto2D obj2 = (Oggetto2D)point.shape2.getUserData();
			if (obj1!=null && obj2!=null){
				obj2.notSee(obj1);
				obj1.isNotSee(obj2);
				System.out.println(obj2.ID+" not see "+obj1.ID);
			}else{
				//end wall collision
			}
		}
		/* NON CI INTERESSA
		if ( !point.shape1.isSensor() && !point.shape2.isSensor() ){
			//collisione tra shape1 e shape2
		}
		*/
	}

	@Override
	public void result(ContactResult point) {
		// TODO Auto-generated method stub
		
	}

}
