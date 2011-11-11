package net.schattenkind.androidLove.test;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;
import net.schattenkind.androidLove.LuaUtils;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.jse.JsePlatform;

public class TestJLua extends TestCase {
	public void testJLuaBugId3430986() {
		// http://sourceforge.net/tracker/?func=detail&aid=3430986&group_id=197627&atid=962226
		
		String lua = "x = {a=1, b=2, c=3}\nx[b] = nil\nprint(next(x, 'b'))";
		
		LuaTable g = JsePlatform.debugGlobals();
		
		try {
			LuaUtils.executeCode(g, lua);
		} catch (UnsupportedEncodingException e) {
		} catch (LuaError e) {
			fail("this should not happen");
		}
	}
}
