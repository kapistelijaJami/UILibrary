package uilibrary;

import uilibrary.enums.DividerOrientation;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import uilibrary.util.HelperFunctions;

//TODO: Tee tähän viel sillei, että pystyy pilkkoo useamminkin samaan suuntaan
//TODO: Divider is Panel, because then you can put a divider to be one of the Panels inside another divider.
//This could be changed so that dividers are just dividing another panel and are not panels themselves,
//but it would require current dividers to use an extra panel.
public class Divider extends Panel {
	private int value;			//actual vertical/horizontal distance from top/left
	private int length;			//how long the divider is
	private int thickness;		//how thick it is for mouse to grab onto
	private int firstMinSpace;	//how much space first panel have to have minimum.
	private int secondMinSpace;	//how much space second panel have to have minimum.
	private int maxSpace;
	
	public DividerOrientation dir;
	
	private boolean dragging = false;
	private boolean valueChanged = false;
	private Point lastMouseLocation;
	private boolean lengthChanged = false;
	private boolean maxSpaceChanged = false;
	
	private boolean movable = true;
	
	private Panel first, second; //panels that this scales and is in between at.
	
	public Divider(int value, int thickness, int minSpace, Panel first, Panel second, DividerOrientation dir) {
		this(value, thickness, minSpace, minSpace, first, second, dir);
	}
	
	public Divider(int value, int thickness, int firstMinSpace, int secondMinSpace, Panel first, Panel second, DividerOrientation dir) {
		super(0, 0); //Doesn't have x, y, width or height. Calculates them in getBounds() every time.
		
		this.value = value;
		this.thickness = thickness;
		this.firstMinSpace = firstMinSpace;
		this.secondMinSpace = secondMinSpace;
		
		this.first = first;
		this.second = second;
		
		this.maxSpace = first.getSpace(dir) + second.getSpace(dir);
		
		
		this.dir = dir;
		changePosition();
		
		switch (dir) {
			case HORIZONTAL:
				length = first.getWidth();
				break;
			case VERTICAL:
				length = first.getHeight();
				break;
		}
	}
	
	public void setMovable(boolean b) {
		movable = b;
	}
	
	@Override
	public void update() {
		if (dragging || valueChanged) {
			calculateNewValueFromCoordinates(lastMouseLocation.x, lastMouseLocation.y);
		}
		if (lengthChanged) {
			updateLength();
		}
		if (maxSpaceChanged) {
			updateMaxSpace();
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		Rectangle rect = getBounds();
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
	
	public void updateValue(int newVal) {
		value = HelperFunctions.clamp(newVal, firstMinSpace, getTotalSpace() - secondMinSpace);
		changePosition();
	}
	
	private void changePosition() {
		int totalSpace = getTotalSpace(); //have to take it into a variable, because the first setSpace() call already changes this result, and I need it in the second one.
		
		first.setSpace(value, dir.getFirst());
		second.setSpace(totalSpace - value, dir.getSecond());
		
		updateSecondLocation();
	}
	
	private void updateSecondLocation() { //this is here so you don't manually have to calculate the location for the second panel
		switch (dir) {
			case HORIZONTAL:
				second.setX(first.getX());
				second.setY(value);
				break;
			case VERTICAL:
				second.setX(value);
				second.setY(first.getY());
				break;
		}
	}
	
	public int getTotalSpace() {
		return first.getSpace(dir) + second.getSpace(dir);
	}
	
	public int getDividerX() {
		return this.first.getX() + value - thickness / 2;
	}
	
	public int getDividerY() {
		return this.first.getY() + value - thickness / 2;
	}
	
	public void setLength(int length, boolean instantUpdate) {
		this.length = length;
		if (instantUpdate) {
			updateLength();
		} else {
			lengthChanged = true;
		}
	}
	
	@Override
	public Rectangle getBounds() { //Is the bounds of the divider itself, the area that you can hover and click if it's movable. Not the total size of panels it divides.
		Rectangle rect = new Rectangle();
		switch (dir) {
			case HORIZONTAL:
				rect.x = first.getX();
				rect.y = first.getY() + value - thickness / 2;
				rect.width = length;
				rect.height = thickness;
				break;
			case VERTICAL:
				rect.x = first.getX() + value - thickness / 2;
				rect.y = first.getY();
				rect.width = thickness;
				rect.height = length;
				break;
		}
		return rect;
	}
	
	private void calculateNewValueFromCoordinates(int x, int y) {
		switch (dir) {
			case HORIZONTAL:
				updateValue(y - first.getY());
				break;
			case VERTICAL:
				updateValue(x - first.getX());
				break;
		}
		
		valueChanged = false;
	}
	
	public boolean mousePressed(MouseEvent e) {
		if (!movable) {
			return false;
		}
		if (dragging || isInside(e.getX(), e.getY())) {
			lastMouseLocation = e.getPoint();
			
			valueChanged = true;
			dragging = true;
			return true;
		}
		
		return false;
	}
	
	public boolean mouseDragged(MouseEvent e) { //almost the same as mousePressed, except we dont want to initiate the drag.
		if (!movable) {
			return false;
		}
		if (!dragging) {
			return false;
		}
		
		return mousePressed(e);
	}
	
	public boolean mouseReleased(MouseEvent e) {
		if (!movable) {
			return false;
		}
		if (dragging) {
			lastMouseLocation = e.getPoint();
			valueChanged = true;
			dragging = false;
			return true;
		}
		
		return false;
	}
	
	public boolean hover(MouseEvent e) {
		if (!movable) {
			return false;
		}
		return dragging || isInside(e.getX(), e.getY());
	}
	
	public int getCursorTypeForHover() {
		switch (dir) {
			case HORIZONTAL:
				return Cursor.N_RESIZE_CURSOR;
			case VERTICAL:
				return Cursor.W_RESIZE_CURSOR;
		}
		return Cursor.DEFAULT_CURSOR;
	}
	
	private void updateLength() {
		switch (dir) {
			case HORIZONTAL:
				first.setWidth(length);
				second.setWidth(length);
				break;
			case VERTICAL:
				first.setHeight(length);
				second.setHeight(length);
				break;
		}
		
		lengthChanged = false;
	}
	
	/**
	 * Updates the maximum space for this Divider and its children.
	 * This method will update the space for the first child based on the proportion of space it currently occupies in relation to the total space of this Divider.
	 * Then, the start location of the second child will be updated based on the direction of this Divider.
	 * The space for the second child will then be set to the remaining space.
	 * If either child's space falls below its minimum allowed space, the value will be updated accordingly.
	 * 
	 * @param maxSpace the new maximum space for this Divider
	 */
	private void updateMaxSpace() {
		int totalSpace = getTotalSpace();
		double firstCoefficient = first.getSpace(dir) / (double) totalSpace;
		
		first.setSpace((int) (firstCoefficient * maxSpace), dir.getFirst());
		
		value = first.getSpace(dir);
		switch (dir) {
			case HORIZONTAL:
				second.setY(value);
				break;
			case VERTICAL:
				second.setX(value);
				break;
		}
		
		second.setSpace((int) (maxSpace - first.getSpace(dir)), dir.getFirst()); //this is dir.getFirst() on purpose, to change the space so that only space changes, not the start location. We changed start location manually.
		
		if (first.getSpace(dir) < firstMinSpace) {
			updateValue(firstMinSpace);
		}
		
		if (second.getSpace(dir) < secondMinSpace) {
			updateValue(getTotalSpace() - secondMinSpace);
		}
		
		maxSpaceChanged = false;
	}
	
	public void setMaxSpace(int space, boolean instantUpdate) {
		maxSpace = space;
		if (instantUpdate) {
			updateMaxSpace();
		} else {
			maxSpaceChanged = true;
		}
	}

	public int getLength() {
		return length;
	}

	public Panel getFirst() {
		return first;
	}
	
	@Override
	public int getWidth() {
		switch (dir) {
			case HORIZONTAL:
				return getLength();
			case VERTICAL:
				return getTotalSpace();
		}
		
		return 0;
	}
	
	@Override
	public int getHeight() {
		switch (dir) {
			case HORIZONTAL:
				return getTotalSpace();
			case VERTICAL:
				return getLength();
		}
		
		return 0;
	}
	
	@Override
	public int getX() {
		return first.getX();
	}
	
	@Override
	public int getY() {
		return first.getY();
	}
	
	@Override
	public void setX(int x) {
		switch (dir) {
			case HORIZONTAL:
				first.setX(x);
				second.setX(x);
				break;
			case VERTICAL:
				first.setX(x);
				break;
		}
	}

	@Override
	public void setY(int y) {
		switch (dir) {
			case HORIZONTAL:
				first.setY(y);
				break;
			case VERTICAL:
				first.setY(y);
				second.setY(y);
				break;
		}
	}
	
	@Override
	public void setWidth(int width) {
		switch (dir) {
			case HORIZONTAL:
				setLength(width, true);
				break;
			case VERTICAL:
				setMaxSpace(width, true);
				break;
		}
	}
	
	@Override
	public void setHeight(int height) {
		switch (dir) {
			case HORIZONTAL:
				setMaxSpace(height, true);
				break;
			case VERTICAL:
				setLength(height, true);
				break;
		}
	}
}
