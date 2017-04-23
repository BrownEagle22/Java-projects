import java.awt.Point;

public class Tile {
	private int x, y;
	private int xVel, yVel;
	private int xyMax;
	
	public Tile(int x, int y, int xVel, int yVel, int rows) {
		this.x = x;
		this.y = y;
		this.xyMax = rows;
		this.xVel = xVel;
		this.yVel = yVel;
	}
	
	public void move() {
		x += xVel;
		y += yVel;
		
		if (x > xyMax-1) {
			x = 0;
		}
		if (x < 0) {
			x = xyMax-1;
		}
		if (y > xyMax-1) {
			y = 0;
		}
		if (y < 0) {
			y = xyMax-1;
		}
		
		
	}
	
	public Point getCoord() {
		return new Point(this.x, this.y);
	}
	
	public void setCoord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point getVel() {
		return new Point(xVel, yVel);
	}
	
	public void setVel(int xVel, int yVel) {
		if (!(xVel != 0 && xVel == -this.xVel)
				&& !(yVel != 0 && yVel == -this.yVel)) {
			this.xVel = xVel;
			this.yVel = yVel;
		}
		//Jaunais virziens netiek uzlikts, ja virziens ir pretçjs iepriekðçjam virzienam
	}
}
