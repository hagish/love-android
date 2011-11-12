package net.schattenkind.androidLove;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.LoveStorage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Launcher extends ActivitiyWithExitMenu {
	private static final String TAG = "LoveLauncher";
	private static final String loveRootOnSdCard = "/love/";
	public static String launchMeGameName;
	public static String launchMeGamePath;

	private LinkedList<String> gameListName = new LinkedList<String>();
	private LinkedList<String> gameListPath = new LinkedList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);

		populateGameList();

		ListView gameListView = (ListView) this.findViewById(R.id.gameList);
		gameListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, gameListName));

		gameListView.setTextFilterEnabled(true);
		gameListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				launchGame(gameListName.get(position),
						gameListPath.get(position));
			}
		});
	}

	public static boolean isFileLoveZip (File f) { return f.isFile() && (f.getName().endsWith(".love") || f.getName().endsWith(".zip")); }
	
	/// name filter for dir.listFiles, e.g. list only .love files
	public class FilenameFilterEndsWith implements FilenameFilter {
		String sEndsWith;
		public FilenameFilterEndsWith (String sEndsWith) { this.sEndsWith = sEndsWith; }
		public boolean accept(File dir, String name) { return name.endsWith(sEndsWith); }
	}

	private void populateGameList() {
		// scan sd card (/mnt/sdcard/love)
		try {
			File dir = new File(Environment.getExternalStorageDirectory() + "/"
					+ loveRootOnSdCard);

			File[] files = dir.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					tryToPopulateGameFromDirectory(f);
				}
				if (isFileLoveZip(f)) {
					tryToPopulateGameFromZipOrLoveFile(f);
				}
			}
			
		} catch (Exception e) {
			// avoid crash if folder doesn't exist
		}
			
		// scan downloads folder for .love files
		try {
			// ex Environment.DIRECTORY_DOWNLOADS
			File dir = Environment.getDownloadCacheDirectory();
			File[] files = dir.listFiles(new FilenameFilterEndsWith(".love")); // don't scan all zip files
			for (File f : files) {
				if (isFileLoveZip(f)) {
					tryToPopulateGameFromZipOrLoveFile(f);
				}
			}
			
			
		} catch (Exception e) {
			// avoid crash folder doesn't exist
		}
	}

	private void tryToPopulateGameFromZipOrLoveFile(File f) {
		LoveVM.LoveLog(TAG,"love archive:"+f.getPath());
		
		// check if main.lua exists
		if (LoveZip.zipPeekHasMainLua(f)) {
			addGameToList(f.getPath(), f.getPath());
		} else {
			LoveVM.LoveLog(TAG,"love archive: no main.lua found in:"+f.getPath());
		}
	}
		
	private void tryToPopulateGameFromDirectory(File dir) {
		LoveVM.LoveLog(TAG,"love folder:"+dir.getPath());

		File main = new File(dir.getPath() + "/main.lua");
		if (main.isFile()) {
			// read gamename from conf.lua
			String name = loadTitleFromConfigInPath(dir.getPath());
			addGameToList(name, dir.getPath());
		}
	}

	private String loadTitleFromConfigInPath(String path) {
		try {
			LoveConfig config = new LoveConfig();
			config.title = path;
			LoveVM.loadConfigFromFile(config, new LoveStorage(this, path),
					"conf.lua");
			return config.title;
		} catch (Exception e) {
			return path;
		}
	}

	private void addGameToList(final String name, final String path) {
		gameListName.add(name);
		gameListPath.add(path);
	}

	private void launchGame(String name, String path) {
		launchMeGameName = name;
		launchMeGamePath = path;

		Intent intent = new Intent(this, LoveAndroid.class);
		startActivity(intent);
	}
}
