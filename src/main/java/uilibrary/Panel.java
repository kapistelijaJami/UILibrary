package uilibrary;

import uilibrary.enums.DividerOrientation;
import uilibrary.enums.DividerOrientation.ScalingDirection;

public abstract class Panel {
	protected int x, y;
	protected int width, height;
	
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
