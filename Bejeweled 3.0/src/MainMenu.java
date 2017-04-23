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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MainMenu extends JPanel implements ActionListener {
	
	final static int WIDTH = MainPanel.WIDTH + SidePanel.WIDTH;
	final static int HEIGHT = MainPanel.HEIGHT;
	final static int H_GAP = 20;
	final static int UPPER_INSET = 250;
	
	Font font = new Font("Calibri", Font.BOLD, 80);
	
	private final Font FONT = new Font("Clarendon BT", Font.PLAIN, 35);
	
	private Poga startButton, recordButton, exitButton, continueButton;
	
	Game game;
	
	Container c;
	
	MainMenu(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new BorderLayout());
		
		c = new Container();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		//c.setLayout(new GridBagLayout());
		
		addButtons();
		
		add(c, BorderLayout.CENTER);
	}
	
	public void addButtons()  {
		continueButton = new Poga("Continue", FONT);
		startButton = new Poga("New game", FONT);
		recordButton = new Poga("Highscore", FONT);
		exitButton = new Poga("Exit", FONT);
		
		continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		recordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		continueButton.addActionListener(this);
		startButton.addActionListener(this);
		recordButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		c.add(Box.createVerticalStrut(UPPER_INSET));
		c.add(continueButton);
		c.add(Box.createVerticalStrut(H_GAP));
		c.add(startButton);
		c.add(Box.createVerticalStrut(H_GAP));
		c.add(recordButton);
		c.add(Box.createVerticalStrut(H_GAP));
		c.add(exitButton);
		
		continueButton.setVisible(false);
	}
	
	public void setContinue(boolean bool) {
		continueButton.setVisible(bool);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		g2d.setColor(Color.decode("#ffdd99"));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		g2d.setColor(Color.decode("#73e600"));
		g2d.setFont(font);
		g2d.drawString("BEJEWELED", 200, 130);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {
			game.goToOptions();;
		} else if (e.getSource() == recordButton) {
			game.goToHighscores();
		} else if (e.getSource() == exitButton) {
			game.endGame();
		} else if (e.getSource() == continueButton) {
			game.continueGame();
		}
	}
}
