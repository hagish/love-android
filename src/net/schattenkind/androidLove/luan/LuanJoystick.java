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
		t.set("close"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"close"				);	return LuaValue.NONE; } });	
		t.set("getAxes"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getAxes"			);	return LuaValue.varargsOf(LuaValue.ZERO,LuaValue.ZERO,LuaValue.ZERO); } });	
		t.set("getAxis"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getAxis"			);	return LuaValue.ZERO; } });	
		t.set("getBall"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getBall"			);	return LuaValue.varargsOf(LuaValue.ZERO,LuaValue.ZERO); } });	
		t.set("getHat"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getHat"			);	return LuaValue.ZERO; } });	
		t.set("getName"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getName"			);	return LuaValue.NONE; } });	
		t.set("getNumAxes"		, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumAxes"		);	return LuaValue.ZERO; } });	
		t.set("getNumBalls"		, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumBalls"		);	return LuaValue.ZERO; } });	
		t.set("getNumButtons"	, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumButtons"		);	return LuaValue.ZERO; } });	
		t.set("getNumHats"		, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumHats"		);	return LuaValue.ZERO; } });	
		t.set("getNumJoysticks"	, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"getNumJoysticks"	);	return LuaValue.ZERO; } });	
		t.set("isDown"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"isDown"			);	return LuaValue.NONE; } });	
		t.set("isOpen"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"isOpen"			);	return LuaValue.NONE; } });	
		t.set("open"			, new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.joystick."+"open"				);	return LuaValue.NONE; } });	

		return t;
	}

}
