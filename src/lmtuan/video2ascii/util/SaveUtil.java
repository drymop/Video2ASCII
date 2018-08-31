package lmtuan.video2ascii.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import java.io.FileWriter;
import java.io.PrintWriter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SaveUtil {
	public static final char PAGE_BREAK_CHAR = (char)12;
	private static final String SAVE_DATA_DIRECTORY = "data";

	/**
	 * Save an object to a file in save folder
	 * @param o object to be saved
	 * @param fileName name of the save file
	 * @throws IOException if the file specified cannot be written to
	 */
	public static void save(Object o, String fileName) throws IOException {
		File directory = new File(SAVE_DATA_DIRECTORY);
		if (!directory.exists())
			directory.mkdirs();
		File savefile = new File(directory, fileName+ ".dat");
		try (
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(savefile));
		) {
			oos.writeObject(o);
		}
	}
	
	/**
	 * Load an object from a file in save folder
	 * @param fileName name of save file
	 * @return object saved in the file
	 * @throws IOException if the file cannot be found or opened
	 * @throws ClassNotFoundException if the object in the file cannot be deserialized
	 */
	public static Object load(String fileName) 
			throws IOException, ClassNotFoundException {
		try (  
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(SAVE_DATA_DIRECTORY + "/" + fileName + ".dat"));  
		) {
			return ois.readObject();
		}
	}

	/**
	 * Write multiple pages of texts to a file. Each row is printed with a new
	 * line, while each page is printed with a page break character at the end.
	 * @param filePath path to file
	 * @param text 2D array of string, where each element of the outer array
	 * reprents a page of content, while each element of the inner array is
	 * a row in the current page.
	 * @throws IOException if the file cannot be written to
	 */
	public static void write(String filePath, String[][] text) throws IOException {
		PrintWriter pwriter = new PrintWriter(
				new BufferedWriter(new FileWriter(filePath, false)));
		for (String s : text[0]) {
			if (s == null)
				pwriter.printf("%s" + "%n", "null");
			pwriter.printf("%s" + "%n", s);
		}
		for (int page = 1; page < text.length; page++) {
			pwriter.printf("%s" + "%n", PAGE_BREAK_CHAR);
			for (String s: text[page]) {
				if (s == null)
					pwriter.printf("%s" + "%n", "null");
				pwriter.printf("%s" + "%n", s);
			}
		}
		pwriter.flush();
		pwriter.close();
	}
}