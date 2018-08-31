package lmtuan.video2ascii.util;

import java.io.File;
import java.util.Set;

public class FileExtensionUtil {
	/**
	 * Immutable set of supported image extensions
	 */
	public static final Set<String> imageExtensions = Set.of("jpg", "png");
	
	/**
	 * Immutable set of supported video extensions
	 */
	public static final Set<String> videoExtensions = Set.of("mp4", "avi");
	
	/**
	 * @param  extension a file extension
	 * @return           true if the extension is a supported image extension
	 */
	public static boolean isImageExtension(String extension) {
		return imageExtensions.contains(extension.toLowerCase());
	}
	
	/**
	 * @param  file a file
	 * @return      true if file extension is an image extension
	 */
	public static boolean isImage(File file) {
		return isImageExtension(getExtension(file));
	}
	
	/**
	 * @param  extension a file extension
	 * @return           true if the extension is a supported video extension
	 */
	public static boolean isVideoExtension(String extension) {
		return videoExtensions.contains(extension.toLowerCase());
	}
	
	/**
	 * @param  file a file
	 * @return      true if file extension is a video extension
	 */
	public static boolean isVideo(File file) {
		return isVideoExtension(getExtension(file));
	}
	
	/**
	 * Extract the extension from a file name. The extension is the substring
	 * after the last dot (.) character. If the file name doesn't contain any
	 * dot character, an empty string is returned.
	 * @param  fileName name of file
	 * @return          extension of file
	 */
	public static String getExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		if (i < 0)
			return "";
		else
			return fileName.substring(i+1).toLowerCase();
	}
	
	/**
	 * Extract the extension from a file. The extension is the substring after 
	 * the last dot (.) character in the file name. If the file name doesn't 
	 * contain any dot character, an empty string is returned.
	 * @param  file file
	 * @return      extension of file
	 */
	public static String getExtension(File file) {
		return getExtension(file.getName());
	}	
}
