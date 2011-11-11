package net.schattenkind.androidLove.test;

import org.luaj.vm2.LuaTable;

import net.schattenkind.androidLove.LoveConfig;
import junit.framework.TestCase;

public class TestLuaConfig extends TestCase {
	public void testGetFloatFromTableByPath() {
		LoveConfig c = new LoveConfig();
		
		LuaTable t = new LuaTable();
		t.set("title", "great title");
		t.set("author", "someone");
		t.set("screen", new LuaTable());
		t.get("screen").set("width", 100);
		
		c.loadFromLuaTable(t);
		
		assertEquals(c.title, "great title");
		assertEquals(c.author, "someone");
		assertEquals(c.screen_width, 100);
		assertEquals(c.screen_height, 600);
	}
}
