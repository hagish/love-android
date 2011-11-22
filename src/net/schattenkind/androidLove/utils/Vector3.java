package net.schattenkind.androidLove.utils;

public class Vector3 {
	public float x;
	public float y;
	public float z;

	private static boolean floatEq(float a, float b) {
		return Math.abs(a - b) < 0.000001f;
	}

	public static Vector3 zero() {
		return new Vector3();
	}

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

	public static Vector3 from2D(float x, float y) {
		return new Vector3(x, y, 1.0f);
	}

	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof Vector3))
			return false;
		Vector3 that = (Vector3) aThat;

		return floatEq(x, that.x) && floatEq(y, that.y) && floatEq(z, that.z);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
