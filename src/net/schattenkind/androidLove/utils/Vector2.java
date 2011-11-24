package net.schattenkind.androidLove.utils;

public class Vector2 {
	public float x;
	public float y;

	private static boolean floatEq(float a, float b) {
		return Math.abs(a - b) < 0.000001f;
	}

	public static Vector2 zero() {
		return new Vector2();
	}

	public Vector2() {
		this(0.0f, 0.0f);
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
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

		return floatEq(x, that.x) && floatEq(y, that.y);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
