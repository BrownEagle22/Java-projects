import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class Tile extends JComponent implements MouseListener, MouseMotionListener {
	
	final static int WIDTH = MainPanel.ROW_WIDTH;
	final static int HEIGHT = MainPanel.ROW_WIDTH;
	final static int REMOVE_SPEED = 3;
	
	BufferedImage[] img = null;
	
	int imgNumber = 0;
	
	private int imgX, imgY=0;
	private int currentWidth = WIDTH;
	private int currentHeight = HEIGHT;
	
	private int x = 0, y = 0;
	private int xVel = 0, yVel = 0;
	private int xDest, yDest;
	
	private boolean increases = true;
	
	private boolean rotate = false;
	private boolean selected = false;
	private boolean decreasing = false;
	private boolean remove = false;
	private boolean moving = false;
	
	MainPanel mainPanel;
	
	public Tile(BufferedImage[] image, int x, int y, MainPanel panel) {
		this.x = x;
		this.y = y;
		this.xDest = x;
		this.yDest = y;
		this.mainPanel = panel;
		img = image;
		setBounds(x, y, WIDTH, HEIGHT);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		g2d.drawImage(img[imgNumber], imgX, imgY, currentWidth, currentHeight, null);
		
	}
	
	public void updateImageNumber() {
		if (imgNumber < 14) {
			imgNumber++;
		} else {
			imgNumber = 0;
			rotate = false;
		}
	}
	
	public void decrease() {
		if (increases) {
			if (currentWidth < (double)WIDTH * 1.2) {
				imgX-=REMOVE_SPEED;
				imgY-=REMOVE_SPEED;
				currentWidth+=2*REMOVE_SPEED;
				currentHeight+=2*REMOVE_SPEED;
			} else increases = false;
		} else {
			if (currentWidth-2*REMOVE_SPEED > 0 && currentHeight-2*REMOVE_SPEED > 0) {
				imgX+=REMOVE_SPEED;
				imgY+=REMOVE_SPEED;
				currentWidth-=2*REMOVE_SPEED;
				currentHeight-=2*REMOVE_SPEED;
			} else {
				currentWidth = 0;
				currentHeight = 0;
				remove = true;
			}
		}
	}
	
	public void startMoving(int xDif, int yDif, int speed) {
		if (xDif != 0) {
			xDest = x+xDif;
			xVel = (xDif > 0) ? speed : -speed;
		}
		if (yDif != 0) {
			yDest = y+yDif;
			yVel = (yDif > 0) ? speed : -speed;
		}
		moving = true;
	}
	
	public void move() {
		if ( (xVel > 0 && x + xVel >= xDest)
				|| (xVel < 0 && x + xVel <= xDest)
				|| (yVel > 0 && y + yVel >= yDest)
				|| (yVel < 0 && y + yVel <= yDest)) {
			endMoving();
		} else {
			x += xVel;
			y += yVel;
		}
		
		setBounds(x, y, WIDTH, HEIGHT);
	}
	
	public void endMoving() {
		x = xDest;
		y = yDest;
		xVel = 0;
		yVel = 0;
		moving = false;
	}
	
	public void updatePos() {
		setBounds(x, y, WIDTH, HEIGHT);
	}
	
	public boolean isRotating() {
		return rotate;
	}
	
	public void setRotation(boolean rotate) {
		this.rotate = rotate;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected (boolean selected) {
		this.selected = selected;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getXvel() {
		return xVel;
	}
	
	public void setXvel(int xVel) {
		this.xVel = xVel;
	}
	
	public int getYvel() {
		return yVel;
	}
	
	public void setYvel(int yVel) {
		this.yVel = yVel;
	}
	
	public int getXdest() {
		return xDest;
	}
	
	public void setXdest(int xDest) {
		this.xDest = xDest;
	}
	
	public int getYdest() {
		return yDest;
	}
	
	public void setYdest(int yDest) {
		this.yDest = yDest;
	}
	
	public boolean isDecreasing() {
		return decreasing;
	}
	
	public void setDecreasing(boolean decreasing) {
		this.decreasing = decreasing;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public boolean needRemove() {
		return remove;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		mainPanel.tileClick(this);
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (selected) {
			if (e.getX() > WIDTH) {
				mainPanel.dragged(this, 1, 0);
			}
			if (e.getX() < 0) {
				mainPanel.dragged(this, -1, 0);
			}
			if (e.getY() > HEIGHT) {
				mainPanel.dragged(this, 0, 1);
			}
			if (e.getY() < 0) {
				mainPanel.dragged(this, 0, -1);
			}
			System.out.println();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
