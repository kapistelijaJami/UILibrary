package uilibrary.elements;

import java.awt.Graphics2D;
import uilibrary.arrangement.Arrangement;
import static uilibrary.enums.Alignment.*;
import uilibrary.enums.ReferenceType;
import uilibrary.interfaces.HasBounds;

public abstract class Element implements HasBounds {
	protected int width, height;
	private final Arrangement arrangement;
	
	public Element(int width, int height) {
		this.width = width;
		this.height = height;
		
		arrangement = new Arrangement(this);
	}
	
	/**
	 * Places the object so that top left will touch the x and y coordinates by default.
	 * @param x
	 * @param y
	 * @param width
	 * @param height 
	 */
	public Element(int x, int y, int width, int height) {
		this(width, height);
		arrangement.setReference(x, y).align(TOP, LEFT);
	}
	
	public Arrangement arrange() {
		return arrangement;
	}
	
	public Arrangement arrange(int x, int y) {
		return arrangement.setReference(x, y);
	}
	
	public Arrangement arrange(HasBounds reference, ReferenceType type) {
		return arrangement.setReference(reference, type);
	}
	
	public Arrangement arrange(HasBounds horizontal, ReferenceType horType, HasBounds vertical, ReferenceType verType) {
		return arrangement.setReference(horizontal, horType, vertical, verType);
	}
	
	//Another method to get Arrangement more semantically matching the situation better in some cases.
	public Arrangement getArrangement() {
		return arrangement;
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
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
		arrangement.updateLocation(true); //Have to update location, it might depend on size. Must force update on other objects as well.
	}
	
	public void setHeight(int height) {
		this.height = height;
		arrangement.updateLocation(true); //Have to update location, it might depend on size. Must force update on other objects as well.
	}
	
	public abstract void render(Graphics2D g);
}
