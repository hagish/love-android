package net.schattenkind.androidLove.luan.module;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.luan.LuanBase;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LuanEvent extends LuanBase {
	protected static final String TAG = "LoveEvent";
	public void Log (String s) { LoveVM.LoveLog(TAG, s); }
	
	public LuanEvent(LoveVM vm) {
		super(vm);
	}
	
	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();
		
		/// love.event.clear() 
		/// Clears the event queue. 
		/// TODO: not yet implemented
		t.set("clear", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.event.clear"); return LuaValue.NONE; } });
		
		/// i = love.event.poll( )
		/// Gets an iterator for messages in the event queue. 
		/// TODO: not yet implemented
		t.set("poll", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.event.poll"); return LuaValue.NONE; } });
		
		/// love.event.pump( )
		/// Pump events into the event queue. This is a low-level function, and is usually not called explicitly, but implicitly by love.event.poll() or love.event.wait(). 
		/// TODO: not yet implemented
		t.set("pump", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.event.pump"); return LuaValue.NONE; } });
		
		/// love.event.clear()
		/// TODO: not yet implemented
		t.set("clear", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.event.clear"); return LuaValue.NONE; } });
		
		/// love.event.push( e, a, b, c )
		/// Adds an event to the event queue. 
		/// TODO: not yet implemented
		t.set("push", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.event.push"); return LuaValue.NONE; } });
		
		/// e, a, b, c = love.event.wait( )
		/// Like love.event.poll(), but blocks until there is an event in the queue. 
		/// TODO: not yet implemented
		t.set("wait", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.event.wait"); return LuaValue.NONE; } });

		return t;
	}

	
	
}
