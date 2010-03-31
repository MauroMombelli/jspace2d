package Camera_Positions;

import Main.main;

public class Camera_Container {
	public Top_View top;
	private main Main;
	public Front_View front;
	public Side_View side;
	
	public Camera_Container(main m){
		Main = m;
		top = new Top_View(m);
		front = new Front_View(m);
		side = new Side_View(m);
	}
}
