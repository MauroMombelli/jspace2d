package jspace2d.gui;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.common.Vec2;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Visualizer implements Runnable {

	private static final Logger log = Logger.getLogger(Visualizer.class.getName());

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	// The window handle
	private long window;
	private VisualizerListener listener;
	private HashMap<Long, ActorGui> actors = new HashMap<Long, ActorGui>();

	int WIDTH = 600;
	int HEIGHT = 600;

	private float zoom = 1f/10f;

	@Override
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		try {
			init();
			loop();

			// Release window and window callbacks
			GLFW.glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			// Terminate GLFW and release the GLFWErrorCallback
			GLFW.glfwTerminate();
			errorCallback.release();
			listener.close();
		}
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (GLFW.glfwInit() != GLFW.GLFW_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable

		// Create the window
		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		GLFW.glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
					GLFW.glfwSetWindowShouldClose(window, GLFW.GLFW_TRUE); // We will detect this in our rendering loop
			}
		});

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		// Center our window
		GLFW.glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(window);
		// Enable v-sync
		GLFW.glfwSwapInterval(1);

		// Make the window visible
		GLFW.glfwShowWindow(window);
	}

	private void loop() {
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		/* Declare buffers for using inside the loop */
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		
		/* Set ortographic projection */
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-1f, 1f, -1f, 1f, 0f, 1f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		

		long startMs = System.currentTimeMillis();
		long loopCount = 0;
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_FALSE) {

			if (System.currentTimeMillis() - startMs >= 1000){
				startMs = System.currentTimeMillis();
				log.log(Level.INFO, "FPS GUI: "+loopCount);
				loopCount = 0;
			}
			loopCount++;
			
			if (listener != null) {
				listener.preRender();
			}

			float ratio;

			/* Get width and height to calcualte the ratio */
			GLFW.glfwGetFramebufferSize(window, width, height);
			ratio = width.get() / (float) height.get();

			/* Rewind buffers for next get */
			width.rewind();
			height.rewind();

			/* Set viewport and clear screen */
			GL11.glViewport(0, 0, width.get(), height.get());
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			render(ratio);

			GLFW.glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			GLFW.glfwPollEvents();

			/* Flip buffers for next loop */
			width.flip();
			height.flip();

		}
		if (listener != null) {
			listener.close();
		}
	}

	private void render(float ratio) {
		//synchronized (actors) {
			for (ActorGui a : actors.values()) {
				
				GL11.glColor3f(0.1f, 0.1f, 0.1f);

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(a.pos.x * zoom, a.pos.y * zoom, 0);
					GL11.glRotatef((float) Math.toDegrees(a.angle), 0f, 0f, 1f);

					GL11.glBegin(GL11.GL_POLYGON);
					{
						float halfX = (a.size.x / 2) * zoom;
						float halfY = (a.size.y / 2) * zoom;
						GL11.glVertex2f(-halfX, -halfY);
						GL11.glVertex2f(halfX, -halfY);
						GL11.glVertex2f(halfX, halfY);
						GL11.glVertex2f(-halfX, halfY);
					}
					GL11.glEnd();
				}
				GL11.glPopMatrix();
			}
		//}
	}

	public void setListener(VisualizerListener v) {
		this.listener = v;
	}

	public ActorGui add(long id, GraphicBlueprint graphicBlueprint, Vec2 pos, float angle) {
		ActorGui ris = new ActorGui(pos, angle, graphicBlueprint.getSize());
		synchronized (actors) {
			actors.put(id, ris);
			log.log(Level.INFO, "Created ActorGui");
		}
		return ris;
	}

	public void remove(long id) {
		synchronized (actors) {
			actors.remove(id);
		}
	}
}
