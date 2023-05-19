package uilibrary.arrangement;

public class Position {
	public int x;
	public int y;
	
	public Position() {
        this(0, 0);
	}
	
	public Position(Position loc) {
        this(loc.x, loc.y);
    }
	
	public Position(int x, int y) {
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
		final Position other = (Position) obj;
		if (this.x != other.x) {
			return false;
		}
		return this.y == other.y;
	}
}
