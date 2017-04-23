import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Pazinojums extends JPanel {
	
	final int IZM = 560;
	int player = 0;
	boolean pauze;
	
	public Pazinojums() {
		setOpaque(false);
	}
	
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		switch (player) {
		case 1: g2d.setColor(Color.decode("#00b300"));
				break;
		case 2: g2d.setColor(Color.decode("#ff6600"));
				break;
		}
		
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 80));
		FontMetrics metrics = g2d.getFontMetrics();
		int textW = metrics.stringWidth("Player " + Integer.toString(player) + " wins!");
		int textH = metrics.getHeight();
		if (player != 0)
			g2d.drawString("Player " + Integer.toString(player) + " wins!",
					IZM/2 - textW/2, IZM/2-4);
		
		if (pauze) {
			metrics = g2d.getFontMetrics();
			textW = metrics.stringWidth("Pauze");
			textH = metrics.getHeight();
			g2d.drawString("Pauze",
					IZM/2 - textW/2, IZM/2-4);
		}
	}
}
