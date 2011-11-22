package net.schattenkind.androidLove.test;

import net.schattenkind.androidLove.utils.Matrix3;
import net.schattenkind.androidLove.utils.Vector3;
import junit.framework.TestCase;

public class TestMatrix3 extends TestCase {
	public void testRotation2D() {
		Vector3 v = Vector3.from2D(1f, 0f);
		Vector3 r = new Vector3();
		Matrix3 t;

		t = Matrix3.rotation2D((float) (0.5f * Math.PI));
		t.mul(v, r);
		assertEquals(Vector3.from2D(0f, 1f), r);

		t = Matrix3.rotation2D((float) (-0.5f * Math.PI));
		t.mul(v, r);
		assertEquals(Vector3.from2D(0f, -1f), r);

		t = Matrix3.rotation2D((float) (0.25f * Math.PI));
		t.mul(v, r);
		assertEquals(Vector3.from2D((float) Math.sqrt(0.5f),
				(float) Math.sqrt(0.5f)), r);
	}

	public void testTranslation2D() {
		Vector3 v = Vector3.from2D(1f, 2f);
		Vector3 r = new Vector3();
		Matrix3 t;

		t = Matrix3.translation2D(2f, -1f);
		t.mul(v, r);
		assertEquals(Vector3.from2D(3f, 1f), r);

		t = Matrix3.translation2D(-1f, -2f);
		t.mul(v, r);
		assertEquals(Vector3.from2D(0f, 0f), r);
	}

	public void testIdentityMul() {
		Matrix3 a = new Matrix3();

		a.m[1] = 1.0f;
		a.m[4] = 7.0f;
		a.m[5] = -5.0f;

		Matrix3 r = new Matrix3();

		a.mul(Matrix3.identity(), r);

		assertEquals(r, a);
	}
}
