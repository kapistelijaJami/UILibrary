package uilibrary.menu;

import java.awt.Cursor;

public abstract class InteractableElement extends Element {
	protected int hoverCursor = Cursor.HAND_CURSOR;
	
	public InteractableElement(int width, int height) {
		super(width, height);
	}
	
	public void setHoverCursor(int hoverCursor) {
		this.hoverCursor = hoverCursor;
	}
	
	public int getHoverCursor() {
		return hoverCursor;
	}
	
	public abstract boolean hover(int x, int y); //TODO: see how you want to change cursor type from hover. Maybe this returns Integer etc and null is for false? Or just keep boolean, and use getHoverCursor().
	public abstract boolean click(int x, int y);
}
