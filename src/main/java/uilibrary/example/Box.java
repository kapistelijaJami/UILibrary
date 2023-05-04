package uilibrary.example;

import java.awt.Color;
import java.awt.Graphics2D;
import uilibrary.elements.Element;

public class Box extends Element {
	private final Color color;
	
	public Box(int width, int height, Color color) {
		super(width, height);
		
		this.color = color;
	}
	
	//For absolute positioning:
	public Box(int x, int y, int width, int height, Color color) {
		super(x, y, width, height);
		
		this.color = color;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRect(getX(), getY(), width, height);
	}
	
	public Color getColor() {
		return color;
	}
}
