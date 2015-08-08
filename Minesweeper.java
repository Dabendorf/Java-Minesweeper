package minesweeper;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.text.NumberFormatter;

/**
 * Dies ist die Hauptklasse des Spiels Minesweeper. Sie steuert die gesamte spielerische Interaktion und das User-Interfase.
 * @author Lukas Schramm
 * @version 1.0
 */
public class Minesweeper {
	
	private JFrame frame1 = new JFrame("Minesweeper");
	private NumberFormat format = NumberFormat.getInstance(); 
	private NumberFormatter formatter = new NumberFormatter(format);
	private int breite,hoehe;
	private int anzbomben;
	public Zelle[][] spielfeld;
	public ArrayList<Bombe> bomben = new ArrayList<Bombe>();

	public Minesweeper() {
		format.setGroupingUsed(false); 
		formatter.setAllowsInvalid(false);
		switch(startspiel()) {
			case 0:
				breite = 9;
				hoehe = 9;
				anzbomben = 10;
				break;
			case 1:
				breite = 16;
				hoehe = 16;
				anzbomben = 40;
				break;
			case 2:
				breite = 30;
				hoehe = 16;
				anzbomben = 99;
				break;
			case 3:
				JFormattedTextField breitenfeld = new JFormattedTextField(formatter);
				JFormattedTextField hoehenfeld = new JFormattedTextField(formatter);
				JFormattedTextField bombenfeld = new JFormattedTextField(formatter);
				Object[] nummernfelder = {"Breite",breitenfeld,"Höhe",hoehenfeld,"Bomben",bombenfeld};
				Object[] zahlenfrage = {"Bitte gib die gewünschten Maße ein.",nummernfelder};
				JOptionPane pane = new JOptionPane(zahlenfrage, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
				pane.createDialog(null, "Benutzerdefiniert").setVisible(true);
				try {
					breite = Integer.parseInt(breitenfeld.getText());
					hoehe = Integer.parseInt(hoehenfeld.getText());
					anzbomben = Integer.parseInt(bombenfeld.getText());
				} catch(NumberFormatException e) {
					breite = 16;
					hoehe = 16;
					anzbomben = 40;
				}
				
				if(breite > 75) {
					breite = 75;
				} else if(breite < 5) {
					breite = 5;
				}
				if(hoehe > 40) {
					hoehe = 40;
				} else if(hoehe < 5) {
					hoehe = 5;
				}
				if(anzbomben > (hoehe*breite)-1) {
					anzbomben = hoehe*breite-1;
				} else if(anzbomben > 1000) {
					anzbomben = 1000;
				} else if(anzbomben < 10) {
					anzbomben = 10;
				}
				break;
			default: break;
		}
		spielfeld =  new Zelle[breite][hoehe];
		
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(20*breite,20*hoehe+20));
		frame1.setMinimumSize(new Dimension(20*breite,20*hoehe+20));
		frame1.setMaximumSize(new Dimension(60*breite,60*hoehe+20));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridLayout(hoehe,breite));
		
		for(int px=0;px<breite;px++) {
			for(int py=0;py<hoehe;py++) {
				spielfeld[px][py] = new Zelle(1,1);
	    		spielfeld[px][py].setBounds(10+(px)*20,10+(py)*20,20,20);
	    		spielfeld[px][py].setBorder(BorderFactory.createLineBorder(Color.black));
	    		spielfeld[px][py].setBackground(new Color(0xBDBDBD));
	    		spielfeld[px][py].setAktiviert(2);
	    		frame1.add(spielfeld[px][py]);
			}
		}
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);

		neuebomben();
		nachbaren();
	}
	
	/**
	 * Diese Methode erstellt eine Anzahl von Bomben.
	 */
	private void neuebomben() {
		for(int n=0;n<anzbomben;n++) {
			Random zufall = new Random();
			int posx = zufall.nextInt(breite);
			int posy = zufall.nextInt(hoehe);
			do{
				posx = zufall.nextInt(breite);
				posy = zufall.nextInt(hoehe);
			}while(spielfeld[posx][posy].isBombe() == true);
			bomben.add(new Bombe(posx, posy));
			spielfeld[posx][posy].setBombe(true);
		}
	}
	
	/**
	 * Diese Methode speichert die Anzahl der Nachbarbomben in jede Zelle.
	 */
	private void nachbaren() {
		for(int px=0;px<breite;px++) {
			for(int py=0;py<hoehe;py++) {
				spielfeld[px][py].setAnznachbaren(berechnenachbaren(px,py));
			}
		}
	}
	
	/**
	 * Diese Methode berechnet die Anzahl der Nachbaren jeder Zelle ab.
	 * @param x Nimmt den x-Wert der zu berechnenden Zelle an.
	 * @param y Nimmt den y-Wert der zu berechnenden Zelle an.
	 * @return Gibt die Anzahl der Nachbarbomben aus.
	 */
	private int berechnenachbaren(int x, int y) {
		int anzahl = 0;
		for(int i=-1;i<2;i++) {
			for(int j=-1;j<2;j++) {
				if(!(i==j && i==0)) {
					try {
						if(spielfeld[x+i][y+j].isBombe()) {
							anzahl++;
						}
					} catch(ArrayIndexOutOfBoundsException e) {
					} catch(NullPointerException e) { }
				}
			}
		}
		return anzahl;
	}
	
	/**
	 * Diese Methode wird ausgefuert, wenn ein Spieler eine Zelle anklickt.
	 * Hierbei wird der Spielalgorithmus zum Aufdecken neuer Zellen benuetzt.
	 * @param z Nimmt die angeklickte Spielzelle als Parameter an.
	 */
	public static void spielzug(Zelle z) {
		//int x = z.getX();
		//int y = z.getY();
	}
	
	/**
	 * Diese Methode fragt den Spieler bei Programmstart, auf welcher Schwierigkeitsstufe er spielen moechte.
	 * @return Gibt einen int zurueck, welcher Spielmodus gewaehlt wurde.
	 */
	private int startspiel() {
		JRadioButton anfaenger = new JRadioButton("Anfänger");
		JRadioButton fortgeschrittener = new JRadioButton("Fortgeschrittener");
		JRadioButton experte = new JRadioButton("Experte");
		JRadioButton benutzerdef = new JRadioButton("Benutzerdefiniert");
		ButtonGroup bg = new ButtonGroup();
		bg.add(anfaenger);
		bg.add(fortgeschrittener);
		bg.add(experte);
		bg.add(benutzerdef);
		anfaenger.setSelected(true);
		Object[] parameter = {anfaenger,fortgeschrittener,experte,benutzerdef};
		JOptionPane pane = new JOptionPane(parameter, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
		pane.createDialog(null, "Schwierigkeitsstufe").setVisible(true);
		if(anfaenger.isSelected()) {
			return 0;
		} else if(fortgeschrittener.isSelected()) {
			return 1;
		} else if(experte.isSelected()) {
			return 2;
		} else if(benutzerdef.isSelected()){
			return 3;
		} else {
			return 1;
		}
	}
}