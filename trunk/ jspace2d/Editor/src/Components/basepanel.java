package Components;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class basepanel {
	public JPanel panel;
	
	public basepanel(){
		panel = new JPanel();
		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
}
