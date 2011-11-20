package net.schattenkind.androidLove.luan.module;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.luan.LuanBase;
import net.schattenkind.androidLove.luan.LuanObjBase;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LuanThread extends LuanBase {
	static final String sMetaName_LuanThread = "__MetaLuanThread";

	protected static final String TAG = "LoveThread";

	public LuanThread(LoveVM vm) {
		super(vm);
	}
	
	public void Log (String s) { LoveVM.LoveLog(TAG, s); }

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();
		LuaValue _G = vm.get_G();
		
		_G.set(sMetaName_LuanThread,LuanObjThreadObj.CreateMetaTable(this));

		/// thread = love.thread.newThread( name, filename )
		/// thread = love.thread.newThread( name, file )  -- File file : The file to use as source. 
		/// thread = love.thread.newThread( name, data )  -- Data data : The data to use as source. 
		/// Creates a new Thread from a File or Data Object. 
		t.set("newThread", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String name = args.checkjstring(1);
				// TODO: param2 ? can be different things
				try {
					return LuaValue.userdataOf(new LuanObjThreadObj(LuanThread.this,name),vm.get_G().get(sMetaName_LuanThread));
				} catch (Exception e) {
					vm.handleError(e);
				}
				return LuaValue.NONE;
			}
		});
		
		/// thread = love.thread.getThread( name )
		/// Look for a thread and get its object. 
		/// TODO: not yet implemented
		t.set("getThread", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.thread.getThread"); return LuaValue.NONE; } });
		
		/// threads = love.thread.getThreads( )
		/// Get all threads. 
		/// TODO: not yet implemented
		t.set("getThreads", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.thread.getThreads"); return LuaValue.NONE; } });
		
		return t;
	}
	
	// ***** ***** ***** ***** *****  LuanDecoder
		
	public static class LuanObjThreadObj extends LuanObjBase {
		public String name;
		public LuanThread th;
		
		public LuanObjThreadObj (LuanThread th,String name) { this.th = th; this.name = name; }
		
		public static LuaTable CreateMetaTable (final LuanThread th) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
				
			/// value = Thread:demand(name)
			/// Receive a message from a thread. Wait for the message to exist before returning. (Can return nil in case of an error in the thread.) 
			/// TODO: not yet implemented
			t.set("demand", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { th.vm.NotImplemented("Thread:demand"); return LuaValue.NONE; } });
			
			/// name = Thread:getName( )
			/// Get the name of a thread. 
			/// TODO: not yet implemented
			t.set("getName", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { th.vm.NotImplemented("Thread:getName"); return LuaValue.NONE; } });
			
			/// Thread:kill( )
			/// Forcefully terminate the thread. 
			/// TODO: not yet implemented
			t.set("kill", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { th.vm.NotImplemented("Thread:kill"); return LuaValue.NONE; } });
			
			/// value = Thread:peek(name)
			/// Receive a message from a thread, but leave it in the message box. 
			/// TODO: not yet implemented
			t.set("peek", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { th.vm.NotImplemented("Thread:peek"); return LuaValue.NONE; } });
			
			/// value = Thread:receive(name)
			/// Receive a message from a thread. Returns nil when a message is not in the message box. 
			/// TODO: not yet implemented
			t.set("receive", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { th.vm.NotImplemented("Thread:receive"); return LuaValue.NONE; } });
			
			/// Thread:send(name, value)
			/// Send a message (put it in the message box). 
			/// TODO: not yet implemented
			t.set("send", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { th.vm.NotImplemented("Thread:send"); return LuaValue.NONE; } });
			
			/// Thread:start( )
			/// Starts the thread. 
			/// TODO: not yet implemented
			t.set("start", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { th.vm.NotImplemented("Thread:start"); return LuaValue.NONE; } });
			
			/// Thread:wait( )
			/// Wait for a thread to finish. This call will block until the thread finishes. 
			/// TODO: not yet implemented
			t.set("wait", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { th.vm.NotImplemented("Thread:wait"); return LuaValue.NONE; } });
			
			/// type = Object:type()  , e.g. "Image" or audio:"Source"
			t.set("type", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf("Thread"); } });
			
			/// b = Object:typeOf( name )
			t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
				String s = args.checkjstring(2); 
				return LuaValue.valueOf(s.equals("Object") || s.equals("Thread")); 
			} });
			
			return mt;
		}
	}

}
