import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.omg.PortableServer.POAManagerPackage.State;

public class MainPanel extends JPanel {
	
	final static int WIDTH = 600;
	final static int HEIGHT = 600;
	final static int ROW_COUNT = 8;
	final static int ROW_WIDTH = WIDTH / ROW_COUNT;
	final static int SWAP_SPEED = 5;
	final static int FALL_SPEED = 12;
	
	final static float ROTATION_SPEED = 1;
	
	SpriteSheet sheet;
	
	Game game;
	
	Status status = Status.INACTIVE;
	
	Tile[][] tiles = new Tile[8][8];
	
	private boolean checkForSwapBack = false;
	private boolean needRepaint = false;
	private boolean rotate = false;
	private boolean repaintAll = false;
	
	public MainPanel(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBounds(SidePanel.WIDTH, 0, WIDTH, HEIGHT);
		setLayout(null);
		
		sheet = new SpriteSheet();
	}
	
//UPDATES PROGRAM AT EVERY FRAME
	public void update() {
		if (rotate){
			rotateTiles();
			needRepaint = true;
		}
		
		switch(status) {
			case SWAPPING:
			case MOVING: {
				moveAllTiles();
				needRepaint = true;
				break;
			}
			case DECREASING: {
				decreaseAllTiles();
				needRepaint = true;
				break;
			}
			case FALL: {
				startDropDown();
				addNewTiles();
			}
		}
	}
	
	public void render() {
		if (repaintAll) {
			repaint();
			repaintAll = false;
		}
		
		if (needRepaint) {
			repaintTiles();
			needRepaint = false;
		}
	}
	
	
//-------------------------------------GUI------------------------------------------
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.decode("#00cc44"));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.black);
		drawGrid(g);
		
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j] != null)
					if (tiles[i][j].isSelected())
						drawSelection(g, i*ROW_WIDTH, j*ROW_WIDTH);
	}
	
	//////Uzzime rutinas
	public void drawGrid(Graphics g) {
		for (int i=ROW_WIDTH; i<WIDTH; i+=ROW_WIDTH) {
			g.drawLine(0, i, WIDTH, i);
		}
		for (int i=0; i<WIDTH; i+=ROW_WIDTH) {  //uzzime ari atdaloso liniju starp sidepanel un mainpanel
			g.drawLine(i, 0, i, WIDTH);
		}
	}
	
	//////Uzzime izvelesanas dizainu
	public void drawSelection(Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		int lineLen = ROW_WIDTH / 3;
		
		g2d.setColor(Color.decode("#ff6666"));
		g2d.fillRect(x, y, ROW_WIDTH, ROW_WIDTH);
		
		g2d.setColor(Color.decode("#3399ff"));
		g2d.setStroke(new BasicStroke(4));
		
		g2d.drawLine(x, y, x+lineLen, y);
		g2d.drawLine(x, y, x, y+lineLen);
		
		g2d.drawLine(x+ROW_WIDTH, y, x+ROW_WIDTH-lineLen, y);
		g2d.drawLine(x+ROW_WIDTH, y, x+ROW_WIDTH, y+lineLen);
		
		g2d.drawLine(x, y+ROW_WIDTH, x+lineLen, y+ROW_WIDTH);
		g2d.drawLine(x, y+ROW_WIDTH, x, y+ROW_WIDTH-lineLen);
		
		g2d.drawLine(x+ROW_WIDTH, y+ROW_WIDTH, x+ROW_WIDTH-lineLen, y+ROW_WIDTH);
		g2d.drawLine(x+ROW_WIDTH, y+ROW_WIDTH, x+ROW_WIDTH, y+ROW_WIDTH-lineLen);
	}
	
	//////parkraso visas figuras
	public void repaintTiles() {
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (tiles[i][j] != null)
					tiles[i][j].repaint();
			}
		}
	}
//-------------------------------------GUI-----------------------------------------
	
//---------------------------JAUNU LAUKU IZVEIDOÐANA--------------------------------
	public BufferedImage[] getRandomImage() {
		int rand = (int)Math.round(Math.random()*4);
		BufferedImage[] img = null;
		
		switch(rand) {
		case 0: img = sheet.getRedShape();
				break;
		case 1: img = sheet.getBlueShape();
				break;
		case 2: img = sheet.getPurpleShape();
				break;
		case 3: img = sheet.getOrangeShape();
				break;
		case 4: img = sheet.getGreenShape();
				break;
		}
		
		return img;
	}
	
	public void addNewTiles() {
		int counter = 1;
		
		for (int i=0; i<8; i++) {
			counter = 1;
			for (int j=7; j>=0; j--) {
				if(tiles[i][j] == null) {
					tiles[i][j] = new Tile(getRandomImage(), i*ROW_WIDTH, -counter*ROW_WIDTH, this);
					add(tiles[i][j]);
					tiles[i][j].startMoving(0, (j+counter)*ROW_WIDTH, FALL_SPEED);
					counter++;
					status = Status.MOVING;
				}
			}
		}
	}
//---------------------------JAUNU LAUKU IZVEIDOÐANA--------------------------------
	
	
	//ONCLICK
	public void tileClick(Tile tile) {
		Tile selectTile = getSelectedTile();
		if (status == Status.INACTIVE) {
			if (selectTile != null) {
				if (canSwap(tile)) {
					checkForSwapBack = true;
					startSwapping(tile, selectTile);
				} else {
					selectTile.setSelected(false);
					needRepaint = true;
				}
			} else {
				tile.setRotation(true);
				rotate = true;
				tile.setSelected(true);
			}
		}
	}
	
	//KAD VELK FIGÛRU
	public void dragged(Tile tile, int iDif, int jDif)  {
		if (status == Status.INACTIVE) {
			int[] coord = getTileCoord(tile);
			int i = coord[0];
			int j = coord[1];
			
			checkForSwapBack = true;
			startSwapping(tiles[i+iDif][j+jDif], tiles[i][j]);
		}
	}
	
	//LAUKU ROTÂCIJA
	public void rotateTiles() {
		rotate = false;
		
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j].isRotating()) {
					tiles[i][j].updateImageNumber();
					rotate = true;
				}
	}
	
	//KUSTINA LAUKUS
	public void moveAllTiles() {
		boolean endMoving = true;
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (tiles[i][j] != null)
					if (tiles[i][j].isMoving()) {
						tiles[i][j].move();
						if (tiles[i][j].isMoving()) endMoving = false;
					}
			}
		}
		
		if (endMoving) {
			matchAllTiles();
		}//ja visas figûras ir pabeiguðas kustîbas, tad skatâs vai sakrît figûras
	}
	

//------------------------------------SWAPPING-----------------------------------
	/////Pârbauda, vai blakus atrodas jau iezîmçta figûra
	public boolean canSwap(Tile tile) {
		int[] coord = getTileCoord(tile);
		int x = coord[0];
		int y = coord[1];
		
		int[] coord2 = getTileCoord(getSelectedTile());
		int x2 = coord2[0];
		int y2 = coord2[1];
		
		if (x == x2) {
			if (y == y2-1 || y == y2+1) {
				return true;
			}
		}
		if (y == y2) {
			if (x == x2-1 || x == x2+1) {
				return true;
			}
		}
		
		return false;
	}
	
	//Uzstâda figûru kustîbu galapunktus, kâ arî masîvâ samaina figûras ar vietâm
	public void startSwapping(Tile tile1, Tile tile2) {
		
		status = Status.SWAPPING;
		
		tile1.setSelected(true);
		tile2.setSelected(true);
		
		int xDif1 = tile2.getX() - tile1.getX();
		int yDif1 = tile2.getY() - tile1.getY();
		
		int xDif2 = -xDif1;
		int yDif2 = -yDif1;
		
		tile1.startMoving(xDif1, yDif1, SWAP_SPEED);
		tile2.startMoving(xDif2, yDif2, SWAP_SPEED);
		
		int[] coord1 = getTileCoord(tile1);
		int[] coord2 = getTileCoord(tile2);
		
		Tile memo = tiles[ coord1[0] ][ coord1[1] ];
		tiles[ coord1[0] ][ coord1[1] ] = tiles[ coord2[0] ][ coord2[1] ];
		tiles[ coord2[0] ][ coord2[1] ] = memo;
		
	}
	
	//Swappo figûras, kuras ir iezîmçtas
	public void swapBack() {
		System.out.println("Swapping back");
		checkForSwapBack = false;
		
		Tile swapTiles[] = new Tile[2];
		int n = 0;
		
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (tiles[i][j].isSelected() && n < 2) {
					swapTiles[n] = tiles[i][j];
					n++;
				}
			}
		}
		
		startSwapping(swapTiles[0], swapTiles[1]);
	}
//------------------------------------SWAPPING-----------------------------------
	
//------------------------------------MATCHING-----------------------------------
	public void matchAllTiles() {
		int n=1;
		int n2=1;
		int count = 0;
		boolean action = false;
		for (int i=0; i<8; i++) {
			for (int j=0; j<7; j++) {  //ir 7 nevis 8, jo pedejaa figura nevar apskatit nakamo
				
				if (tiles[i][j].img == tiles[i][j+1].img) {
					n++;
				} else {
					if (n > 2) {
						for (int z=j; z>j-n; z--) {
							tiles[i][z].setDecreasing(true);
							action = true;
						}
						count += n;
					}
					n=1;
				}
				//ja figura sakrit ar nakamo, tad palielina skaitu,
				//savadak(ja nesakrit) atzimee noteikto skaitu figuras sakot ar pedejo apskatito
				//pec tam skaitu uzliek atpakal uz 1
				//////////////////KOLONNAS////////////////////////
				
				if (tiles[j][i].img == tiles[j+1][i].img)
					n2++;
				else {
					if (n2 > 2) {
						for (int z=j; z>j-n2; z--) {
							tiles[z][i].setDecreasing(true);
							action = true;
						}
						count += n2;
					}
					n2=1;
				}
				//Tas tas kas iepriekð, bet rindas ar kolonam ir samainitas
				//////////////////RINDAS////////////////////////
				
				if (j==6) {
					if (n>2) {
						for (int z=j+1; z>j+1-n; z--) {
							tiles[i][z].setDecreasing(true);
							action = true;
						}
						count += n;
					}
					n=1;
				////////////////KOLONNAI
					if (n2>2) {
						for (int z=j+1; z>j+1-n2; z--) {
							tiles[z][i].setDecreasing(true);
							action = true;
						}
						count += n2;
					}
					n2=1;
				////////////////RINDAI
				}
				//Ja tiek apskatita pirmsPedeja figura, tad, ja ir vairak ka 2 atrasti rindaa
				//tad atzime sakot no pedejas uz atpakalu noteiktu skaitu
				//Jebkura gadijuma skaitu uzliek atpakal uz 1, jo ies nakamais cikls
			}
		}
		
		boolean stillSwapping = false;
			
		if (status == Status.SWAPPING) {
			if (checkForSwapBack && !action) {
				swapBack();
				stillSwapping = true;
			} else {
				getSelectedTile().setSelected(false);
				getSelectedTile().setSelected(false);
			}
		}
		
		if (!stillSwapping) status = (action) ? Status.DECREASING : Status.INACTIVE;
		
		if (count > 0) addScore(count);
	}
	
	//Samazina kâ arî noòem attiecîgâs figûras
	public void decreaseAllTiles() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++) {
				if (tiles[i][j].needRemove()) {
					remove(tiles[i][j]);
					tiles[i][j] = null;
					status = Status.FALL;
					continue;
				}
				if (tiles[i][j].isDecreasing())
					tiles[i][j].decrease();
			}
	}
//------------------------------------MATCHING-----------------------------------
	
	
	//UZSTÂDA KRIÐANAS KOORDINÂTAS
	public void startDropDown() {
		for (int i=0; i<8; i++)
			for (int j=6; j>=0; j--) {
				if (tiles[i][j] != null) {
					
					int ySteps = 0;
					int z=j+1;
					while(z < 8 && tiles[i][z] == null) {
						ySteps++;
						z++;
					}
					//Skaita cik daudz bus jakrit uz leju
					//To dara ciklaa ejot uz leju, kamer uziet kadai figurai vai beigam
					//Katrs solis tiek skaitits
					
					
					if (ySteps > 0) {    //Ja var krist uz leju...
						tiles[i][j].startMoving(0, ySteps*ROW_WIDTH, FALL_SPEED);
						//Uzsak figuras kustibu noteiktu solu garumaa
						
						tiles[i][j+ySteps] = tiles[i][j];
						tiles[i][j] = null;
						//Jau sakumaa updato masivu, noliekot figuru galapunktaa
					}
					
				}
			}
	}
	
	
//----------------------------ATGRIEÞ INFORMÂCIJU PAR LAUKIEM-----------------------------
	/////Atgrieþ pirmo atrasto iezîmçto figûru, ja tâda ir
	public Tile getSelectedTile() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j].isSelected())
					return tiles[i][j];
		return null;
	}
	
	/////Atgrieþ figûras koordinâtas masîvâ
	public int[] getTileCoord(Tile tile) {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j] == tile)
					return new int[] {i, j};
		return null;
	}
//----------------------------ATGRIEÞ INFORMÂCIJU PAR LAUKIEM-----------------------------
	
//----------------------------IZVADA INFORMÂCIJU PAR LAUKIEM-----------------------------
	public void outputTiles() {
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++)
				if (tiles[j][i] == null)
					System.out.print("O ");
				else System.out.print("(" + tiles[j][i].getX() + "; " + tiles[j][i].getY() + ")");
			System.out.println();
		}
	}
	
	public void outputCombinations() {
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (tiles[j][i].isDecreasing())
					System.out.print("1 ");
				else System.out.print("0 ");
			}
			System.out.println();
		}
	}
//----------------------------IZVADA INFORMÂCIJU PAR LAUKIEM-----------------------------

	public int pow(int baze, int pak) {
		int rez = 1;
		for (int i=0; i<pak; i++)
			rez*=baze;
		return rez;
	}
	
	public void addScore(int count) {
		int score = 0;
		
		for (int z=1; z<=count-3; z++)
			score += 10 + 10*z;
		score += 30;
		
		game.addScore(score);
	}
	
	public void newGame() {
		status = Status.INACTIVE;
		
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				if (tiles[i][j] != null) {
					remove(tiles[i][j]);
					tiles[i][j] = null;
				}
			}
		}
		
		addNewTiles();
		repaintAll = true;
		needRepaint = true;
		
		game.resetGame();
	}
	
	public void setRepaintAll(boolean repaint) {
		this.repaintAll = repaint;
	}
}
