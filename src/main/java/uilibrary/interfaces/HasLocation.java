package uilibrary.interfaces;

import uilibrary.arrangement.Location;

public interface HasLocation {
	public int getX();
	public int getY();
	
	public default Location getLocation() {
		return new Location(getX(), getY());
	}
}
