package net.schattenkind.androidLove;

import java.io.FileNotFoundException;
import java.io.IOException;

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
	private LuanMouse mLuanMouse;

	public LoveVM(Activity attachedToThisActivity) {
		this.attachedToThisActivity = attachedToThisActivity;
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
					s.append(args.arg(i).toString());
				}

				Log.i(TAG, s.toString());

				return LuaValue.NONE;
			}
		});

	}

	private void setupLoveFunctions() {
		_G.set("love", LuaValue.tableOf());

		LuaTable t;

		mLuanGraphics = new LuanGraphics(attachedToThisActivity);
		t = mLuanGraphics.InitLib();
		_G.get("love").set("graphics", t);

		mLuanMouse = new LuanMouse(_G);
		t = mLuanMouse.InitLib();
		_G.get("love").set("mouse", t);
	}

	public void load() {
		_G.get("love").get("load").call();
	}

	public void draw() {
		_G.get("love").get("draw").call();
	}

	public void update(float dt) {
		_G.get("love").get("update").call(LuaNumber.valueOf(dt));
	}

	public void feedPosition(int x, int y) {
		mLuanMouse.feedPosition(x, y);
	}

	public void feedButtonState(boolean left, boolean middle, boolean right) {
		mLuanMouse.feedButtonState(left, middle, right);
	}
}
