package uilibrary.enums;

/**
 * Used to align an object relative to some bounds (reference).
 * <br>
 * Object can have up to 2 Alignments, for horizontal and vertical.
 * If one or both are missing, the default will be CENTER when ReferenceType is INSIDE,
 * but when ReferenceType is OUTSIDE and only one is missing, then it will default to TOP or LEFT.
 * 
 * <br>
 * <br>
 * 
 * ReferenceType (INSIDE or OUTSIDE) is also used with Alignments.
 * It will determine if the Alignment will place the object inside the bounds, or outside of them.
 * The order of Alignments doesn't matter when ReferenceType is INSIDE,
 * but it does matter when ReferenceType is OUTSIDE.
 * 
 * <br>
 * <br>
 * <br>
 * 
 * If the ReferenceType is INSIDE:
 *		TOP will place the object inside the bounds with both tops touching.
 *		LEFT will place the object inside the bounds with both left sides touching.
 *		RIGHT will place the object inside the bounds with both right sides touching.
 *		BOTTOM will place the object inside the bounds with both bottoms touching.
 *		CENTER will act as a default alignment to center the object inside the bounds.
 * <br>
 * <br>
 * <br>
 * 
 * If the ReferenceType is OUTSIDE:
 * <br>
 *	- The first Alignment is the main Alignment, it will choose the side where the object will be placed:
 *		TOP will place the object ABOVE the bounds with bottom touching the top of the bounds.
 *		LEFT will place the object LEFT SIDE of the bounds with right side touching the left side of the bounds.
 *		RIGHT will place the object RIGHT SIDE of the bounds with left side touching the right side of the bounds.
 *		BOTTOM will place the object UNDER the bounds with top touching the bottom of the bounds.
 *		CENTER shouldn't be the first Alignment if ReferenceType is OUTSIDE. It doesn't mean anything to place outside, but center.
 * <br>
 * <br>
 *	- The second Alignment on the other hand will take care of the other direction after it's placed with the first Alignment.
 *		The effect of the second alignment will be same as with normal INSIDE alignment.
 */
public enum Alignment {
	TOP, CENTER, LEFT, RIGHT, BOTTOM;
	
	public boolean isHorizontal() {
		return this == LEFT || this == RIGHT;
	}
	
	public boolean isVertical() {
		return this == TOP || this == BOTTOM;
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
	
	/**
	 * If current Alignment is CENTER, then you have to see which orientation the other one is to know which one current is.
	 * If both are CENTER, then the first one will get horizontal, and second vertical. Otherwise it doesn't matter, but both orientations have to be used once.
	 * @param other
	 * @param currentIsFirst
	 * @return 
	 */
	private Orientation getOrientationByOther(Alignment other, boolean currentIsFirst) {
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
}
