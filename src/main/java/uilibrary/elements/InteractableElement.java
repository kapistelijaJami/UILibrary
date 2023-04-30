package uilibrary.elements;

import java.awt.Cursor;
import uilibrary.interfaces.Interactable;

public abstract class InteractableElement extends Element implements Interactable {
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
}
