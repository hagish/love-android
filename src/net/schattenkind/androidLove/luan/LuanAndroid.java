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
		
		/// img = love.android.newResourceImage(int iResID)
		/// loads an image from a resource id
		t.set("newResourceImage", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				int iResID = args.checkint(1);
				try {
					return LuaValue.userdataOf(new LuanImage(vm.getLuanGraphics(),iResID),vm.get_G().get(LuanGraphics.sMetaName_LuanImage));
				} catch (Exception e) {
					vm.handleError(e);
				}
				return LuaValue.NONE;
			}
		});
		
		/// String 	love.android.getPackageName()
		/// Return the name of this application's package.
		t.set("getPackageName", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(vm.getActivity().getPackageName()); } });
		
		/// String 	love.android.getResourceName(int iResID)
		/// Return the full name for a given resource identifier.
		t.set("getResourceName", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			try {
				return LuaValue.valueOf(vm.getResources().getResourceName(args.checkint(1)));
			} catch (Exception e) {
				vm.handleError(e);
			}
			return LuaValue.NONE;
		} });
		
		/// iResID = love.android.getResourceID(String name, String defType, String defPackage)
		/// @name 	The name of the desired resource.
		/// @defType 	Optional default resource type to find, if "type/" is not included in the name. Can be null to require an explicit type.
		/// @defPackage 	Optional default package to find, if "package:" is not included in the name. Can be null to require an explicit package.
		t.set("getResourceID", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				return LuaValue.valueOf(vm.getResources().getIdentifier(
					args.checkjstring(1),
					IsArgSet(args,2) ? args.checkjstring(2) : null,
					IsArgSet(args,3) ? args.checkjstring(3) : null
					));
			}
		});
		// int 	getIdentifier(String name, String defType, String defPackage)
		
		return t;
	}

}


