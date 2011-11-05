package net.schattenkind.androidLove;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Launcher extends Activity {
	public static String launchMeGameName;
	public static String launchMeGamePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);

		// add games
		addGameStartButton("clouds", "/love/clouds/");
		addGameStartButton("in your face", "/love/iyfct/");
		addGameStartButton("stealth2d", "/love/Stealth2D/");

		// hide template button
		Button exampleButton = (Button) findViewById(R.id.exampleButton);
		exampleButton.setVisibility(View.GONE);
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
