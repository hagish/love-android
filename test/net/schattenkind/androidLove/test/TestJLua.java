package net.schattenkind.androidLove.test;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;
import net.schattenkind.androidLove.LuaUtils;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.jse.JsePlatform;

public class TestJLua extends TestCase {
	private LuaTable g;
	private String pairs_hack = "local _pairs = pairs\npairs = function(t)\n	-- generate a map to map each key to its successor\n	local map_cur_key_to_next = {}\n	local last_key = nil\n	local fist_key = nil\n	\n	for k,v in _pairs(t) do \n		if last_key then\n			map_cur_key_to_next[last_key] = k\n		else\n			first_key = k\n		end\n\n		last_key = k\n	end\n	\n	local local_next = function(t, index) \n		if index == nil then\n			return first_key, t[first_key]\n		elseif index == last_key or t == nil then\n			return nil\n		else\n			local k = map_cur_key_to_next[index]\n			return k, t[k]\n		end\n		\n		return next(t, index) \n	end\n	\n	-- next,t,nil\n	return local_next, t, nil\nend";
	
	public void setUp()
	{
		g = JsePlatform.debugGlobals();
		try {
			LuaUtils.executeCode(g, pairs_hack);
		} catch (Exception e) {
			fail("this should not happen: " + e.getMessage());
		}
	}
	
	public void tearDown()
	{
		g = null;
	}
	
	public void testJLuaBugId3430986Next() {
		// http://sourceforge.net/tracker/?func=detail&aid=3430986&group_id=197627&atid=962226
		
		String lua = "x = {a=1, b=2, c=3}\nx[b] = nil\nprint(next(x, 'b'))";
		
		try {
			LuaUtils.executeCode(g, lua);
		} catch (UnsupportedEncodingException e) {
		} catch (LuaError e) {
			fail("this should not happen: " + e.getMessage());
		}
	}
	
	public void testJLuaBugId3430986ForWithHash() {
		// http://sourceforge.net/tracker/?func=detail&aid=3430986&group_id=197627&atid=962226
		
		String lua = "x = {a=1, b=2, c=3}\n\nfor k,v in pairs(x) do\n	if k == \"b\" then x[k] = nil end\nend\n\nfor k,v in pairs(x) do print(k,v) end";
		
		try {
			LuaUtils.executeCode(g, lua);
		} catch (UnsupportedEncodingException e) {
		} catch (LuaError e) {
			fail("this should not happen: " + e.getMessage());
		}
	}
	
	public void testJLuaBugId3430986ForWithInt() {
		// http://sourceforge.net/tracker/?func=detail&aid=3430986&group_id=197627&atid=962226
		
		String lua = "x = {1, 2, 3}\n\nfor k,v in pairs(x) do\n	if k == 2 then x[k] = nil end\nend\n\nfor k,v in pairs(x) do print(k,v) end";
		
		try {
			LuaUtils.executeCode(g, lua);
		} catch (UnsupportedEncodingException e) {
		} catch (LuaError e) {
			fail("this should not happen: " + e.getMessage());
		}
	}
}
