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

	public String toString(){
		return name+" "+pass;
	}
}
