package uilibrary.interfaces;

import java.awt.Dimension;

public interface HasSize {
	public int getWidth();
	public int getHeight();
	
	public default Dimension getSize() {
		return new Dimension(getWidth(), getHeight());
	}
}
