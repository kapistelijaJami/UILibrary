package uilibrary.elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import uilibrary.arrangement.Alignments;
import uilibrary.arrangement.Arrangement;
import uilibrary.arrangement.Padding;
import static uilibrary.enums.Alignment.*;
import uilibrary.interfaces.HasSize;
import uilibrary.util.RenderMultilineText;
import uilibrary.util.RenderText;

//There could be three types of fixed width texts.
//	1. Fixed width single line, overflow from the right.
//  2. Fixed width single line, no overflow, so clip the text.
//  3. Fixed width multiline.
//Only one of them is implemented right now, need to allow all. Clipping from the right hasn't been done yet either.
//-- Now made overflow and multiline setters.

//TODO: Also add title so you can hover
public class TextElement extends Element {
	private String text;
	private Color color;
	private boolean multiline = false;
	private boolean overflow = true;
	private Padding padding = new Padding();
	private boolean visible = true;
	private boolean fixedWidth = false;
	
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
	
	public TextElement(String text, int width, Color color) {
		this(text, width, color, defaultFont);
	}
	
	public TextElement(String text, Color color, Font font) {
		this(text, 0, 0, color, font);
		
		multiline = false;
		updateSize();
	}
	
	public TextElement(String text, int width, Color color, Font font) {
		this(text, width, 0, color, font);
		
		//multiline = false; //TODO: If this is false, then the width doesn't really do anything, fixed width has positioning bugs too
							 //TODO: Not sure what to do with vertical positioning if height is not given.
		fixedWidth = true;
		updateSize();
	}
	
	public TextElement(String text, Dimension size, Color color) {
		this(text, size.width, size.height, color, defaultFont);
	}
	
	public TextElement(String text, HasSize hasSize, Color color) {
		this(text, hasSize, color, defaultFont);
	}
	
	public TextElement(String text, int width, int height, Color color) {
		this(text, width, height, color, defaultFont);
	}
	
	public TextElement(String text, Dimension size, Color color, Font font) {
		this(text, size.width, size.height, color, font);
	}
	
	public TextElement(String text, HasSize hasSize, Color color, Font font) {
		super(hasSize);
		
		this.text = text;
		this.color = color;
		this.font = font;
		
		multiline = true;
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
		if (!multiline) {
			updateSize();
		}
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
	
	public TextElement setMultiline(boolean multiline) {
		this.multiline = multiline;
		return this;
	}
	
	public TextElement setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}
	
	@Override
	public void render(Graphics2D g) {
		if (!visible) {
			return;
		}
		g.setColor(color);
		Rectangle bounds = getBounds();
		Arrangement arrangement = getArrangement();
		if (multiline) {
			//Making the bounds smaller by padding
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
			
			//If not allowed to overflow, we will clip the area to render.
			if (!overflow) {
				g.setClip(bounds); //TODO: There's something wrong when clipping is on and the bounds are small, it doesn't render if it's barely below or something. Has to do with positioning bottom with fixed width.
			}
			
			RenderText.drawStringWithAlignment(g, text, bounds, font, aligns.asArray());
			g.setClip(null);
		}
	}
	
	private void updateSize() {
		if (!fixedWidth) {
			setWidth(RenderText.getStringWidth(font, text));
		}
		setHeight(RenderText.getFontHeight(font));
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
	
	@Override
	public void setSize(HasSize hasSize) {
		multiline = true;
		super.setSize(hasSize);
	}
}
