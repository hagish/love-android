package net.schattenkind.androidLove.test;

import junit.framework.TestCase;
import net.schattenkind.androidLove.LoveConfig;

import org.luaj.vm2.LuaTable;

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
