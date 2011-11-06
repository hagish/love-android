// android specific extensions

package net.schattenkind.androidLove.luan;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.LuaUtils;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

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


