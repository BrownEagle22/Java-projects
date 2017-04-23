import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class TimeBar extends JPanel {
	final static int WIDTH = SidePanel.WIDTH + MainPanel.WIDTH;
	final static int HEIGHT = 50;
	
	final static int PADDING_X = 10;
	final static int PADDING_Y = 10;
	
	final static int BAR_WIDTH = WIDTH-2*PADDING_X;
	final static int BAR_HEIGHT = HEIGHT-2*PADDING_Y;
	
	double percents;
	
	double time = 0;
	int startTime;
	
	private boolean needRepaint = false;
	private boolean timeOver = false;
	
	Game game;
	
	BufferedImage sourceBar, bar = null;
	
	TimeBar(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		percents = 1;
		
		drawBarImg();
		cropNewImg();
	}
	
	public void render() {
		if (needRepaint) {
			repaint();
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		//Uzzîmç fonu
		g2d.setColor(Color.decode("#b3daff"));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		if (!timeOver) g2d.drawImage(bar, PADDING_X, PADDING_Y, null);
		
		g2d.setColor(Color.magenta);
		g2d.setStroke(new BasicStroke(4));
		g2d.drawRoundRect(PADDING_X, PADDING_Y, WIDTH-2*PADDING_X, HEIGHT-2*PADDING_Y, 40, 200);
	}
	
	public void drawBarImg() {
		sourceBar = new BufferedImage(BAR_WIDTH, BAR_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics g = sourceBar.createGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.blue);
		g2d.fillRoundRect(0, 0, WIDTH-2*PADDING_X, HEIGHT-2*PADDING_Y, 40, 200);
		g2d.dispose();
	}
	
	public void cropNewImg() {
		int newWidth = (int) Math.round((double)BAR_WIDTH*percents);
		if (newWidth == 0) newWidth = 1;
		bar = new BufferedImage(BAR_WIDTH, BAR_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		bar = sourceBar.getSubimage(0, 0, newWidth, BAR_HEIGHT);
		
		//percents -= 0.05;
	}
	
	public void setNeedRepaint(boolean needRepaint) {
		this.needRepaint = needRepaint;
	}
	
	public void setStartTime(int time) {
		this.time = time;
		this.startTime = time;
		timeOver = false;
	}
	
	public void resetTime() {
		this.time = this.startTime;
		timeOver = false;
	}
	
	public void decreaseTime(double timeUpdate) {
		time -= timeUpdate;
		percents = (double)time / (double)startTime;
		
		if (time<=0) {
			time = 0;
			percents = 1;
			timeOver = true;
		} else cropNewImg();
		
		repaint();
	}
	
	public boolean isTimeOver() {
		return timeOver;
	}
	
	public void setTimeOver(boolean timeOver) {
		this.timeOver = timeOver;
	}
}
