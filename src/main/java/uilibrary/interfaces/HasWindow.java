package uilibrary.interfaces;

import java.awt.Graphics2D;
import uilibrary.Window;

public interface HasWindow {
	public Window getWindow();
	
	/**
	 * Render to the window returned with getWindow().
	 * No need to dispose g or display manually.
	 * The GameLoop / Window will handle disposal and buffer flipping.
	 * @param g 
	 */
	public void render(Graphics2D g);
}
