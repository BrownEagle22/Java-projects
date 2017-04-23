import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Program3D extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
	final static int POINT_SIZE = 5;
	final static int OIL = 40;
	
	Color[] colors = {Color.red, Color.green, Color.blue, Color.pink, Color.cyan, Color.yellow};
	
	double rotateAngle = 2.0;
	double xAngle = 0.0;
	double yAngle = 0.0;
	double xInertiaAngle = 0.0;
	double yInertiaAngle = 0.0;
	int mouseX, mouseY;
	
	boolean running;
	boolean pause;
	Point3D center;
	
	ArrayList<Point3D> points3d = new ArrayList<>();
	ArrayList<Side> sides = new ArrayList<>();
	
	Point3D[][] faces = new Point3D[6][4];
	
	public Program3D() {
		setPreferredSize(new Dimension(800, 600));
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		setFocusable(true);
		
		makeDisplay();
		
		makeCube();
		
		run();
	}
	
	private void makeCube() {
		center = new Point3D(400, 300, 200);
		
		points3d.add(new Point3D(300, 200, 300));
		points3d.add(new Point3D(500, 200, 300));
		points3d.add(new Point3D(500, 400, 300));
		points3d.add(new Point3D(300, 400, 300));
		
		points3d.add(new Point3D(300, 200, 100));
		points3d.add(new Point3D(500, 200, 100));
		points3d.add(new Point3D(500, 400, 100));
		points3d.add(new Point3D(300, 400, 100));
		
		sides.add(new Side(0, 1));
		sides.add(new Side(1, 2));
		sides.add(new Side(2, 3));
		sides.add(new Side(0, 3));
		
		sides.add(new Side(4, 5));
		sides.add(new Side(5, 6));
		sides.add(new Side(6, 7));
		sides.add(new Side(4, 7));
		
		sides.add(new Side(0, 4));
		sides.add(new Side(1, 5));
		sides.add(new Side(2, 6));
		sides.add(new Side(3, 7));
		
		faces[0][0] = points3d.get(0);
		faces[0][1] = points3d.get(1);
		faces[0][2] = points3d.get(2);
		faces[0][3] = points3d.get(3);
		
		faces[1][0] = points3d.get(0);
		faces[1][1] = points3d.get(1);
		faces[1][2] = points3d.get(5);
		faces[1][3] = points3d.get(4);
		
		faces[2][0] = points3d.get(1);
		faces[2][1] = points3d.get(2);
		faces[2][2] = points3d.get(6);
		faces[2][3] = points3d.get(5);
		
		faces[3][0] = points3d.get(4);
		faces[3][1] = points3d.get(5);
		faces[3][2] = points3d.get(6);
		faces[3][3] = points3d.get(7);
		
		faces[4][0] = points3d.get(2);
		faces[4][1] = points3d.get(3);
		faces[4][2] = points3d.get(7);
		faces[4][3] = points3d.get(6);
		
		faces[5][0] = points3d.get(0);
		faces[5][1] = points3d.get(3);
		faces[5][2] = points3d.get(7);
		faces[5][3] = points3d.get(4);
	}
	
	private void deleteCube() {
		ArrayList<Point3D> removePoints = new ArrayList<>();
		for (Point3D point : points3d) {
			removePoints.add(point);
		}
		points3d.removeAll(removePoints);
	}
	
	private void makeDisplay() {
		JFrame frame = new JFrame("Rotacija");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(this);
		
		frame.setVisible(true);
		frame.pack();
	}
	
	private void run() {
		running = true;
		int fps = 60;
		int fpsCounter = 0;
		double timePerFrame = 1000000000 / fps;
		double delta = 0;
		double timer = 0;
		long now;
		long lastTime = System.nanoTime();
		
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerFrame;
			timer += now - lastTime;
			lastTime = now;
			
			if (delta >= 1) {
				update();
				render();
				delta--;
				fpsCounter++;
			}
			
			if (timer >= 1000000000) {
				System.out.println("FPS: " + fpsCounter);
				fpsCounter = 0;
				timer = 0;
			}
		}
	}
	
	private void update() {
		Point3D point3d;
		Coord point;
		
		for (int i=0; i<points3d.size(); i++) {
			if (!pause) {
				point3d = points3d.get(i);
				point = rotate(center.getYface(), point3d.getYface(), rotateAngle);
				point3d.x = point.x;
				point3d.z = point.y;
				points3d.set(i, point3d);
				
				point3d = points3d.get(i);
				point = rotate(center.getZface(), point3d.getZface(), rotateAngle);
				point3d.x = point.x;
				point3d.y = point.y;
				points3d.set(i, point3d);
			} else if (xInertiaAngle != 0 || yInertiaAngle != 0) {
				point3d = points3d.get(i);
				point = rotate(center.getXface(), point3d.getXface(), yInertiaAngle);
				point3d.y = point.x;
				point3d.z = point.y;
				points3d.set(i, point3d);
				
				point3d = points3d.get(i);
				point = rotate(center.getYface(), point3d.getYface(), xInertiaAngle);
				point3d.x = point.x;
				point3d.z = point.y;
				points3d.set(i, point3d);
				
				if (xInertiaAngle > -0.2 && xInertiaAngle < 0.2) {
					xInertiaAngle = 0.0;
				} else {
					xInertiaAngle -= xInertiaAngle / Math.abs(xInertiaAngle) / OIL;
				}
				
				if (yInertiaAngle > -0.2 && yInertiaAngle < 0.2) {
					yInertiaAngle = 0.0;
				} else {
					yInertiaAngle -= yInertiaAngle / Math.abs(yInertiaAngle) / OIL;
				}
			}
			
			point3d = points3d.get(i);
			point = rotate(center.getXface(), point3d.getXface(), yAngle);
			point3d.y = point.x;
			point3d.z = point.y;
			points3d.set(i, point3d);
			
			point3d = points3d.get(i);
			point = rotate(center.getYface(), point3d.getYface(), xAngle);
			point3d.x = point.x;
			point3d.z = point.y;
			points3d.set(i, point3d);
		}
		xAngle = 0.0;
		yAngle = 0.0;
	}
	
	private void render() {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		g2d.setColor(Color.red);
		g2d.fillOval(center.getX()-POINT_SIZE, center.getY()-POINT_SIZE, POINT_SIZE*2, POINT_SIZE*2);
		
		g2d.setColor(Color.black);
		
		for (int i=0; i<3; i++) {
			int index;
			if (closestPoint(faces[i]) >= closestPoint(faces[i+3])) {
				index = i;
			} else {
				index = i+3;
			}
			Point3D[] points = faces[index];
			
			GeneralPath path = new GeneralPath();
			path.moveTo(points[0].x, points[0].y);
			
			for (int j=0; j<points.length; j++) {
				Point3D point3d = points[j];
				path.lineTo(point3d.x, point3d.y);
			}
			
			path.closePath();
			g2d.setColor(colors[index]);
			g2d.fill(path);
		}
		
		g2d.setColor(Color.black);
		
		Point3D farest = points3d.get(0);
		for (int i=1; i<points3d.size(); i++) {
			if (points3d.get(i).z < farest.z) {
				farest = points3d.get(i);
			}
		}
		
		for (Point3D point : points3d) {
			if (point != farest)
				g2d.fillOval(point.getX()-POINT_SIZE, point.getY()-POINT_SIZE, POINT_SIZE*2, POINT_SIZE*2);
		}
		
		for (Side side : sides) {
			if (points3d.get(side.i1) != farest && points3d.get(side.i2) != farest) {
				Coord p1 = points3d.get(side.i1).getZface();
				Coord p2 = points3d.get(side.i2).getZface();
				g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
			}
		}
	}
	
	private double closestPoint(Point3D[] point3D) {
		double largest = point3D[0].z;
		for (int i=1; i<point3D.length; i++) {
			if (point3D[i].z > largest)
				largest = point3D[i].z;
		}
		return largest;
	}
	
	private Coord rotate(Coord center, Coord point, double angle) {
		double alfa = Math.toRadians(angle);
		
		double px = point.x - center.x;
		double py = point.y - center.y;
		double distance = Math.sqrt(px*px + py*py);
		
		Point signs = new Point(0, 0);
		int quadr = 0;
		if (px >= 0 && py <= 0) {
			quadr = 1;
			signs = new Point(1, -1);
		}
		if (px <= 0 && py <= 0) {
			quadr = 2;
			signs = new Point(-1, -1);
		}
		if (px <= 0 && py >= 0) {
			quadr = 3;
			signs = new Point(-1, 1);
		}
		if (px >= 0 && py >= 0) {
			quadr = 4;
			signs = new Point(1, 1);
		}
		
		double xRez = 0.0;
		double yRez = 0.0;
		switch (quadr) {
			case 1: case 3: {
				double beta = Math.asin(Math.abs(py) / distance);
				
				double gamma = Math.toRadians(90.0) - alfa - beta;
				
				xRez = Math.sin(gamma) * distance * signs.x;
				yRez = Math.cos(gamma) * distance * signs.y;
				break;
			}
			
			case 2: case 4: {
				double beta = Math.asin(Math.abs(px) / distance);
				
				double gamma = Math.toRadians(90.0) - alfa - beta;
				
				xRez = Math.cos(gamma) * distance * signs.x;
				yRez = Math.sin(gamma) * distance * signs.y;
				break;
			}
		}
		
		return new Coord(center.x + xRez, center.y + yRez);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			pause = (pause) ? false : true;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			rotateAngle = 1.0;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			rotateAngle = 2.0;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			rotateAngle = 3.0;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
			rotateAngle = 4.0;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			rotateAngle = 5.0;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
			rotateAngle = 6.0;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
			rotateAngle = 7.0;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			rotateAngle = 8.0;
		}
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			rotateAngle = 9.0;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_R) {
			deleteCube();
			makeCube();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		xAngle += (e.getX()-mouseX) * 3.0 / 4.0;
		yAngle += (e.getY()-mouseY) * 3.0 / 4.0;
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (pause) {
			xInertiaAngle = xAngle;
			yInertiaAngle = yAngle;
		}
	}
}

class Point3D {
	double x, y, z;
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public int getZ() {
		return (int)z;
	}
	
	public Coord getXface() {
		return new Coord(y, z);
	}
	
	public Coord getYface() {
		return new Coord(x, z);
	}
	
	public Coord getZface() {
		return new Coord(x, y);
	}
}