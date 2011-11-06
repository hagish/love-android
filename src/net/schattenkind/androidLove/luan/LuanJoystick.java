package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LuanJoystick extends LuanBase {
	public LuanJoystick(LoveVM vm) {
		super(vm);
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// TODO: not yet implemented
		t.set("close"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"close"			);	return LuaValue.NONE; } });	
		t.set("getAxes"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getAxes"			);	return LuaValue.NONE; } });	
		t.set("getAxis"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getAxis"			);	return LuaValue.NONE; } });	
		t.set("getBall"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getBall"			);	return LuaValue.NONE; } });	
		t.set("getHat"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getHat"			);	return LuaValue.NONE; } });	
		t.set("getName"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getName"			);	return LuaValue.NONE; } });	
		t.set("getNumAxes"		, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumAxes"		);	return LuaValue.NONE; } });	
		t.set("getNumBalls"		, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumBalls"		);	return LuaValue.NONE; } });	
		t.set("getNumButtons"	, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumButtons"	);	return LuaValue.NONE; } });	
		t.set("getNumHats"		, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumHats"		);	return LuaValue.NONE; } });	
		t.set("getNumJoysticks"	, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumJoysticks"	);	return LuaValue.NONE; } });	
		t.set("isDown"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"isDown"			);	return LuaValue.NONE; } });	
		t.set("isOpen"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"isOpen"			);	return LuaValue.NONE; } });	
		t.set("open"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"open"			);	return LuaValue.NONE; } });	

		return t;
	}

}
