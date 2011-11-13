package net.schattenkind.androidLove;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

public class LoveStorage {
	public enum FileType {
		FILE, DIR, NONE
	};

	private static final String sRootPathForZipAndResource = ".";
	
	private Activity activity;
	private String loveAppRootOnSdCard;
	private LoveZip mLoveZip;

	// ***** ***** ***** ***** ***** constructor
	
	public LoveStorage(Activity activity, int iLoveZipResID) throws IOException {
		this.activity = activity;
		loveAppRootOnSdCard = sRootPathForZipAndResource;
		mLoveZip = new LoveZip(this,getResourceInputStream(iLoveZipResID));
	}
	
	public LoveStorage(Activity activity, String path) throws IOException {
		this.activity = activity;
		File f = new File(path);
		if (Launcher.isFileLoveZip(f)) { 
			loveAppRootOnSdCard = sRootPathForZipAndResource;
			mLoveZip = new LoveZip(this,f);
		} else {
			loveAppRootOnSdCard = path;
		}
	}
	
	// ***** ***** ***** ***** ***** resources
	
	public InputStream getResourceInputStream (int id) {
		return activity.getResources().openRawResource(id);
	}
	
	// ***** ***** ***** ***** ***** sdcard
	
	private String convertFilePath (String relpath) {
		// return Environment.getExternalStorageDirectory() + "/" + loveAppRootOnSdCard + "/" + relpath; 
		return loveAppRootOnSdCard + "/" + relpath; 
	}

	private File getFileFromSdCard(String filename) {
		assert(isZip());
		return new File(convertFilePath(filename));
	}
	
	private FileInputStream getFileStreamFromSdCard(String filename)
			throws FileNotFoundException {
		File f = new File(convertFilePath(filename));
		return new FileInputStream(f);
	}
	
	public File getSdCardRootDir() { return new File(loveAppRootOnSdCard); }
	
	// ***** ***** ***** ***** ***** api

	public boolean isZip () { return mLoveZip != null; }
		
	/// avoid if possible
	/// use getFileStreamFromLovePath instead
	/// only works if tempdir is writable
	/// needed for soundbuffer/mediaplayer etc which only accepts filepaths and resids, not input steams
	public String forceGetFilePathFromLovePath	(String sPath) throws IOException { return isZip() ? mLoveZip.forceExtractToTempFilePath(sPath) : convertFilePath(sPath); }
	
	/// avoid if possible, see forceGetFilePathFromLovePath
	public File forceGetFileFromLovePath		(String sPath) throws IOException { return isZip() ? mLoveZip.forceExtractToTempFile(sPath) : getFileFromSdCard(sPath); }
	
	/// generic access to real files and files inside .love/.zip
	public InputStream getFileStreamFromLovePath(String path) throws FileNotFoundException {
		return isZip() ? mLoveZip.getFileStreamFromPath(path) : getFileStreamFromSdCard(path);
	}
	
	/// used for love.filesystem.getAppdataDirectory,getSaveDirectory,getUserDirectory,getWorkingDirectory
	public String getRootPath() {
		return loveAppRootOnSdCard;
	}
	
	/// list/enumerate directory childs
	/// used for love.filesystem.enumerate
	public String[] getChildren(String dirpath) throws FileNotFoundException {
		if (isZip()) return mLoveZip.getChildren(dirpath);
		File f = new File(convertFilePath(dirpath));
		return f.list();
	}
	
	/// used for LoveVM.loadConfigFromFile,loadConfig
	/// used for love.filesystem.exists,isFile,isDirectory
	public FileType getFileType(String filename) {
		if (isZip()) return mLoveZip.getFileType(filename);
		File f = new File(convertFilePath(filename));
		if (f.isDirectory()) return FileType.DIR;
		if (f.isFile()) return FileType.FILE;
		return FileType.NONE;
	}

	public List<String> getLines(String filename) throws IOException {
		InputStream fin = getFileStreamFromLovePath(filename);
		DataInputStream in = new DataInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		LinkedList<String> lines = new LinkedList<String>();

		String strLine;
		while ((strLine = br.readLine()) != null) {
			lines.add(strLine);
		}

		return lines;
	}
	
	// TODO: LoveVM.loadFileFromRes
	// TODO: LoveVM.loadFileFromSdCard
}
