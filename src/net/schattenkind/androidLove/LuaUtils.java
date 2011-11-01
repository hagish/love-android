package net.schattenkind.androidLove;

import java.util.List;

import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;

public class LuaUtils {
	public static LuaValue[] convertStringListToLuaStringArray(
			List<String> strings) {

		LuaValue[] luaStrings = new LuaValue[strings.size()];

		for (int i = 0; i < strings.size(); ++i) {
			luaStrings[i] = LuaString.valueOf(strings.get(i));
		}

		return luaStrings;
	}
}
