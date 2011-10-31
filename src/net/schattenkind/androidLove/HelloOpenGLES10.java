package net.schattenkind.androidLove;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class HelloOpenGLES10 extends Activity {
	private LoveVM vm;
	private GLSurfaceView mGLView;
	
	@Override
	public void onStop() {
		super.onStop();
		// TODO hack to kill the process on home button
		System.exit(0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity.
		mGLView = new HelloOpenGLES10SurfaceView(this);
		setContentView(mGLView);
		
		vm = new LoveVM(this);
		vm.init();
		vm.load();
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

	public HelloOpenGLES10SurfaceView(Context context) {
		super(context);

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(new HelloOpenGLES10Renderer());
	}
}