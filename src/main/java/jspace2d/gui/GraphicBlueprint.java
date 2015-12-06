package jspace2d.gui;

import org.jbox2d.common.Vec2;

public class GraphicBlueprint {
	private final Vec2 size;
	
	public GraphicBlueprint(Vec2 size){
		this.size = size;
	}
	
	public Vec2 getSize(){
		return size;
	}
}
