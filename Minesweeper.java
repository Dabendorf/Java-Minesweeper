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
	private int anzahlBombenFrei;
	private int anzahlFreigelegt = 0;
	public int gefundeneBomben = 0;

	public Minesweeper() {
		format.setGroupingUsed(false); 
		formatter.setAllowsInvalid(false);
		
		startspiel();
		neuebomben();
		nachbaren();
	}
	
	/**
	 * Diese Methode erstellt eine Anzahl von Bomben.
	 */
	private void neuebomben() {
		bomben.clear();
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
	 * Diese Methode berechnet die Anzahl der Nachbarbomben jeder Zelle.
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
	public void spielzug(Zelle z) {
        int posx = z.getPosx();
        int posy = z.getPosy();
        if(!z.isBombe() && z.getAktiviert()==1) {
        	if(!z.isRechtsgeklickt()) {
        		anzahlFreigelegt++;
        		z.setGeklickt(true);
                z.setAktiviert(0);
                z.repaint();
                if(anzahlFreigelegt==anzahlBombenFrei) {
                	JOptionPane.showMessageDialog(null, "Du hast alle Bomben gefunden."+System.getProperty("line.separator")+"Herzlichen Glückwunsch!", "Gewonnen", JOptionPane.INFORMATION_MESSAGE);
                	neuepartie();
                }
        	}
            if(z.getAnznachbaren()==0) {
            	if(posx+1>-1 && posx+1<breite && posy>-1 && posy<hoehe) {
            		spielzug(spielfeld[posx+1][posy]);
            	}
            	if(posx-1>-1 && posx-1<breite && posy>-1 && posy<hoehe) {
            		spielzug(spielfeld[posx-1][posy]);
            	}
            	if(posx>-1 && posx<breite && posy+1>-1 && posy+1<hoehe) {
            		spielzug(spielfeld[posx][posy+1]);
            	}
				if(posx>-1 && posx<breite && posy-1>-1 && posy-1<hoehe) {
					spielzug(spielfeld[posx][posy-1]);
				}
				if(posx+1>-1 && posx+1<breite && posy+1>-1 && posy+1<hoehe) {
					spielzug(spielfeld[posx+1][posy+1]);
				}
				if(posx+1>-1 && posx+1<breite && posy-1>-1 && posy-1<hoehe) {
					spielzug(spielfeld[posx+1][posy-1]);
				}
				if(posx-1>-1 && posx-1<breite && posy+1>-1 && posy+1<hoehe) {
					spielzug(spielfeld[posx-1][posy+1]);
				}
				if(posx-1>-1 && posx-1<breite && posy-1>-1 && posy-1<hoehe) {
					spielzug(spielfeld[posx-1][posy-1]);
				}
            }
        } else {
        	if(!z.isRechtsgeklickt()) {
        		z.setGeklickt(true);
                z.setAktiviert(0);
                z.repaint();
                if(z.isBombe()) {
                	JOptionPane.showMessageDialog(null, "Du bist in eine Bombe getreten."+System.getProperty("line.separator")+"Das Spiel ist verloren!", "Verloren", JOptionPane.WARNING_MESSAGE);
                	neuepartie();
                }
        	}
        }
    }
	
	/**
	 * Diese Methode fragt den Spieler bei Programmstart, auf welcher Schwierigkeitsstufe er spielen moechte.
	 * @return Gibt einen int zurueck, welcher Spielmodus gewaehlt wurde.
	 */
	private int spielGroesse() {
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
	
	/**
	 * Diese Methode generiert auf Grundlage der Nutzereingaben das vollstaendige Spielfeld.
	 */
	private void startspiel() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		switch(spielGroesse()) {
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
		anzahlBombenFrei = breite*hoehe - anzbomben;
		anzahlFreigelegt = 0;
		gefundeneBomben = 0;
		spielfeld = null;
		spielfeld = new Zelle[breite][hoehe];
		
		frame1.setPreferredSize(new Dimension(25*breite,25*hoehe+20));
		frame1.setMinimumSize(new Dimension(25*breite,25*hoehe+20));
		frame1.setMaximumSize(new Dimension(60*breite,60*hoehe+20));
		frame1.setResizable(true);
		
		Container cp = frame1.getContentPane();
		cp.removeAll();
		cp.repaint();
		cp.setLayout(new GridLayout(hoehe,breite));
		
		for(int py=0;py<hoehe;py++) {
			for(int px=0;px<breite;px++) {
				spielfeld[px][py] = new Zelle(px,py);
				spielfeld[px][py].setBackground(new Color(0xBDBDBD));
	    		spielfeld[px][py].setBorder(BorderFactory.createLineBorder(Color.black));
	    		spielfeld[px][py].repaint();
	    			frame1.add(spielfeld[px][py]);
			}
		}
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Diese Methode fragt den Spieler, ob er eine weitere Partie spielen moechte und startet je nach Nutzereingabe entweder ein neues Spiel oder beendet das Programm.
	 */
	private void neuepartie() {
		int dialogneustart = JOptionPane.showConfirmDialog(null, "Möchtest Du eine neue Partie spielen?", "Neue Runde?", JOptionPane.YES_NO_OPTION);
        if(dialogneustart == 0) {
        	startspiel();
        	neuebomben();
    		nachbaren();
        } else {
        	System.exit(0);
        }
	}
}