package jspace2d.gui;

import org.jbox2d.common.Vec2;
import org.lwjgl.opengl.GL11;

public class ActorGuiRectangle extends ActorGui {
	public final Vec2 size;

	public ActorGuiRectangle(long id, Vec2 position, float angle, Vec2 size) {
		super(id, position, angle);
		this.size = size;
	}

	float halfX, halfY;

	@Override
	public void draw() {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(pos.x, pos.y, 0);
			GL11.glRotatef((float) Math.toDegrees(angle), 0f, 0f, 1f);

			GL11.glBegin(GL11.GL_POLYGON);
			{
				halfX = size.x / 2;
				halfY = size.y / 2;

				GL11.glVertex2f(-halfX, -halfY);
				GL11.glVertex2f(halfX, -halfY);
				GL11.glVertex2f(halfX, halfY);
				GL11.glVertex2f(-halfX, halfY);
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
	}
}
