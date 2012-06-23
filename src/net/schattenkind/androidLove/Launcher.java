package net.schattenkind.androidLove;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;

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

	public int miGamesFound = 0;

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

				// only launch if path is set, avoid info-entries
				if (gameListPath.get(position).length() > 0) {
					launchGame(gameListName.get(position),
							gameListPath.get(position));
				}
			}
		});
	}

	public static boolean isFileLoveZip(File f) {
		return f.isFile()
				&& (f.getName().endsWith(".love") || f.getName().endsWith(
						".zip"));
	}

	// / name filter for dir.listFiles, e.g. list only .love files
	public class FilenameFilterEndsWith implements FilenameFilter {
		String sEndsWith;

		public FilenameFilterEndsWith(String sEndsWith) {
			this.sEndsWith = sEndsWith;
		}

		public boolean accept(File dir, String name) {
			return name.endsWith(sEndsWith);
		}
	}

	private void populateGameList() {
		miGamesFound = 0;
		String sPathDown = "";
		String sPathSD = "";
		
		
		try {
			LoveVM.MyDummy();
		} catch (Throwable t) {
			// wtf ? i really get this case on emulator 2012-06-23
			addGameToList("ERROR: LoveVM methods not accessible", "");
			return;
		}
		
		// scan sd card (/mnt/sdcard/love)
		try {
			File dir = new File(Environment.getExternalStorageDirectory() + "/"
					+ loveRootOnSdCard);
			sPathSD = dir.getPath();

			File[] files = dir.listFiles();
			for (File f : files) {
				try {
					if (f.isDirectory()) {
						tryToPopulateGameFromDirectory(f);
					}
					if (isFileLoveZip(f)) {
						tryToPopulateGameFromZipOrLoveFile(f);
					}
				} catch (Throwable t) {
					// at least continue trying with other entries
					addGameToList("FAILED(1):"+f.getName(), "");
				}
			}
		} catch (Exception e) {
			// avoid crash if folder doesn't exist
		} catch (Throwable t) {
			// some java linking error, cannot call LoveVM methods etc. java.lang.VerifyError: net.schattenkind.androidLove.LoveVM ?
		}

		// scan downloads folder for .love files
		try {
			// ex Environment.DIRECTORY_DOWNLOADS
			File dir = Environment.getDownloadCacheDirectory();
			sPathDown = dir.getPath();
			File[] files = dir.listFiles(new FilenameFilterEndsWith(".love")); // don't
																				// scan
																				// all
																				// zip
																				// files
			for (File f : files) {
				try {
					if (isFileLoveZip(f)) {
						tryToPopulateGameFromZipOrLoveFile(f);
					}
				} catch (Throwable t) {
					// at least continue trying with other entries
					addGameToList("FAILED(2):"+f.getName(), "");
				}
			}

		} catch (Exception e) {
			// avoid crash folder doesn't exist
		} catch (Throwable t) {
			// some java linking error, cannot call LoveVM methods etc. java.lang.VerifyError: net.schattenkind.androidLove.LoveVM ?
		}

		// no games found, display hints :
		if (miGamesFound == 0) {
			addGameToList("no games found", "");
			addGameToList("add some .love files", "");
			addGameToList("to your sdcard:", "");
			addGameToList(sPathSD, "");
			addGameToList("or downloads folder:", "");
			addGameToList(sPathDown, "");
		}
	}

	private void tryToPopulateGameFromZipOrLoveFile(File f) {
		LoveVM.LoveLog(TAG, "love archive:" + f.getPath());

		// check if main.lua exists
		if (LoveZip.zipPeekHasMainLua(f)) {
			addGameToList(f.getPath(), f.getPath());
		} else {
			LoveVM.LoveLog(TAG,
					"love archive: no main.lua found in:" + f.getPath());
		}
	}

	private void tryToPopulateGameFromDirectory(File dir) {
		LoveVM.LoveLog(TAG, "love folder:" + dir.getPath());

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

			LoveStorage storage = new LoveStorage(path);
			storage.assignActivity(this);
			LoveVM.loadConfigFromFile(config, storage, "conf.lua");

			return config.title;
		} catch (Exception e) {
			return path;
		}
	}

	private void addGameToList(final String name, final String path) {
		gameListName.add(name);
		gameListPath.add(path);
		++miGamesFound;
	}

	private void launchGame(String name, String path) {
		launchMeGameName = name;
		launchMeGamePath = path;
		
		LoveAndroid.shutdownRunningVM();

		Intent intent = new Intent(this, LoveAndroid.class);
		startActivity(intent);
	}
}
