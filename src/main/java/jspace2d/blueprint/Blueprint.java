package jspace2d.blueprint;

import jspace2d.gui.GraphicBlueprint;
import jspace2d.physics.BodyBlueprint;

public class Blueprint {

	private final BodyBlueprint bodyBlueprint;
	private final GraphicBlueprint graphicBlueprint;

	public Blueprint(){
		bodyBlueprint = null;
		graphicBlueprint = null;
	}

	public Blueprint(BodyBlueprint bodyBlueprint, GraphicBlueprint graphicBlueprint) {
		this.bodyBlueprint = bodyBlueprint;
		this.graphicBlueprint = graphicBlueprint;
	}

	public BodyBlueprint getBodyBlueprint() {
		return bodyBlueprint;
	}

	public GraphicBlueprint getGraphicBlueprint() {
		return graphicBlueprint;
	}

}
