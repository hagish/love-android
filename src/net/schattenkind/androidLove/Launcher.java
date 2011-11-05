package net.schattenkind.androidLove;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Launcher extends Activity {
	private static final String loveRootOnSdCard = "/love/";
	public static String launchMeGameName;
	public static String launchMeGamePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);

		populateGameList();

		// hide template button
		Button exampleButton = (Button) findViewById(R.id.exampleButton);
		exampleButton.setVisibility(View.GONE);
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
			addGameStartButton(name, path.getPath());
		}
	}

	private void addGameStartButton(final String name, final String path) {
		LinearLayout gameList = (LinearLayout) findViewById(R.id.gameList);

		Button exampleButton = (Button) findViewById(R.id.exampleButton);

		Button gameStartButton = new Button(getApplicationContext());
		gameStartButton.setLayoutParams(exampleButton.getLayoutParams());

		gameStartButton.setText(name);

		gameStartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				launchGame(name, path);
			}
		});

		gameList.addView(gameStartButton);
	}

	private void launchGame(String name, String path) {
		launchMeGameName = name;
		launchMeGamePath = path;

		Intent intent = new Intent(this, LoveAndroid.class);
		startActivity(intent);
	}
}
