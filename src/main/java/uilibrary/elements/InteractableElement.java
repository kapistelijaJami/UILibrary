package uilibrary.elements;

import java.awt.Cursor;
import uilibrary.interfaces.HasSize;
import uilibrary.interfaces.Interactable;

public abstract class InteractableElement extends Element implements Interactable {
	protected int hoverCursor = Cursor.HAND_CURSOR;
	
	public InteractableElement(int width, int height) {
		super(width, height);
	}
	
	public InteractableElement(int width, int height, boolean alignCenter) {
		super(0, 0, width, height);
	}
	
	public InteractableElement(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public InteractableElement(HasSize hasSize) {
		super(hasSize, true);
	}
	
	public InteractableElement(HasSize hasSize, boolean alignCenter) {
		super(0, 0, hasSize);
	}
	
	public InteractableElement(int x, int y, HasSize hasSize) {
		super(x, y, hasSize);
	}
	
	public void setHoverCursor(int hoverCursor) {
		this.hoverCursor = hoverCursor;
	}
	
	public int getHoverCursor() {
		return hoverCursor;
	}
}
