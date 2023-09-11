package uilibrary;

import java.awt.Graphics2D;
import uilibrary.enums.DividerOrientation;
import uilibrary.enums.DividerOrientation.ScalingDirection;
import uilibrary.interfaces.HasBounds;

public abstract class Panel implements HasBounds { //TODO: create a panel that extends Element, and uses arrangement things to position itself. Same for divider.
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
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public int getHeight() {
		return height;
	}
	@Override
	public int getX() {
		return x;
	}
	@Override
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
