package Actions;

import javax.swing.Icon;

import Camera_Positions.Camera_Container;
import Main.main;

public class actionContainer {
	public materialMode materialmode;
	public quit quit;
	public testMode testmode;
	public load load;
	public refresh refresh;
	private main Main;
	public Camera_Container cam;
	
	public actionContainer(main m){
		Main = m;
		materialmode = new materialMode(m);
		quit = new quit(m);
		testmode = new testMode(m);
		load = new load(m);
		refresh = new refresh(m);
		
		cam = new Camera_Container(m);
	}

	public void Intersecate() {	
    	materialmode.setOther(testmode);
    	testmode.setOther(materialmode);		
	}
}
