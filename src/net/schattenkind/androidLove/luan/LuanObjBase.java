package net.schattenkind.androidLove.luan;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.GfxReinitListener;
import net.schattenkind.androidLove.LoveVM;

public class LuanObjBase implements GfxReinitListener {
	public static int count = 0;

	public LuanObjBase(LoveVM vm) {
		vm.listenForGfxReinit(this);
		
		++count;
		System.out.println("objects+: " + count);
	}

	protected void finalize() throws Throwable {
		super.finalize();
		--count;
		System.out.println("objects-: " + count);
	}

	@Override
	public void onGfxReinit(GL10 gl, float w, float h) {
		// empty stub	
	}
}
