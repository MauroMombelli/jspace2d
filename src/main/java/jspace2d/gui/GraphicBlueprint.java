package jspace2d.gui;

import java.io.File;

import org.jbox2d.common.Vec2;

public class GraphicBlueprint {
	private final Vec2 size;
	
	@SuppressWarnings("unused") //needed fo jackson serialization
	private GraphicBlueprint(){
		this.size = null;
	}
	
	public GraphicBlueprint(Vec2 size){
		this.size = size;
	}
	
	public Vec2 getSize(){
		return size;
	}

	public static GraphicBlueprint load(File assetFolder) {
		// TODO Auto-generated method stub
		return null;
	}
}
