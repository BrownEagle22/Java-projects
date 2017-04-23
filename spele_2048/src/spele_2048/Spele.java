package spele_2048;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Spele extends JFrame {
	

	public static void main(String[] args) throws InterruptedException {
		Spele spele = new Spele();
		Panels panels = new Panels();
		spele.add(panels);
		spele.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: if (panels.varKusteties)
										panels.move("up");
							break;
				case KeyEvent.VK_RIGHT: if (panels.varKusteties)
										panels.move("right");
				 			break;
				case KeyEvent.VK_DOWN: if (panels.varKusteties)
										panels.move("down");
							break;
				case KeyEvent.VK_LEFT: if (panels.varKusteties)
										panels.move("left");
							break;
				}
			}
		});
		spele.pack();
		spele.setVisible(true);
		while (true) {
			panels.flag = true;
			switch (panels.virziens) {
			case "up": for (int i=0; i<4; i++)
						for (int j=0; j<4; j++)
							panels.render(i, j);
						break;
			case "down": for (int i=0; i<4; i++)
							for (int j=3; j>=0; j--)
								panels.render(i, j);
						break;
			case "right": for (int i=0; i<4; i++)
							for (int j=3; j>=0; j--)
								panels.render(j, i);
						break;
			case "left": for (int i=0; i<4; i++)
							for (int j=0; j<4; j++)
								panels.render(j, i);
						break;
			default:
			}
			panels.repaint(new Rectangle(0, 0, 464, 464));
			if (panels.flag) {
				if (!panels.varKusteties) {
					if (!panels.end)
						panels.spawn(1);
					panels.end = panels.gameOver();
				}
				panels.varKusteties = true;
			}
			Thread.sleep(3);
			//}
		}
	}
	
	public Spele() {
		setTitle("Spele 2048");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2 - 232, dim.height/2-this.getSize().height/2 - 275);
	}
}
