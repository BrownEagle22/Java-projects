import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

public class Laucins extends JPanel{
	
	public final int MALA = 70;
	public final int KMALA = 60;
	public final int RINKIS1 = 50;
	public final int RINKIS2 = 26;
	
	public final Shape circle = new Ellipse2D.Double(5, 5, KMALA, KMALA);
	
	int player;
	int i;
	int j;
	
	int[] kaujamaKoord = new int[2];
	
	String krasa;
	String kvKrasa;
	String hoverKrasa;
	String clickKrasa;
	String sakKrasa;
	String rinkaKrasa = "#ff0000";
	
	SahaGalds galds;
	
	boolean irKaulins = false;
	boolean iegajusi = false;
	boolean iegajKv = false;
	boolean klikskis = false;
	boolean irRinkis = false;
	boolean irGajiens = false;
	boolean kaujamaisLauks = false;
	boolean velGajiens = false;
	boolean irDama = false;
	boolean pauze = false;
	
	public Laucins(String krasa, SahaGalds galds, int i, int j) {
		this.galds = galds;
		this.krasa = krasa;
		this.i = i;
		this.j = j;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(MALA, MALA));
		this.addMouseMotionListener(new Pele());
		this.addMouseListener(new Klikskis());
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.decode(krasa));
		g2d.fillRect(0, 0, MALA, MALA);
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		if (irKaulins) {
			g2d.setColor(Color.decode(kvKrasa));
			g2d.fill(circle);
		} else if (irRinkis) {
			g2d.setStroke(new BasicStroke(8));
			g2d.setColor(Color.decode(rinkaKrasa));
			g2d.drawOval((MALA-RINKIS1)/2, (MALA-RINKIS1)/2, RINKIS1, RINKIS1);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawOval((MALA-RINKIS2)/2, (MALA-RINKIS2)/2, RINKIS2, RINKIS2);
		}
		
		if (irDama) {
			GeneralPath path = new GeneralPath();
			path.moveTo(15, 47);		//path.moveTo(0, 0);
			path.lineTo(15, 27);		//path.lineTo(0, 20);
			path.lineTo(25, 37);		//path.lineTo(10, 10);
			path.lineTo(35, 19);		//path.lineTo(20, 25);      pamata kronis
			path.lineTo(45, 37);		//path.lineTo(30, 10);
			path.lineTo(55, 27);		//path.lineTo(40, 20);
			path.lineTo(55, 47);		//path.lineTo(40, 0);
			path.closePath();
			g2d.setColor(Color.decode("#ffcc00"));
			g2d.fill(path);
		}
	}
	
	public class Pele implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			peleKustas(e);
		}		
	}
	
	public class Klikskis implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			nospiests();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			
		}
		
	}
	
	public void peleKustas(MouseEvent e) {
		if (irKaulins && !galds.speleBeidzas) {
			if (circle.contains(e.getPoint()) && !iegajusi) {
				iegajusi = true;
				if (!klikskis) {
					kvKrasa = hoverKrasa;
					repaint();
				}
			} else if (!circle.contains(e.getPoint()) && iegajusi) {
				iegajusi = false;
				if (!klikskis) {
					kvKrasa = sakKrasa;
					repaint();
				}
			}
		}
		
		if (irRinkis) {
			rinkaKrasa = "#cc0000";
			repaint();
		} else {
			rinkaKrasa = "#ff0000";
			repaint();
		}
		
		if (!iegajKv && !galds.speleBeidzas) {
			iegajKv = true;
			galds.checkHover(this);
		}
		//pirmo reizi ieejot kvadrataa parbauda vai kada citaa kvadrataa nav paliicis ieslegts hover un novers to
	}
	
	public void nospiests() {
		if (iegajusi/* && !galds.velNospKv(this)*/ && irGajiens && !galds.speleBeidzas) {
			if (!klikskis) {
				if (galds.velNospKv(this)) {
					galds.delIespejas();
					for (int i=0; i<8; i++)
						for (int j=0; j<8; j++)
							if (galds.tiles[i][j].klikskis && galds.tiles[i][j] != this) {
								galds.tiles[i][j].klikskis = false;
								galds.tiles[i][j].kvKrasa = galds.tiles[i][j].sakKrasa;
								galds.tiles[i][j].repaint();
							}
				}
				klikskis = true;
				kvKrasa = clickKrasa;
				iegajKv = false;
				if (irDama) galds.findDamaIespejas(this, true, true);
				else galds.findIespejas(this, true);
				System.out.println(player);
			} else if (iegajusi && !velGajiens) {
				klikskis = false;
				kvKrasa = hoverKrasa;
				/*if (velGajiens) {
					velGajiens = false;
					galds.mainitGajienu();
					galds.panelis.changeLabel();
				}*/
				galds.delIespejas();
			}
			repaint();
		}
		if (irRinkis)
			galds.paietGajienu(this);
	}
}
