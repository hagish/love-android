package net.schattenkind.androidLove.luan;

import java.util.HashMap;

import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.view.KeyEvent;

public class LuanKeyboard {
	private float repeatDelay;
	private float repeatInterval;

	private LuaValue _G;

	private HashMap<String, Boolean> isLuaKeyDown = new HashMap<String, Boolean>();

	public LuanKeyboard(LuaValue _G) {
		this._G = _G;
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// delay, interval = love.keyboard.getKeyRepeat( )
		t.set("getKeyRepeat", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.varargsOf(LuaNumber.valueOf(repeatDelay),
						LuaNumber.valueOf(repeatInterval));
			}
		});

		// down = love.keyboard.isDown( key )
		t.set("isDown", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaBoolean.valueOf(isDown(args.arg1().toString()));
			}
		});

		// love.keyboard.setKeyRepeat( delay, interval )
		t.set("setKeyRepeat", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				repeatDelay = args.arg(1).tofloat();
				repeatInterval = args.arg(2).tofloat();

				return LuaValue.NONE;
			}
		});

		return t;
	}

	protected boolean isDown(String luaKeyName) {
		return isLuaKeyDown.containsKey(luaKeyName)
				&& isLuaKeyDown.get(luaKeyName);
	}

	private void triggerCallsbackIfNecessary(boolean oldState,
			boolean newState, String luaKeyCode) {
		if (oldState != newState) {
			String callback = "keypressed";
			if (oldState == true) {
				callback = "keyreleased";
			}

			// TODO unicode
			_G.get("love").get(callback).call(LuaString.valueOf(luaKeyCode));
		}
	}

	public boolean feedKey(int keyCode, boolean isDown) {
		String luaKeyCode = keyCodeToLuaKeyCode(keyCode);

		if (luaKeyCode != null) {
			boolean old = isDown(luaKeyCode);

			isLuaKeyDown.put(luaKeyCode, isDown);

			triggerCallsbackIfNecessary(old, isDown, luaKeyCode);

			return true;
		} else {
			return false;
		}
	}

	protected String keyCodeToLuaKeyCode(int keyCode) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			return "0";
		case KeyEvent.KEYCODE_1:
			return "1";
		case KeyEvent.KEYCODE_2:
			return "2";
		case KeyEvent.KEYCODE_3:
			return "3";
		case KeyEvent.KEYCODE_4:
			return "4";
		case KeyEvent.KEYCODE_5:
			return "5";
		case KeyEvent.KEYCODE_6:
			return "6";
		case KeyEvent.KEYCODE_7:
			return "7";
		case KeyEvent.KEYCODE_8:
			return "8";
		case KeyEvent.KEYCODE_9:
			return "9";

		case KeyEvent.KEYCODE_A:
			return "a";
		case KeyEvent.KEYCODE_B:
			return "b";
		case KeyEvent.KEYCODE_C:
			return "c";
		case KeyEvent.KEYCODE_D:
			return "d";
		case KeyEvent.KEYCODE_E:
			return "e";
		case KeyEvent.KEYCODE_F:
			return "f";
		case KeyEvent.KEYCODE_G:
			return "g";
		case KeyEvent.KEYCODE_H:
			return "h";
		case KeyEvent.KEYCODE_I:
			return "i";
		case KeyEvent.KEYCODE_J:
			return "j";
		case KeyEvent.KEYCODE_K:
			return "k";
		case KeyEvent.KEYCODE_L:
			return "l";
		case KeyEvent.KEYCODE_M:
			return "m";
		case KeyEvent.KEYCODE_N:
			return "n";
		case KeyEvent.KEYCODE_O:
			return "o";
		case KeyEvent.KEYCODE_P:
			return "p";
		case KeyEvent.KEYCODE_Q:
			return "q";
		case KeyEvent.KEYCODE_R:
			return "r";
		case KeyEvent.KEYCODE_S:
			return "s";
		case KeyEvent.KEYCODE_T:
			return "t";
		case KeyEvent.KEYCODE_U:
			return "u";
		case KeyEvent.KEYCODE_V:
			return "v";
		case KeyEvent.KEYCODE_W:
			return "w";
		case KeyEvent.KEYCODE_X:
			return "x";
		case KeyEvent.KEYCODE_Y:
			return "y";
		case KeyEvent.KEYCODE_Z:
			return "z";

		case KeyEvent.KEYCODE_DPAD_UP:
			return "up";
		case KeyEvent.KEYCODE_DPAD_DOWN:
			return "down";
		case KeyEvent.KEYCODE_DPAD_LEFT:
			return "left";
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			return "right";

		default:
			return null;
		}

	}

}
