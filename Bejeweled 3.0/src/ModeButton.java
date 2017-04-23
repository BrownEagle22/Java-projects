import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class ModeButton extends JButton {
	
	boolean activated = false;
	
	ModeButton(String text) {
		super(text);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (activated) {
			g2d.setColor(Color.red);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRect(1, 1, getWidth()-3, getHeight()-3);
		}
	}
	
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
}
