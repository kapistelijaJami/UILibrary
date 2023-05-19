package uilibrary.interfaces;

import java.awt.Rectangle;

public interface HasBounds extends HasPosition, HasSize {
	public default Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public default boolean isInside(int x, int y) {
		return getBounds().contains(x, y);
	}
}
