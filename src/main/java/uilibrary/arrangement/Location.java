package uilibrary.arrangement;

public class Location {
	public int x;
	public int y;
	
	public Location() {
        this(0, 0);
    }
	
	public Location(Location loc) {
        this(loc.x, loc.y);
    }
	
	public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
