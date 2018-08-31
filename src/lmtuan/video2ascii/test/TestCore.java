package lmtuan.video2ascii.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.indexer.Indexer;
import org.bytedeco.javacpp.indexer.IntIndexer;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;

import lmtuan.video2ascii.core.Image2ASCII;

public class TestCore {
	static private final Image2ASCII imgToString = new Image2ASCII();

	static String[][] convertVideo(File file) throws Exception {
		avutil.av_log_set_level(avutil.AV_LOG_QUIET);
		//imgToString.setCharBlockSize(1, 1);
		//imgToString.setFont("Consolas", 0);


		FFmpegFrameGrabber g = new FFmpegFrameGrabber(file);
		g.start();
		g.setImageHeight(70);
		g.setImageWidth(100);
		
		int nFrames = g.getLengthInFrames();
		nFrames = 10;

		String[][] results = new String[nFrames][];
		System.out.println(nFrames + " frames");

		for (int i = 0; i < nFrames; i++) {
			Frame f = g.grabImage();
			int frameNum = g.getFrameNumber();
			if (f == null) {
				System.out.println("frame null");
				continue;
			}
			System.out.println("frame number: " + frameNum + ", depth: " + f.imageDepth);
			System.out.println("Time: " + f.timestamp);

			// transform
			/*IplImage iplImg = new ToIplImage().convert(f);
			
			
			IplImage grayScale = IplImage.create(iplImg.cvSize(), iplImg.depth(), 1);
			org.bytedeco.javacpp.opencv_imgproc.cvCvtColor(iplImg, grayScale, org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY);
			f = new ToIplImage().convert(grayScale);*/
			Mat mat = new ToMat().convert(f);
			Mat grayMat = new Mat();
			org.bytedeco.javacpp.opencv_imgproc.cvtColor(mat, grayMat, org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY);
			
			if (i == nFrames - 1) {
				ByteBuffer bb = grayMat.createBuffer();
				System.out.println("buffer: " + bb.limit());
				bb.rewind();
				while (bb.hasRemaining()) {
					System.out.print((bb.get() & 0xFF) + ", ");
				}
				System.out.println();
			}
			
			f = new ToMat().convert(grayMat);
			if (i == nFrames - 1) {
				Indexer indx = grayMat.createIndexer();
				System.out.println(indx.width() + " x " + indx.height());
				for (int y = 0; y < indx.height(); y++) { // 
					for (int x = 0; x < indx.width(); x++) {
						int pixel = (int)indx.getDouble(y, x);
						System.out.print(pixel + ", ");
					}
				}
				System.out.println();
				
				
				ByteBuffer bb = (ByteBuffer)f.image[0];
				bb.rewind();
				while (bb.hasRemaining()) {
					System.out.print((bb.get() & 0xFF) + ", ");
				}
				System.out.println();
			}
			if (i == nFrames - 1) {
				Indexer indx = mat.createIndexer();
				System.out.println(indx.width() + " x " + indx.height());
				for (int y = 0; y < indx.height(); y++) { // 
					for (int x = 0; x < indx.width(); x++) {
						int pixel = (int)indx.getDouble(y, x, 4);
						System.out.print(pixel + ", ");
					}
				}
				System.out.println();
			}
			
			// save to files

			/*String saveFile = "C:/Users/lmtuan/Desktop/testCV/frame" + String.format("%03d", frameNum) + "jpg";
			try {
				saveFrame(f, saveFile);
			} catch (IOException e) {
				e.printStackTrace();
			}*/

			//results[i] = processImage(f);
		}

		g.stop();
		return results;
	}

	static String[] processImage(Frame f) {
		//return imgToString.convertImage(f);
		return null;
	}

	static void saveFrame(Frame f, String fileName) throws IOException {
		Java2DFrameConverter conv = new Java2DFrameConverter();
		BufferedImage img = conv.convert(f);
		ImageIO.write(img, "png", new File(fileName));
	}
}
