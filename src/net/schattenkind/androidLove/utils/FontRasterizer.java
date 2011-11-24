package net.schattenkind.androidLove.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class FontRasterizer {
	private Typeface typeface;

	public FontRasterizer(File fontFile) {
		typeface = Typeface.createFromFile(fontFile);
	}
	
	public Bitmap renderBitmapFont(String text, float size, int gapWidth, int gapColor, int backgroundColor, int fontColor)
	{
		Paint font = new Paint();
		font.setTextSize(size);
		font.setTypeface(typeface);
		font.setColor(fontColor);
		font.setTextAlign(Align.LEFT);

		int extraSpaceAroundLetters = 1;
		
		int w = (int)Math.ceil(font.measureText(text) + (text.length() + 1) * gapWidth + (text.length() * extraSpaceAroundLetters * 2));
		int h = (int)Math.ceil(size);
		
		Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(img);
		
		Paint background = new Paint();
		background.setColor(backgroundColor);
		canvas.drawRect(0f, 0f, (float)w, (float)h, background);
				
		Paint gap = new Paint();
		gap.setColor(gapColor);

		
		float x = 0f;
		
		for(int i = 0; i < text.length(); ++i)
		{
			canvas.drawRect(x, 0f, x + (float)gapWidth, (float)h, gap);
			x += (float)gapWidth;
			
			x += extraSpaceAroundLetters;

			canvas.drawText(text, i, i+1, x, (int)(-font.ascent()), font);
			x += font.measureText(text, i, i+1);

			x += extraSpaceAroundLetters;
		}

		canvas.drawRect(x, 0f, x + (float)gapWidth, (float)h, gap);
		
		return img;
	}
}
