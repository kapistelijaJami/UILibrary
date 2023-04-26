package uilibrary.menu;

public abstract class InteractableElement extends Element {
	public InteractableElement(int width, int height) {
		super(width, height);
	}
	
	public abstract boolean hover(int x, int y); //TODO: see how you want to do cursor type from hover. Maybe this returns Integer etc and null is for false?
	public abstract boolean click(int x, int y);
}
