package uilibrary.interfaces;

import java.awt.Graphics2D;
import uilibrary.Window;

public interface HasWindow {
	public Window getWindow();
	public void render(Graphics2D g);
}
