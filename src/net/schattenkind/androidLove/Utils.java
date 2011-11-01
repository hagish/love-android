package net.schattenkind.androidLove;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
	public static String fileInStreamToString(FileInputStream fin) throws IOException {
		DataInputStream in = new DataInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		StringBuffer b = new StringBuffer();

		String strLine;
		while ((strLine = br.readLine()) != null) {
			b.append(strLine);
		}

		return b.toString();
	}
}
