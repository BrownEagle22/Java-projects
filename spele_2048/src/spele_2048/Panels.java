package spele_2048;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import javax.swing.JPanel;

public class Panels extends JPanel {
	
	final int LIN = 464/29;
	final int KV = 464*6/29;
	final int LINKV = LIN + KV;
	final int IZM = 464;
	Bloks bloki[][] = new Bloks[4][4];
	int xDif = 0;
	int yDif = 0;
	int sk = 0;
	boolean youWin = false;
	boolean varKusteties = true;
	boolean flag;
	boolean end = false;
	String virziens = "";
	
	public Panels() {
		this.setPreferredSize(new Dimension(464, 464));
		spawn(2);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.gray);
		g2d.fillRect(0, 0, IZM, IZM);
		g2d.setColor(Color.lightGray);
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
									  RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus

		for (int x=LIN; x<IZM; x+=LIN+KV)
			for (int y=LIN; y<IZM; y+=LIN+KV)
				g2d.fillRoundRect(x, y, KV, KV, 20, 20);
		//Iekraso visus kvadratus
		
		for (int i=0; i<4; i++)
			for (int j=0; j<4; j++)
				if (bloki[i][j] != null) {
					switch (bloki[i][j].vertiba) {
					case 2: g2d.setColor(Color.orange);
							break;
					case 4: g2d.setColor(Color.yellow);
							break;
					case 8: g2d.setColor(Color.green);
							break;
					case 16: g2d.setColor(Color.red);
							break;
					case 32: g2d.setColor(Color.magenta);
							break;
					case 64: g2d.setColor(Color.pink);
							break;
					case 128: g2d.setColor(Color.cyan);
							break;
					case 256: g2d.setColor(Color.decode("#3399ff"));
							break;
					case 512: g2d.setColor(Color.decode("#0080ff"));
							break;
					case 1024: g2d.setColor(Color.decode("#0066cc"));
							break;
					case 2048: g2d.setColor(Color.decode("#004d99"));
							break;
					default: g2d.setColor(Color.decode("#004d99"));
							break;
					}
					if (bloki[i][j].palielinas)
						if (bloki[i][j].izm == KV*12/10)
							bloki[i][j].palielinas = false;
						else bloki[i][j].izm++;
					if (bloki[i][j].izm != KV && !bloki[i][j].palielinas)
						bloki[i][j].izm--;
					//System.out.println(bloki[i][j].izm);
					g2d.fillRoundRect(bloki[i][j].xKoord - (bloki[i][j].izm - KV)/2, bloki[i][j].yKoord - (bloki[i][j].izm - KV)/2, bloki[i][j].izm, bloki[i][j].izm, 20, 20);
					g2d.setColor(Color.black);
					g2d.setFont(new Font("TimesRoman", Font.PLAIN, 40));
					FontMetrics metrics2 = g2d.getFontMetrics(); //Satur visus fonta izmera parametrus
					int height2 = metrics2.getHeight();  //fonta teksta augstums
					int adv2 = metrics2.stringWidth(Integer.toString(bloki[i][j].vertiba));  //teksta platums
					g2d.drawString(Integer.toString(bloki[i][j].vertiba), bloki[i][j].xKoord + KV/2 - adv2/2, bloki[i][j].yKoord + KV/2 + height2/4);
				}
		if (youWin) {
			g2d.setColor(Color.green);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 80));
			FontMetrics metrics = g2d.getFontMetrics(); //Satur visus fonta izmera parametrus
			int height = metrics.getHeight();  //fonta teksta augstums
			int adv = metrics.stringWidth("You Win!");  //teksta platums
			g2d.drawString("You Win!", IZM/2 - adv/2, IZM/2 + height/4);
		} else if (end) {
			g2d.setColor(Color.red);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 80));
			FontMetrics metrics3 = g2d.getFontMetrics(); //Satur visus fonta izmera parametrus
			int height3 = metrics3.getHeight();  //fonta teksta augstums
			int adv3 = metrics3.stringWidth("Game Over!");  //teksta platums
			g2d.drawString("Game Over!", IZM/2 - adv3/2, IZM/2 + height3/4);
		}
	}
	
	public void move(String virziens) {
		this.virziens = virziens;
		//varKusteties = false;
		xDif = 0;
		yDif = 0;
		switch (virziens) {
			case "up": {
				yDif-=4;
				for (int i=0; i<4; i++)
					for (int j=0; j<4; j++)
						if (bloki[i][j] != null) {
							for (int z=j-1; z>=0; z--)
								if (bloki[i][z] != null) 
									if (bloki[i][j].vertiba == bloki[i][z].vertiba && !bloki[i][z].pazudis) {
										varKusteties = false;
										bloki[i][j].yBeig = bloki[i][z].yBeig;
										bloki[i][j].pazudis = true;
									} else {
										bloki[i][j].yBeig = bloki[i][z].yBeig+1;
										break;
									}
								else {
									varKusteties = false;
									bloki[i][j].yBeig = z;
								}
						}
				break;
			}
			case "down": {
				yDif+=4;
				for (int i=0; i<4; i++)
					for (int j=3; j>=0; j--)
						if (bloki[i][j] != null) {
							for (int z=j+1; z<4; z++)
								if (bloki[i][z] != null)
									if (bloki[i][j].vertiba == bloki[i][z].vertiba && !bloki[i][z].pazudis) {
										varKusteties = false;
										bloki[i][j].yBeig = bloki[i][z].yBeig;
										bloki[i][j].pazudis = true;
									} else {
										bloki[i][j].yBeig = bloki[i][z].yBeig-1;
										break;
									}
								else {
									varKusteties = false;
									bloki[i][j].yBeig = z;
								}
						}
				break;
			}
			case "left": {
				xDif-=4;
				for (int i=0; i<4; i++)
					for (int j=0; j<4; j++)
						if (bloki[j][i] != null) {
							for (int z=j-1; z>=0; z--)
								if (bloki[z][i] != null)
									if (bloki[j][i].vertiba == bloki[z][i].vertiba && !bloki[z][i].pazudis) {
										varKusteties = false;
										bloki[j][i].xBeig = bloki[z][i].xBeig;
										bloki[j][i].pazudis = true;
									} else {
										bloki[j][i].xBeig = bloki[z][i].xBeig+1;
										break;
									}
								else {
									varKusteties = false;
									bloki[j][i].xBeig = z;
								}
						}
				break;
			}
			case "right": {
				xDif+=4;
				for (int i=0; i<4; i++)
					for (int j=3; j>=0; j--)
						if (bloki[j][i] != null) {
							for (int z=j+1; z<4; z++)
								if (bloki[z][i] != null)
									if (bloki[j][i].vertiba == bloki[z][i].vertiba && !bloki[z][i].pazudis) {
										varKusteties = false;
										bloki[j][i].xBeig = bloki[z][i].xBeig;
										bloki[j][i].pazudis = true;
									} else {
										bloki[j][i].xBeig = bloki[z][i].xBeig-1;
										break;
									}
								else {
									varKusteties = false;
									bloki[j][i].xBeig = z;
								}
						}
				break;
			}
		}
	}
	
	public void render(int i, int j) {
		if (bloki[i][j] != null) {
			
			if (bloki[i][j].vertiba == 2048) {
				//System.out.println("You Win!");
				youWin = true;
			}
			
			if (bloki[i][j].xSak != bloki[i][j].xBeig || bloki[i][j].ySak != bloki[i][j].yBeig) {
				flag = false;
				bloki[i][j].kustas = true;
				bloki[i][j].xKoord += xDif;
				if (xDif > 0)
					bloki[i][j].xSak = (bloki[i][j].xKoord - LIN) / LINKV;
				else
					bloki[i][j].xSak = (int)Math.ceil((bloki[i][j].xKoord - LIN) / (double)LINKV);
				bloki[i][j].yKoord += yDif;
				if (yDif > 0)
					bloki[i][j].ySak = (bloki[i][j].yKoord - LIN) / LINKV;
				else
					bloki[i][j].ySak = (int)Math.ceil((bloki[i][j].yKoord - LIN) / (double)LINKV);
			} else if (bloki[i][j] != null && bloki[i][j].pazudis) {
				bloki[bloki[i][j].xBeig][bloki[i][j].yBeig].vertiba *= 2;
				bloki[bloki[i][j].xBeig][bloki[i][j].yBeig].palielinas = true;
				bloki[i][j] = null;
			} else if (bloki[i][j].xSak == bloki[i][j].xBeig &&
						bloki[i][j].ySak == bloki[i][j].yBeig &&
						bloki[i][j].kustas) {		
				bloki[i][j].kustas = false;
				bloki[bloki[i][j].xSak][bloki[i][j].ySak] = bloki[i][j];
				bloki[i][j] = null;
			}
		}
	}
	
	public void spawn(int skaits) {
		int x, y, vert;
		for (int i=0; i<skaits; i++) {
			Random rand = new Random();
			do {
				x = rand.nextInt(4) + 0;
				y = rand.nextInt(4) + 0;
				vert = rand.nextInt(10) + 1;
			} while(bloki[x][y] != null);
			if (vert == 1) vert = 4;
			else vert = 2;
			bloki[x][y] = new Bloks(x, y, vert, KV);
			bloki[x][y].xKoord = LIN + LINKV * x;
			bloki[x][y].yKoord = LIN + LINKV * y;
			bloki[x][y].palielinas = true;
		}
	}
	
	public boolean gameOver () {
		for (int i=0; i<4; i++)
			for (int j=1; j<4; j++)
				if (bloki[i][j] != null)
					if (bloki[i][j-1] != null) {
						if (bloki[i][j].vertiba == bloki[i][j-1].vertiba)
							return false;
					} else return false;
		for (int i=0; i<4; i++)
			for (int j=2; j>=0; j--)
				if (bloki[i][j] != null)
					if (bloki[i][j+1] != null) {
						if (bloki[i][j].vertiba == bloki[i][j+1].vertiba)
							return false;
					} else return false;
		for (int i=0; i<4; i++)
			for (int j=1; j<4; j++)
				if (bloki[j][i] != null)
					if (bloki[j-1][i] != null) {
						if (bloki[j][i].vertiba == bloki[j-1][i].vertiba)
							return false;
					} else return false;
		for (int i=0; i<4; i++)
			for (int j=2; j>=0; j--)
				if (bloki[j][i] != null)
					if (bloki[j+1][i] != null) {
						if (bloki[j][i].vertiba == bloki[j+1][i].vertiba)
							return false;
					} else return false;
		return true;
	}
	
	public void jaunaSpele() {
		for (int i=0; i<4; i++)
			for (int j=0; j<4; j++)
				bloki[i][j] = null;
		xDif = 0;
		yDif = 0;
		spawn(2);
	}
}
