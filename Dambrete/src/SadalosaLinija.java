import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class SadalosaLinija extends JPanel {
	
	int augstums;
	
	public SadalosaLinija(int augstums) {
		this.augstums = augstums;
		setPreferredSize(new Dimension(augstums, 4));
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.decode("#cc0000"));
		g2d.fillRect(0, 0/*augstums/2-2*/, augstums, 4);
		//g2d.drawLine(0, augstums/2, augstums-2, augstums/2);
	}
}
