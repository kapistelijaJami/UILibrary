package uilibrary.menu;

import uilibrary.RenderText;
import uilibrary.Window;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import java.awt.BasicStroke;
import java.awt.RenderingHints;
import uilibrary.interfaces.HasBounds;

/*
Different callback function types:
	Supplier       ()    -> x
	Consumer       x     -> ()
	BiConsumer     x, y  -> ()
	Callable       ()    -> x	- throws exception
	Runnable       ()    -> ()
	Function       x     -> y
	BiFunction     x,y   -> z
	Predicate      x     -> boolean
	UnaryOperator  x1    -> x2
	BinaryOperator x1,x2 -> x3

This is why button uses Runnable.
*/
public class Button extends InteractableElement {
	protected boolean isMouseOver = false;
	protected boolean selected = false;
	protected boolean inactive = false;
	
	protected Color color;
	protected Color selectedColor = Color.YELLOW;
	protected Color highlightedColor;
	protected Color edgeColor = null;
	
	protected int textPadding = 5;
	
	protected ArrayList<StringAlignment> texts = new ArrayList<>();
	
	protected Runnable action;
	protected Runnable hoverAction = null;
	
	public Button(int width, int height, Color color) {
		this(width, height, color, null);
	}
	
	public Button(int width, int height, Color color, Runnable action) {
		super(width, height);
		this.color = color;
		this.action = action;
		
		highlightedColor = HelperFunctions.getDarkerColor(color, 100);
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public void setSelectedColor(Color color) {
		selectedColor = color;
	}
	
	public void setHighlightedColor(Color color) {
		highlightedColor = color;
	}
	
	public void setEdgeColor(Color color) {
		edgeColor = color;
	}
	
	public void addStringAlignment(StringAlignment s) {
		this.texts.add(s);
	}
	
	public void setStringAlignment(StringAlignment s) {
		if (texts.isEmpty()) {
			texts.add(s);
		} else {
			this.texts.set(0, s);
		}
	}
	
	public String getMainText() {
		return texts.get(0).getText();
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setIsMouseOver(boolean isMouseOver) {
		this.isMouseOver = isMouseOver;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	public void setHoverAction(Runnable action) {
		this.hoverAction = action;
	}
	
	@Override
	public boolean hover(int x, int y) {
		if (inactive) {
			return false;
		}
		if (isInside(x, y)) {
			if (isMouseOver) {
				return true;
			}
			isMouseOver = true;
			if (hoverAction != null) {
				hoverAction.run();
			}
			return true;
		}
		isMouseOver = false;
		return false;
	}
	
	/**
	 * Force a click if active.
	 * @return 
	 */
	public boolean click() {
		return click(0, 0, true);
	}
	
	/**
	 * Clicks the button if x and y are inside it and the button is active.
	 * @param x
	 * @param y
	 * @return 
	 */
	@Override
	public boolean click(int x, int y) {
		return click(x, y, false);
	}
	
	/**
	 * Clicks the button if x and y are inside it, and it is active.
	 * If forceClick is true, it doesn't check for location.
	 * @param x X location of the click.
	 * @param y Y location of the click.
	 * @param forceClick If true, it doesn't check for location.
	 * @return 
	 */
	private boolean click(int x, int y, boolean forceClick) {
		if (inactive) {
			return false;
		}
		
		if (!forceClick && !isInside(x, y)) {
			return false;
		}
		
		if (action != null) {
			action.run();
		}
		
		return true;
	}
	
	public void update() {}
	
	@Override
	public void render(Graphics2D g) {
		render(g, inactive);
	}
	
	public void render(Graphics2D g, boolean inactive) {
		renderBox(g, isMouseOver, inactive);
		
		g.setColor(Color.black);
		renderTexts(g, inactive);
	}
	
	public void render(Graphics2D g, int x, int y) {}
	
	public void renderBox(Graphics2D g, boolean highlighted, boolean inactive) {
		if (inactive) {
			g.setColor(Color.decode("#747474"));
			g.fillRect(getX(), getY(), width, height);
		} else if (highlighted) {
			g.setColor(highlightedColor);
			g.fillRect(getX(), getY(), width, height);
		} else {
			g.setColor(color);
			g.fillRect(getX(), getY(), width, height);
		}
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		if (selected) {
			g.setColor(selectedColor);
			int thickness = 2;
			g.setStroke(new BasicStroke(thickness));
			g.drawRect(getX() + thickness / 2, getY() + thickness / 2, width - thickness, height - thickness);
		} else if (edgeColor != null) {
			g.setColor(edgeColor);
			int thickness = 2;
			g.setStroke(new BasicStroke(thickness));
			g.drawRect(getX() + thickness / 2, getY() + thickness / 2, width - thickness, height - thickness);
		}
		
		Window.setGraphicsRenderingHints(g);
	}
	
	public void renderTexts(Graphics2D g, boolean inactive) {
		g.setColor(HelperFunctions.getDarkerColor(Color.decode("#747474"), 70));
		
		for (StringAlignment sa : texts) {
			if (!inactive) {
				g.setColor(sa.getColor());
			}
			RenderText.drawStringWithAlignment(g, sa.getText(), new Rectangle(getX() + textPadding, getY() + textPadding, width - textPadding * 2, height - textPadding * 2), sa.getFont(), sa.getAlign(), sa.getAlign2());
		}
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setInactive(boolean b) {
		inactive = b;
	}
}
