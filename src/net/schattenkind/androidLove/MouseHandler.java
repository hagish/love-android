package net.schattenkind.androidLove;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public class MouseHandler implements OnClickListener, OnLongClickListener,
		OnTouchListener {
	private LoveVM vm;
	private View view;

	public MouseHandler(View view, LoveVM vm) {
		this.view = view;
		this.vm = vm;

		this.view.setOnClickListener(this);
		this.view.setOnLongClickListener(this);
		this.view.setOnTouchListener(this);
	}

	//~ @Override // compile error on arch linux sun java
	public boolean onLongClick(View v) {
		return false;
	}

	//~ @Override // compile error on arch linux sun java
	public void onClick(View v) {
	}

	//~ @Override // compile error on arch linux sun java
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

		return true;
	}
}
