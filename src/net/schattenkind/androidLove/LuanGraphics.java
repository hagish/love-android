package net.schattenkind.androidLove;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class LuanGraphics {
	private Activity mActivity;

	LuanGraphics (Activity activity) { mActivity = activity; }
	
	public void Log (String s) { Log.i("LuanGraphics", s); }
	public void LogException (Exception e) { Log.e("LuanGraphics",e.getMessage()); }
	
	public LuaTable InitLib () {
		LuaTable t = LuaValue.tableOf();

		// love.graphics.print(sText,x,y)
		t.set("print", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				int x = args.checkint(2);
				int y = args.checkint(3);
				Log("print:"+s);
				return LuaValue.NONE;
			}
		});

		// img = love.graphics.newImage(sFileName)
		t.set("newImage", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				try {
					return LuaValue.userdataOf(new LuanImage(s));
				} catch (Exception e) {
					// TODO : throw lua error ?
					LogException(e);
				}
				return LuaValue.NONE;
			}
		});

		// love.graphics.draw(img,x,y)
		t.set("draw", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// String s = args.checkjstring(1);
				int x = args.checkint(2);
				int y = args.checkint(3);
				return LuaValue.NONE;
			}
		});
		
		return t;
	}
	
		
	// ***** ***** ***** ***** *****  LuanImage

	public class LuanImage {
		public LuanImage (String filepath) throws FileNotFoundException {
			//~ http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/graphics/CompressedTextureActivity.html
			//~ von ressource : InputStream input = getResources().openRawResource(R.raw["bla.png"]);  // NICHT M�GLICH!!!! ->> sd card
			//~ von ressource : InputStream input = getResources().openRawResource(R.raw.androids);
			//~ von sd laden : InputStreamODERSO input = openFileInput("lala.lua");
			//~ http://gamedev.stackexchange.com/questions/10829/loading-png-textures-for-use-in-android-opengl-es1
			/*
				... GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bitmap, 0 );...
			
				Drawable image = resources.getDrawable( resourceID ); // http://developer.android.com/reference/android/graphics/drawable/Drawable.html
				bitmap = Bitmap.createBitmap( powWidth, powHeight, Bitmap.Config.ARGB_4444 );
				// get a canvas to paint over the bitmap
				Canvas canvas = new Canvas( bitmap );
				bitmap.eraseColor(0);
				image.draw( canvas ); // draw the image onto our bitmap
				-- call sth... GLUtils.texImage2D(image)....
				bitmap.recycle();
				
				//~ static Drawable 	Drawable.createFromStream(InputStream is, String srcName)
			*/

			
			Log.i("LuanImage","constructor:"+filepath);
			// TODO : throw lua error if file not found ?
			InputStream input = mActivity.openFileInput(filepath);
			//~ Drawable d = Drawable.createFromStream(input,filepath);
			Log.i("LuanImage","InputStream ok");
			
			BitmapDrawable bmd = new BitmapDrawable(mActivity.getResources(),input); // ressources needed for "density" / dpi etc ?  no idea
			Log.i("LuanImage","BitmapDrawable ok");
			Bitmap bm = bmd.getBitmap();
			Log.i("LuanImage","Bitmap ok w="+bm.getWidth()+",h="+bm.getHeight());
			
			//~ texImage2D
			Log.i("LuanImage","constructor done.");
		}
	}
}

