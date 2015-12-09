package jspace2d.gui;

import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
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

	int WIDTH = 600;
	int HEIGHT = 600;

	private float zoom = 0.01f;

	private final String title;

	private Camera camera;

	public volatile boolean runnig = false;

	private boolean shouldClose = false;

	public Visualizer(String title) {
		if (title != null)
			this.title = title;
		else
			this.title = "Untitled";
	}

	@Override
	public void run() {
		runnig = true;
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
			if (listener != null)
				listener.close();

			runnig = false;
		}
	}

	public void setListener(VisualizerListener v) {
		this.listener = v;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
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
		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, title, MemoryUtil.NULL, MemoryUtil.NULL);
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
		
		GL11.glScalef(zoom, zoom, 1);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_FALSE || shouldClose) {

			if (System.currentTimeMillis() - startMs >= 1000) {
				startMs = System.currentTimeMillis();
				log.log(Level.INFO, "FPS GUI: " + loopCount);
				loopCount = 0;
			}
			loopCount++;

			float ratio;

			/* Get width and height to calcualte the ratio */
			GLFW.glfwGetFramebufferSize(window, width, height);

			int w = width.get();
			int h = height.get();

			ratio = w / (float) h;

			/* Set viewport and clear screen */
			GL11.glViewport(0, 0, w, h);
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
	}

	private void render(float ratio) {
		if (camera == null) {
			log.log(Level.INFO, "No camera, turning off visualizer");
			shouldClose = true;
			return;
		}
		
		GL11.glColor3f(0.1f, 0.1f, 0.1f);
		for (ActorGui a : camera.getToPrint()) {
			a.draw();
		}
	}
}
