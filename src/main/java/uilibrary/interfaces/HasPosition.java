package uilibrary.interfaces;

import uilibrary.arrangement.Position;

public interface HasPosition {
	public int getX();
	public int getY();
	
	public default Position getPosition() {
		return new Position(getX(), getY());
	}
}
