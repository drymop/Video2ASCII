package lmtuan.video2ascii.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import org.bytedeco.javacpp.indexer.Indexable;
import org.bytedeco.javacpp.indexer.Indexer;
import org.bytedeco.javacv.Frame;


/**
 * Converter of grayscale images to ASCII images.
 */
public class Image2ASCII {

	/**
	 * Grayscale pixel to ASCII character converter.
	 */
	private Pixel2Char pix2Char = new Pixel2Char();

	/**
	 * Set the font used for conversion
	 * @param font   name of font
	 * @param style  style used (bold, italic, etc), calculated by ORing 
	 *               style constant from class Font
	 */
	public void setFont(String font, int style) {
		pix2Char.setFont(font, style);
	}

	/**
	 * Convert a grayscale image into an ASCII image, with each pixel converted 
	 * to an ASCII character.
	 * @param  imgBuf Byte Buffer contaning pixel values of the image
	 * @param  width  width of image
	 * @param  height height of image
	 * @return        array of String, each element represent a row of the ASCII image
	 */
	public String[] convert(ByteBuffer imgBuf, int width, int height) {
		String[] asciiImg = new String[height];

		imgBuf.rewind(); // start reading from the head of buffer
		for (int y = 0; y < height; y++) { // 
			StringBuilder sb = new StringBuilder(width);
			for (int x = 0; x < width; x++) {
				int pixel = imgBuf.get() & 0xFF; // get next pixel as an unsigned byte
				sb.append(pix2Char.getChar(pixel));
			}
			asciiImg[y] = sb.toString();
		}

		return asciiImg;
	}

	/**
	 * Convert a grayscale (single channel) image into ASCII image, with each pixel converted to an ASCII character
	 * @param img an indexable grayscale image (allowing access to each pixel value)
	 * @param imgDepth the number of bits used for each pixel value
	 * @return array of String, each element represent a row of the ASCII image
	 */
	public String[] convert(Indexable img) {
		Indexer imgIndexer = img.createIndexer(true);
		int width = (int)imgIndexer.width();
		int height = (int)imgIndexer.height();
		String[] asciiImg = new String[height];

		for (int y = 0; y < height; y++) { // 
			StringBuilder sb = new StringBuilder(width);
			for (int x = 0; x < width; x++) {
				int pixel = (int)imgIndexer.getDouble(y, x);
				sb.append(pix2Char.getChar(pixel));
			}
			asciiImg[y] = sb.toString();
		}
		return asciiImg;
	}

	/**
	 * Convert a BufferedImage of TYPE_BYTE_GRAY to ASCII image 
	 * @param  img the image to be converted
	 * @return     ASCII image as a String array, with each String represent
	 *             a row of the image
	 */
	public String[] convert(BufferedImage img) {
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		ByteBuffer imgBuf = ByteBuffer.wrap(pixels);
		return convert(imgBuf, img.getWidth(), img.getHeight());
	}

//	/**
//	 * Make sure the buffered image is of TYPE_BYTE_GRAY.
//	 * @param img
//	 * @return
//	 */
//	private BufferedImage preprocess(BufferedImage img) {
//		if (img.getType() == BufferedImage.TYPE_BYTE_GRAY)
//			return img;
//		BufferedImage processedImg = new BufferedImage(
//				img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//		Graphics g = processedImg.getGraphics();
//		g.drawImage(img, 0, 0, null);
//		g.dispose();
//		return processedImg;
//	}

//	/**
//	 * Convert a Frame of TYPE_BYTE_GRAY to ASCII image 
//	 * @param  img the image to be converted
//	 * @return     ASCII image as a String array, with each String represent
//	 *             a row of the image
//	 */
//	public String[] convert(Frame f) {
//		return convert(f.createIndexer());
//	}
}
