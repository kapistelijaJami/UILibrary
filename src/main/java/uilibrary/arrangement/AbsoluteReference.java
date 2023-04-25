package uilibrary.arrangement;

import java.awt.Point;
import java.awt.Rectangle;
import uilibrary.interfaces.HasBounds;

/**
 * Is used as a reference object to position with absolute rather than relative.
 */
public class AbsoluteReference implements HasBounds {
	private int x, y, width, height;
	
	public AbsoluteReference() {
		this(0, 0, 0, 0);
	}
	
	public AbsoluteReference(Point p) {
		this(p.x, p.y, 0, 0);
	}
	
	public AbsoluteReference(int x, int y) {
		this(x, y, 0, 0);
	}
	
	public AbsoluteReference(Rectangle rect) {
		this(rect.x, rect.y, rect.width, rect.height);
	}
	
	public AbsoluteReference(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
}
