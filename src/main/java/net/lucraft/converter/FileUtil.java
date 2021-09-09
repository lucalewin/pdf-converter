package net.lucraft.converter;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

public class FileUtil {

	/**
	 *
	 * @param files
	 * @return
	 */
	public static boolean containsOnlyImages(List<File> files) {
		for (File file : files) {
			if (!isImageFile(file)) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public static boolean isImageFile(File file) {
		return isImageFile(file.getAbsolutePath());
	}

	/**
	 *
	 * @param filename
	 * @return
	 */
	public static boolean isImageFile(String filename) {
		String extension = FilenameUtils.getExtension(filename);
		return extension.equalsIgnoreCase("png") ||
				extension.equalsIgnoreCase("jpg") ||
				extension.equalsIgnoreCase("jpeg");
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public static boolean isPdf(File file) {
		return isPdf(file.getAbsolutePath());
	}

	/**
	 *
	 * @param filename
	 * @return
	 */
	public static boolean isPdf(String filename) {
		return FilenameUtils.getExtension(filename).equalsIgnoreCase("pdf");
	}
}
