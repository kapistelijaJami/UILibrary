package uilibrary.elements;

import java.awt.Graphics2D;
import uilibrary.arrangement.Arrangement;
import static uilibrary.enums.Alignment.*;
import uilibrary.enums.ReferenceType;
import uilibrary.interfaces.HasBounds;
import uilibrary.interfaces.HasSize;

public abstract class Element implements HasBounds {
	private int width, height;
	private HasSize hasSize; //If this is null, it uses width and height. Otherwise gets the size from getSize() method of HasSize object.
	private Arrangement arrangement;
	
	public Element(int width, int height) { //For variable size width and height, just override getWidth() and getHeight() methods.
		this(width, height, true);
	}
	
	public Element(int width, int height, boolean alignCenter) {
		this(0, 0, width, height);
		
		if (alignCenter) {
			arrangement.align(CENTER);
		}
	}
	
	/**
	 * Places the object relative the x and y coordinates.
	 * Top left will touch the coordinates by default.
	 * @param x
	 * @param y
	 * @param width
	 * @param height 
	 */
	public Element(int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		arrangement = new Arrangement(this);
		arrangement.setReference(x, y).align(TOP, LEFT);
	}
	
	public Element(HasSize hasSize) {
		this(hasSize, true);
	}
	
	public Element(HasSize hasSize, boolean alignCenter) {
		this(0, 0, hasSize);
		
		if (alignCenter) {
			arrangement.align(CENTER);
		}
	}
	
	/**
	 * Places the object relative the x and y coordinates.
	 * Top left will touch the coordinates by default.
	 * Element's size will always be taken from HasSize object, so they will stay synced.
	 * @param x
	 * @param y
	 * @param hasSize 
	 */
	public Element(int x, int y, HasSize hasSize) {
		this.hasSize = hasSize;
		arrangement = new Arrangement(this);
		arrangement.setReference(x, y).align(TOP, LEFT);
	}
	
	public Arrangement arrange() {
		return arrangement;
	}
	
	public Arrangement arrange(int x, int y) {
		return arrangement.setReference(x, y);
	}
	
	public Arrangement arrange(HasBounds reference) {
		return arrangement.setReference(reference);
	}
	
	public Arrangement arrange(HasBounds reference, ReferenceType type) {
		return arrangement.setReference(reference, type);
	}
	
	public Arrangement arrange(HasBounds horizontal, ReferenceType horType, HasBounds vertical, ReferenceType verType) {
		return arrangement.setReference(horizontal, horType, vertical, verType);
	}
	
	//Another method to get Arrangement which matches semantically better to the situation in some cases.
	public Arrangement getArrangement() {
		return arrangement;
	}
	
	public void setArrangement(Arrangement arrangement) {
		this.arrangement = arrangement;
	}
	
	@Override
	public int getX() {
		return arrangement.getX();
	}
	
	@Override
	public int getY() {
		return arrangement.getY();
	}
	
	@Override
	public int getWidth() {
		if (hasSize != null) {
			return hasSize.getWidth();
		}
		return width;
	}
	
	@Override
	public int getHeight() {
		if (hasSize != null) {
			return hasSize.getHeight();
		}
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
		hasSize = null;
		arrangement.updateLocation(true); //Have to update location, it might depend on size. Must force update on other objects as well.
	}
	
	public void setHeight(int height) {
		this.height = height;
		hasSize = null;
		arrangement.updateLocation(true); //Have to update location, it might depend on size. Must force update on other objects as well.
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		hasSize = null;
		arrangement.updateLocation(true); //Have to update location, it might depend on size. Must force update on other objects as well.
	}
	
	public void setSize(HasSize hasSize) {
		this.hasSize = hasSize;
		arrangement.updateLocation(true); //Have to update location, it might depend on size. Must force update on other objects as well.
	}
	
	public abstract void render(Graphics2D g);
}
