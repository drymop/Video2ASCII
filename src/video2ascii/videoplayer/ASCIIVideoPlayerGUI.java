package video2ascii.videoplayer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ASCIIVideoPlayerGUI extends JTextArea {
	
	private SwingWorker<Void, Void> frameUpdateWorker = null;
	
	private final String NEW_LINE = System.getProperty("line.separator");
	
	private final Dimension DEFAULT_SIZE = new Dimension(1000, 600);
	
	/**
	 * A string with the number of character equal to the video width
	 * used for font size adjustment.
	 */
	private final String TEST_STRING;
	
	/**
	 * Create an ASCIIVideoPlayerGUI with the given font and sizes
	 * @param font   font used for displaying text
	 * @param width  width of frame
	 * @param height height of frame
	 */
	public ASCIIVideoPlayerGUI(Font font, int width, int height) {
		super(height, width);
		super.setFont(font.deriveFont(2f));
		//super.setFont(font.deriveFont(0.5f));
		
		StringBuilder testStringBuilder = new StringBuilder(width);
		for (int i = 0; i < width; i++)
			testStringBuilder.append('a');
		TEST_STRING = testStringBuilder.toString();
		
		Dimension dim = super.getPreferredSize();
		System.out.println("Initial pref size: " + dim);
		super.setPreferredSize(DEFAULT_SIZE);
		super.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				System.out.print("Resized: ");
				System.out.println(getSize());
				adaptFontSize();
			}
		});
	}
	
	
	private void adaptFontSize() {
		//this.getTextSize();
//		Graphics g = super.getGraphics();
//		System.out.println(g);
//		if (g == null)
//			return;
//		Font curFont = super.getFont();
//
//		float size = 0.5f;
//		for (; size < 100; size += 0.5f) {
//			Font f = curFont.deriveFont(size);
//			FontMetrics fm = g.getFontMetrics(f);
//			System.out.println(size + "," + fm.stringWidth(TEST_STRING) + "," + fm.getHeight());
//		}		
	}
	
	private Dimension getTextSize() {
		/*Graphics g = super.getGraphics();
		System.out.println(g);
		if (g == null)
			return null;

		Font curFont = super.getFont();
		//super.setFont(curFont.deriveFont(1.5f));
		System.out.println(super.getFont());*/
		return null;
	}
	
//	@Override
//	protected void paintComponent(Graphics g) {
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//		super.paintComponent(g);
//	}
	
	/**
	 * Play the given ASCII video. The format of video is a 2D array of String,
	 * where the first dimension represent frames, and the second dimension
	 * represent rows in a frame.
	 * @param asciiVideo
	 */
	public void playVideo(String[][] asciiVideo) {
		System.out.println("Curr pref size: " + super.getPreferredSize());
		System.out.println("Curr size: " + super.getSize());
		frameUpdateWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				int height = asciiVideo[0].length;
				int width  = asciiVideo[0][0].length();
				int frameSize = height * width;
				
				// update for each frame of video
				for (int i = 0; i < asciiVideo.length; i++) {
					if (isCancelled())
						return null;
					// prepare frame as text
					StringBuilder frameBuilder = new StringBuilder(frameSize);
					// first row of frame
					frameBuilder.append(asciiVideo[i][0]);
					// remaining rows of frame
					for (int j = 1; j < height; j++)
						frameBuilder.append(NEW_LINE).append(asciiVideo[i][j]);
					String frameData = frameBuilder.toString();
					SwingUtilities.invokeLater( () -> setText(frameData) );
					try {
						Thread.sleep(1000/15);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
			
			@Override
			protected void done() {
				donePlaying();
			}
		};
		frameUpdateWorker.execute();
	}
	
	private void donePlaying() {
		// do stuff when done playing
	}
	
	/**
	 * Clean up resources before closing
	 */
	public void close() {
		if (frameUpdateWorker != null)
			frameUpdateWorker.cancel(true);
		frameUpdateWorker = null;
	}
}
