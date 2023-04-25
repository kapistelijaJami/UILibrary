package uilibrary.example;

import java.awt.Color;
import java.awt.Graphics2D;
import uilibrary.arrangement.Arrangement;
import uilibrary.interfaces.HasBounds;

public class Box implements HasBounds {
	private int width, height;
	private Arrangement arrangement;
	
	private Color color;
	
	public Box(int width, int height, Color color) {
		this.width = width;
		this.height = height;
		this.color = color;
		
		arrangement = new Arrangement(this);
	}
	
	public Arrangement arrange() {
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
	
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRect(getX(), getY(), width, height);
	}
}
