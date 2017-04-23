import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.ietf.jgss.Oid;

public class Game {
	private int rows = 10;
	private int points = 0;
	
	private boolean running;
	private boolean waitingForUpdate;
	private boolean isPaused;
	private boolean isGameOver;
	private boolean hasExtraSpeed;
	
	private float gameSpeed = 1f;
	
	private ArrayList<Tile> snake;
	private Point appleCoords;
	
	private Board board;
	
	public Game() {
		init();
		newGame();
		run();
	}
	
	//Izveido displeju, keyboardlistener
	private void init() {
		JFrame frame = new JFrame("Snake");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		board = new Board(rows);
		frame.add(board);
		
		frame.pack();
		frame.setVisible(true);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2 - frame.getWidth()/2, dim.height/2 - frame.getHeight()/2);
	//Izveido, inicializç displeju
		
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameSpeed /= 2;
					hasExtraSpeed = false;
					System.out.println("Released");
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (!waitingForUpdate && !isPaused) {
					if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
						snake.get(0).setVel(0, -1);
						waitingForUpdate = true;
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
						snake.get(0).setVel(1, 0);
						waitingForUpdate = true;
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
						snake.get(0).setVel(0, 1);
						waitingForUpdate = true;
					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
						snake.get(0).setVel(-1, 0);
						waitingForUpdate = true;
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (!hasExtraSpeed) {
						gameSpeed *= 2;
						hasExtraSpeed = true;
						System.out.println("Pressed");
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_R) {
					if (!isPaused) {
						isGameOver = true;
						newGame();
					}
				}
				
				if (e.getKeyCode() == KeyEvent.VK_P) {
					isPaused = (isPaused) ? false : true;
					board.togglePaused();
				}
			}
		});
		//Uz bultiòas nospieðanu uzliek èûskas galvai attiecîgo kustîbas virzienu
	}
	
	//Izveido jaunu èûsku un âbolu
	private void newGame() {
		String text;
		do {
			text = JOptionPane.showInputDialog("Row count: ", "10");
		} while(!isNumber(text) || text.equals("0") || text.equals("1") || text.equals("2"));
		rows = Integer.valueOf(text);
		
		board.setRows(rows);
		board.reset();
		
		isGameOver = false;
		points = 0;
		gameSpeed = 1f;
		hasExtraSpeed = false;
		
		snake = new ArrayList<>();
		snake.add(new Tile(2, 0, 1, 0, rows));
		addSnakeTile();
		addSnakeTile();
		
		addNewApple();
		
		render();
	}
	
	private void run() {
		running = true;
		int fps = 30;
		double renderTimePerFrame = 1000000000 / fps;
		int ticksPerSecond = 3;
		double tickTimePerFrame = 1000000000 / ticksPerSecond;
		double delta = 0;
		double tickDelta = 0;
		long now;
		long lastTime = System.nanoTime();
		int tickCount = 0;
		int fpsCount = 0;
		long timer = 0;
		
		while (running) {
			now = System.nanoTime();
			tickDelta += (now - lastTime) / tickTimePerFrame * gameSpeed;
			delta += (now - lastTime) / renderTimePerFrame;
			timer += now - lastTime;
			lastTime = now;
			
			if (tickDelta >= 1) {
				if (!isPaused && !isGameOver) {
					update();
				}
				tickDelta--;
				tickCount++;
			}
			
			if (delta >= 1) {
				render();
				delta--;
				fpsCount++;
			}
			
			if (timer >= 1000000000) {
				System.out.println("ticks: " + tickCount + "; FPS: " + fpsCount);
				tickCount = 0;
				fpsCount = 0;
				timer = 0;
			}
		}
	}
	
	private void update() {
		for (Tile t : snake) {
			t.move();
		}
		//Kustina katru gabalu
		
		for (int i=snake.size()-1; i>0; i--) {
			Point vel = snake.get(i-1).getVel();
			snake.get(i).setVel(vel.x, vel.y);
		}
		//Sâkot no galvas (neieskaitot) lîdz astei katrs gabals pieòem nâkamâ gabala kustîbas virzienu
		
		Point headCoord = snake.get(0).getCoord();
		if (headCoord.x == appleCoords.x && headCoord.y == appleCoords.y) {
			
			addSnakeTile();
			points++;
			
			if (snake.size() >= rows * rows) {
				appleCoords = null;
				render();
				
				isGameOver = true;
				
				String text = "You got all possible points: " + points + '\n' + "Do you want to play again?";
				int answer = JOptionPane.showConfirmDialog(null, text, "You Win!", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.OK_OPTION) {
					newGame();
				}
				
				return;
			}
			
			addNewApple();
			
			gameSpeed += (hasExtraSpeed) ? 0.2f : 0.1f;
		}
		//Ja èûskas galva uziet uz âbola, tâ vieta nomainâs un èûska pagarinâs
		
		for (Tile tile : snake) {
			if (tile != snake.get(0)) {
				
				Point coord1 = tile.getCoord();
				Point coord2 = snake.get(0).getCoord();
				
				if (coord1.x == coord2.x && coord1.y == coord2.y) {
					board.setCollisionCoord(new Point(coord1.x, coord2.y));
					render();
					isGameOver = true;
					
					String text = "Your points: " + points + '\n' + "Do you want to play again?";
					int answer = JOptionPane.showConfirmDialog(null, text, "Game Over!", JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.OK_OPTION) {
						newGame();
					}
					
					return;
				}
			}
		}
		//Pârbauda, vai èûska ir ieskrçjusi sevî
		//Ja ir tad uzzîmç krustu attiecîgajâ vietâ un beidzas spçle
		
		waitingForUpdate = false;
		System.out.println(gameSpeed);
	}
	
	private void render() {
		setBoardTiles();
		board.setAppleCoord(appleCoords);
		board.repaint();
	}
	
	//Iedod board objektam sarakstu ar koordinâtâm, kuras vajag aizpildît
	//Ðo sarakstu dabû no snake lista
	private void setBoardTiles() {
		ArrayList<Point> tileCoords = new ArrayList<>();
		
		for (Tile tile : snake) {
			Point coord = tile.getCoord();
			tileCoords.add(coord);
		}
		
		board.setPaintingTiles(tileCoords);
	}
	
	//Pievieno snake lista beigâm jaunu gabalu
	//Jaunâ gabala kustîbas virziens ir kâ nâkamajam
	private void addSnakeTile() {
		int size = snake.size();
		Tile nextTile = snake.get(size-1);
		
		Point nextCoord = nextTile.getCoord();
		Point nextVel = nextTile.getVel();
		
		int xNew = nextCoord.x - nextVel.x;
		int yNew = nextCoord.y - nextVel.y;
		
		Tile newTile = new Tile(xNew, yNew, nextVel.x, nextVel.y, rows);
		snake.add(newTile);
	}
	
	//Nomaina âbola atraðanâs vietu
	private void addNewApple() {
		Random rand = new Random();
		boolean validCoords = true;
		int x, y;
		
		do {
			validCoords = true;
			x = rand.nextInt(rows);
			y = rand.nextInt(rows);
			
			for (Tile tile : snake) {
				Point coord = tile.getCoord();
				if (coord.x == x && coord.y == y) {
					validCoords = false;
				}
			}
			System.out.println("(" + x + "; " + y + ")");
		} while(!validCoords);
		
		appleCoords = new Point(x, y);
	}
	
	private boolean isNumber (String text) {
		if (text == null) return false;
		
		boolean flag = false;
		char[] numbers = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		for (int i=0; i<text.length(); i++) {
			for(int j=0; j<numbers.length; j++) {
				if (numbers[j] == text.charAt(i)) {
					flag = true;
				}
			}
			if (!flag) return false;
		}
		return true;
	}
}