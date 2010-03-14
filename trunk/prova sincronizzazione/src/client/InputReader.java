package client;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;

public class InputReader implements MouseInputListener{

	public InputReader(){
		MouseInput.get().addListener(this);
		
		KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

		//movement
		keyboard.set("move_up", KeyInput.KEY_NUMPAD8);
		keyboard.set("move_down", KeyInput.KEY_NUMPAD2);
		keyboard.set("move_left", KeyInput.KEY_NUMPAD4);
		keyboard.set("move_right", KeyInput.KEY_NUMPAD6);
		/*
		keyboard.set("roll_left", KeyInput.KEY_NUMPAD7); //rollio sx
		keyboard.set("roll_right", KeyInput.KEY_NUMPAD9); //rollio dx
		
		keyboard.set("pitch_up", KeyInput.KEY_NUMPAD1); //beccheggio alto
		keyboard.set("pitch_down", KeyInput.KEY_NUMPAD1); //beccheggio basso
		*/
	}

	@Override
	public void onButton(int button, boolean pressed, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMove(int xDelta, int yDelta, int newX, int newY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWheel(int wheelDelta, int x, int y) {
		// TODO Auto-generated method stub
		
	}
}
