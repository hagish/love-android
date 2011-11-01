package net.schattenkind.androidLove;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

public class LoveStorage {
	private Activity activity;
	private String loveAppRootOnSdCard;

	public LoveStorage(Activity activity, String loveAppRootOnSdCard) {
		this.activity = activity;
		this.loveAppRootOnSdCard = loveAppRootOnSdCard;
	}

	public void setAppRootOnSdCard(String loveAppRootOnSdCard) {
		this.loveAppRootOnSdCard = loveAppRootOnSdCard;
	}

	public BitmapDrawable loadBitmapDrawable(String file)
			throws FileNotFoundException {
		InputStream input = getFileStreamFromSdCard(file);
		// ressources needed for "density" / dpi etc ? no idea
		BitmapDrawable bmd = new BitmapDrawable(activity.getResources(), input);
		return bmd;
	}

	public FileInputStream getFileStreamFromSdCard(String filename)
			throws FileNotFoundException {
		File f = new File(Environment.getExternalStorageDirectory() + "/"
				+ loveAppRootOnSdCard + "/" + filename);
		return new FileInputStream(f);
	}
}
