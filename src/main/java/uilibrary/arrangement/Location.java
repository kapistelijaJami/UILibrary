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

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + this.x;
		hash = 79 * hash + this.y;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Location other = (Location) obj;
		if (this.x != other.x) {
			return false;
		}
		return this.y == other.y;
	}
}
