package net.schattenkind.androidLove;

import java.util.List;

import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
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

	public static float getFromTableByPath(LuaTable t, String path,
			float defaultValue) {
		LuaValue v = getFromTableByPath(t, path);

		if (v.equals(LuaValue.NIL)) {
			return defaultValue;
		} else {
			return v.tofloat();
		}
	}

	public static boolean getFromTableByPath(LuaTable t, String path,
			boolean defaultValue) {
		LuaValue v = getFromTableByPath(t, path);

		if (v.equals(LuaValue.NIL)) {
			return defaultValue;
		} else {
			return v.toboolean();
		}
	}

	public static int getFromTableByPath(LuaTable t, String path,
			int defaultValue) {
		LuaValue v = getFromTableByPath(t, path);

		if (v.equals(LuaValue.NIL)) {
			return defaultValue;
		} else {
			return v.toint();
		}
	}

	public static String getFromTableByPath(LuaTable t, String path,
			String defaultValue) {
		LuaValue v = getFromTableByPath(t, path);

		if (v.equals(LuaValue.NIL)) {
			return defaultValue;
		} else {
			return v.toString();
		}
	}

	public static LuaValue getFromTableByPath(LuaTable t, String path) {
		String[] pathParts = path.split("\\.");

		LuaValue cur = t;

		for (String part : pathParts) {
			LuaValue next = cur.get(part);

			if (next.equals(LuaValue.NIL)) {
				cur = next;
				break;
			} else {
				cur = next;
			}
		}

		return cur;
	}
}
