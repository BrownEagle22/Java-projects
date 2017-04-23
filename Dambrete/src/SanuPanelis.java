import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SanuPanelis extends JPanel {
	
	public final int AUGST = 560;
	public final int PLAT = 240;
	public final Font BIG = new Font("TimesRoman", Font.BOLD, 30);
	public final Font SMALL = new Font("TimesRoman", Font.BOLD, 15);
	
	boolean player1 = true;
	boolean jakauj = false;
	
	int sk1 = 0;
	int sk2 = 0;
	
	SahaGalds galds;
	
	JButton b1;
	JButton b2;

	public SanuPanelis() {
		setPreferredSize(new Dimension(240, 560));
		setLayout(null);
		
		b1 = new JButton("Jauna spele");
		b2 = new JButton("Pauze");
		
		add(b1);
		//add(b2);
		
		b1.addActionListener(new Action1());
		b2.addActionListener(new Action2());
		
		b1.setBounds(65, 400, 110, 30);
		b2.setBounds(65, 450, 110, 30);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.decode("#ccffcc"));
		g2d.fillRect(0, 0, PLAT, AUGST);
		//Fona krasas uzstadisana
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				  RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		String text = (player1) ? "Player 1" : "Player 2";
		if (player1) g2d.setColor(Color.decode("#00b300"));
		else g2d.setColor(Color.decode("#ff6600"));
		g2d.setFont(BIG);
		FontMetrics metrics = g2d.getFontMetrics();
		int textW = metrics.stringWidth(text);
		g2d.drawString(text, PLAT/2 - textW/2, 60);
		//Izvada speletaja numuru, kam ir gajiens
		
		if (jakauj) {
			g2d.setColor(Color.red);
			g2d.setFont(SMALL);
			metrics = g2d.getFontMetrics();
			textW = metrics.stringWidth("Tagad ir obligati jakauj!");
			g2d.drawString("Tagad ir obligati jakauj!", PLAT/2 - textW/2, 90);
		}
		
		g2d.setColor(Color.black);
		g2d.drawRect(11, 194, 216, 75);
		
		int y = 200;
		int x = 0;
		for (int i=0; i<sk1; i++) {
			if (i==6) {
				y+=35;
				x = 0;
			}
			g2d.setColor(Color.black);
			g2d.drawOval((16+x*35)-1, y-1, 31, 31);
			g2d.setColor(Color.decode("#d9d9d9"));
			g2d.fillOval(16+x*35, y, 30, 30);
			x++;
		}
		
		g2d.setColor(Color.black);
		g2d.drawRect(11, 279, 216, 75);
		
		y = 285;
		x = 0;
		for (int i=0; i<sk2; i++) {
			if (i==6) {
				y+=35;
				x = 0;
			}
			g2d.setColor(Color.black);
			g2d.drawOval((16+x*35)-1, y-1, 31, 31);
			g2d.setColor(Color.decode("#bf8040"));
			g2d.fillOval(16+x*35, y, 30, 30);
			x++;
		}
	}
	
	public void pievienotKaulinu(int player) {
		switch (player) {
		case 1: sk1++;
				break;
		case 2: sk2++;
				break;
		}
	}
	
	public class Action1 implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			galds.jaunaSpele();
		}
	}
	
	public class Action2 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			galds.pauze();
		}
	}
}
