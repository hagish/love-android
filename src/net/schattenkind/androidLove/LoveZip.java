// ***** ***** ***** ***** ***** zip helper

package net.schattenkind.androidLove;

import net.schattenkind.androidLove.LoveStorage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LoveZip {
	protected static final String TAG = "LoveZip";
	
	// ***** ***** ***** ***** ***** constructor
	
	/// create from filepath
	public LoveZip (String sPath) throws IOException { this(new File(sPath)); }
	
	/// create from file
	public LoveZip (File f) throws IOException { this(new FileInputStream(f)); }
	
	/// create from inputstream, e.g. resource
	public LoveZip (InputStream is) throws IOException {
		/*
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is,8*1024));
		try {
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				//~ ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//~ byte[] buffer = new byte[1024];
				//~ int count;
				//~ while ((count = zis.read(buffer)) != -1) {
					//~ baos.write(buffer, 0, count);
				//~ }
				String filename = ze.getName();
				if (filename == sSearchFileName) return true;
				//~ LoveVM.LoveLog(TAG,"file:"+(ze.isDirectory() ? "[D]" : "")+filename);
				//~ byte[] bytes = baos.toByteArray();
				// do something with 'filename' and 'bytes'...
			}
		} finally {
			zis.close();
		}
		*/
	}
	
	// ***** ***** ***** ***** ***** peek for launcher, doesn't load full file
	
	public static boolean zipPeekHasMainLua (File f) { return zipPeekHasFileName(f,"main.lua"); }
	
	/// warning, might be slow, shouldn't be used for every file access
	private static boolean zipPeekHasFileName (File f,String sSearchFileName) {
		try {
			InputStream is = new FileInputStream(f);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is,8*1024));
			try {
				ZipEntry ze;
				while ((ze = zis.getNextEntry()) != null) {
					//~ ByteArrayOutputStream baos = new ByteArrayOutputStream();
					//~ byte[] buffer = new byte[1024];
					//~ int count;
					//~ while ((count = zis.read(buffer)) != -1) {
						//~ baos.write(buffer, 0, count);
					//~ }
					String filename = ze.getName();
					if (filename == sSearchFileName) return true;
					//~ LoveVM.LoveLog(TAG,"file:"+(ze.isDirectory() ? "[D]" : "")+filename);
					//~ byte[] bytes = baos.toByteArray();
					// do something with 'filename' and 'bytes'...
				}
			} finally {
				zis.close();
			}
		} catch (IOException e) {
			LoveVM.LoveLogE(TAG,"zipHasMainLua:failed to open file",e);
		}
		return false;
	}
	
	// ***** ***** ***** ***** ***** rest
	
	public String 				forceExtractToTempFilePath	(String sPath) throws IOException { return null; } // TODO
	public File 				forceExtractToTempFile		(String sPath) throws IOException { return null; } // TODO
	public InputStream			getFileStreamFromPath		(String sPath) throws FileNotFoundException { return null; } // TODO
	public String[] 			getChildren(String sPath) throws FileNotFoundException { return null; } // TODO
	public LoveStorage.FileType getFileType(String sPath) { return null; } // TODO
	
}