import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	BufferedImage spriteSheet = null;;
	
	private BufferedImage[] sprites0 = new BufferedImage[1];
	private BufferedImage[] sprites1 = new BufferedImage[1];
	private BufferedImage[] sprites2 = new BufferedImage[14];
	private BufferedImage[] sprites3 = new BufferedImage[12];
	private BufferedImage[] sprites4 = new BufferedImage[48];
	
	private BufferedImage[] redShape = new BufferedImage[15];
	private BufferedImage[] greenShape = new BufferedImage[15];
	private BufferedImage[] blueShape = new BufferedImage[15];
	private BufferedImage[] purpleShape = new BufferedImage[15];
	private BufferedImage[] orangeShape = new BufferedImage[15];
	
	public SpriteSheet() {
		try {
			spriteSheet = ImageIO.read(SpriteSheet.class.getResource("Sprite sheet.png"));
		} catch (IOException e) {
			System.out.println("Kluda! Neizdevas ieladet sprite sheet!");
		}
		
		cropSprites(sprites0, 6, 0, 118, 128, 1, 1);
		cropSprites(sprites1, 130, 0, 108, 115, 1, 1);
		cropSprites(sprites2, 0, 128, 228, 805, 2, 7);
		cropSprites(sprites3, 244, 0, 677, 226, 6, 2);
		cropSprites(sprites4, 232, 226, 788, 790, 7, 7);
		
		groupSprites();
	}
/////SPRITE ZERO 118x128 (koord 6; 0) (1x1)
/////SPRITE ONE 108x115 (koord 130; 0) (1x1)
/////SPRITE TWO 228x805 (koord 0; 128) (2x7)
/////SPRITE THREE 677x226 (koord 244; 0) (6x2)
/////SPRITE FOUR 788x790 (koord 232; 226) (7x7)
	
	public void cropSprites(BufferedImage[] img, int x, int y, int width, int height, int col, int row) {
		int n=0;
		
		for (int i=0; i<row; i++) {
			for (int j=0; j<col; j++) {
				if (n<img.length) {
					img[n] = new BufferedImage(width/col, height/row, BufferedImage.TYPE_INT_ARGB);
					Graphics g = img[n].createGraphics();
					
					int xPos = x+j*width/col;
					int yPos = y+i*height/row;
					
					g.drawImage(spriteSheet, 0, 0, width/col, height/row, xPos, yPos, xPos + width/col, yPos + height/row, null);
					g.dispose();
				}
				n++;
			}
		}
	}
	
	public void groupSprites() {
		int greenN=0;
		int blueN=0;
		int purpleN=0;
		int redN=0;
		int orangeN=0;
		greenN = moveSprites(sprites2, greenShape, greenN, 7, 0, 2);
		greenN = moveSprites(sprites2, greenShape, greenN, 1, 1, 1);
		greenN = moveSprites(sprites1, greenShape, greenN, 1, 0, 1);
		greenN = moveSprites(sprites2, greenShape, greenN, 6, 3, 2);
		//Group green shapes
		
		blueN = moveSprites(sprites3, blueShape, blueN, 12, 0, 1);
		blueN = moveSprites(sprites4, blueShape, blueN, 3, 0, 1);
		//Group blue shapes
		
		purpleN = moveSprites(sprites4, purpleShape, purpleN, 10, 3, 1);
		purpleN = moveSprites(sprites4, purpleShape, purpleN, 5, 14, 7);
		//Group purple shapes
		
		redN = moveSprites(sprites0, redShape, redN, 1, 0, 1);
		redN = moveSprites(sprites4, redShape, redN, 1, 13, 1);
		redN = moveSprites(sprites4, redShape, redN, 5, 15, 7);
		redN = moveSprites(sprites4, redShape, redN, 5, 16, 1);
		redN = moveSprites(sprites4, redShape, redN, 3, 23, 7);
		//Group red shapes
		
		orangeN = moveSprites(sprites4, orangeShape, orangeN, 1, 44, 1);
		orangeN = moveSprites(sprites4, orangeShape, orangeN, 4, 24, 1);
		orangeN = moveSprites(sprites4, orangeShape, orangeN, 3, 31, 7);
		orangeN = moveSprites(sprites4, orangeShape, orangeN, 3, 32, 1);
		orangeN = moveSprites(sprites4, orangeShape, orangeN, 2, 39, 7);
		orangeN = moveSprites(sprites4, orangeShape, orangeN, 2, 40, 1);
		//Group orange shapes
	}
	
	private int moveSprites(BufferedImage[] sourceImg, BufferedImage[] destImg, int counter, int count, int sak, int step) {
		for (int i=sak; i<=sak+(count-1)*step; i+=step) {
			destImg[counter] = sourceImg[i];
			counter++;
		}
		return counter;
	}
	
	public BufferedImage[] getRedShape() {
		return redShape;
	}
	
	public BufferedImage[] getGreenShape() {
		return greenShape;
	}
	
	public BufferedImage[] getPurpleShape() {
		return purpleShape;
	}
	
	public BufferedImage[] getBlueShape() {
		return blueShape;
	}
	
	public BufferedImage[] getOrangeShape() {
		return orangeShape;
	}
}
