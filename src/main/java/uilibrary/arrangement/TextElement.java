package uilibrary.arrangement;

import uilibrary.elements.Element;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static uilibrary.enums.Alignment.*;
import uilibrary.util.RenderMultilineText;
import uilibrary.util.RenderText;

public class TextElement extends Element {
	private String text;
	private Color color;
	private boolean multiline = false;
	private boolean overflow = true;
	private Padding padding = new Padding();
	
	private Font font;
	public static Font defaultFont = new Font("Serif", Font.BOLD, 20);
	
	/**
	 * If arrangement doesn't have a size, text will be single line, otherwise multiline inside the size.
	 * @param text
	 * @param color
	 */
	public TextElement(String text, Color color) {
		this(text, 0, 0, color, defaultFont); //No size, will be single line. We'll calculate the size for one line.
		
		multiline = false;
		updateSize();
	}
	
	public TextElement(String text, Color color, Font font) {
		this(text, 0, 0, color, font);
		
		multiline = false;
		updateSize();
	}
	
	public TextElement(String text, Dimension size, Color color) {
		this(text, size.width, size.height, color, defaultFont);
	}
	
	public TextElement(String text, int width, int height, Color color) {
		this(text, width, height, color, defaultFont);
	}
	
	public TextElement(String text, Dimension size, Color color, Font font) {
		this(text, size.width, size.height, color, font);
	}
	
	public TextElement(String text, int width, int height, Color color, Font font) {
		super(width, height);
		
		this.text = text;
		this.color = color;
		this.font = font;
		
		multiline = true;
	}
	
	public TextElement setFontSize(float size) {
		font = font.deriveFont(size);
		return this;
	}
	
	public TextElement setOverflow(boolean b) {
		this.overflow = b;
		return this;
	}
	
	public TextElement setPadding(int x, int y) {
		return setPadding(new Padding(x, y));
	}
	
	public TextElement setPadding(Padding padding) {
		this.padding = padding;
		return this;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		Rectangle bounds = getBounds();
		Arrangement arrangement = getArrangement();
		if (multiline) {
			bounds.x += padding.x;
			bounds.y += padding.y;
			bounds.width -= padding.x * 2;
			bounds.height -= padding.y * 2;
			RenderMultilineText.drawMultilineText(g, text, bounds, font, overflow, arrangement.getAlignsAsArray());
		} else {
			Alignments aligns = arrangement.getAligns();
			
			//When type is outside padding moves the text to the opposite direction
			int horMult = arrangement.getReference().getTypeHorizontal(aligns).isInside() ? 1 : -1;
			int verMult = arrangement.getReference().getTypeVertical(aligns).isInside() ? 1 : -1;
			
			if (aligns.getHorizontal() == LEFT) {
				bounds.x += horMult * padding.x;
			} else if (aligns.getHorizontal() == RIGHT) {
				bounds.x -= horMult * padding.x;
			}
			
			if (aligns.getVertical() == TOP) {
				bounds.y += verMult * padding.y;
			} else if (aligns.getVertical() == BOTTOM) {
				bounds.y -= verMult * padding.y;
			}
			
			RenderText.drawStringWithAlignment(g, text, bounds, font, aligns.asArray());
		}
	}
	
	private void updateSize() {
		width = RenderText.getStringWidth(font, text);
		height = RenderText.getFontHeight(font);
	}
	
	public String getText() {
		return text;
	}
	
	public TextElement setText(String text) {
		this.text = text;
		if (!multiline) {
			updateSize();
		}
		return this;
	}
	
	public Color getColor() {
		return color;
	}
	
	public TextElement setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public Font getFont() {
		return font;
	}
	
	public TextElement setFont(Font font) {
		this.font = font;
		if (!multiline) {
			updateSize();
		}
		return this;
	}
	
	@Override
	public void setSize(int width, int height) {
		multiline = true;
		super.setSize(width, height);
	}
}
