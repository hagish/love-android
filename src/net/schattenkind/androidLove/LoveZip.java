// ***** ***** ***** ***** ***** zip helper

package net.schattenkind.androidLove;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LoveZip {
	protected static final int BUFFER_SIZE_EXTRACT_TO_TMP = 1024*8;
	protected static final int BUFFER_SIZE_READ_FROM_FILE = 1024*8;
	protected static final String TAG = "LoveZip";
	protected static final Character PATH_SEP_C = '/'; // TODO: device dependent ?
	protected static final String PATH_SEP = Character.toString(PATH_SEP_C);
	protected static final String PATH_SEP_REGEX_ESCAPED = PATH_SEP; // TODO, might be needed for String.replaceAll
	protected static final String PATH_SEP_REPLACE = "_"; // for temp filename
	
	private HashMap<String, LoveZipEntry> mLoveZipEntryMap = new HashMap<String, LoveZipEntry>();
	
	public LoveStorage storage;

	private HashMap<String, File> unzippedTempFileMap = new HashMap<String, File>();
	
	// ***** ***** ***** ***** ***** constructor
	
	/// create from filepath
	public LoveZip (LoveStorage storage,String sPath) throws IOException { this(storage,new File(sPath)); }
	
	/// create from file
	public LoveZip (LoveStorage storage,File f) throws IOException { this(storage,new FileInputStream(f)); }
	
	/// create from inputstream, e.g. resource
	public LoveZip (LoveStorage storage,InputStream is) throws IOException {
		this.storage = storage;
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is,8*1024));
		try {
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				String sFilePath = ze.getName();
				if (ze.isDirectory()) {
					// handle directory
					mLoveZipEntryMap.put(sFilePath,new LoveZipEntry(sFilePath));
				} else {
					// handle file
					long iSize = ze.getSize();
					if (iSize <= 0) LoveVM.LoveLog(TAG,"LoveZip constructor: weird size:"+iSize);
					ByteArrayOutputStream baos = new ByteArrayOutputStream((int)((iSize > 0) ? iSize : 1024)); // param is just a hint for pre-alloc
					
					// read file data
					int count;
					byte buffer[] = new byte[BUFFER_SIZE_READ_FROM_FILE];
					while ((count = zis.read(buffer,0,BUFFER_SIZE_READ_FROM_FILE)) != -1) {
						baos.write(buffer, 0, count);
					}
					
					byte[] bytes = baos.toByteArray();
					mLoveZipEntryMap.put(sFilePath,new LoveZipEntry(sFilePath,bytes));
				}
			}
		} finally {
			zis.close();
		}
	}
	
	// ***** ***** ***** ***** ***** peek for launcher, doesn't load full file
	
	public static boolean zipPeekHasMainLua (File f) { return zipPeekHasFileName(f,"main.lua"); }
	
	/// warning, might be slow, shouldn't be used for every file access
	private static boolean zipPeekHasFileName (File f,String sSearchFileName) {
		//~ LoveVM.LoveLog(TAG,"zipPeekHasFileName path='"+f.getPath()+"' search='"+sSearchFileName+"'");
		try {
			InputStream is = new FileInputStream(f);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is,8*1024));
			try {
				ZipEntry ze;
				while ((ze = zis.getNextEntry()) != null) {
					String filename = ze.getName();
					//~ LoveVM.LoveLog(TAG,"zipPeekHasFileName filename='"+filename+"'");
					if (filename.equals(sSearchFileName)) return true;
				}
			} finally {
				zis.close();
			}
		} catch (IOException e) {
			LoveVM.LoveLogE(TAG,"zipPeekHasFileName:failed to open file",e);
		}
		return false;
	}
	
	// ***** ***** ***** ***** ***** rest
	
	private LoveZipEntry getZipEntry(String sPath) { return (LoveZipEntry)mLoveZipEntryMap.get(sPath); }
	
	// ***** ***** ***** ***** ***** api
	
	/// handle and remove/resolve  ../ ./ //
	public static String NormalizePath (String sPath) {
		String S = PATH_SEP_REGEX_ESCAPED;
		String res = S+sPath+S;
		res = res.replaceAll(S+"."+S,S);
		res = res.replaceAll(S+"[^"+S+"]+"+S+"+.."+S,S); 
		res = res.replaceAll(S+".."+S,S); // remove any remaining that couldn't be resolved
		res = res.replaceAll(S+"+",S); // summarize separators
		res = res.replaceAll("^"+S,""); // remove first (assumed relative, it's inside zip anyway)
		// String 	replaceAll(String regularExpression, String replacement)
		return res;
	}
	
	/// dirlist/enumerate childs
	public String[] 			getChildren(String sPath) {
		//~ LoveVM.LoveLog(TAG,"getChildren path='"+sPath+"'");
		String sBasePath = NormalizePath(sPath);
		if (!sBasePath.endsWith(PATH_SEP)) sBasePath += PATH_SEP; // make sure sBasePath ends with /
		if (sBasePath.length() == 1) sBasePath = ""; // turn / into empty string
			
		//~ LoveVM.LoveLog(TAG,"getChildren normalized='"+sBasePath+"'");
		
		// NOTE : it doesn't matter if dir isn't found inn zip entries, e.g. .
		
		int iRelPathStart = sBasePath.length();
		
		// list all files and dirs directly below
		ArrayList<String> arrlist = new ArrayList<String>();
		for (Map.Entry<String,LoveZipEntry> entry : mLoveZipEntryMap.entrySet()) {
			LoveZipEntry e = entry.getValue();
			String sFilePath = e.sFilePath;
			// example dir : gfx/
			// example file : gfx/imgfont.png
			
			// check if filepath is below
			if (iRelPathStart == 0 || sFilePath.startsWith(sBasePath)) {
				// search first separator /
				int sep = sFilePath.indexOf(PATH_SEP_C,iRelPathStart);
				boolean bIsDirectChild = e.isDirectory() ? (sep >= 0 && sep == sFilePath.length() - 1) : (sep == -1);
				//~ LoveVM.LoveLog(TAG,"found '"+sFilePath+"' sep="+sep+" bIsDirectChild="+bIsDirectChild);
				
				// only accept if it is a direct child rather than a child of a subdir.  directory filepath ends with / and subdirs are listed
				if (bIsDirectChild) {
					String sRelPath = sFilePath.substring(iRelPathStart); // love outputs relative paths
					if (e.isDirectory() && sRelPath.endsWith(PATH_SEP)) sRelPath = sRelPath.substring(0,sRelPath.length()-1); // remove trailing slash for dirs
					arrlist.add(sRelPath); // add filename/dirname to list
				}
			}
		}
		//~ LoveVM.LoveLog(TAG,"getChildren finished...");
		String[] res = new String[arrlist.size()];
		res = arrlist.toArray(res);
		// TODO: sort by filename, ArrayUtils or sth...
		return res;
	}
	
	public void unzipFile(String fileInZip, String destFile) throws IOException
	{
		// open input stream of file in zip
		InputStream	is = getFileStreamFromPath(fileInZip);
		
		File f = new File(destFile);
		
		// copy data
		int count;
		byte data[] = new byte[BUFFER_SIZE_EXTRACT_TO_TMP];
		FileOutputStream		fos		= new FileOutputStream(f);
		BufferedOutputStream	dest	= new BufferedOutputStream(fos, BUFFER_SIZE_EXTRACT_TO_TMP);
		
		while ((count = is.read(data, 0, BUFFER_SIZE_EXTRACT_TO_TMP)) != -1) {
			dest.write(data, 0, count);
		}

		dest.flush();
		dest.close();
	}
	
	/// avoid if possible, use getFileStreamFromPath instead. 2011-11-12 needed by SoundBuffer and MediaPlayer
	/// writes contents of file entry in zip to temporary file, this might not work due to permissions etc
	/// temp file get removed after vm exit
	public File 				forceExtractToTempFile		(String sPath) throws IOException {
		if (unzippedTempFileMap.containsKey(sPath))
		{
			return unzippedTempFileMap.get(sPath);
		}
		
		// open output file in temp dir
		// TODO: check if tempdir has to be set ? 3rd parameter to createTempFile, defaults to java.io.tmpdir
		File fTempDir = storage.getSdCardRootDir();
		File f = File.createTempFile(sTempPrefix,sTempSuffix,fTempDir);
		f.deleteOnExit();

		LoveVM.LoveLog(TAG,"forceExtractToTempFile temppath='"+f.getPath()+"'");
		
		unzipFile(sPath, f.getPath());
		
		unzippedTempFileMap.put(sPath, f);
		
		return f;
	}
	
	/// avoid if possible, use getFileStreamFromPath instead
	public String 				forceExtractToTempFilePath	(String sPath) throws IOException {
		File f = forceExtractToTempFile(sPath);
		return f.getPath();
	}
	
	/// access data as InputStream
	public InputStream			getFileStreamFromPath		(String sPath) throws FileNotFoundException {
		LoveZipEntry ze = getZipEntry(sPath);
		if (ze == null) throw new FileNotFoundException(TAG+":"+sPath);
		return ze.createInputStream();
	}
	
	/// dir,file or not exists
	public LoveStorage.FileType getFileType(String sPath) {
		LoveZipEntry ze = getZipEntry(sPath);
		if (ze == null) return LoveStorage.FileType.NONE;
		if (ze.isDirectory()) return LoveStorage.FileType.DIR;
		return LoveStorage.FileType.FILE;
	}
	
	// ***** ***** ***** ***** ***** LoveZipEntry
	
	public class LoveZipEntry {
		public String	sFilePath; 
		public boolean	bIsDirectory;
		public byte[]	bytes;
		
		/// directory
		public LoveZipEntry (String sFilePath) { this.sFilePath = sFilePath; bIsDirectory = true; }
		
		/// file
		public LoveZipEntry (String sFilePath,byte[] bytes) { this.sFilePath = sFilePath; bIsDirectory = false; this.bytes = bytes; }
		
		public boolean isDirectory () { return bIsDirectory; }
		
		/// access data as InputStream
		public InputStream			createInputStream		() throws FileNotFoundException {
			if (bIsDirectory) throw new FileNotFoundException(TAG+":(isDirectory):"+sFilePath);
			return new ByteArrayInputStream(bytes);
		}
	}
	
}