package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaTable;

public abstract class LuanBase {
	public LoveVM vm;
	
	public LuanBase(LoveVM vm) {
		this.vm = vm;
	}

	abstract public LuaTable InitLib();
}
