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
import android.content.res.Resources.NotFoundException;
import android.util.Log;

public class LoveVM {
	private static final String TAG = "LoveVM";
	private Activity attachedToThisActivity;
	private LuaValue _G;

	private LuanGraphics mLuanGraphics;
	private LuanMouse mLuanMouse;
	private LuanKeyboard mLuanKeyboard;
	private LuanTimer mLuanTimer;

	private GL10 gl;
	private boolean bOnCreateDone = false;
	private boolean bInitDone = false;

	public LoveVM(Activity attachedToThisActivity) {
		this.attachedToThisActivity = attachedToThisActivity;
	}

	// / access to latest valid gl object
	public GL10 getGL() {
		return gl;
	}

	// / called when gl context is created or updated
	public void notifyGL(GL10 gl) {
		this.gl = gl;
		if (!bInitDone && bOnCreateDone)
			init();
	}

	// / called when activity.onCreate has finished setting up the window
	public void notifyOnCreateDone() {
		bOnCreateDone = true;
		if (!bInitDone && gl != null)
			init();
	}

	// / may only be called after BOTH notifyGL AND notifyOnCreateDone have been
	// called
	public void init() {
		assert (!bInitDone); // don't init twice
		bInitDone = true;
		_G = JsePlatform.standardGlobals();

		setupCoreFunctions();
		setupLoveFunctions();

		loadFileFromRes(R.raw.core, "core.lua");

		loadFileFromSdCard("main.lua");

		this.load();
	}

	private void loadFileFromRes(int id, String filename) {
		try {
			LoadState.load(
					attachedToThisActivity.getResources().openRawResource(id),
					filename, _G).call();
		} catch (NotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void loadFileFromSdCard(String filename) {
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

		mLuanGraphics = new LuanGraphics(this, attachedToThisActivity);
		t = mLuanGraphics.InitLib();
		_G.get("love").set("graphics", t);

		mLuanMouse = new LuanMouse(_G);
		t = mLuanMouse.InitLib();
		_G.get("love").set("mouse", t);

		mLuanKeyboard = new LuanKeyboard(_G);
		t = mLuanKeyboard.InitLib();
		_G.get("love").set("keyboard", t);

		mLuanTimer = new LuanTimer(_G);
		t = mLuanTimer.InitLib();
		_G.get("love").set("timer", t);
	}

	public void load() {
		assert (bInitDone);
		_G.get("love").get("load").call();
	}

	public void draw() {
		if (!bInitDone)
			return;
		mLuanTimer.notifyFrameStart();
		_G.get("love").get("draw").call();
	}

	public void update(float dt) {
		if (!bInitDone)
			return;
		// WARNING! must be called in mainthread, since gl can be accessed, not
		// multi-thread safe with draw !
		_G.get("love").get("update").call(LuaNumber.valueOf(dt));
	}

	public void feedPosition(int x, int y) {
		if (!bInitDone)
			return;
		mLuanMouse.feedPosition(x, y);
	}

	public void feedButtonState(boolean left, boolean middle, boolean right) {
		if (!bInitDone)
			return;
		mLuanMouse.feedButtonState(left, middle, right);
	}

	public boolean feedKey(int keyCode, boolean isDown) {
		return mLuanKeyboard.feedKey(keyCode, isDown);
	}
}
