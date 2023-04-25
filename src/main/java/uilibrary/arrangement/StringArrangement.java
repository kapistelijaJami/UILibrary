package uilibrary.arrangement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import uilibrary.RenderMultilineText;
import uilibrary.RenderText;

public class StringArrangement extends Element {
	private String text;
	private Color color;
	private boolean multiline = false;
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
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		if (multiline) {
			RenderMultilineText.drawMultilineText(g, text, font, getBounds(), true, arrangement.getAligns());
		} else {
			RenderText.drawStringWithAlignment(g, text, getBounds(), font, arrangement.getAligns());
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
