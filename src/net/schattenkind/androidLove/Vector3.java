package net.schattenkind.androidLove;

public class Vector3 {
	public float x;
	public float y;
	public float z;

	public Vector3() {
		init(0.0f, 0.0f, 0.0f);
	}

	public Vector3(float x, float y, float z) {
		init(x, y, z);
	}

	private void init(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
