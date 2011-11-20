package net.schattenkind.androidLove.luan;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.GfxReinitListener;
import net.schattenkind.androidLove.LoveVM;

public class LuanObjBase implements GfxReinitListener {
	public static int count = 0;

	public LuanObjBase(LoveVM vm) {
		vm.listenForGfxReinit(this); // TODO: not every object should be registered as listener for performance reasons, e.g. quad?
		
		++count;
		System.out.println("objects+: " + count); // TODO: remove for performance
	}

	protected void finalize() throws Throwable {
		super.finalize();
		--count;
		System.out.println("objects-: " + count); // TODO: remove for performance
	}

	@Override
	public void onGfxReinit(GL10 gl, float w, float h) {
		// empty stub	
	}
}
