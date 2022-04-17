package uilibrary.enums;

public enum Alignment {
	TOP, CENTER, LEFT, RIGHT, BOTTOM;
	
	public boolean isHorizontal() {
		return this == LEFT || this == RIGHT;
	}
	
	public boolean isVertical() {
		return this == TOP || this == BOTTOM;
	}
	
	public Orientation getOrientationByOther(Alignment other, boolean currentIsFirst) {
		if (this == CENTER && other == CENTER) {
			if (currentIsFirst) {
				return Orientation.HORIZONTAL;
			} else {
				return Orientation.VERTICAL;
			}
		}
		
		if (this == CENTER) {
			if (other.isHorizontal()) {
				return Orientation.VERTICAL;
			} else {
				return Orientation.HORIZONTAL;
			}
		}
		
		if (this.isHorizontal()) {
			return Orientation.HORIZONTAL;
		} else {
			return Orientation.VERTICAL;
		}
	}
	
	public static Alignment getHorizontal(Alignment[] aligns) {
		Alignment a = CENTER;
		for (Alignment align : aligns) {
			if (align.isHorizontal()) {
				a = align;
			}
		}
		return a;
	}
	
	public static Alignment getVertical(Alignment[] aligns) {
		Alignment a = CENTER;
		for (Alignment align : aligns) {
			if (align.isVertical()) {
				a = align;
			}
		}
		return a;
	}
	
	/**
	 * Get orientation from alignment array.
	 * @param aligns
	 * @param first If the alignment to be checked is the first one or not.
	 * @return 
	 */
	public static Orientation getOrientation(Alignment[] aligns, boolean first) {
		Alignment current = first ? aligns[0] : aligns[1];
		Alignment other = first ? aligns[1] : aligns[0];
		
		return current.getOrientationByOther(other, first);
	}
}
