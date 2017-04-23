import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class Spele extends JFrame {
	
	public Spele() {
		setTitle("Dambrete");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		Spele spele = new Spele();
		SanuPanelis sans = new SanuPanelis();
		Panels panels = new Panels(sans);
		spele.setLayout(new BorderLayout());
		spele.setResizable(false);
		
		spele.add(panels, BorderLayout.WEST);
		spele.add(sans, BorderLayout.EAST);
		spele.pack();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		spele.setLocation(dim.width/2-spele.getSize().width/2, dim.height/2-spele.getSize().height/2);
		spele.setVisible(true);
		
		while (true) {
			//panels.repaint();
			//panels.sahaGalds.la
			//sans.repaint();
		}
	}

}
