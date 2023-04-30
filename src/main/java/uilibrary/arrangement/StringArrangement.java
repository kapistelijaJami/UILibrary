package uilibrary.arrangement;

import uilibrary.menu.Element;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import uilibrary.RenderMultilineText;
import uilibrary.RenderText;
import uilibrary.menu.Margin;

public class StringArrangement extends Element {
	private String text;
	private Color color;
	private boolean multiline = false;
	private boolean overflow = true;
	private Font font;
	public static Font defaultFont = new Font("Serif", Font.BOLD, 20);
	
	/**
	 * If arrangement doesn't have a size, text will be single line, otherwise multiline inside the size.
	 * @param text
	 * @param color
	 */
	public StringArrangement(String text, Color color) {
		this(text, 0, 0, color, defaultFont); //No size, will be single line. We'll calculate the size for one line.
		
		updateSize();
		multiline = false;
	}
	
	public StringArrangement(String text, Color color, Font font) {
		this(text, 0, 0, color, font);
		
		updateSize();
		multiline = false;
	}
	
	public StringArrangement(String text, Dimension size, Color color) {
		this(text, size.width, size.height, color, defaultFont);
	}
	
	public StringArrangement(String text, int width, int height, Color color) {
		this(text, width, height, color, defaultFont);
	}
	
	public StringArrangement(String text, Dimension size, Color color, Font font) {
		this(text, size.width, size.height, color, font);
	}
	
	public StringArrangement(String text, int width, int height, Color color, Font font) {
		super(width, height);
		
		this.text = text;
		this.color = color;
		this.font = font;
		
		multiline = true;
	}
	
	public void setFontSize(float size) {
		font = font.deriveFont(size);
	}
	
	public void setOverflow(boolean b) {
		this.overflow = b;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		Rectangle bounds = getBounds();
		if (multiline) {
			//Margin works differently in multiline StringArrangement.
			//It shrinks the bounds the text will be fit inside,
			//so that the margin is how much space there is between the edge,
			//and you can just have the parent element as bounds.
			Margin margin = arrangement.getMargin();
			bounds.width -= margin.getX() * 2;
			bounds.height -= margin.getY() * 2;
			RenderMultilineText.drawMultilineText(g, text, bounds, font, overflow, arrangement.getAligns());
		} else {
			RenderText.drawStringWithAlignment(g, text, bounds, font, arrangement.getAligns());
		}
	}
	
	private void updateSize() {
		width = RenderText.getStringWidth(font, text);
		height = RenderText.getFontHeight(font);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		if (!multiline) {
			updateSize();
		}
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
		if (!multiline) {
			updateSize();
		}
	}
	
	public void setSize(int width, int height) { //TODO: make these type of changes update all the Arrangements.
		this.width = width;
		this.height = height;
		
		multiline = true;
	}
}
