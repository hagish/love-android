package net.schattenkind.androidLove.utils;

public class FileUtils {
	/**
	 * @param filename eg. lala.txt
	 * @return eg. txt (without leading .)
	 */
	public static String getFileExtension(String filename) {
		String[] parts = filename.split("\\.");
		return parts[parts.length - 1];
	}
}
