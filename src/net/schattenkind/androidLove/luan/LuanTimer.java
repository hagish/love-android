package net.schattenkind.androidLove.luan;

import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.os.SystemClock;
import android.util.Log;

public class LuanTimer {
	private float lastFrameStartInSec;
	private float lastFrameDeltaInSec;
	private long timerStartInMs;

	public LuanTimer(LuaValue _G) {
		timerStartInMs = SystemClock.uptimeMillis();
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// dt = love.timer.getDelta( )
		t.set("getDelta", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaNumber.valueOf(lastFrameDeltaInSec);
			}
		});

		// fps = love.timer.getFPS( )
		t.set("getFPS", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				float fps = 0.0f;

				if (lastFrameDeltaInSec > 0.0f) {
					fps = 1.0f / lastFrameDeltaInSec;
				}

				return LuaNumber.valueOf(fps);
			}
		});

		// t = love.timer.getMicroTime( )
		t.set("getMicroTime", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaNumber.valueOf(getTime());
			}
		});

		// time = love.timer.getTime( )
		t.set("getTime", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaNumber.valueOf(getTime());
			}
		});

		// love.timer.sleep( ms )
		t.set("sleep", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				try {
					Thread.sleep(args.arg1().toint(), 0);
				} catch (InterruptedException e) {
					Log.e("LuanTimer", e.getMessage());
				}

				return LuaValue.NONE;
			}
		});

		// love.timer.step( )
		t.set("step", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				lastFrameStartInSec = getTime();

				return LuaValue.NONE;
			}
		});

		return t;
	}

	public void notifyFrameStart() {
		float timeInSec = getTime();
		lastFrameDeltaInSec = timeInSec - lastFrameStartInSec;
		lastFrameStartInSec = timeInSec;
	}

	private float getTime() {
		return (float) (SystemClock.uptimeMillis() - timerStartInMs) / 1000.0f;
	}

}
