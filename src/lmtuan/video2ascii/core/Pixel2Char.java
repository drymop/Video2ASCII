package lmtuan.video2ascii.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import lmtuan.video2ascii.util.SaveUtil;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

/**
 * Convert from grayscale value (0-255) to ASCII characters
 */
public class Pixel2Char {
	public static final char FIRST_CHAR = ' ';
	public static final char LAST_CHAR = '~';
	/**
	 * Number of valid showable ASCII chars (from SPACE to '~').
	 */
	public static final int NUM_CHARS = LAST_CHAR - FIRST_CHAR + 1;
	
	/**
	 * Current font with style (bold, italic, etc).
	 */
	private Font font = null; 

	/**
	 * Conversion table from a grayscale value (0-255) to an ASCII character.
	 */
	private char[] grayscale2CharTable = new char[256];

	/**
	 * Set the font for the conversion.
	 * @param name   name of font
	 * @param style  style of font (bold, italic, etc)
	 */
	public void setFont(String name, int style) {
		Font newFont = new Font(name, style, CHAR_IMG_CHAR_SIZE);
		if (newFont.equals(font))
			return;

		font = newFont;
		try {
			loadConversionTable();
		} catch (Exception e) {
			createConversionTable();
			saveConversionTable();
		}
	}
	
	/**
	 * @return current font
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * Get the character for a grayscale value. Must call setFont at least
	 * once to set the font used for conversion.
	 * @param  grayscale  value between 0-255, with 0 being black
	 * @return            a character suitable for the given grayscale
	 */
	public char getChar(int grayscale) {
		return grayscale2CharTable[grayscale];
	}
	
	/**
	 * @return A copy of the conversion table (an array mapping grayscale value
	 *         to an ascii character)
	 */
	public char[] getConversionTable() {
		return Arrays.copyOf(grayscale2CharTable, grayscale2CharTable.length);
	}
	
	private void createConversionTable() {    
		// a map of each character to its intensity (number of non-white pixels)
		int[][] intensities = new int[NUM_CHARS][2];
		intensities[0][0] = ' '; 
		intensities[0][1] = 0;
		for (int i = 0; i < NUM_CHARS; i++) {
			int currChar = FIRST_CHAR + i;
			intensities[i][0] = currChar;
			intensities[i][1] = countNonWhitePixels(
					createCharImage( (char)currChar ));
		}

		// sort intensity map in increasing order
		quickSortMap(intensities);
//		for (int i = 0; i < intensities.length; i++)
//			intensities[i][1] = (int) Math.round(Math.sqrt(intensities[i][1]));
		
		// normalize intensity map between 0-255, with 0 being the highest number
		// of non-white pixel
		double convRate = 255.0 / intensities[0][1];
		for (int i = 0; i < NUM_CHARS; i++)
			intensities[i][1] = 255 - (int)Math.round(intensities[i][1] * convRate);
		
		// create conversion table based on intensity map of characters
		grayscale2CharTable = new char[256];
		int curCharIndex = 0;
		int threshold = (intensities[0][1] + intensities[1][1])/2;
		for (int i = 0; i < 256; i++) {
			while (i > threshold && curCharIndex < NUM_CHARS - 2) {
				curCharIndex++;
				threshold = (intensities[curCharIndex][1] + intensities[curCharIndex+1][1])/2;
			}
			grayscale2CharTable[i] = (char)(intensities[curCharIndex][0]);
		}
	}

	/*
	 * Parameters for creating BufferedImage of a character. Calculated to fit
	 * the bounding box of all ASCII characters.
	 */
	private static final int 
	CHAR_IMG_WIDTH     = 37, 
	CHAR_IMG_HEIGHT    = 57,
	CHAR_IMG_INDENT    = 5, // x of character
	CHAR_IMG_BASE_LINE = 43, // y of character
	CHAR_IMG_CHAR_SIZE = 50; 

	/**
	 * @param  c the character
	 * @return   a TYPE_BYTE_GRAY BufferedImage of the given character 
	 */
	private BufferedImage createCharImage(char c) {
		BufferedImage img = new BufferedImage(CHAR_IMG_WIDTH, CHAR_IMG_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g0 = img.createGraphics();

		// white back ground
		g0.setColor(Color.WHITE);
		g0.fillRect(0, 0, img.getWidth(), img.getHeight());
		// black character with specified font
		g0.setFont(font);
		g0.setColor(Color.BLACK);
		String str = "" + c;
		g0.drawString(str, CHAR_IMG_INDENT, CHAR_IMG_BASE_LINE);
		
		g0.dispose();

		// For saving images, if need to visualize each character
//		String name = String.format("char_img_%s/%02d.png", font.getName(), c - 32);
//	    try {
//			ImageIO.write(img, "png", new File(name));
//		} catch (IOException e) { e.printStackTrace(); }

		return img;
	}

	/**
	 * @param  img  The image 
	 * @return      Number of non-white pixels in the image (grayscale != 0xFF)
	 */
	private int countNonWhitePixels(BufferedImage img) {
		int count = 0;
		for (int i = 0; i < img.getWidth(); i++)
			for (int j = 0; j < img.getHeight(); j++)
				if (img.getRGB(i, j) != -1) // if pixel is not white
					count++;
		return count;
	}
	
	/**
	 * Sort a map in-place in decreasing order of values.
	 * @param map The map implemented as double array ({K1, V1}, {K2, V2}, ...}) 
	 */
	private void quickSortMap(int[][] map) {
		quickSortMap(map, 0, map.length-1);
	}
	
	/**
	 * Sort a map in-place in decreasing order of values.
	 * @param map   The map implemented as double array ({K1, V1}, {K2, V2}, ...}) 
	 * @param start Index of the first element in the sorting range
	 * @param end   Index of the last element in the sorting range
	 */
	private void quickSortMap(int[][] map, int start, int end) {
		int pivot = map[(start+end)/2][1];
		int i = start, j = end;
		while(i <= j) {
			while (map[i][1] > pivot) 
				i++;
			while (map[j][1] < pivot) 
				j--;
			if (i <= j) {
				if (i != j) {
					int t = map[i][0];
					map[i][0] = map[j][0];
					map[j][0] = t;
					t = map[i][1];
					map[i][1] = map[j][1];
					map[j][1] = t;
				} 
				i++; 
				j--;
			}
		}
		if (start < j)
			quickSortMap(map, start, j);
		if (end > i)
			quickSortMap(map, i, end);
	}

	/**
	 * Save current conversion table to file
	 */
	private void saveConversionTable() {
		try {  
			SaveUtil.save(grayscale2CharTable, getSaveFileName(font));
		} catch (Exception e) {}
	}

	/**
	 * Load conversion table from file.
	 * @throws ClassNotFoundException when failed to deserialize object in file
	 * @throws IOException when failed to open file
	 */
	private void loadConversionTable() throws ClassNotFoundException, IOException {    
		grayscale2CharTable = (char[])SaveUtil.load(getSaveFileName(font));
	}

	/**
	 * Get the name of the appropiate save file for a font. The save file
	 * should cache all data related to a font, so that the calculations related
	 * to that font don't have to be performed again.
	 * @param  font  The font
	 * @return       Name of save file for that font
	 */
	private String getSaveFileName(Font font) {
		return "g2c_" + font.getName() + font.getStyle();
	}
}