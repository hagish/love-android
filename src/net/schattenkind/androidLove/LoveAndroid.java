package net.schattenkind.androidLove;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class LoveAndroid extends ActivitiyWithExitMenu {
	private static final long updateDelayMillis = 1000 / 30;
	private LoveVM vm;
	private GLSurfaceView mGLView;

	//~ private final static String		kGamePath = "/mnt/sdcard/love/clouds/";
	//~ private final static String		kGamePath = "/mnt/sdcard/love/iyfct/";
	//~ private final static String		kGamePath = "/mnt/sdcard/love/love_for_zombies/";
	private final static String		kGamePath = "/mnt/sdcard/love/test/";

	@SuppressWarnings("unused")
	private MouseHandler mouseHandler;

	private class UpdateHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			LoveAndroid.this._update();
		}

		public void start() {
			sendMessageDelayed(obtainMessage(0), updateDelayMillis);
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	private UpdateHandler mUpdateHandler = new UpdateHandler();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return vm.feedKey(keyCode, true);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return vm.feedKey(keyCode, false);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public void _update() {
		vm.notifyUpdateTimerMainThread(updateDelayMillis / 1000.0f);
		mUpdateHandler.sleep(updateDelayMillis);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String path = kGamePath;

		if (Launcher.launchMeGamePath != null) {
			path = Launcher.launchMeGamePath;
		}

		vm = new LoveVM(this, new LoveStorage(this, path));

		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity.
		mGLView = new HelloOpenGLES10SurfaceView(this, vm);
		setContentView(mGLView);

		vm.notifyOnCreateDone();

		mUpdateHandler.start();

		mouseHandler = new MouseHandler(mGLView, vm);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();
	}
}

class HelloOpenGLES10SurfaceView extends GLSurfaceView {

	public HelloOpenGLES10SurfaceView(Context context, LoveVM vm) {
		super(context);

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(new LoveAndroidRenderer(vm));
	}
}
