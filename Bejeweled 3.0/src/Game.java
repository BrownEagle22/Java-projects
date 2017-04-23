import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class Game {
	
	final static int MAIN_PANEL_ID = 0;
	final static int SIDE_PANEL_ID = 1;
	final static int GAME_OVER_ID = 2;
	
	//ID, kas norâda atraðanâs vietas iekðâ JLayeredPane
	FileManager fileManager;
	
	JLayeredPane gamePane;
	
	MainPanel mainPanel;
	SidePanel sidePanel;
	MainMenu menu;
	Options options;
	TimeBar timeBar;
	GameOverPanel gameOverPanel;
	HighscorePanel highscorePanel;
	
	JFrame f;
	
	boolean running = false;
	boolean pause = false;
	
	int time = 0;
	
	String gameMode;
	int timeMode;
	
	private boolean gameRunning = false;
	
	public Game() {
		createDisplay();
		init();
		goToMainMenu();
		
		centerFrame();
		
		f.setVisible(true);
		
		run();
	}
	
	//Izveido galveno displeju
	void createDisplay() {
		f = new JFrame("Bejeweled");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());
	}
	
	//Inicializç objektus
	void init() {
		fileManager = new FileManager("Dati.txt");
		
		mainPanel = new MainPanel(this);
		sidePanel = new SidePanel(this);
		menu = new MainMenu(this);
		options = new Options(this);
		timeBar = new TimeBar(this);
		gameOverPanel = new GameOverPanel(this);
		highscorePanel = new HighscorePanel(this);
		
		gamePane = new JLayeredPane();
		gamePane.setPreferredSize(new Dimension(MainPanel.WIDTH+SidePanel.WIDTH, MainPanel.HEIGHT));
		gamePane.add(mainPanel, new Integer(MAIN_PANEL_ID));
		gamePane.add(sidePanel, new Integer(SIDE_PANEL_ID));
		//gamePane.add(gameOverPanel);
	}
	
	public void gameOver() {
		gameRunning = false;
		pause = true;
		System.out.println("GAMEOVER");
		gameOverPanel.checkNewHighscore();
		gamePane.add(gameOverPanel, new Integer(GAME_OVER_ID));
		sidePanel.disableButtons();
		menu.setContinue(false);
	}
	
	//Nospieþot Continue samaina paneïus un noòem pauzi
	public void continueGame() {
		f.remove(menu);
		f.add(gamePane, BorderLayout.CENTER);
		if (options.getGameMode().equals("Timed"))
			f.add(timeBar, BorderLayout.SOUTH);
		f.pack();
		
		centerFrame();
		
		gameRunning = true;
		pause = false;
		
		mainPanel.setRepaintAll(true);
	}
	
	//Sâk jaunu spçli
	public void startNewGame() {
		time = 0;
		f.remove(options);
		gamePane.remove(gameOverPanel);
		f.add(gamePane, BorderLayout.CENTER);
		
		menu.setContinue(true);
		sidePanel.enableButtons();
		
		gameMode = options.getGameMode();
		if (gameMode.equals("Timed")) {
			timeMode = options.getTimeMode();
			f.add(timeBar, BorderLayout.SOUTH);
			timeBar.setStartTime(timeMode*60);
			sidePanel.setShowTime(false);
		} else sidePanel.setShowTime(true);
		f.pack();
		
		centerFrame();
		
		gameRunning = true;
		pause = false;
		
		mainPanel.newGame();
	}
	
	//Samaina paneïus, lai ietu uz main menu
	public void goToMainMenu() {
		f.remove(gamePane);
		f.remove(options);
		f.remove(timeBar);
		f.remove(highscorePanel);
		
		f.add(menu, BorderLayout.CENTER);
		f.pack();
		
		centerFrame();
		
		gameRunning = false;
		pause = true;
		
		menu.repaint();
	}
	
	//Samaina paneïus uz opcijâm
	public void goToOptions() {
		f.remove(menu);
		
		f.add(options);
		f.pack();
		
		centerFrame();
		
		options.repaint();
	}
	
	public void goToHighscores() {
		f.remove(menu);
		
		f.add(highscorePanel);
		f.pack();
		
		centerFrame();
		
		highscorePanel.updateContent();
		highscorePanel.repaint();
	}
	
	public void centerFrame() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
	}
	
	void run() {
		running = true;
		int fps = 60;
		double TimePerFrame = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / TimePerFrame;
			timer += now - lastTime;
			lastTime = now;
			
			if (delta >= 1) {
				update(1000000000 / fps);
				render();
				ticks++;
				delta--;
			}
			
			if (timer >= 1000000000) {
				System.out.println("FPS: " + ticks);
				
				if (gameRunning && !pause) {
					time++;
					sidePanel.setNeedRepaint(true);
				}
				timer = 0;
				ticks = 0;
			}
			Thread.yield();
			try{ Thread.sleep(1); } catch(InterruptedException ie){};
		}
	}
	
	void update(double fpsTime) {
		if (!pause) mainPanel.update();
		
		sidePanel.setTime(time);
		
		if (!pause && gameRunning && !timeBar.isTimeOver()) timeBar.decreaseTime(fpsTime/1000000000.0);
		
		if (options.getGameMode().equals("Timed") && timeBar.isTimeOver() && gameRunning) {
			gameOver();
			System.out.println("Time Over!");
		}
	}
	
	void render() {
		sidePanel.render();
		if (!pause) mainPanel.render();
		timeBar.render();
	}
	
	public void addScore(int score) {
		sidePanel.addScore(score);
	}
	
	public int getScore() {
		return sidePanel.getScore();
	}
	
	public int checkNewScore(String timeMode) {
		return fileManager.checkNewScore(getScore(), timeMode);
	}
	
	public void saveNewScore(String name, String timeMode) {
		fileManager.writeNewScore(name, getScore(), timeMode);
	}
	
	public String getTimeMode() {
		return Integer.toString(timeMode);
	}
	
	public void resetGame() {
		sidePanel.resetGame();
		pause = false;
		time = 0;
	}
	
	public void newGame() {
		mainPanel.newGame();
		timeBar.resetTime();
	}
	
	public void togglePause() {
		pause = (pause) ? false : true;
	}
	
	public void endGame() {
		System.exit(0);
	}
	
	public String[] getFileContent(String timeMode) {
		return fileManager.getContent(timeMode);
	}
}
