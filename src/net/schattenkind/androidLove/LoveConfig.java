package net.schattenkind.androidLove;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LoveConfig {

	// The title of the window the game is in (string)
	public String title = "Untitled";

	// The author of the game (string)
	public String author = "Unnamed";

	// The name of the save directory (string)
	public String identity = null;

	// The LÖVE version this game was made for (number)
	public float version = 0f;

	// Attach a console (boolean, Windows only)
	public boolean console = false;

	// The window width (number)
	public int screen_width = 800;

	// The window height (number)
	public int screen_height = 600;

	// Enable fullscreen (boolean)
	public boolean screen_fullscreen = false;

	// Enable vertical sync (boolean)
	public boolean screen_vsync = true;

	// The number of FSAA-buffers (number)
	public int screen_fsaa = 0;

	// Enable the joystick module (boolean)
	public boolean modules_joystick = true;

	// Enable the audio module (boolean)
	public boolean modules_audio = true;

	// Enable the keyboard module (boolean)
	public boolean modules_keyboard = true;

	// Enable the event module (boolean)
	public boolean modules_event = true;

	// Enable the image module (boolean)
	public boolean modules_image = true;

	// Enable the graphics module (boolean)
	public boolean modules_graphics = true;

	// Enable the timer module (boolean)
	public boolean modules_timer = true;

	// Enable the mouse module (boolean)
	public boolean modules_mouse = true;

	// Enable the sound module (boolean)
	public boolean modules_sound = true;

	// Enable the physics module (boolean)
	public boolean modules_physics = true;

	public void loadFromLuaTable(LuaTable t) {
		title = LuaUtils.getFromTableByPath(t, "title", title);
		author = LuaUtils.getFromTableByPath(t, "author", author);
		identity = LuaUtils.getFromTableByPath(t, "identity", identity);
		version = LuaUtils.getFromTableByPath(t, "version", version);
		screen_width = LuaUtils.getFromTableByPath(t, "screen.width",
				screen_width);
		screen_height = LuaUtils.getFromTableByPath(t, "screen.height",
				screen_height);
		screen_fullscreen = LuaUtils.getFromTableByPath(t, "screen.fullscreen",
				screen_fullscreen);
		screen_vsync = LuaUtils.getFromTableByPath(t, "screen.vsync",
				screen_vsync);
		screen_fsaa = LuaUtils
				.getFromTableByPath(t, "screen.fsaa", screen_fsaa);

		modules_joystick = LuaUtils.getFromTableByPath(t, "modules.joystick",
				modules_joystick);
		modules_audio = LuaUtils.getFromTableByPath(t, "modules.audio",
				modules_audio);
		modules_keyboard = LuaUtils.getFromTableByPath(t, "modules.keyboard",
				modules_keyboard);
		modules_event = LuaUtils.getFromTableByPath(t, "modules.event",
				modules_event);
		modules_image = LuaUtils.getFromTableByPath(t, "modules.image",
				modules_image);
		modules_graphics = LuaUtils.getFromTableByPath(t, "modules.graphics",
				modules_graphics);
		modules_timer = LuaUtils.getFromTableByPath(t, "modules.timer",
				modules_timer);
		modules_mouse = LuaUtils.getFromTableByPath(t, "modules.mouse",
				modules_mouse);
		modules_sound = LuaUtils.getFromTableByPath(t, "modules.sound",
				modules_sound);
		modules_physics = LuaUtils.getFromTableByPath(t, "modules.physics",
				modules_physics);
	}

	public void loadFromFileStream(InputStream configFileInputStream) throws IOException {
		LuaTable g = JsePlatform.debugGlobals();

		g.set("love", new LuaTable());
		LoadState.load(configFileInputStream, "conf.lua", g).call();

		LuaValue conf = LuaUtils.getFromTableByPath(g, "love.conf");
		if (!conf.equals(LuaValue.NIL)) {
			LuaTable t = new LuaTable();
			
			t.set("modules", new LuaTable());
			t.set("screen", new LuaTable());
			
			conf.call(t);
			
			loadFromLuaTable(t);
		}
	}
}
