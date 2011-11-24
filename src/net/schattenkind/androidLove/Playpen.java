package net.schattenkind.androidLove;

import java.io.File;

import net.schattenkind.androidLove.utils.FontRasterizer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

public class Playpen extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playpen);
		
		
		FontRasterizer r = new FontRasterizer(new File("/mnt/sdcard/love/dye/media/wendy.ttf"));
		
		Bitmap b = r.renderBitmapFont("abcABC123 !§$%",  20, 3, Color.MAGENTA, Color.TRANSPARENT, Color.WHITE);
		
		ImageView img = new ImageView(this);
		img.setImageBitmap(b);
		
		setContentView(img);
	}
}
