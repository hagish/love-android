package net.schattenkind.androidLove.luan;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LuanFilesystem {

	public LuanFilesystem(LuaValue _G) {
		// TODO Auto-generated constructor stub
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// chunk = love.filesystem.load( name )
		t.set("load", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String filename = args.arg1().toString();
				// TODO
				return LuaValue.NONE;
			}
		});

		return t;
	}

}
