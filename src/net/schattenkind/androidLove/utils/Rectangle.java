package net.schattenkind.androidLove.utils;

public class Rectangle {
	public float left;
	public float top;
	public float width;
	public float height;

	public Rectangle() {
		this(0f, 0f, 0f, 0f);
	}

	public Rectangle(float left, float top, float width, float height) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
	}
}
