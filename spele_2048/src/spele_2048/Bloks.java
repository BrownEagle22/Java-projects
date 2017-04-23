package spele_2048;

import javax.swing.JPanel;

public class Bloks extends JPanel {
	
	int vertiba, xSak, ySak/*, xBeig, yBeig*/;
	int xBeig;
	int yBeig;
	int xKoord;
	int yKoord;
	int izm;
	boolean palielinas = false;
	boolean pazudis = false;
	boolean kustas = false;
	
	public Bloks(int x, int y, int vertiba, int izm) {
		this.vertiba = vertiba;
		//this.vertiba = 128;
		this.izm = izm;
		xSak = x;
		xBeig = x;
		ySak = y;
		yBeig = y;
	}
}
