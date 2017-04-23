import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameOverPanel extends JPanel implements ActionListener {
	
	final static int WIDTH = 600;
	final static int HEIGHT = 400;
	final static int CORNER_RADIUS = 100;
	
	private Font bigFont = new Font("Clarendon BT", Font.PLAIN, 50);
	private Font medFont = new Font("Clarendon BT", Font.PLAIN, 25);
	private Font textFont = new Font("Clarendon BT", Font.PLAIN, 30);
	
	private Game game;
	private JButton b1, b2;
	private JTextField field;
	
	private int b1W = 60;
	private int b1H = 30;
	
	private int b2W = 80;
	private int b2H = 30;
	
	private int bPaddingX = 50;
	private int bPaddingY = 50;
	
	private int extraY = 60;
	
	private boolean newHighscore = false;
	
	GameOverPanel(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBounds((MainPanel.WIDTH+SidePanel.WIDTH-WIDTH)/2, (MainPanel.HEIGHT-HEIGHT)/2, WIDTH, HEIGHT);
		setOpaque(false);
		setLayout(null);
		
		prepareComponents();
	}
	
	private void prepareComponents() {
		b1 = new JButton("OK");
		b2 = new JButton("Restart");
		
		b1.addActionListener(this);
		b2.addActionListener(this);
		
		b1.setBounds(WIDTH-b1W-bPaddingX ,HEIGHT-b1H-bPaddingY, b1W, b1H);
		b2.setBounds(bPaddingX, HEIGHT-b2H-bPaddingY, b2W, b2H);
		
		add(b1);
		add(b2);
		
		field = new JTextField();
		field.setFont(textFont);
		field.setBounds(WIDTH/2-300/2, 240, 300, 40);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		//Uzzîmç loga fonu
		g2d.setColor(Color.decode("#ff9966"));
		g2d.fillRoundRect(0, 0, WIDTH, HEIGHT, CORNER_RADIUS, CORNER_RADIUS);
		
		//"Game over" teksts
		g2d.setColor(Color.black);
		g2d.setFont(bigFont);
		FontMetrics metrics = g2d.getFontMetrics();
		int textW = metrics.stringWidth("Game Over!");
		g2d.drawString("Game Over!", WIDTH/2-textW/2, 60);
		
		//"New highscore" tests
		if (newHighscore) {
			g2d.setFont(medFont);
			metrics = g2d.getFontMetrics();
			textW = metrics.stringWidth("New Highscore!");
			g2d.drawString("New Highscore!", WIDTH/2-textW/2, 100);
		}
		
		//Izvada laika reþîmu
		g2d.setFont(medFont);
		g2d.drawString("Time Mode: " + game.getTimeMode() + " min", 50, 100+extraY);
		
		//Izvada punktus
		//g2d.setFont(medFont);
		g2d.drawString("Score: " + game.getScore(), 50, 140+extraY);
	}
	
	public void checkNewHighscore() {
		int result = game.checkNewScore(game.getTimeMode());
		if (result > 0) {
			add(field);
			if (result == 2) newHighscore = true;
		}
		//extraY = (newHighscore) ? 60 : 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		remove(field);
		
		if (e.getSource() == b1) {
			game.saveNewScore(field.getText(), game.getTimeMode());
			field.setText("");
			game.goToMainMenu();
		}
		if (e.getSource() == b2) {
			game.startNewGame();
		}
	}
}
