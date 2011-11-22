package net.schattenkind.androidLove.utils;

public class Matrix3 {
	// 0 3 6
	// 1 4 7
	// 2 5 8
	public float[] m = new float[3 * 3];

	public Matrix3(float value) {
		for (int i = 0; i < m.length; ++i)
			m[i] = value;
	}

	public static Matrix3 identity() {
		Matrix3 m = Matrix3.zero();

		m.m[0] = 1f;
		m.m[4] = 1f;
		m.m[8] = 1f;

		return m;
	}

	public static Matrix3 zero() {
		return new Matrix3();
	}

	public Matrix3() {
		this(0.0f);
	}

	public void set(int x, int y, float v) {
		m[3 * y + x] = v;
	}

	public float get(int x, int y) {
		return m[3 * y + x];
	}

	public void mul(float v, Matrix3 result) {
		for (int i = 0; i < 3 * 3; ++i) {
			result.m[i] = m[i] * v;
		}
	}

	// this * o = result
	public void mul(Matrix3 o, Matrix3 result) {
		result.m[0] = m[0] * o.m[0] + m[3] * o.m[1] + m[6] * o.m[2];
		result.m[1] = m[1] * o.m[0] + m[4] * o.m[1] + m[7] * o.m[2];
		result.m[2] = m[3] * o.m[0] + m[5] * o.m[1] + m[8] * o.m[2];

		result.m[3] = m[0] * o.m[3] + m[3] * o.m[4] + m[6] * o.m[5];
		result.m[4] = m[1] * o.m[3] + m[4] * o.m[4] + m[7] * o.m[5];
		result.m[5] = m[3] * o.m[3] + m[5] * o.m[4] + m[8] * o.m[5];

		result.m[6] = m[0] * o.m[6] + m[3] * o.m[7] + m[6] * o.m[8];
		result.m[7] = m[1] * o.m[6] + m[4] * o.m[7] + m[7] * o.m[8];
		result.m[8] = m[3] * o.m[6] + m[5] * o.m[7] + m[8] * o.m[8];
	}

	public void mul(Vector3 v, Vector3 result) {
		result.x = m[0] * v.x + m[3] * v.y + m[6] * v.z;
		result.y = m[1] * v.x + m[4] * v.y + m[7] * v.z;
		result.z = m[2] * v.x + m[5] * v.y + m[8] * v.z;
	}

	public static Matrix3 rotation2D(float rad) {
		Matrix3 r = new Matrix3();

		r.m[0] = (float) Math.cos(rad);
		r.m[1] = (float) Math.sin(rad);
		r.m[3] = -r.m[1];
		r.m[4] = r.m[0];
		r.m[8] = 1.0f;

		return r;
	}

	public static Matrix3 translation2D(float dx, float dy) {
		Matrix3 r = new Matrix3();

		r.m[0] = 1.0f;
		r.m[4] = 1.0f;
		r.m[8] = 1.0f;

		r.m[6] = dx;
		r.m[7] = dy;

		return r;
	}

	private static boolean floatEq(float a, float b) {
		return Math.abs(a - b) < 0.000001f;
	}

	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof Matrix3))
			return false;
		Matrix3 that = (Matrix3) aThat;

		for (int i = 0; i < 3 * 3; ++i) {
			if (!floatEq(m[i], that.m[i])) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "[" + m[0] + ", " + m[3] + ", " + m[6] + "; " + m[1] + ", "
				+ m[4] + ", " + m[7] + "; " + m[2] + ", " + m[5] + ", " + m[8]
				+ "]";
	}
}
