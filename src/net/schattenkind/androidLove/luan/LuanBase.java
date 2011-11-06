package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public abstract class LuanBase {
	public LoveVM vm;
	
	public LuanBase(LoveVM vm) {
		this.vm = vm;
	}
	
	public static boolean IsArgSet (Varargs args,int i) { return args.narg() >= i && !args.isnil(i); }
	
	public static String getLuaTypeName (int t) {
		switch (t) {
			case LuaValue.TNIL:			return "nil";
			case LuaValue.TBOOLEAN:		return "boolean";
			case LuaValue.TNUMBER:		return "number";
			case LuaValue.TSTRING:		return "string";
			case LuaValue.TTABLE:		return "table";
			case LuaValue.TFUNCTION:	return "function";
			case LuaValue.TUSERDATA:	return "userdata";
			case LuaValue.TTHREAD:		return "thread";	
			default: return "unknown";
		}
	}

	abstract public LuaTable InitLib();
}
