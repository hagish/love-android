package net.schattenkind.androidLove;

import java.io.IOException;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LoveAndroidActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final TextView text = (TextView) this.findViewById(R.id.text);

		LuaValue _G = JsePlatform.standardGlobals();

		_G.set("print", new OneArgFunction() {

			@Override
			public LuaValue call(LuaValue luaValue) {
				text.setText(text.getText() + "\n" + luaValue.toString());
				Log.i("lua", luaValue.toString());

				return LuaValue.NONE;
			}
		});

		try {
			LoadState.load(getResources().openRawResource(R.raw.test),
					"test.lua", _G).call();

			LoadState.load(openFileInput("lala.lua"), "lala.lua", _G).call();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LuaValue t = _G.get("test").call();
		_G.get("lala").call();

		text.setText(text.getText() + "\n" + t.toString());
		text.setText(text.getText() + "\n" + getFilesDir());
	}
}