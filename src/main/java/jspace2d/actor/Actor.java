package jspace2d.actor;

import org.jbox2d.dynamics.Body;

import jspace2d.blueprint.Blueprint;
import jspace2d.gui.ActorGui;

public class Actor {

	public final long id;
	public final Blueprint blue;
	public final Body body;
	public final ActorGui graphic;

	public Actor(long id, Blueprint blue, Body body, ActorGui graphic) {
		this.id = id;
		this.blue = blue;
		this.body = body;
		this.graphic = graphic;
	}

}
