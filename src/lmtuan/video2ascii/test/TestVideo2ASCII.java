package lmtuan.video2ascii.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import lmtuan.video2ascii.app.Control;
import lmtuan.video2ascii.core.Converter;
import lmtuan.video2ascii.util.SaveUtil;

public class TestVideo2ASCII {
	static final String video = "C:/Users/lmtuan/Desktop/test.mp4";
	static final String savePath = "C:/Users/lmtuan/Desktop/test.txt";
	
	public static void main(String[] args) throws Exception {
		/*Converter conv = new Converter(new File(video), "Consolas", 0, 4, 16, 26);
		String[][] img = conv.doInBackground();
		saveTextFile(img);*/
		System.out.println("Start");
		long time = System.currentTimeMillis();
		String[][] img = TestCore.convertVideo(new File(video));
		time = System.currentTimeMillis() - time;
		//saveTextFile(img);
		System.out.println("Time taken: " + (time/1000.0) + " secs");
	}
	
	public static void saveTextFile(String[][] content) throws Exception{
		try {
			SaveUtil.write(savePath, content);
		} catch (IOException e) {}
		try {
			openNotepad(savePath);
		} catch (Exception e) {}
	}
	
	private static void openNotepad(String path) throws Exception {
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().edit(new File(path));
		}
	}
}
