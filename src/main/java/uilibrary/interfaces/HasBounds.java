package uilibrary.interfaces;

import java.awt.Point;
import java.awt.Rectangle;

public interface HasBounds extends HasPosition, HasSize {
	public default Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public default boolean isInside(int x, int y) {
		return getBounds().contains(x, y);
	}
	
	public default Point getCenter() {
		Rectangle b = getBounds();
		return new Point(b.x + b.width / 2, b.y + b.height / 2);
	}
}
