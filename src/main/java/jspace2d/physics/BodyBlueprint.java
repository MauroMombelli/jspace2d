package jspace2d.physics;

import java.io.File;

public class BodyBlueprint {
	private final boolean isDynamic;
	private final boolean isBullet;
	
	private final FixtureBlueprint fixture[];
	
	@SuppressWarnings("unused") //needed fo jackson serialization
	private BodyBlueprint(){
		isDynamic = false;
		isBullet = false;
		fixture = null;
	}
	
	
	public BodyBlueprint(boolean isDynamic, boolean isBullet, FixtureBlueprint fixture[]){
		this.isDynamic = isDynamic;
		this.isBullet = isBullet;
		this.fixture = fixture; 
	}

	public boolean isDynamic() {
		return isDynamic;
	}
	
	public boolean isBullet() {
		return isBullet;
	}

	public FixtureBlueprint[] getFixture() {
		return fixture;
	}

	public static BodyBlueprint load(File asset) {
		return null;
	}

}
