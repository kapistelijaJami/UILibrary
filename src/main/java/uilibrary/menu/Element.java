package uilibrary.menu;

import java.awt.Graphics2D;
import uilibrary.arrangement.Arrangement;
import uilibrary.enums.ReferenceType;
import uilibrary.interfaces.HasBounds;

public abstract class Element implements HasBounds {
	protected int width, height;
	protected Arrangement arrangement;
	
	public Element(int width, int height) {
		this.width = width;
		this.height = height;
		
		arrangement = new Arrangement(this);
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
	
	public abstract void render(Graphics2D g);
}
