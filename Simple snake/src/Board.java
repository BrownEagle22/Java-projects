import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Board extends JPanel {
	private final int WIDTH = 300;
	private final int HEIGHT = 300;
	private int rows;
	private int gameEnd = 0;
	
	private boolean isPaused;
	
	private ArrayList<Point> tileCoords;
	private Point appleCoord;
	private Point collisionCoord;
	
	public Board(int rows) {
		this.rows = rows;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		g2d.setColor(Color.green);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		//Iezîmç fonu
		
		g2d.setColor(Color.black);
		for (int i=0; i<rows; i++) {
			g2d.drawLine(0, i*HEIGHT/rows, WIDTH, i*HEIGHT/rows);
			g2d.drawLine(i*WIDTH/rows, 0, i*WIDTH/rows, HEIGHT);
		}
		//Iezîmç rûtiòas lîniju veidâ
		
		g2d.setColor(Color.red);
		if (tileCoords != null) {
			for (Point p : tileCoords) {
				fillTile(g2d, p.x, p.y);
			}
		}
		//Iezîmç visus kvadrâtus, kas ir tileCoords lista
		
		g2d.setColor(Color.orange);
		if (appleCoord != null) {
			fillTile(g2d, appleCoord.x, appleCoord.y);
		}
		//Iezîmç âbolu
		
		if (isPaused) {
			g2d.setStroke(new BasicStroke(5));
			
			g2d.setColor(Color.gray);
			g2d.fillRoundRect(WIDTH/2-40, HEIGHT/2-40, 80, 80, 30, 30);
			
			g2d.setColor(Color.black);
			g2d.drawRoundRect(WIDTH/2-40, HEIGHT/2-40, 80, 80, 30, 30);
			
			g2d.fillRect(WIDTH/2-15, HEIGHT/2-15, 30, 30);
		}
		//Iezîmç pauzes kvadrâtu, ja ir pauze
		
		if (collisionCoord != null) {
			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(Color.black);
			
			g2d.drawLine(collisionCoord.x * WIDTH/rows,
						collisionCoord.y * HEIGHT/rows,
						collisionCoord.x * WIDTH/rows + WIDTH/rows,
						collisionCoord.y * HEIGHT/rows + HEIGHT/rows);
			g2d.drawLine(collisionCoord.x * WIDTH/rows,
						collisionCoord.y * HEIGHT/rows + HEIGHT/rows,
						collisionCoord.x * WIDTH/rows + WIDTH/rows,
						collisionCoord.y * HEIGHT/rows);
		}
		//Iezîmç krustu satriekðanâs vietâ
		
		if (gameEnd == 1) {
			
		} else if (gameEnd == 2) {
			
		}
	}
	
	public void reset() {
		collisionCoord = null;
		gameEnd = 0;
	}
	
	private void fillTile(Graphics2D g2d, int x, int y) {
		int startX = x * WIDTH/rows;
		int startY = y * HEIGHT/rows;
		int endX = (x+1) * WIDTH/rows;
		int endY = (y+1) * WIDTH/rows;
		g2d.fillRect(startX + 1, startY + 1, endX - startX - 1, endY - startY - 1);
	}
	
	public void setPaintingTiles(ArrayList<Point> tileCoords) {
		this.tileCoords = tileCoords;
	}
	
	public void setAppleCoord(Point coord) {
		this.appleCoord = coord;
	}
	
	public void togglePaused() {
		this.isPaused = (this.isPaused) ? false : true;
	}
	
	public void setCollisionCoord(Point point) {
		this.collisionCoord = point;
	}
	
	public void setRows (int rows) {
		this.rows = rows;
	}
}
