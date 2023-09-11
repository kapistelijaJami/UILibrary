package uilibrary.menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import uilibrary.elements.Element;
import uilibrary.interfaces.HasSize;

public abstract class Menu extends Element {
	protected Color backgroundColor;
	protected boolean active = true;
	protected Color edgeColor;
	protected int edgeThickness = 2;
	
	public Menu(Color backgroundColor, HasSize size) {
		super(size);
		
		this.backgroundColor = backgroundColor;
	}
	
	public Menu(Color backgroundColor, int width, int height) {
		super(width, height);
		
		this.backgroundColor = backgroundColor;
	}
	
	public Menu(Color backgroundColor, int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.backgroundColor = backgroundColor;
	}
	
	//TODO: see if you want to add empty update method here or in Element, so you can call update on any Menu object, even though some wouldn't do anything. Or add updateable interface, and have default implementation be empty.
	//TODO: same with keyPressed method.
	
	protected void renderBackground(Graphics2D g) {
		if (!active) {
			return;
		}
		
		Rectangle b = getBounds();
		
		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(b.x, b.y, b.width, b.height);
		}
		
		if (edgeColor != null) {
			g.setColor(edgeColor);
			g.setStroke(new BasicStroke(edgeThickness));
			g.drawRect(b.x, b.y, b.width, b.height);
		}
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void toggleActive() {
		active = !active;
	}
	
	public void setEdgeColor(Color color) {
		edgeColor = color;
	}
	
	public void setEdgeColor(Color color, int thickness) {
		edgeColor = color;
		edgeThickness = thickness;
	}
}
