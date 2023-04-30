package uilibrary.interfaces;

public interface Hoverable {
	public boolean hover(int x, int y); //TODO: see how you want to change cursor type from hover. Maybe this returns Integer etc and null is for false? Or just keep boolean, and use getHoverCursor().
}
