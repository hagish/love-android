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
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

public class LoveStorage {
	public enum FileType {
		FILE, DIR, NONE
	};

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
		File f = new File(loveAppRootOnSdCard + "/" + filename);
		return new FileInputStream(f);
	}

	public String getRootPath() {
		return loveAppRootOnSdCard;
	}

	public FileType getFileType(String filename) {
		// TODO
		return FileType.NONE;
	}

	public List<String> getLines(String filename) throws IOException {
		FileInputStream fin = getFileStreamFromSdCard(filename);
		DataInputStream in = new DataInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		LinkedList<String> lines = new LinkedList<String>();

		String strLine;
		while ((strLine = br.readLine()) != null) {
			lines.add(strLine);
		}

		return lines;
	}
}
