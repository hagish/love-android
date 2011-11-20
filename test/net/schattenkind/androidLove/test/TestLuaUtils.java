package net.schattenkind.androidLove.test;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import net.schattenkind.androidLove.utils.LuaUtils;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class TestLuaUtils extends TestCase {

	public void testConvertStringListToLuaStringArray() {
		fail("I don't have a clue why this test does not work");
		
		List<String> sl = new LinkedList<String>();

		sl.add("123");
		sl.add("abc");

		LuaValue[] ll = LuaUtils.convertStringListToLuaStringArray(sl);

		assertEquals(ll.length, 2);

		assertEquals(ll[0].toString(), "123");
		assertEquals(ll[1].toString(), "abc");
	}

	public void testGetFromTableByPath() {
		LuaTable t = new LuaTable();
		t.set("p1", LuaValue.valueOf(1.0f));

		assertEquals(LuaUtils.getFromTableByPath(t, "p1").tofloat(), 1.0f);

		assertEquals(LuaUtils.getFromTableByPath(t, "p2"), LuaValue.NIL);

		assertEquals(LuaUtils.getFromTableByPath(t, "p2.x"), LuaValue.NIL);

		LuaTable tt = new LuaTable();
		tt.set("xxx", t);

		assertEquals(LuaUtils.getFromTableByPath(tt, "xxx.p1").tofloat(), 1.0f);
		assertEquals(LuaUtils.getFromTableByPath(tt, "xxx..p1"), LuaValue.NIL);

	}

	public void testGetFloatFromTableByPath() {
		LuaTable t = new LuaTable();
		t.set("p1", LuaValue.valueOf(1.0f));

		assertEquals(LuaUtils.getFromTableByPath(t, "p1", 0.0f), 1.0f);

		assertEquals(LuaUtils.getFromTableByPath(t, "p2", 1.0f), 1.0f);
	}

}
