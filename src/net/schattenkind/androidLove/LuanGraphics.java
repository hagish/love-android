package net.schattenkind.androidLove;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import android.app.Activity;
import android.util.Log;

public class LuanGraphics {

	public LuaTable InitLib () {
		LuaTable t = LuaValue.tableOf();

		// love.graphics.print(sText,x,y)
		t.set("print", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				int x = args.checkint(2);
				int y = args.checkint(3);
				Log.i("lua", s);
				return LuaValue.NONE;
			}
		});

		// img = love.graphics.newImage(sFileName)
		t.set("newImage", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				return LuaValue.NONE;
			}
		});

		// love.graphics.draw(img,x,y)
		t.set("draw", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// String s = args.checkjstring(1);
				//~  java.lang.Object 	checkuserdata(int i, java.lang.Class c) 
				//~  java.lang.Object 	checkuserdata(int i, java.lang.Class c) 
				int x = args.checkint(2);
				int y = args.checkint(3);
				return LuaValue.NONE;
			}
		});
		
		return t;
	}
	
		
	// ***** ***** ***** ***** *****  LuanImage

	public class LuanImage {
		
	}
}

