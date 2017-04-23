import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class SahaGalds extends JPanel {
	
	Laucins tiles[][] = new Laucins[8][8];
	Panels panelis;
	Pazinojums pazin;
	
	boolean speleBeidzas = false;
	boolean pauze = false;
	boolean jakauj = false;
	
	int navGajiens = 0;
	int p1Punkti = 0;
	int p2Punkti = 0;
	int player = 0;
	
	public SahaGalds(Panels panelis, Pazinojums pazin) {
		this.panelis = panelis;
		this.pazin = pazin;
		panelis.sans.galds = this;
		setLayout(new GridLayout(8, 8));
		setPreferredSize(new Dimension(560, 560));
		System.out.println("hello");
		String krasa = "#000000";
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				tiles[i][j] = new Laucins(krasa, this, i, j);
				add(tiles[i][j]);
				krasa = (krasa == "#000000") ? "#ffffff" : "#000000";
			}
			krasa = (krasa == "#000000") ? "#ffffff" : "#000000";
		}
		player = 1;
	}
	
	public void parkrasotRutinas() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				tiles[i][j].repaint();
	}
	
	public void setKaulins(String krasa, String hoKrasa, String clKrasa, int i, int j, int player, boolean gaj, boolean dama) {
		tiles[i][j].kvKrasa = krasa;
		tiles[i][j].sakKrasa = krasa;
		tiles[i][j].hoverKrasa = hoKrasa;
		tiles[i][j].clickKrasa = clKrasa;
		tiles[i][j].irKaulins = true;
		tiles[i][j].player = player;
		tiles[i][j].irGajiens = gaj;
		tiles[i][j].irDama = dama;
	}
	
	public void checkHover(Laucins l) {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j].iegajKv && tiles[i][j] != l) {
					tiles[i][j].iegajKv = false;
					if (tiles[i][j].irKaulins && !tiles[i][j].klikskis) {
						tiles[i][j].iegajusi = false;
						tiles[i][j].kvKrasa = tiles[i][j].sakKrasa;
					} else if (tiles[i][j].irRinkis) {
						tiles[i][j].rinkaKrasa = "#ff0000";
					}
					tiles[i][j].repaint();
				}
	}
	
	public boolean velNospKv (Laucins l) {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j].klikskis && tiles[i][j] != l)
					return true;
		return false;
	}
	
	public void setIespejas (Laucins l, int i, int j) {
		tiles[i][j].irRinkis = true;
		tiles[i][j].repaint();
	}
	
	public void delIespejas () {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++) {
				if (tiles[i][j].irRinkis) {
					tiles[i][j].irRinkis = false;
					tiles[i][j].kaujamaisLauks = false;
					tiles[i][j].repaint();
				}
			}
	}
	
	public void findIespejas (Laucins l, boolean liktIespejas2) {
		int iDif;
		int jDif = -1;
		int i = l.i;
		int j = l.j;
		boolean flag = true;
		if (l.player == 1) iDif = -1;
		else iDif = 1;
		flag = findKausana(l, liktIespejas2);
		for (int z=0; z<2; z++) {
			if (j+jDif >= 0 && j+jDif <= 7&& i+iDif >= 0 && i+iDif <= 7)
				if (!tiles[i+iDif][j+jDif].irKaulins && !jakauj) {
					if (liktIespejas2)
						setIespejas (l, i+iDif, j+jDif);
					flag = false;
				}
			jDif *= -1;
		}
		if (flag) {
			if (!liktIespejas2)
				navGajiens++;
			else {
				l.klikskis = false;
				l.kvKrasa = l.hoverKrasa;
			}
		}
	}
	
	public boolean findKausana(Laucins l, boolean liktIespejas) {
		int iDif = 1;
		int jDif = -1;
		int i = l.i;
		int j = l.j;
		boolean flag = true;
		
		for (int z=0; z<4; z++) {
			switch (z) {
			case 1: iDif *= -1;
					break;
			case 2: jDif *= -1;
					break;
			case 3: iDif *= -1;
					break;
			}
			if (j+2*jDif >= 0 && j+2*jDif <= 7 && i+2*iDif >= 0 && i+2*iDif <= 7)
				if (tiles[i+iDif][j+jDif].irKaulins)
					if (!tiles[i+2*iDif][j+2*jDif].irKaulins &&
							tiles[i][j].player != tiles[i+iDif][j+jDif].player) {
						if (liktIespejas) {
							setIespejas (l, i+2*iDif, j+2*jDif);
							tiles[i+2*iDif][j+2*jDif].kaujamaisLauks = true;
						}
						flag = false;
						
					}
		}
		return flag;
	}

	public void paietGajienu(Laucins rinkis) {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j].klikskis) {
					if (rinkis.kaujamaisLauks) {
						Laucins kaujamais = atrastKaujamo(i, j, rinkis.i, rinkis.j);
						kaujamais.irKaulins = false;
						kaujamais.irDama = false;
						kaujamais.repaint();
						if (kaujamais.player == 1) p2Punkti++;
						else p1Punkti++;
						panelis.sans.pievienotKaulinu(kaujamais.player);
						panelis.sans.repaint();
						rinkis.velGajiens = true;
						System.out.println(i + " & " + j + " ir velviens gajiens");
						rinkis.kaujamaisLauks = false;
					}
					
					setKaulins(tiles[i][j].sakKrasa, tiles[i][j].hoverKrasa,
							tiles[i][j].clickKrasa, rinkis.i, rinkis.j,
							tiles[i][j].player, tiles[i][j].irGajiens, tiles[i][j].irDama);
					tiles[i][j].irKaulins = false;
					tiles[i][j].klikskis = false;
					tiles[i][j].irDama = false;
					delIespejas();
					tiles[i][j].repaint();
					rinkis.repaint();
				}
		
		jakauj = false;
		panelis.sans.jakauj = false;
		
		if (checkForWin()) {
			return;
		}
		
		if (checkForMoves())
			return;
		
		if (!rinkis.irDama)
			checkIfDama(rinkis);
		
		if (!rinkis.irDama)
			if (rinkis.velGajiens && !findKausana(rinkis, true)) {
				rinkis.klikskis = true;
				//rinkis.velGajiens = false;
				rinkis.kvKrasa = rinkis.hoverKrasa;
			}
			else {
				rinkis.velGajiens = false;
				mainitGajienu();
				panelis.changeLabel();
			}
		else {
			if (rinkis.velGajiens) {
				System.out.println("VELVIENS GAJIENS");
				rinkis.klikskis = true;
				rinkis.kvKrasa = rinkis.hoverKrasa;
				findDamaIespejas(rinkis, true, true);
				/*if (rinkis.klikskis) {
					
				}*/
			}
			if (!rinkis.klikskis) {
				rinkis.velGajiens = false;
				mainitGajienu();
				panelis.changeLabel();
			}
		}
		
		parbauditKausanu();
		
		player = (player == 1) ? 2 : 1;
	}
	
	public void mainitGajienu() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j] != null)
					tiles[i][j].irGajiens = (tiles[i][j].irGajiens) ? false : true;
	}
	
	public boolean checkForWin() {
		if (p1Punkti >= 12)  {
			speleBeidzas = true;
			pazin.player = 1;
			repaint();
			pazin.repaint();
			return true;
		}
		
		if (p2Punkti >= 12) {
			speleBeidzas = true;
			pazin.player = 2;
			repaint();
			pazin.repaint();
			return true;
		}
		return false;
	}
	
	public void checkIfDama(Laucins lauc) {
		switch(lauc.player) {
		case 1: if (lauc.i <= 0) {
			lauc.irDama = true;
			lauc.velGajiens = false;
		}
				break;
		case 2: if (lauc.i >= 7) {
			lauc.irDama = true;
			lauc.velGajiens = false;
		}
				break;
		}
		
		lauc.repaint();
	}
	
	public void findDamaIespejas(Laucins l, boolean liktIespeju, boolean liktIespeju2) {
		int iDif = 1;
		int jDif = 1;
		int iDif2;
		int jDif2;
		int i = l.i;
		int j = l.j;
		boolean flag = true;
		//flag = findKausana(l, true);
		for (int z=0; z<2; z++) {
			for (int y=0; y<2; y++) {
				iDif2 = iDif;
				jDif2 = jDif;
				while (j+jDif2 >= 0 && j+jDif2 <= 7 && i+iDif2 >= 0 && i+iDif2 <= 7) {
					if (!tiles[i+iDif2][j+jDif2].irKaulins) {
						if (!l.velGajiens && liktIespeju && !jakauj) {
							if (liktIespeju2)
								setIespejas (l, i+iDif2, j+jDif2);
							flag = false;
						}
					} else {
						if (j+jDif2+jDif >= 0 && j+jDif2+jDif <= 7 && i+iDif2+iDif >= 0 && i+iDif2+iDif <= 7)
							if (findDamaKausana(l, tiles[i+iDif2][j+jDif2], iDif, jDif, liktIespeju, liktIespeju2)) flag = false;
						if (liktIespeju)
							break;
					}
					iDif2+=iDif;
					jDif2+=jDif;
				}
				iDif *= -1;
			}
			jDif *= -1;
		}
		if (flag) {
			if (!liktIespeju2)
				navGajiens++;
			else {
				l.klikskis = false;
				l.kvKrasa = l.hoverKrasa;
			}
		} else if (!liktIespeju) jakauj = true;
	}
	
	public boolean findDamaKausana(Laucins l, Laucins kaujamais, int iDif, int jDif, boolean liktIespeju, boolean liktIespeju2) {
		boolean flag = false;
		if (!tiles[kaujamais.i+iDif][kaujamais.j+jDif].irKaulins) {
			int i = kaujamais.i;
			int j = kaujamais.j;
				if (!tiles[i+iDif][j+jDif].irKaulins && l.player != kaujamais.player) {
					if (liktIespeju && liktIespeju2) {
						l.kaujamaKoord[0] = i+iDif;
						l.kaujamaKoord[1] = j+jDif;
						tiles[i+iDif][j+jDif].kaujamaisLauks = true;
						setIespejas (l, i+iDif, j+jDif);
					}
					flag = true;
				}
		}
		return flag;
	}
	
	public Laucins atrastKaujamo(int i, int j, int i1, int j1) {
		int i2 = 0;
		int j2 = 0;
		int iDif, jDif;
		if (i>i1)
			iDif = -1;
		else iDif = 1;
		if (j>j1)
			jDif = -1;
		else jDif = 1;
		while (i != i1 && j != j1) {
			i+=iDif;
			j+=jDif;
			if (tiles[i][j].irKaulins) {
				i2 = i;
				j2 = j;
			}
		}
		return tiles[i2][j2];
	}
	
	public void pauze() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (!pauze && tiles[i][j].irKaulins) {
					tiles[i][j].pauze = true;
					tiles[i][j].irKaulins = false;
					//tiles[i][j].kvKrasa = tiles[i][j].sakKrasa;
					tiles[i][j].repaint();
				} else if (pauze && tiles[i][j].pauze) {
					tiles[i][j].pauze = false;
					tiles[i][j].irKaulins = true;
					tiles[i][j].repaint();
				}
		
		if (!pauze) {
			pauze = true;
			pazin.pauze = true;
			pazin.repaint();
		} else {
			pauze = false;
			pazin.pauze = false;
			pazin.repaint();
		}
	}
	
	public void jaunaSpele() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++) {
				tiles[i][j].irKaulins = false;
				tiles[i][j].repaint();
			}
		p1Punkti = 0;
		p2Punkti = 0;
		panelis.sans.sk1 = 0;
		panelis.sans.sk2 = 0;
		panelis.sans.player1 = true;
		panelis.sans.repaint();
		pazin.player = 0;
		speleBeidzas = false;
		jakauj = false;
		panelis.sans.jakauj = false;
		panelis.sakums(0, "#bf8040", "#996633", "#734d26", 2, false);
		panelis.sakums(5, "#d9d9d9", "#b3b3b3", "#737373", 1, true);
	}
	
	public void parbauditKausanu() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j].irKaulins && tiles[i][j].irGajiens)
					if (tiles[i][j].irDama)
						findDamaIespejas(tiles[i][j], false, true);
					else if (!findKausana(tiles[i][j], false)) jakauj = true;
		System.out.println("jakauj: " + jakauj);
		if (jakauj) panelis.sans.jakauj = true;
		//panelis.sans.repaint();
	}
	
	public boolean checkForMoves() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				if (tiles[i][j].irKaulins && !tiles[i][j].irGajiens)
					if (tiles[i][j].irDama) findDamaIespejas(tiles[i][j], true, false);
					else findIespejas(tiles[i][j], false);
		if (player == 1) {
			if (navGajiens >= 12-panelis.sans.sk1) {
				speleBeidzas = true;
				pazin.player = 2;
				repaint();
				pazin.repaint();
				navGajiens = 0;
				return true;
			}
		} else if (navGajiens >= 12-panelis.sans.sk2) {
			speleBeidzas = true;
			pazin.player = 1;
			repaint();
			pazin.repaint();
			navGajiens = 0;
			return true;
		}
		System.out.println(navGajiens + " vs " + (12-panelis.sans.sk1) + " vs " + (12-panelis.sans.sk2));
		navGajiens = 0;
		return false;
	}
}
