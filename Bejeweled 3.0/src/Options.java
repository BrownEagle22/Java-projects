import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class Options extends JPanel implements ActionListener {
	
	final static int WIDTH = MainPanel.WIDTH + SidePanel.WIDTH;
	final static int HEIGHT = MainPanel.HEIGHT;
	final static int PADDING_X = 50;
	final static int PADDING_Y = 50;
	final static int CORNER_RADIUS = 40;
	final static int TIME_B_Y = HEIGHT/2-40 + 75;
	private final Font FONT = new Font("Clarendon BT", Font.PLAIN, 30);
	private final Font FONT2 = new Font("Clarendon BT", Font.PLAIN, 50);
	private final Font FONT3 = new Font("Clarendon BT", Font.PLAIN, 25);
	
	Game game;
	
	Poga returnButton, playButton;
	ModeButton infiniteB, timedB, chosenMode;
	ModeButton chosenTime;
	ModeButton[] timeButtons;
	
	Options(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(null);
		
		initButtons();
	}
	
	void initButtons() {
	//RETURN un PLAY pogas
		returnButton = new Poga("Return", FONT);
		playButton = new Poga("Play", FONT);
		
		returnButton.addActionListener(this);
		playButton.addActionListener(this);
		
		returnButton.setPosition(PADDING_X+30, HEIGHT-PADDING_Y-returnButton.buttonH-30);
		playButton.setPosition(WIDTH-PADDING_X-returnButton.buttonW-10, HEIGHT-PADDING_Y-returnButton.buttonH-30);
		
		add(returnButton);
		add(playButton);
		
		
	//REÞÎMU pogas
		infiniteB = new ModeButton("Infinite");
		timedB = new ModeButton("Timed");
		
		infiniteB.setFont(FONT);
		timedB.setFont(FONT);
		
		infiniteB.addActionListener(this);
		timedB.addActionListener(this);
		
		infiniteB.setBounds(WIDTH/2-160-10, 170, 160, 80);
		timedB.setBounds(WIDTH/2+10, 170, 160, 80);
		
		add(infiniteB);
		add(timedB);
		
		infiniteB.setActivated(true);
		chosenMode = infiniteB;
		
		
	//LAIKA pogas
		timeButtons = new ModeButton[5];
		String laiki[] = {"1", "2", "5", "10", "30"};
		
		int width = 30;
		int xPos = PADDING_X+170;
		for (int i=0; i<timeButtons.length; i++) {
			if (i == 3) width = 42;
			
			timeButtons[i] = new ModeButton(laiki[i]);
			timeButtons[i].setFont(FONT3);
			timeButtons[i].addActionListener(this);
			timeButtons[i].setBounds(xPos, TIME_B_Y, width, 30);
			add(timeButtons[i]);
			timeButtons[i].setVisible(false);
			
			xPos += width;
		}
		
		chosenTime = timeButtons[0];
		chosenTime.setActivated(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);   //Padara sturus ne tik robainus
		
		//FONS
		g2d.setColor(Color.decode("#ffdd99"));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		//OPTIONS LOGS
		g2d.setColor(Color.decode("#e67300"));
		g2d.fillRoundRect(PADDING_X, PADDING_Y, WIDTH-2*PADDING_X, HEIGHT-2*PADDING_Y, CORNER_RADIUS, CORNER_RADIUS);
		
		//VIRSRAKSTS
		g2d.setColor(Color.black);
		g2d.setFont(FONT2);
		FontMetrics metrics = g2d.getFontMetrics();
		int fontH = metrics.getHeight();
		int fontW = metrics.stringWidth("Choose game mode");
		g2d.drawString("Choose game mode", WIDTH/2 - fontW/2, PADDING_Y + fontH);
		
		//MINUTES teksts
		if (chosenMode == timedB) {
			g2d.setColor(Color.black);
			g2d.setFont(FONT3);
			g2d.drawString("Minutes:", PADDING_X+50, HEIGHT/2-40 + 100);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == returnButton) {
			game.goToMainMenu();
			changeTimeMode(timeButtons[0]);
		}
		
		if (e.getSource() == playButton) {
			game.startNewGame();
			changeTimeMode(timeButtons[0]);
		}
		
		if (e.getSource() == infiniteB)
			changeMode(infiniteB);
		
		if (e.getSource() == timedB)
			changeMode(timedB);
		
		for (int i=0; i<timeButtons.length; i++)
			if (e.getSource() == timeButtons[i])
				changeTimeMode(timeButtons[i]);
	}
	
 	public void changeMode(ModeButton button) {
 		chosenMode.setActivated(false);
 		chosenMode.repaint();
 		
 		button.setActivated(true);
 		button.repaint();
 		chosenMode = button;
 		
 		boolean timeEnabled = (button == infiniteB) ? false : true;
 		for (int i=0; i<timeButtons.length; i++)
 			timeButtons[i].setVisible(timeEnabled);
 		
 		repaint();
 	}
 	
 	public void changeTimeMode(ModeButton button) {
 		chosenTime.setActivated(false);
 		chosenTime.repaint();
 		
 		button.setActivated(true);
 		button.repaint();
 		chosenTime = button;
 	}
 	
 	public String getGameMode() {
 		return chosenMode.getText();
 	}
 	
 	public int getTimeMode() {
 		return Integer.valueOf(chosenTime.getText());
 	}
}
