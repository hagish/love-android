package net.schattenkind.androidLove;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.luan.LuanAudio;
import net.schattenkind.androidLove.luan.LuanFilesystem;
import net.schattenkind.androidLove.luan.LuanGraphics;
import net.schattenkind.androidLove.luan.LuanKeyboard;
import net.schattenkind.androidLove.luan.LuanMouse;
import net.schattenkind.androidLove.luan.LuanJoystick;
import net.schattenkind.androidLove.luan.LuanTimer;
import net.schattenkind.androidLove.luan.LuanEvent;
import net.schattenkind.androidLove.luan.LuanAndroid;
import net.schattenkind.androidLove.luan.LuanThread;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.widget.Toast;

public class LoveVM {
	private static final String TAG = "LoveVM";
	private Activity attachedToThisActivity;
	private LuaValue _G;

	public void RobLog (String s) { Log.i("LoveVMRobLog", s.toString()); }
	
	public InputStream getResourceInputStream(int id) { return attachedToThisActivity.getResources().openRawResource(id); }
	
	private LuanGraphics mLuanGraphics;
	private LuanAudio mLuanAudio;
	private LuanMouse mLuanMouse;
	private LuanKeyboard mLuanKeyboard;
	private LuanJoystick mLuanJoystick;
	private LuanTimer mLuanTimer;
	private LuanEvent mLuanEvent;
	private LuanFilesystem mLuanFilesystem;
	private LuanAndroid mLuanAndroid;
	private LuanThread mLuanThread;
	public float mfScreenW;
	public float mfScreenH;
	public long mfLastUpdateTick = 0;
	public boolean bCallUpdateDuringDraw = true; // false leads to weird behavior in love_for_zombies
	
	public boolean bInitCondition_ScreenSize = false;
	public boolean bInitCondition_NotifyGL = false;
	public boolean bInitCondition_onCreate = false;
	public boolean bInitCondition_initAlreadyCalled = false;

	private GL10 gl;
	private boolean bInitDone = false;
	private boolean isBroken = false;
	private boolean bInitInProgress = false;

	private LoveStorage storage;

	public LoveVM(Activity attachedToThisActivity, LoveStorage storage) {
		this.attachedToThisActivity = attachedToThisActivity;
		this.storage = storage;
	}
	
	public int convertMouseX(int mouseX,int mouseY) { return mLuanGraphics.convertMouseX(mouseX,mouseY); }
	public int convertMouseY(int mouseX,int mouseY) { return mLuanGraphics.convertMouseY(mouseX,mouseY); }
	
	// / access to latest valid gl object
	public GL10 getGL() {
		return gl;
	}

	public LuaValue get_G() {
		return _G;
	}
	
	public void checkAllInitOk () {
		if (!bInitDone && gl != null && bInitCondition_onCreate && bInitCondition_ScreenSize && bInitCondition_NotifyGL && !bInitCondition_initAlreadyCalled) {
			bInitCondition_initAlreadyCalled = true;
			init();
		}
	}
	
	// seems to be called LATE! after notifyGL, so made this an init condition to avoid screensize being unavailable during love.load
	public void notifyScreenSize (float w, float h) {
		RobLog("notifyScreenSize:"+w+","+h); 
		mfScreenW = w;
		mfScreenH = h;
		bInitCondition_ScreenSize = true;
		checkAllInitOk();
	}

	// / called when gl context is created or updated
	public void notifyGL(GL10 gl) {
		if (!bInitCondition_NotifyGL) RobLog("notifyGL");
		this.gl = gl;
		bInitCondition_NotifyGL = true;
		checkAllInitOk();
	}

	// / called when activity.onCreate has finished setting up the window
	public void notifyOnCreateDone() {
		RobLog("notifyOnCreateDone"); 
		bInitCondition_onCreate = true;
		checkAllInitOk();
	}

	// / may only be called after BOTH notifyGL AND notifyOnCreateDone have been
	// called
	public void init() {
		assert (!bInitDone); // don't init twice
		if (bInitInProgress) return; // multithread problem??
		bInitInProgress = true;
		// _G = JsePlatform.standardGlobals();
		_G = JsePlatform.debugGlobals();

		try {
			setupCoreFunctions();
			setupLoveFunctions();

			RobLog("exec core.lua...");
			loadFileFromRes(R.raw.core, "core.lua");

			RobLog("exec main.lua...");
			loadFileFromSdCard("main.lua");
		} catch (LuaError e) {
			handleLuaError(e);
		}

		this.load();
		bInitDone = true;
		bInitInProgress = false;
	}

	private void loadFileFromRes(int id, String filename) {
		try {
			LoadState.load(
					getResourceInputStream(id),
					filename, _G).call();
		} catch (NotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void loadFileFromSdCard(String filename) {
		try {
			LoadState.load(storage.getFileStreamFromSdCard(filename), filename,
					_G).call();
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

		_G.set("toast", new VarArgFunction() {
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

				toast(s.toString());

				return LuaValue.NONE;
			}
		});
	}

	public void toast(final String string) {
		attachedToThisActivity.runOnUiThread(new Runnable() {
			public void run() {
				Context context = attachedToThisActivity
						.getApplicationContext();
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, string, duration);
				toast.show();
			}
		});
	}

	private void setupLoveFunctions() {
		_G.set("love", LuaValue.tableOf());

		mLuanGraphics = new LuanGraphics(this);
		_G.get("love").set("graphics", mLuanGraphics.InitLib());

		mLuanAudio = new LuanAudio(this);
		_G.get("love").set("audio", mLuanAudio.InitLib());

		mLuanMouse = new LuanMouse(this);
		_G.get("love").set("mouse", mLuanMouse.InitLib());

		mLuanKeyboard = new LuanKeyboard(this);
		_G.get("love").set("keyboard", mLuanKeyboard.InitLib());

		mLuanJoystick = new LuanJoystick(this);
		_G.get("love").set("joystick", mLuanJoystick.InitLib());
		
		mLuanTimer = new LuanTimer(this);
		_G.get("love").set("timer", mLuanTimer.InitLib());
		
		mLuanEvent = new LuanEvent(this);
		_G.get("love").set("event", mLuanEvent.InitLib());

		mLuanFilesystem = new LuanFilesystem(this);
		_G.get("love").set("filesystem", mLuanFilesystem.InitLib());
		
		mLuanThread = new LuanThread(this);
		_G.get("love").set("thread", mLuanThread.InitLib());
		
		// set love.android, so android-aware games can detect it, might be used for additional util functions later
		mLuanAndroid = new LuanAndroid(this);
		_G.get("love").set("android", mLuanAndroid.InitLib());
	}

	public void load() {
		assert (bInitDone);
		try {
			RobLog("calling love.load...");
			_G.get("love").get("load").call();
		} catch (LuaError e) {
			handleLuaError(e);
		}
	}

	public void handleError(Exception e) {
		Log.e(TAG, "ERROR: " + e.getMessage());
		toast("ERROR: " + e.getMessage());
		isBroken = true;
	}

	public void handleLuaError(LuaError e) {
		Log.e(TAG, "LUA ERROR: " + e.getMessage());
		toast("LUA ERROR: " + e.getMessage());
		isBroken = true;
	}
	
	// warn first time, then ignore
	HashSet mSetKnownNotImplemented = new HashSet();
	public void NotImplemented(String s) {
		if (mSetKnownNotImplemented.contains(s)) return;
		mSetKnownNotImplemented.add(s);
		Log.e(TAG, "WARNING:NotImplemented: " + s);
	}

	public void draw(GL10 gl) {
		if (!bInitDone || isBroken)
			return;
		mLuanTimer.notifyFrameStart();
		
		if (bCallUpdateDuringDraw) {
			long t = mLuanTimer.getLoveClockMillis();
			if (mfLastUpdateTick == 0) mfLastUpdateTick = t;
			long dt = t - mfLastUpdateTick;
			mfLastUpdateTick = t;
			this.update(dt / 1000.0f);
		}
		
		mLuanGraphics.notifyFrameStart(gl);
		//~ RobLog("calling love.draw...");
		try {
			_G.get("love").get("draw").call();
		} catch (LuaError e) {
			handleLuaError(e);
		}
		mLuanGraphics.notifyFrameEnd(gl);
	}
	
	public void notifyUpdateTimerMainThread(float dt) {
		if (!bCallUpdateDuringDraw) update(dt);
	}

	public void update(float dt) {
		if (!bInitDone || isBroken)
			return;
		// WARNING! must be called in mainthread, since gl can be accessed, not
		// multi-thread safe with draw ! (this is violated if bCallUpdateDuringDraw=true! otherwise we get weird behavior in love_for_zombies tho...)

		//~ RobLog("calling love.update..."+dt);
		try {
			_G.get("love").get("update").call(LuaNumber.valueOf(dt));
		} catch (LuaError e) {
			handleLuaError(e);
		}
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

	public Resources getResources() {
		return attachedToThisActivity.getResources();
	}

	public LoveStorage getStorage() {
		return storage;
	}
}
