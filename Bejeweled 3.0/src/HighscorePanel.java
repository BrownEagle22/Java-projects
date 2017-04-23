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
import javax.swing.JTabbedPane;

public class HighscorePanel extends JPanel implements ActionListener{
	
	final static int WIDTH = MainMenu.WIDTH;
	final static int HEIGHT = MainMenu.HEIGHT;
	
	Font font = new Font("Clarendon BT", Font.PLAIN, 35);
	Font font2 = new Font("Clarendon BT", Font.PLAIN, 25);
	Font font3 = new Font("Clarendon BT", Font.BOLD, 50);
	
	Game game;
	
	ModeButton[] timeButtons;
	ModeButton chosenButton;
	
	String[] names, scores;
	
	int timeButtonY = 490;
	
	int startY = 200;
	int startX = 250;
	int numX = 0;
	int nameX = 40;
	int scoreX = 220;
	
	Poga poga;
	
	public HighscorePanel(Game game) {
		this.game = game;
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		names = new String[5];
		scores = new String[5];
		
		prepareButtons();
		updateContent();
	}
	
	private void prepareButtons() {
		poga = new Poga("Return", font);
		poga.setPosition(WIDTH-poga.buttonW-60, HEIGHT-poga.buttonH-60);
		poga.addActionListener(this);
		add(poga);
		
		timeButtons = new ModeButton[5];
		String laiki[] = {"1", "2", "5", "10", "30"};
		
		int width = 30;
		int xPos = /*PADDING_X+*/250;
		for (int i=0; i<timeButtons.length; i++) {
			if (i == 3) width = 42;
			
			timeButtons[i] = new ModeButton(laiki[i]);
			timeButtons[i].setFont(font2);
			timeButtons[i].addActionListener(this);
			timeButtons[i].setBounds(xPos, timeButtonY, width, 30);
			add(timeButtons[i]);
			
			xPos += width;
		}
		
		chosenButton = timeButtons[0];
		chosenButton.setActivated(true);
		
		
	}
	
	public void updateContent() {
		String[] data = game.getFileContent(chosenButton.getText());
		for (int i=0; i<5; i++) {
			int index = data[i].indexOf(".") + 1;
			int index2 = data[i].indexOf("-->") + 3;
			
			names[i] = data[i].substring(index, index2-3);
			scores[i] = data[i].substring(index2, data[i].length());
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);
		
		g2d.setColor(Color.decode("#ffdd99"));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		g2d.setColor(Color.black);
		g2d.setFont(font3);
		FontMetrics metrics = g2d.getFontMetrics();
		int textW = metrics.stringWidth("Highscores");
		g2d.drawString("Highscores", WIDTH/2-textW/2, 80);
		
		g2d.setFont(font2);
		g2d.drawString("TimeMode(min):", 30, HEIGHT-85);
		
		int yPos = startY;
		for (int i=0; i<5; i++) {
			g2d.drawString((i+1)+".", startX+numX, yPos);
			g2d.drawString(names[i], startX+nameX, yPos);
			g2d.drawString(scores[i], startX+scoreX, yPos);
			
			yPos += 40;
		}
	}
	
	public void changeTimeMode(ModeButton button) {
		chosenButton.setActivated(false);
		chosenButton.repaint();
 		
 		button.setActivated(true);
 		button.repaint();
 		chosenButton = button;
 	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == poga) {
			game.goToMainMenu();
			changeTimeMode(timeButtons[0]);
		}
		for (int i=0; i<timeButtons.length; i++) {
			if (e.getSource() == timeButtons[i]){
				changeTimeMode(timeButtons[i]);
				updateContent();
				repaint();
			}
		}
	}
}
