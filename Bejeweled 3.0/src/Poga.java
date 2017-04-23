import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class Poga extends JButton implements MouseListener {
	BufferedImage baseColor;
	BufferedImage hoverColor;
	BufferedImage clickColor;
	
	Font font;
	
	Shape poga;
	
	Color textColor = Color.black;
	
	String text;
	
	int textWidth;
	int textHeight;
	
	int buttonW;
	int buttonH;
	
	int paddingX;
	int paddingY;
	
	boolean mouseEntered;
	boolean mousePressed;
	
	public Poga(String text, Font font) {
		setOpaque(true);
		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		//tas viss tiek darits, lai neuzkrasotos un nobloketu parasto JButton
		
		this.text = text;
		this.font = font;
		
		paddingX = font.getSize()/2*3;
		paddingY = font.getSize()/2;
		//saglaba atstarpju lielumus starp tekstu un malu
		
		addMouseListener(this);
		
		FontMetrics metrics = this.getFontMetrics(font);
		textWidth = metrics.stringWidth(text);
		textHeight = metrics.getHeight();
		
		buttonW = textWidth + paddingX;
		buttonH = textHeight + paddingY;
		
		poga = new RoundRectangle2D.Double(0, 0, buttonW, buttonH, buttonH*0.5, buttonH);
		//izveido pogas kvadrata figuru un saglaba to mainigaja
		
		setPreferredSize(new Dimension(buttonW, buttonH));
		setMinimumSize(new Dimension(buttonW, buttonH));
		setMaximumSize(new Dimension(buttonW, buttonH));
		
		baseColor = setColor(baseColor, "poga_base.png");
		hoverColor = setColor(hoverColor, "poga_hover.png");
		clickColor = setColor(clickColor, "poga_click.png");
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);
		//Padara otinu precizaku un gludaku
		
		//Color color;
		BufferedImage img = null;
		if (mousePressed)
			img = clickColor;
		else if (mouseEntered)
			img = hoverColor;
		else img = baseColor;
		//izvelas krasu atbilstosi situacijai
		
		//iekraso pogu
		g2d.drawImage(img, 0, 0, null);
		
		g2d.setColor(textColor);
		g2d.setFont(font);
		g2d.drawString(text, paddingX/2, buttonH/2 + textHeight/4);
		//iekraso tekstu
	}
	
	public BufferedImage setAttels(String name) {
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(Poga.class.getResource(name));
		} catch(IOException e) {
			System.out.println("Neizdevas ieladet failu " + name);
		}
		
		return img;
	}
	
	public BufferedImage resize(BufferedImage img, int x, int y) {
		BufferedImage newImg = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		Graphics g = newImg.createGraphics();
		g.drawImage(img, 0, 0, x, y, null);
		g.dispose();
		return newImg;
	}
	
	public static BufferedImage makeRoundedCorner(BufferedImage image, int xCornerRadius, int yCornerRadius) {
	    int w = image.getWidth();
	    int h = image.getHeight();
	    BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2 = output.createGraphics();
	    
	    g2.setComposite(AlphaComposite.Clear);
	    g2.fillRect(0, 0, w, h);
	    // This is what we want, but it only does hard-clipping, i.e. aliasing
	    // g2.setClip(new RoundRectangle2D ...)

	    // so instead fake soft-clipping by first drawing the desired clip shape
	    // in fully opaque white with antialiasing enabled...
	    g2.setComposite(AlphaComposite.Src);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.WHITE);
	    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, xCornerRadius, yCornerRadius));

	    // ... then compositing the image on top,
	    // using the white shape from above as alpha source
	    g2.setComposite(AlphaComposite.SrcAtop);
	    g2.drawImage(image, 0, 0, null);

	    g2.dispose();

	    return output;
	}
	
	public void setPosition(int x, int y) {
		setBounds(x, y, buttonW, buttonH);
	}
	
	public BufferedImage setColor (BufferedImage color, String name) {
		color = setAttels(name);
		color = resize(color, buttonW, buttonH);
		color = makeRoundedCorner(color, (int)(buttonH*0.5), buttonH);
		return color;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		mouseEntered = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseEntered = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mousePressed = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mousePressed = false;
		repaint();
	}

}
