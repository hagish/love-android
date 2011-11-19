package net.schattenkind.androidLove;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.LoveStorage.FileType;
import net.schattenkind.androidLove.luan.LuanAudio;
import net.schattenkind.androidLove.luan.LuanEvent;
import net.schattenkind.androidLove.luan.LuanFilesystem;
import net.schattenkind.androidLove.luan.LuanGraphics;
import net.schattenkind.androidLove.luan.LuanJoystick;
import net.schattenkind.androidLove.luan.LuanKeyboard;
import net.schattenkind.androidLove.luan.LuanMouse;
import net.schattenkind.androidLove.luan.LuanPhone;
import net.schattenkind.androidLove.luan.LuanPhysics;
import net.schattenkind.androidLove.luan.LuanThread;
import net.schattenkind.androidLove.luan.LuanTimer;

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
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class LoveVM {
	// TODO: disable for release
	private static final boolean loggingEnabled = true;
	
	private static final String TAG = "LoveVM";
	private Activity attachedToThisActivity;
	private LuaValue _G;

	private LuanPhysics mLuanPhysics;
	private LuanGraphics mLuanGraphics;
	private LuanAudio mLuanAudio;
	private LuanMouse mLuanMouse;
	private LuanKeyboard mLuanKeyboard;
	private LuanJoystick mLuanJoystick;
	private LuanTimer mLuanTimer;
	private LuanEvent mLuanEvent;
	private LuanFilesystem mLuanFilesystem;
	private LuanPhone mLuanPhone;
	private LuanThread mLuanThread;

	private LoveConfig config;

	public float mfScreenW;
	public float mfScreenH;
	public long mfLastUpdateTick = 0;
	public boolean bCallUpdateDuringDraw = true; // false leads to weird
													// behavior in
													// love_for_zombies

	public boolean bInitCondition_ScreenSize = false;
	public boolean bInitCondition_NotifyGL = false;
	public boolean bInitCondition_onCreate = false;
	public boolean bInitCondition_initAlreadyCalled = false;

	private GL10 gl;
	private boolean bInitDone = false;
	private boolean isBroken = false;
	private boolean bInitInProgress = false;

	private LoveStorage storage;
	
	private SensorManager mSensorMgr;

	// ***** ***** ***** ***** ***** constructor
	
	public LoveVM(Activity attachedToThisActivity, LoveStorage storage) {
		this.attachedToThisActivity = attachedToThisActivity;
		this.storage = storage;
		this.config = new LoveConfig();
		mSensorMgr = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
	}

	// ***** ***** ***** ***** ***** log 
	
	public static void logException(Exception e) {
		LoveLogE(TAG, e.getMessage(), e);
	}
	
	public static void LoveLogE(String sTag,String sTxt,Exception e) {
		Log.e(sTag,sTxt, e);
	}
	
	public static void LoveLogE(String sTag,String sTxt) {
		Log.e(sTag,sTxt);
	}
	
	public static void LoveLog(String sTag,String sTxt) {
		if(loggingEnabled)Log.i(sTag, sTxt.toString());
	}
	
	public static void LoveLog(String s) { LoveLog(TAG,s); }
	

	// ***** ***** ***** ***** ***** init
	
	/// check if all ainit conditions are satisified and if yes run init if it hasn't been run already
	public void checkAllInitOk() {
		if (!bInitDone && gl != null && bInitCondition_onCreate
				&& bInitCondition_ScreenSize && bInitCondition_NotifyGL
				&& !bInitCondition_initAlreadyCalled) {
			bInitCondition_initAlreadyCalled = true;
			init();
		}
	}

	// / may only be called after BOTH notifyGL AND notifyOnCreateDone have been
	// called
	public void init() {
		assert (!bInitDone); // don't init twice
		if (bInitInProgress)
			return; // multithread problem??
		bInitInProgress = true;

		// _G = JsePlatform.standardGlobals();
		_G = JsePlatform.debugGlobals();

		try {
			setupCoreFunctions();
			setupLoveFunctions();

			LoveLog("exec pairs_hack.lua...");
			loadFileFromRes(R.raw.pairs_hack, "pairs_hack.lua");

			LoveLog("exec core.lua...");
			loadFileFromRes(R.raw.core, "core.lua");

			LoveLog("exec conf.lua...");
			loadConfig();
			
			LoveLog("exec bootstrap.lua...");
			loadFileFromRes(R.raw.bootstrap, "bootstrap.lua");

			LoveLog("exec main.lua...");
			loadFileFromSdCard("main.lua");
		} catch (LuaError e) {
			handleLuaError(e);
		}

		// call love.load() in main.lua
		this.load();
		bInitDone = true;
		bInitInProgress = false;
	}
	
	/// call user defined love.load() in main.lua
	public void load() {
		assert (bInitDone);
		try {
			LoveLog("calling love.load...");
			_G.get("love").get("load").call();
		} catch (LuaError e) {
			handleLuaError(e);
		}
	}
	
	// ***** ***** ***** ***** ***** notifications
	
	
	// seems to be called LATE! after notifyGL, so made this an init condition
	// to avoid screensize being unavailable during love.load
	public void notifyScreenSize(float w, float h) {
		LoveLog("notifyScreenSize:" + w + "," + h);
		mfScreenW = w;
		mfScreenH = h;
		bInitCondition_ScreenSize = true;
		checkAllInitOk();
	}

	// / called when gl context is created or updated
	public void notifyGL(GL10 gl) {
		if (!bInitCondition_NotifyGL)
			LoveLog("notifyGL");
		this.gl = gl;
		bInitCondition_NotifyGL = true;
		checkAllInitOk();
	}

	// / called when activity.onCreate has finished setting up the window
	public void notifyOnCreateDone() {
		LoveLog("notifyOnCreateDone");
		bInitCondition_onCreate = true;
		checkAllInitOk();
	}
	
	public void notifyUpdateTimerMainThread(float dt) {
		if (!bCallUpdateDuringDraw)
			update(dt);
	}
	
	// ***** ***** ***** ***** ***** lua api
	
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

				LoveLog(TAG, s.toString());

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


	private void setupLoveFunctions() {
		_G.set("love", LuaValue.tableOf());

		mLuanGraphics = new LuanGraphics(this);
		_G.get("love").set("graphics", mLuanGraphics.InitLib());
		
		mLuanPhysics = new LuanPhysics(this);
		_G.get("love").set("physics", mLuanPhysics.InitLib());

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

		// phone/android specific api extensions
		mLuanPhone = new LuanPhone(this);
		_G.get("love").set("phone", mLuanPhone.InitLib());
	}

	
	// ***** ***** ***** ***** ***** errors, warnings, notifications
	
	public void handleError(Exception e) {
		LoveLogE(TAG, "ERROR: " + e.getMessage());
		toast("ERROR: " + e.getMessage());
		isBroken = true;
	}

	public void handleLuaError(LuaError e) {
		LoveLogE(TAG, "LUA ERROR: " + e.getMessage());
		toast("LUA ERROR: " + e.getMessage());
		isBroken = true;
	}

	HashSet<String> mSetKnownNotImplemented = new HashSet<String>();

	/// warn first time, then ignore
	public void NotImplemented(String s) {
		if (mSetKnownNotImplemented.contains(s))
			return;
		mSetKnownNotImplemented.add(s);
		LoveLogE(TAG, "WARNING:NotImplemented: " + s);
	}
	
	/// android style popup notification
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
	
	// ***** ***** ***** ***** ***** draw
	
	public void draw(GL10 gl) {
		if (!bInitDone || isBroken)
			return;
		mLuanTimer.notifyFrameStart();

		if (bCallUpdateDuringDraw) {
			long t = mLuanTimer.getLoveClockMillis();
			if (mfLastUpdateTick == 0)
				mfLastUpdateTick = t;
			long dt = t - mfLastUpdateTick;
			mfLastUpdateTick = t;
			this.update(dt / 1000.0f);
		}

		mLuanGraphics.notifyFrameStart(gl);
		// ~ LoveLog("calling love.draw...");
		try {
			_G.get("love").get("draw").call();
		} catch (LuaError e) {
			handleLuaError(e);
		}
		mLuanGraphics.notifyFrameEnd(gl);
	}


	// ***** ***** ***** ***** ***** update
	
	public void update(float dt) {
		if (!bInitDone || isBroken)
			return;
		// WARNING! must be called in mainthread, since gl can be accessed, not
		// multi-thread safe with draw ! (this is violated if
		// bCallUpdateDuringDraw=true! otherwise we get weird behavior in
		// love_for_zombies tho...)

		// ~ LoveLog("calling love.update..."+dt);
		try {
			_G.get("love").get("update").call(LuaNumber.valueOf(dt));
		} catch (LuaError e) {
			handleLuaError(e);
		}
	}

	
	// ***** ***** ***** ***** ***** input
	
	public void feedPosition(int x, int y) {
		if (!bInitDone)
			return;
		try {
			mLuanMouse.feedPosition(x, y);
		} catch(Exception e)
		{
			LoveVM.logException(e);
		}
	}

	public void feedButtonState(boolean left, boolean middle, boolean right) {
		if (!bInitDone)
			return;
		
		try {
			mLuanMouse.feedButtonState(left, middle, right);
		} catch(Exception e)
		{
			LoveVM.logException(e);
		}
	}

	public boolean feedKey(int keyCode, boolean isDown) {
		try {
			return mLuanKeyboard.feedKey(keyCode, isDown);
		} catch(Exception e)
		{
			LoveVM.logException(e);
			return false;
		}
		
	}
	
	public int convertMouseX(int mouseX, int mouseY) {
		return mLuanGraphics.convertMouseX(mouseX, mouseY);
	}

	public int convertMouseY(int mouseX, int mouseY) {
		return mLuanGraphics.convertMouseY(mouseX, mouseY);
	}
	

	// ***** ***** ***** ***** ***** api access to other modules
	
	/// check this before calling event-callbacks etc
	public boolean isInitDone () { return bInitDone; }
	
	public Activity getActivity() {
		return attachedToThisActivity;
	}
	
	public LoveStorage getStorage() {
		return storage;
	}
	public LuanGraphics getLuanGraphics() {
		return mLuanGraphics;
	}
	
	public LuanAudio getLuanAudio() {
		return mLuanAudio;
	}
	
	public LuanPhone getLuanPhone() {
		return mLuanPhone;
	}
	
	public android.view.View getView() {
		return ((LoveAndroid)getActivity()).getView();
	}
	
	public LoveConfig getConfig() { return config; }

	public Context getContext () { return attachedToThisActivity; }
	
	public SensorManager getSensorManager () { return mSensorMgr; }
	
	// / access to latest valid gl object
	public GL10 getGL() {
		return gl;
	}

	public LuaValue get_G() {
		return _G;
	}
	
	// ***** ***** ***** ***** ***** file access functions
	
	private void loadConfig() {
		try {
			String confFile = "conf.lua";
			
			loadConfigFromFile(config, storage, confFile);
			if (storage.getFileType(confFile) == FileType.FILE)
			{
				loadFileFromSdCard(confFile);
			}
		} catch (Exception e) {
			LoveLogE(TAG, "error loading config file", e);
		}
	}
	
	public static void loadConfigFromFile(LoveConfig config,
			LoveStorage storage, String filename) throws FileNotFoundException,
			IOException, LuaError {

		if (storage.getFileType(filename) == FileType.FILE) {
			config.loadFromFileStream(storage.getFileStreamFromLovePath(filename));
		}
	}

	private void loadFileFromRes(int id, String filename) {
		try {
			LoadState.load(getResourceInputStream(id), filename, _G).call();
		} catch (NotFoundException e) {
			LoveLogE(TAG, e.getMessage());
		} catch (IOException e) {
			LoveLogE(TAG, e.getMessage());
		}
	}

	private void loadFileFromSdCard(String filename) {
		try {
			LoadState.load(storage.getFileStreamFromLovePath(filename), filename, _G).call();
		} catch (FileNotFoundException e) {
			LoveLogE(TAG, e.getMessage());
		} catch (IOException e) {
			LoveLogE(TAG, e.getMessage());
		}
	}
	
	public Resources getResources() {
		return attachedToThisActivity.getResources();
	}

	public InputStream getResourceInputStream (int id) { return storage.getResourceInputStream(id); }
	
	public void shutdown()
	{
		// TODO
	}
	
}
