// phone/android/iphone specific extensions
// set love.phone, so android-aware games can detect it, might be used for additional util functions later

package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.util.Log;

public class LuanPhone extends LuanBase {

	protected static final String TAG = "LovePhone";

	public LuanPhone(LoveVM vm) {
		super(vm);
	}
	
	public void Log (String s) { Log.i(TAG, s); }

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();
		
		// TODO: add phone/android/iphone specific api additions here, e.g. multi-touch event stuff, accelerometer, gravity sensor, maybe intent/running apps/network/start browser etc ?
		
		/// img = love.phone.newResourceImage(int iResID)
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
		
		
		/// source = love.phone.newResourceAudioSource(int iResID,string type)
		/// loads a sound/music/audio source from a resource id
		t.set("newResourceAudioSource", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				int iResID = args.checkint(1);
				try {
					String sType = IsArgSet(args,2) ? args.checkjstring(2) : "static";
					return LuaValue.userdataOf(new LuanAudio.LuanSource(vm.getLuanAudio(),iResID,sType),vm.get_G().get(LuanAudio.sMetaName_LuanSource));
				} catch (Exception e) {
					vm.handleError(e);
				}
				return LuaValue.NONE;
			}
		});
		
		// TODO: newResourceFontTTF !!!
		/// source = love.phone.newResourceFontTTF(int iResID,int iSize)
		/// loads a ttf from ressource id
		
		/// String 	love.phone.getPackageName()
		/// Return the name of this application's package.
		t.set("getPackageName", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(vm.getActivity().getPackageName()); } });
		
		/// String 	love.phone.getResourceName(int iResID)
		/// Return the full name for a given resource identifier.
		t.set("getResourceName", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			try {
				return LuaValue.valueOf(vm.getResources().getResourceName(args.checkint(1)));
			} catch (Exception e) {
				vm.handleError(e);
			}
			return LuaValue.NONE;
		} });
		
		/// iResID = love.phone.getResourceID(String name, String defType, String defPackage)
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


