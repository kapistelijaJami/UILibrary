package uilibrary.example;

import java.awt.Color;
import java.awt.Graphics2D;
import uilibrary.menu.Element;

public class Box extends Element {
	private Color color;
	
	public Box(int width, int height, Color color) {
		super(width, height);
		
		this.color = color;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRect(getX(), getY(), width, height);
	}
}
