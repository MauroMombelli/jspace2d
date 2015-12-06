package jspace2d;

import org.jbox2d.dynamics.Body;

import jspace2d.physics.BodyBlueprint;

public class Actor {

	final BodyBlueprint blue;
	final Body body;

	public Actor(BodyBlueprint b, Body z) {
		this.blue = b;
		this.body = z;
	}

}
