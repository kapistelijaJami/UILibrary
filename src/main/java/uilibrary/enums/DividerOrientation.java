package uilibrary.enums;

public enum DividerOrientation {
	HORIZONTAL, VERTICAL; //HORIZONTAL: Divider is horizontal, panels are above and below divider. VERTICAL is the other way.
	
	/**
	 * Which edge the divider is attached to == what edge should be moving.
	 * TOP means that y value is changing and height too.
	 * BOTTOM only height is changing.
	 * LEFT x and width is changing.
	 * RIGHT only width is changing.
	 */
	public enum ScalingDirection {
		TOP, BOTTOM, LEFT, RIGHT;
	}
	
	public ScalingDirection getFirst() {
		if (this == HORIZONTAL) {
			return ScalingDirection.BOTTOM; //object is above, so we move the bottom edge
		} else {
			return ScalingDirection.RIGHT; //object is left of the divider, so we move the right edge
		}
	}
	
	public ScalingDirection getSecond() {
		if (this == HORIZONTAL) {
			return ScalingDirection.TOP;
		} else {
			return ScalingDirection.LEFT;
		}
	}
}
