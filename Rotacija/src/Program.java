import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Program extends JPanel implements KeyListener {
	final static int POINT_SIZE = 5;
	
	int rotateAngle = 5;
	
	boolean running;
	boolean pause;
	Coord center;
	
	ArrayList<Coord> points = new ArrayList<>();
	ArrayList<Side> sides = new ArrayList<>();
	
	public Program() {
		setPreferredSize(new Dimension(800, 600));
		addKeyListener(this);
		setFocusable(true);
		
		makeDisplay();
		
		center = new Coord(400, 300);
		
		points.add(new Coord(300, 200));
		points.add(new Coord(500, 200));
		points.add(new Coord(500, 400));
		points.add(new Coord(300, 400));
		
		sides.add(new Side(0, 1));
		sides.add(new Side(1, 2));
		sides.add(new Side(2, 3));
		sides.add(new Side(0, 3));
		
		run();
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
				if (!pause) update();
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
		for (int i=0; i<points.size(); i++) {
			Coord point = points.get(i);
			point = rotate(center, point, rotateAngle);
			points.set(i, point);
		}
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
		
		for (Coord point : points) {
			g2d.fillOval(point.getX()-POINT_SIZE, point.getY()-POINT_SIZE, POINT_SIZE*2, POINT_SIZE*2);
		}
		
		for (Side side : sides) {
			Coord p1 = points.get(side.i1);
			Coord p2 = points.get(side.i2);
			g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		}
	}
	
	private Coord rotate(Coord center, Coord point, int angle) {
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
		
		double xRez = 0;
		double yRez = 0;
		switch (quadr) {
			case 1: case 3: {
				double beta = Math.asin(Math.abs(py) / distance);
				
				double gamma = Math.toRadians(90) - alfa - beta;
				
				xRez = Math.sin(gamma) * distance * signs.x;
				yRez = Math.cos(gamma) * distance * signs.y;
				break;
			}
			
			case 2: case 4: {
				double beta = Math.asin(Math.abs(px) / distance);
				
				double gamma = Math.toRadians(90) - alfa - beta;
				
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
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}