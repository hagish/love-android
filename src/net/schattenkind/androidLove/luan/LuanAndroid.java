// android specific extensions

package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import android.util.Log;

public class LuanAndroid extends LuanBase {

	protected static final String TAG = "LoveAndroid";

	public LuanAndroid(LoveVM vm) {
		super(vm);
	}
	
	public void Log (String s) { Log.i("LoveAndroid", s); }

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();
		
		// TODO: add android specific api additions here, e.g. multi-touch event stuff, accelerometer, gravity sensor, maybe intent/running apps/network/start browser etc ?
		
		return t;
	}

}


