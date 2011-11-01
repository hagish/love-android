package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LuanAudio extends LuanBase {
	public LuanAudio(LoveVM vm) {
		super(vm);
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// source = love.audio.newSource( file, type )
		t.set("newSource", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				String s = args.checkjstring(1);
				// ~ try {
				// ~ return LuaValue.userdataOf(new
				// LuanImage(LuanGraphics.this,s));
				// ~ } catch (Exception e) {
				// ~ // TODO : throw lua error ?
				// ~ LogException(e);
				// ~ }
				return LuaValue.NONE;
			}
		});

		// love.audio.play( source )
		t.set("play", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.NONE;
			}
		});

		return t;
	}

}
