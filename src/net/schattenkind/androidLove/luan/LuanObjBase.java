package net.schattenkind.androidLove.luan;

public class LuanObjBase {
	public static int count = 0;

	public LuanObjBase() {
		++count;
		System.out.println("objects+: " + count);
	}

	protected void finalize() throws Throwable {
		super.finalize();
		--count;
		System.out.println("objects-: " + count);
	}
}
