package net.schattenkind.androidLove.luan;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.util.Log;

public class LuanFilesystem {

	protected static final String TAG = "LoveFilesystem";

	private LuaValue _G;
	private LoveVM vm;

	public LuanFilesystem(LuaValue _G, LoveVM vm) {
		this._G = _G;
		this.vm = vm;
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// chunk = love.filesystem.load( name )
		t.set("load", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String filename = args.arg1().toString();
				try {
					LuaFunction f = LoadState.load(vm.getFileStreamFromSdCard(filename), filename, _G);
					return f;
				} catch (FileNotFoundException e) {
					Log.e(TAG, e.getMessage());
					return LuaValue.NONE;
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					return LuaValue.NONE;
				}
			}
		});

		return t;
	}

}
