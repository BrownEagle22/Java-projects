import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.text.LayeredHighlighter;

public class Panels extends JPanel {
	
	public final int AUGST = 560;
	public final int PLAT = 560;
	public final int MALA = 70;
	public final int KMALA = 60;
	
	SanuPanelis sans;
	SahaGalds sahaGalds;
	SadalosaLinija linija;
	Pazinojums pazin;
	JLayeredPane pane;
	
	public Panels(SanuPanelis sans) {
		this.sans = sans;
		setPreferredSize(new Dimension(PLAT, AUGST));
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		pazin = new Pazinojums();
		
		sahaGalds = new SahaGalds(this, pazin);
		
		linija = new SadalosaLinija(AUGST);
		
		pane = new JLayeredPane();
		pane.setPreferredSize(new Dimension(AUGST, PLAT));
		pane.add(sahaGalds, new Integer(1));
		pane.add(linija, new Integer(2));
		pane.add(pazin, new Integer(3));
		
		sahaGalds.setBounds(0, 0, AUGST, AUGST);
		linija.setBounds(0, AUGST/2-2, AUGST, 4);
		pazin.setBounds(0, 0, 560, 560);
		add(pane);
		sakums(0, "#bf8040", "#996633", "#734d26", 2, false);
		sakums(5, "#d9d9d9", "#b3b3b3", "#737373", 1, true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
	}
	
	public void sakums(int x, String krasa, String hoKrasa, String clKrasa, int player, boolean gaj) {
		boolean melns = (x % 2 == 0) ? true : false;
		for (int i=0; i<8; i++) {
			for (int j=x; j<x+3; j++) {
				if (melns) {
					sahaGalds.setKaulins(krasa, hoKrasa, clKrasa, j, i, player, gaj, false);
				}
				melns = melns ? false : true;
			}
		}
	}
	
	public void changeLabel() {
		sans.player1 = (sans.player1) ? false : true;
		sans.repaint();
	}
}
