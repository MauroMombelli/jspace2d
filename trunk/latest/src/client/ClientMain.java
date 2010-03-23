package client;

public class ClientMain {

	public static void main(String args[]){
		new ClientMain();
	}
	
	public ClientMain(){
		//make a GUI for settings
		new ServerListener("82.84.123.6", 5000);
		//new ServerListener("127.0.0.1", 5000);
	}
	
}
