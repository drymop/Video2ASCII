package lmtuan.video2ascii.test;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import video2ascii.videoplayer.ASCIIVideoPlayerGUI;

public class Test {
	public static void main(String[] args) throws Exception {
		String dir = "C:/Users/lmtuan/Desktop/Stuff/";
		String fileName = "secret_vid2.txt";
		
		File f = new File(dir + fileName);
		int w = 800;
		int h = 200;
		
		// read image
		Scanner sc = new Scanner(new BufferedReader(new FileReader(f)));
		List<String[]> frameList = new ArrayList<>();
		while (sc.hasNext()) {
			try {
				String[] frame = new String[h];
				for (int i = 0; i < h; i++)
					frame[i] = sc.nextLine();
				frameList.add(frame);
				if (sc.hasNextLine())
					sc.nextLine(); // page break char
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		String[][] video = new String[frameList.size()][h];
		frameList.toArray(video);
		
		SwingUtilities.invokeLater(() -> createGUI(video, w, h));
	}
	
	public static void createGUI(String[][] video, int width, int height) {
		Font font = new Font("Consolas", Font.BOLD, 2);
		ASCIIVideoPlayerGUI vidPlayer = new ASCIIVideoPlayerGUI(font, width, height);

		JFrame frame = new JFrame("ASCII Player");
		frame.add(vidPlayer);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		vidPlayer.playVideo(video);
	}
}
