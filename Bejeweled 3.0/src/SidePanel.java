import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SidePanel extends JPanel implements ActionListener {
	
	final static int WIDTH = 200;
	final static int HEIGHT = 600;
	final static int H_GAP = 10;
	final static int BOTTOM_INSET = 60;
	
	private Font font = new Font("TimesRoman", Font.BOLD, 22);
	private Font bFont = new Font("TimesRoman", Font.PLAIN, 20);
	
	private int score = 0;
	private String time = "00:00:00";
	private boolean needRepaint = false;
	private boolean pause = false;
	private boolean showTime = true;
	
	private JButton poga;
	private JButton poga2;
	private JButton poga3;
	private JButton poga4;
	
	private Container c;
	
	private Game game;
	
	public SidePanel(Game game) {
		this.game = game;
		setPreferredSize (new Dimension(WIDTH, HEIGHT));
		setBounds (0, 0, WIDTH, HEIGHT);
		setLayout(new BorderLayout());
		
		c = new Container();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		
		addButtons();
		
		add(c, BorderLayout.SOUTH);
	}
	
	public void addButtons() {
		poga = new JButton("Restart");
		poga2 = new JButton("Pause");
		poga3 = new JButton("Main menu");
		poga4 = new JButton("Exit");
		
		poga.setFont(bFont);
		poga2.setFont(bFont);
		poga3.setFont(bFont);
		poga4.setFont(bFont);
		
		poga.setAlignmentX(Component.CENTER_ALIGNMENT);
		poga2.setAlignmentX(Component.CENTER_ALIGNMENT);
		poga3.setAlignmentX(Component.CENTER_ALIGNMENT);
		poga4.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		poga.addActionListener(this);
		poga2.addActionListener(this);
		poga3.addActionListener(this);
		poga4.addActionListener(this);
		
		c.add(poga);
		c.add(Box.createVerticalStrut(H_GAP));
		c.add(poga2);
		c.add(Box.createVerticalStrut(H_GAP));
		c.add(poga3);
		c.add(Box.createVerticalStrut(H_GAP));
		c.add(poga4);
		c.add(Box.createVerticalStrut(BOTTOM_INSET));
	}
	
	public void disableButtons()  {
		poga.setEnabled(false);
		poga2.setEnabled(false);
		poga3.setEnabled(false);
		poga4.setEnabled(false);
	}
	
	public void enableButtons() {
		poga.setEnabled(true);
		poga2.setEnabled(true);
		poga3.setEnabled(true);
		poga4.setEnabled(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		g2d.setColor(Color.cyan);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		g2d.setColor(Color.orange);
		g2d.setFont(font);
		g2d.drawString("Score " + Integer.toString(score), 30, 60);
		
		if (showTime) {
			g2d.setColor(Color.black);
			g2d.drawString("Time " + time, 30, 100);
		}
		
		if (pause) {
			g2d.setColor(Color.blue);
			g2d.drawString("PAUSE!", 60, 240);
		}
	}
	
	public void render() {
		if (needRepaint) repaint();
	}
	
	public void addScore(int score) {
		this.score += score;
		needRepaint = true;
	}
	
	public int getScore() {
		return score;
	}
	
	public void resetGame() {
		score = 0;
		pause = false;
		needRepaint = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == poga) {
			newGame();
		} else if (e.getSource() == poga2) {
			game.togglePause();
			pause = (pause) ? false : true;
			repaint();
		} else if (e.getSource() == poga3) {
			game.goToMainMenu();
		} else if (e.getSource() == poga4) {
			game.endGame();
		}
	}
	
	public void newGame() {
		game.newGame();
	}
	
	public void setNeedRepaint(boolean needRepaint) {
		this.needRepaint = needRepaint;
	}
	
	public void setTime(int time) {
		String sec = Integer.toString(time % 60);
		String min = Integer.toString((time/60)%60);
		String h = Integer.toString(time / 3600);
		
		if (Integer.valueOf(sec) < 10) sec = "0" + sec;
		if (Integer.valueOf(min) < 10) min = "0" + min;
		if (Integer.valueOf(h) < 10) h = "0" + h;
		
		this.time = h + ":" + min + ":" + sec;
	}
	
	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}
}
