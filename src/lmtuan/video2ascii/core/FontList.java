package lmtuan.video2ascii.core;

import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;

import lmtuan.video2ascii.util.SaveUtil;

public class FontList {
	private ArrayList<String> currFonts, posMonospace;
	private String[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private String lastUsedFont;
	private String[] 
		confirmedMonospace = {"Consolas", "Courier", "Courier New", "Inconsolata", "Lucida Console", "Lucida Sans Typewriter", "Lucida Typewriter", "MS Gothic", "MS Mincho", "SimHei", "SimSun"},
		confirmedNotMonospace = {"Dialog", "DialogInput", "Monospaced", "Symbol", "Webding", "Wingdings", "Wingdings 2", "Wingdings 3"};
	private FontRenderContext frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);

	public String[] getMonospacedFonts() {
		createCurrFontList();
		return currFonts.toArray(new String[currFonts.size()]);
	}
	public String getLastUsedFont() {
		return lastUsedFont;
	}
	public void setLastUsedFont(String newFont) {
		lastUsedFont = newFont;
		saveCustomization();
	}
	private void createCurrFontList() {
		try {
			loadCustomization();
			/*checkCurrFont();
			if (currFonts.size() == 0) throw new Exception();
			if (currFonts.indexOf(lastUsedFont) == -1) lastUsedFont = currFonts.get(0);*/
		} catch (Exception e) {
			currFonts = new ArrayList<String>(Arrays.asList(confirmedMonospace));
			checkCurrFont();
			lastUsedFont = currFonts.get(0);
			saveCustomization();
		}
	}
	/*
	 * Check current font list against all fonts
	 * to see if any font got deleted
	 */
	private void checkCurrFont() {
		int i = 0, j = 0;
		while (i < currFonts.size()) {
			if (j >= allFonts.length || currFonts.get(i).compareToIgnoreCase(allFonts[j]) < 0) {
				currFonts.remove(i);
			} else if (currFonts.get(i).compareToIgnoreCase(allFonts[j]) > 0) {
				j++;
			} else {
				i++; j++;
			}
		}
	}

/*	public String[] getPosibleMonospacedFonts() {
		creatPossibleMonospaceList();
		return posMonospace.toArray(new String[posMonospace.size()]);
	}
	public void creatPossibleMonospaceList() {		
		posMonospace = new ArrayList<String>();
		int i = 0, j = 0;

		for (String s : allFonts) {
			while (i < confirmedMonospace.length && s.compareToIgnoreCase(confirmedMonospace[i]) > 0) {i++;}
			while (j < confirmedNotMonospace.length && s.compareToIgnoreCase(confirmedNotMonospace[j]) > 0) {j++;}
			if (i < confirmedMonospace.length && s.equals(confirmedMonospace[i])) {
				posMonospace.add(s);
			} else if (j < confirmedNotMonospace.length && s.equals(confirmedNotMonospace[j])) {
			} else if (isMonospaced(s)) {
				posMonospace.add(s);
			}
		}
	}*/
	
	/*
	 * Check if a font is monospaced or not
	 * Work in most case, however sometines return true for non-monospaced symbolic fonts
	 */
	/*private boolean isMonospaced(String fontName) {
		double w1, w2;
		Font font = new Font(fontName,Font.PLAIN,12);
		Rectangle2D smallBounds = font.getStringBounds("'''...;;;---|||***```,,,///((()))iiilll!:::!!j...j", frc);
		Rectangle2D bigBounds = font.getStringBounds  ("WmwmwWmwmwWmwmwWmwmwWmwmwWmwmwWmwmwWmwmwWmwmwWmwmw", frc);
		w1 = smallBounds.getWidth();
		w2 = bigBounds.getWidth();
		return (w1 != 0 && w1 == w2);
	}*/

	private void saveCustomization() {
    currFonts.add(lastUsedFont);
    try {  
      SaveUtil.save(currFonts, "FontList");
    } catch (IOException e) {
    } finally {
    	currFonts.remove(currFonts.size()-1);
    }
  }
  @SuppressWarnings("unchecked")
  private void loadCustomization() throws FileNotFoundException, ClassNotFoundException {    
    try {
      currFonts = (ArrayList<String>) SaveUtil.load("FontList");
      lastUsedFont = currFonts.get(currFonts.size()-1);
			currFonts.remove(currFonts.size()-1);
    } catch (FileNotFoundException e) {
    	throw e;
    } catch (IOException e) {}
  }
}