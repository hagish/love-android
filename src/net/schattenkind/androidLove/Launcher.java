package net.schattenkind.androidLove;

import java.io.File;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Launcher extends Activity {
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
				
				launchGame(gameListName.get(position), gameListPath.get(position));
			}
		});
	}

	private void populateGameList() {
		File rootDir = new File(Environment.getExternalStorageDirectory() + "/"
				+ loveRootOnSdCard);

		File[] files = rootDir.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				tryToPopulateGameFromDirectory(f);
			}
		}
	}

	private void tryToPopulateGameFromDirectory(File path) {
		System.out.println(path.getPath());

		File main = new File(path.getPath() + "/main.lua");
		if (main.isFile()) {
			// TODO read gamename from conf.lua
			String name = path.getPath();
			addGameToList(name, path.getPath());
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
