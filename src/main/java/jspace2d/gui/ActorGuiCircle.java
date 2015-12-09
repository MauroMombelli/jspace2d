package jspace2d.gui;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.lwjgl.opengl.GL11;

public class ActorGuiCircle extends ActorGui {
	public final float radius;
	private static final int num_segments = 20;

	public ActorGuiCircle(long id, Vec2 position, float angle, float radius) {
		super(id, position, angle);
		this.radius = radius;
	}

	@Override
	public void draw() {
		/*
		 * from: http://slabode.exofire.net/circle_draw.shtml 
		 */
		float theta = 2.0f * 3.1415926f / (float) num_segments;
		float tangetial_factor = (float) Math.tan(theta);// calculate the tangential factor

		float radial_factor = MathUtils.cos(theta);// calculate the radial factor

		float x = radius;// we start at angle = 0

		float y = 0;

		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int ii = 0; ii < num_segments ; ii++) {
			GL11.glVertex2f(x + pos.x, y + pos.y);// output vertex

			// calculate the tangential vector
			// remember, the radial vector is (x, y)
			// to get the tangential vector we flip those coordinates and negate one of them

			float tx = -y;
			float ty = x;

			// add the tangential vector

			x += tx * tangetial_factor;
			y += ty * tangetial_factor;

			// correct using the radial factor

			x *= radial_factor;
			y *= radial_factor;
		}
		GL11.glEnd();
	}
	
}
