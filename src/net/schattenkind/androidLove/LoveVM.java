package net.schattenkind.androidLove;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import android.app.Activity;
import android.util.Log;

public class LoveVM {
	private static final String TAG = "LoveVM";
	private Activity attachedToThisActivity;
	private LuaValue _G;
	private LuanGraphics mLuanGraphics;

	public LoveVM(Activity attachedToThisActivity) {
		this.attachedToThisActivity = attachedToThisActivity;
		mLuanGraphics = new LuanGraphics(attachedToThisActivity);
	}

	public void init() {
		_G = JsePlatform.standardGlobals();

		setupCoreFunctions();
		setupLoveFunctions();
		loadFile("core.lua");
	}

	private void loadFile(String filename) {
		try {
			LoadState.load(attachedToThisActivity.openFileInput(filename),
					filename, _G).call();
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void setupCoreFunctions() {
		_G.set("print", new VarArgFunction() {
			@Override
			public LuaValue invoke(Varargs args) {
				args.narg();

				StringBuffer s = new StringBuffer();

				for (int i = 1; i <= args.narg(); ++i) {
					if (i > 1) {
						s.append(", ");
					}
					s.append(args.arg(i).checkstring().toString());
				}

				Log.i(TAG, s.toString());

				return LuaValue.NONE;
			}
		});

	}

	private void setupLoveFunctions() {
		_G.set("love", LuaValue.tableOf());
		LuaTable t = mLuanGraphics.InitLib();
		_G.get("love").set("graphics", t);
	}

	public void load() {
		_G.get("love").get("load").call();
	}

	public void draw(GL10 gl) {
		_G.get("love").get("draw").call();
	}

	public void update(float dt) {
		_G.get("love").get("update").call(LuaNumber.valueOf(dt));
	}
}
