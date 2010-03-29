package Components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Actions.actionContainer;

public class materialTab implements ChangeListener{
	
	public JPanel panel;
	actionContainer actions;
	public JButton button;
	public JSlider sliderShine;
	public JColorChooser diffusechooser;
	
	public materialTab(actionContainer act){
		actions = act;
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		button = new JButton(actions.refresh);
		sliderShine = new JSlider(JSlider.HORIZONTAL, 0, 128, 50);
		sliderShine.addChangeListener(this);
		JLabel sliderLabel = new JLabel("Shininess", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		diffusechooser = new JColorChooser();
		
		panel.add(diffusechooser);
		Box.Filler temp = new Box.Filler(new Dimension(200,200), new Dimension(200,300), new Dimension(200,200));
		panel.add(temp);
		panel.add(sliderLabel);
		panel.add(sliderShine);
		panel.add(button);
		temp = new Box.Filler(new Dimension(200,200), new Dimension(200,500), new Dimension(200,200));
		panel.add(temp);
	}
	
	public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            actions.shine.value = (int)source.getValue();
            actions.shine.actionPerformed(null);
        }
    }

}
