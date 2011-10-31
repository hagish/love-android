package net.schattenkind.androidLove;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public class InputHandler implements OnClickListener, OnLongClickListener,
		OnKeyListener, OnTouchListener {
	private LoveVM vm;
	private View view;

	public InputHandler(View view, LoveVM vm) {
		this.view = view;
		this.vm = vm;

		this.view.setOnClickListener(this);
		this.view.setOnLongClickListener(this);
		this.view.setOnKeyListener(this);
		this.view.setOnTouchListener(this);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		vm.feedPosition(x, y);

		int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN) {
			vm.feedButtonState(true, false, false);
		} else if (action == MotionEvent.ACTION_UP) {
			vm.feedButtonState(false, false, false);
		}

		return false;
	}
}
