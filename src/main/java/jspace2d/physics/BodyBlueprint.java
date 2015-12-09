package jspace2d.physics;

public class BodyBlueprint {
	private final boolean isDynamic;
	private final boolean isBullet;
	
	private final FixtureBlueprint fixture[];
	
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

}
