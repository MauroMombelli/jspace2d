package shared;

import java.io.Serializable;

public class Login implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3549016591113152867L;
	
	String name;
	String pass;
	
	public Login(String name, String pass ){
		this.name = name;
		this.pass = pass;
	}
	
	@Override
	public String toString(){
		return name+" "+pass;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Login){
			Login t = (Login)o;
			if ( t.name.equals(name) )
				return true;
		}
		return false; 
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
}
