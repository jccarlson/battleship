package boardGUI;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Describes a square on a {@link BoardPanel}. 
 * 
 * @author Jason Carlson
 * @version 2.0
 * @since 2015-09-19
 */
public class BoardSquare {
	
	/** Colors used in painting the BoardSquare */
	private ColorScheme sqColors;
	private Color currentColor;
	
	/** The size (height and width) of the BoardSquare, in pixels */
	private int size;
	
	/** Passed from parent Component, this is the actual x,y pixel location on that component of the top left pixel, used for painting */
	private int paintX, paintY;
	
	
	/** The logical row and column coordinates of this BoardSquare on the logicalBoard */
	public final int ROW, COL;

	
	/**
	 * Constructor with explicit paint location
	 * 
	 * @param r the row of this square on l
	 * @param c the column of this square on l
	 * @param s the size, in pixels, of the width and height to paint
	 * @param pX the x pixel location of the top left square of this BoardSquare
	 * @param pY the y pixel location of the top left square of this BoardSquare
	 * @param cs a ColorScheme describing the colors of this square. 
	 */
	public BoardSquare(int r, int c, int s, int pX, int pY, ColorScheme cs) {
		ROW = r;
		COL = c;
		size = s;
		sqColors = cs;
		setPaintCoord(pX, pY);
		currentColor = cs.DEFAULT;
	}
	
	
	/** Sets the X and Y coordinates to paint this BoardSquare at (top left) on the parent component */
	void setPaintCoord(int pX, int pY) {
		paintX = pX;
		paintY = pY;
	}
	
	/**
	 * Changes the color of the square if Missed or Hit.
	 * 
	 * @param s 
	 * 			The result of firing at this Square.
	 */
	public void click(char s) {
		if(s == 'M')
			currentColor = sqColors.MISS;
		if(s == 'H')
			currentColor = sqColors.HIT;
	}
	
	/**
	 * Resets the square to the default color.
	 */
	public void reset() {
		currentColor = sqColors.DEFAULT;
	}
	
	/** 
	 *  ONLY CALL FROM PARENT BoardPanel paint(g) METHOD! 
	 * 	<p>
	 * 	Paints the square at paintX and paintY of Graphics g.
	 */
	void paint(Graphics g) {
		g.setColor(currentColor);
		g.fillRect(paintX, paintY, size + 1, size + 1);
		g.setColor(sqColors.BORDER);
		g.drawRect(paintX, paintY, size, size);
	}
	
		
}
