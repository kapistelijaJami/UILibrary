package uilibrary.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

//Selectable button
public class Tab extends Button {
	protected boolean selected = false;
	
	public Tab(int width, int height, Color color) {
		super(width, height, color);
	}
	
	public Tab(int width, int height, Color color, Runnable action) {
		super(width, height, color, action);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	protected void renderEdge(Graphics2D g) {
		Object oldAntiAlias = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		if (selected) {
			g.setColor(selectedColor);
			int thickness = 2;
			g.setStroke(new BasicStroke(thickness));
			g.drawRect(getX() + thickness / 2, getY() + thickness / 2, getWidth() - thickness, getHeight() - thickness);
		} else if (edgeColor != null) {
			g.setColor(edgeColor);
			int thickness = 2;
			g.setStroke(new BasicStroke(thickness));
			g.drawRect(getX() + thickness / 2, getY() + thickness / 2, getWidth() - thickness, getHeight() - thickness);
		}
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntiAlias);
	}
}
