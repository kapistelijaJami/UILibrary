package uilibrary.enums;

public enum ReferenceType {
	INSIDE, OUTSIDE, OUTSIDE_CORNER;
	
	public boolean isInside() {
		return this == INSIDE;
	}
	
	/**
	 * Checks if ReferenceType is OUTSIDE or OUTSIDE_CORNER.
	 * @return 
	 */
	public boolean isOutside() {
		return !isInside();
	}
}
