package uilibrary;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import uilibrary.enums.DividerOrientation;
import uilibrary.enums.DividerOrientation.ScalingDirection;

public abstract class Panel {
	protected int x, y;
	protected int width, height;
	
	public Panel(int width, int height) {
		this(0, 0, width, height);
	}
	
	public Panel(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	//override these when implementation differs
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public void update() {}
	public abstract void render(Graphics2D g);
	
	public int getSpace(DividerOrientation dir) {
		switch (dir) {
			case HORIZONTAL:
				return getHeight();
			case VERTICAL:
				return getWidth();
		}
		
		return 0;
	}
	
	public void setSpace(int val, ScalingDirection dir) {
		switch (dir) {
			case TOP:
				int diff = getHeight() - val;
				setHeight(val);
				setY(getY() + diff);
				break;
			case LEFT:
				diff = getWidth() - val;
				setWidth(val);
				setX(getX() + diff);
				break;
			case BOTTOM:
				setHeight(val);
				break;
			case RIGHT:
				setWidth(val);
				break;
		}
	}
}
