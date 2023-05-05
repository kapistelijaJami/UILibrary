package uilibrary.arrangement;

import uilibrary.enums.ReferenceType;
import uilibrary.interfaces.HasBounds;

/**
 * Reference is used for aligning another object in reference to this one.
 * Reference keeps an object, or two in case you want separate horizontal and vertical aligning with different objects.
 * These objects implement HasBounds interface.
 * Reference returns its bounds with getHorizontal() and getVertical().
 * Reference is used by Arrangement to figure out how to arrange itself in relation to the reference.
 * ReferenceType tells whether to place the other object inside or outside the reference separately both horizontally and vertically.
 */
public class Reference {
	private HasBounds both;
	private ReferenceType type;
	
	//OR
	
	private HasBounds horizontal;
	private HasBounds vertical;
	private ReferenceType horType;
	private ReferenceType verType;
	
	public Reference(HasBounds bounds, ReferenceType type) {
		this.both = bounds;
		this.type = type;
	}
	
	public Reference(HasBounds horizontal, HasBounds vertical, ReferenceType horType, ReferenceType verType) {
		this.horizontal = horizontal;
		this.vertical = vertical;
		
		this.horType = horType;
		this.verType = verType;
	}
	
	public void setReference(HasBounds reference, ReferenceType type) {
		this.both = reference;
		this.type = type;
	}
	
	public void setHorizontal(HasBounds horizontal, ReferenceType horType) {
		this.horizontal = horizontal;
		this.horType = horType;
	}
	
	public void setVertical(HasBounds vertical, ReferenceType verType) {
		this.vertical = vertical;
		this.verType = verType;
	}
	
	public HasBounds getHorizontal() {
		if (horizontal == null) {
			return both;
		} else {
			return horizontal;
		}
	}
	
	public HasBounds getVertical() {
		if (vertical == null) {
			return both;
		} else {
			return vertical;
		}
	}
	
	/**
	 * Returns the ReferenceType of this wrapper for horizontal reference. (Where to place the object horizontally)
	 * Either INSIDE, OUTSIDE or OUTSIDE_CORNER.
	 * If there's only one object reference, the second Alignment will be inside unless type is OUTSIDE_CORNER. Otherwise the defined type is given.
	 * @param isFirstAlignment
	 * @return 
	 */
	public ReferenceType getTypeHorizontal(boolean isFirstAlignment) {
		if (horType != null) {
			return horType; //Two reference objects
		}
		
		//Only one reference object
		if (isFirstAlignment || type == ReferenceType.OUTSIDE_CORNER) {
			return type;
		} else {
			return ReferenceType.INSIDE;
		}
	}
	
	/**
	 * Returns the ReferenceType of this wrapper for vertical reference. (Where to place the object vertically)
	 * Either INSIDE, OUTSIDE or OUTSIDE_CORNER.
	 * If there's only one object reference, the second Alignment will be inside unless type is OUTSIDE_CORNER. Otherwise the defined type is given.
	 * @param isFirstAlignment
	 * @return 
	 */
	public ReferenceType getTypeVertical(boolean isFirstAlignment) {
		if (verType != null) {
			return verType; //Two reference objects
		}
		
		//Only one reference object
		if (isFirstAlignment || type == ReferenceType.OUTSIDE_CORNER) {
			return type;
		} else {
			return ReferenceType.INSIDE;
		}
	}
	
	public ReferenceType getTypeHorizontal(Alignments alignments) {
		boolean isFirst = alignments.firstIsHorizontal();
		return getTypeHorizontal(isFirst);
	}
	
	public ReferenceType getTypeVertical(Alignments alignments) {
		boolean isFirst = alignments.firstIsVertical();
		return getTypeVertical(isFirst);
	}
}
