package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * Diese Klasse stellt jede einzelne Zelle des Spiels dar, in welcher sich eine Bombe verstecken kann.
 * @author Lukas Schramm
 * @version 1.0
 */
public class Zelle extends JPanel {
	
	private int anznachbaren;
	private int aktiviert; //2=Spielstart;1=Anklickbar;0=Benutzt
	private int x,y;
	private boolean bombe = false;
	private boolean geklickt = false;
	private boolean rechtsgeklickt = false;
	private boolean stop = false;
	private boolean markiert = false;

	public Zelle(int x, int y) {
		this.x = x;
		this.y = y;
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if(evt.getButton() == MouseEvent.BUTTON1) {
					klick();
				} else if(evt.getButton() == MouseEvent.BUTTON3) {
					markieren();
				}
		    } 
		});
	}
	
	/**
	 * Dies die paint-Methode der Zelle. Sie steuert das, was dem Nutzer fuer die Zelle angezeigt wird.<br>
	 * Bei erstmaligem Aufruf setzt sie die Variable aktiviert auf 1. Wenn geklickt true ist und aktiviert 0 oder 1 ist, dann wird der Hintergrund dunkler.
	 * Ausserdem wird via Swift-Case die Stift-Farbe anhand der Anzahl von Nachbarbomben bestimmt und die Zahl, wenn es nicht selbst eine Bombe ist in die Zelle geschrieben.
	 * Wenn es sich selbst um eine Bombe handelt, wird diese angezeigt und das Spiel beendet. Abschliessend werden die restlichen Bomben im Feld angezeigt.<br>
	 * Bei ausgefuertem Rechtsklick wird die Zelle aus Spielersicht als Sicher markiert oder demarkiert.
	 */
	@Override
	public void paintComponent(Graphics stift) {
		super.paintComponent(stift);
		Font font = new Font("Arial", Font.BOLD, 17);
		FontMetrics fm = stift.getFontMetrics(font);
		stift.setFont(font);
		if(aktiviert == 2) {
			aktiviert = 1;
		}
		if(geklickt == true && (aktiviert == 1 || aktiviert == 0)) {
			aktiviert = 0;
			this.setBackground(new Color(0x717171));
			switch (this.getAnznachbaren()) {
				case 0: stift.setColor(new Color(0x000000));break;
				case 1: stift.setColor(new Color(0x0000FF));break;
				case 2: stift.setColor(new Color(0x007B00));break;
				case 3: stift.setColor(new Color(0xFF0000));break;
				case 4: stift.setColor(new Color(0x020B7A));break;
				case 5: stift.setColor(new Color(0x7B0000));break;
				case 6: stift.setColor(new Color(0x007B7B));break;
				case 7: stift.setColor(new Color(0x000000));break;
				case 8: stift.setColor(new Color(0x7B7B7B));break;
				default: stift.setColor(new Color(0x000000));break;
			}
			if(this.getAnznachbaren() > 0 && !this.isBombe()) {
				String num = Integer.toString(this.getAnznachbaren());
				stift.drawString(num,fm.stringWidth(num)/2+2,fm.getHeight()-4);
				Minesweeper.spielzug(this);
			} else if(this.isBombe()) {
				this.setBackground(new Color(0xB20000));
				stift.setColor(Color.black);
				stift.drawString("X",fm.stringWidth("X")/2,fm.getHeight()-4);
				if(stop==false) {
					for(Bombe b:MinesweeperMain.mw.bomben) {
						int bx = b.getX();
						int by = b.getY();
						MinesweeperMain.mw.spielfeld[bx][by].setBackground(Color.red);
						stift.drawString("X",fm.stringWidth("X")/2,fm.getHeight()-4);
					}
					this.stop = true;
				}
			}
		} else if(rechtsgeklickt == true) {
			rechtsgeklickt = false;
			if(markiert) {
				stift.setColor(Color.red);
				stift.drawString("T",fm.stringWidth("T")/2,fm.getHeight()-4);
			} else {
				stift.drawString("",fm.stringWidth("")/2,fm.getHeight()-4);
			}
			markiert = !markiert;
		}
	}
	
	public int getAnznachbaren() {
		return anznachbaren;
	}

	public void setAnznachbaren(int anznachbaren) {
		this.anznachbaren = anznachbaren;
	}

	public int getAktiviert() {
		return aktiviert;
	}

	public void setAktiviert(int aktiviert) {
		this.aktiviert = aktiviert;
	}
	
	public boolean isBombe() {
		return bombe;
	}

	public void setBombe(boolean bombe) {
		this.bombe = bombe;
	}

	/**
	 * Diese Methode wird aufgerufen, wenn ein Linksklick stattfindet. Sie setzt den Linksklick-Boolean auf true und laedt die Zelle neu.
	 */
	public void klick() {
		geklickt = true;
		this.repaint();
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn ein Rechtsklick stattfindet. Sie setzt den Rechtsklick-Boolean auf true und laedt die Zelle neu.
	 */
	public void markieren() {
		rechtsgeklickt = true;
		this.repaint();
	}

	/*public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}*/
}