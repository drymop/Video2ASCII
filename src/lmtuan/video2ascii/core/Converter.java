package lmtuan.video2ascii.core;

import javax.swing.SwingWorker;

import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;

import lmtuan.video2ascii.util.FileExtensionUtil;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core.Mat;


/**
 * Represent a task of converting an image file or video file to ASCII images.
 */
public class Converter extends SwingWorker<String[][], Void> {

	private final Image2ASCII img2ASCII = new Image2ASCII();

	private File file;
	private double frameRate;
	private int imgWidth;
	private int imgHeight;

	/**
	 * Constructor taking in the parameters for the task
	 * @param file      image or video file to convert
	 * @param font      name of font used in conversion
	 * @param style     name of style (e.g Font.BOLD) used in conversion
	 * @param imgWidth  width of the resulting ASCII image (unit: char)
	 * @param imgHeight height of the resulting ASCII image (unit: char)
	 * @param frameRate frame rate of resulting ASCII video, irrelevant if the
	 *                  file given is not a video
	 */
	public Converter(File file, String font, int style, int imgWidth, int imgHeight, double frameRate) {
		this.file = file;
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.frameRate = frameRate;
		this.img2ASCII.setFont(font, style);
	}

	/**
	 * Do the conversion and returning an ASCII video in the form of a 2D String
	 * array. The first dimension represents frames (for image there is only 1 
	 * frame); the second dimension represents rows of a frame.
	 * @return the ASCII video
	 * @throws IOException           if file cannot be read
	 * @throws InterruptedException  if user cancels task
	 */
	@Override
	public String[][] doInBackground() throws InterruptedException, IOException {
		try {
			return convertFile();
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
	}

	/**
	 * Do the conversion and returning an ASCII video in the form of a 2D String
	 * array. The first dimension represents frames (for image there is only 1 
	 * frame); the second dimension represents rows of a frame.
	 * @return ASCII video
	 * @throws IOException if file cannot be read
	 */
	private String[][] convertFile() throws InterruptedException, IOException {
		// do image or video conversion
		if(FileExtensionUtil.isImage(file))
			return convertImage();
		else if (FileExtensionUtil.isVideo(file))
			return convertVideo();
		else
			throw new IllegalArgumentException("Invalid extension");
	}

	/**
	 * Convert an image to ASCII image.
	 * @return ASCII image as 2D String array
	 * @throws IOException if image cannot be read
	 */
	private String[][] convertImage() throws IOException {
		String[][] asciiImg = new String[1][imgHeight];
		
		/*
		 * Preprocess image to 8-bit grayscale of appropiate size
		 */
		// read image
		BufferedImage bufImg = ImageIO.read(file);
		setProgress(20);
		
		// resize image and convert to grayscale
		BufferedImage processedImg = new BufferedImage(
				imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = processedImg.getGraphics();
		g.drawImage(bufImg, 0, 0, imgWidth, imgHeight, null);		
		g.dispose();
		setProgress(60);
		
		/*
		 * Convert preprocessed image into ASCII
		 */
		asciiImg[0] = img2ASCII.convert(processedImg);
		setProgress(100);
		return asciiImg;
	}

	
	/**
	 * Convert a video to ASCII
	 * @return ASCII video
	 * @throws InterruptedException  if task is canceled
	 */
	private String[][] convertVideo() throws InterruptedException, FrameGrabber.Exception {
		// disable logging to console of JavaCV
		avutil.av_log_set_level(avutil.AV_LOG_QUIET);
		
		/*
		 * Prepare frame grabber
		 */
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
		grabber.start();
		grabber.setImageWidth(imgWidth);
		grabber.setImageHeight(imgHeight);

		// approximation of number of frame
		int nFrames = grabber.getLengthInFrames();

		String[][] asciiVideo = new String[nFrames/2 + 1][];
		System.out.println("Num frames: " + asciiVideo.length);

		/*
		 * Grabbing frames and convert them to ASCII images
		 */
		for (int i = 0; i < nFrames; i++) {
			// user cancel long running task
			if (isCancelled()) 
				throw new InterruptedException("Task is canceled");
			
			setProgress(i * 100 / nFrames);
			
			// grab a new frame
			Frame frame;
			try {
				frame = grabber.grabImage();
				if (frame == null)
					continue;
			} catch (FrameGrabber.Exception e) {
				continue;
			}
			if (i % 2 == 1)
				continue;
			
			// convert Frame to grayscale Mat
			Mat mat = new ToMat().convert(frame);
			Mat grayMat = new Mat();
			org.bytedeco.javacpp.opencv_imgproc.cvtColor(mat, grayMat, org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY);
			
			// convert gray Mat to ASCII image
			asciiVideo[i/2] = img2ASCII.convert(mat);
		}

		setProgress(100);
		grabber.stop();
		
		
		for (int i = 0; i < asciiVideo.length; i++)
			if (asciiVideo[i] == null)
				System.out.println(i + ": null frame");
		
		return asciiVideo;
	}
}