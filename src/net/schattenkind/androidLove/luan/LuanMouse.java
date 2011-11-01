package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LuanMouse extends LuanBase {
	private int mouseX;
	private int mouseY;

	private boolean mouseButtonLeft = false;
	private boolean mouseButtonMiddle = false;
	private boolean mouseButtonRight = false;

	public static final String LEFT_BUTTON = "l";
	public static final String MIDDLE_BUTTON = "m";
	public static final String RIGHT_BUTTON = "r";
	public static final String WHEEL_UP = "wu";
	public static final String WHEEL_DOWN = "wd";
	public static final String X1 = "x1";
	public static final String X2 = "x2";

	public LuanMouse(LoveVM vm) {
		super(vm);
	}

	public void feedPosition(int x, int y) {
		mouseX = x;
		mouseY = y;
	}

	public void feedButtonState(boolean left, boolean middle, boolean right) {
		triggerCallsbacksIfNecessary(left, middle, right);

		mouseButtonLeft = left;
		mouseButtonMiddle = middle;
		mouseButtonRight = right;
	}

	private void triggerCallsbackIfNecessary(boolean oldState,
			boolean newState, String button) {
		if (oldState != newState) {
			String callback = "mousepressed";
			if (oldState == true) {
				callback = "mousereleased";
			}

			vm.get_G().get("love")
					.get(callback)
					.call(LuaNumber.valueOf(mouseX), LuaNumber.valueOf(mouseY),
							LuaString.valueOf(button));
		}
	}

	private void triggerCallsbacksIfNecessary(boolean left, boolean middle,
			boolean right) {

		triggerCallsbackIfNecessary(mouseButtonLeft, left, LEFT_BUTTON);
		triggerCallsbackIfNecessary(mouseButtonMiddle, middle, MIDDLE_BUTTON);
		triggerCallsbackIfNecessary(mouseButtonRight, right, RIGHT_BUTTON);
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// x, y = love.mouse.getPosition( )
		t.set("getPosition", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.varargsOf(LuaNumber.valueOf(mouseX),
						LuaNumber.valueOf(mouseY));
			}
		});

		// x = love.mouse.getX( )
		t.set("getX", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaNumber.valueOf(mouseX);
			}
		});

		// y = love.mouse.getY( )
		t.set("getY", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaNumber.valueOf(mouseY);
			}
		});

		// down = love.mouse.isDown( button )
		t.set("isDown", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String button = args.arg1().toString();

				if (button.equals(LEFT_BUTTON)) {
					return LuaBoolean.valueOf(mouseButtonLeft);
				} else if (button.equals(MIDDLE_BUTTON)) {
					return LuaBoolean.valueOf(mouseButtonMiddle);
				} else if (button.equals(RIGHT_BUTTON)) {
					return LuaBoolean.valueOf(mouseButtonRight);
				}

				return LuaValue.FALSE;
			}
		});

		// grabbed = love.mouse.isGrabbed( )
		t.set("isGrabbed", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.FALSE;
			}
		});

		// visible = love.mouse.isVisible( )
		t.set("isVisible", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.FALSE;
			}
		});

		// love.mouse.setGrab( grab )
		t.set("setGrab", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.NONE;
			}
		});

		// love.mouse.setPosition( x, y )
		t.set("setGrab", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				mouseX = args.arg(1).toint();
				mouseY = args.arg(2).toint();

				return LuaValue.NONE;
			}
		});

		// love.mouse.setVisible( visible )
		t.set("setVisible", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.NONE;
			}
		});

		return t;
	}

}
