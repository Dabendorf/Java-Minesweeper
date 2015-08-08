package minesweeper;

/**
 * Diese Klasse stellt eine Bombe dar, die mit (x,y)-Position auf dem Raster platziert wird.
 * 
 * @author Lukas Schramm
 * @version 1.0
 */
public class Bombe {
	
	int x, y;
	
	public Bombe(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}