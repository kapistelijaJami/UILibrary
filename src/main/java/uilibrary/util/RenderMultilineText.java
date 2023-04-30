package uilibrary.util;

import uilibrary.enums.Alignment;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import uilibrary.enums.ReferenceType;
import uilibrary.util.HelperFunctions;
import uilibrary.arrangement.Margin;

/**
 * Renders text in multiple lines inside bounds.
 * Splits lines after words.
 * If you need temporary graphics object use RenderText.getFakeGraphics().
 */
public class RenderMultilineText extends RenderText {
	public enum TextHorizontalAlign {
		LEFT, CENTER, RIGHT; //When multiline text has shorter rows than others, they will be aligned with the rest with this enum.
		
		/**
		 * Converts to normal alignment for use in other functions that aligns horizontally.
		 * @return 
		 */
		public Alignment toAlignment() {
			if (this == LEFT) {
				return Alignment.LEFT;
			} else if (this == RIGHT) {
				return Alignment.RIGHT;
			}
			return Alignment.CENTER;
		}
	}
	
	public static Rectangle drawMultilineText(Graphics2D g, String allTexts, Rectangle bounds, Font font, boolean overflow, Alignment... aligns) {
		String[] texts = allTexts.split("\n", -1); //limit -1, so empty strings will be included
		return drawMultilineText(g, texts, bounds, font, overflow, aligns);
	}
	
	/**
	 * Draws a multiline text with bounds and alignments inside the bounds.
	 * @param g
	 * @param texts
	 * @param font
	 * @param bounds
	 * @param aligns
	 * @param overflow Determines if the text overflows (is visible) from the bottom of the bounds (true) or is hidden (false).
	 * @return Returns render area.
	 */
	public static Rectangle drawMultilineText(Graphics2D g, String[] texts, Rectangle bounds, Font font, boolean overflow, Alignment... aligns) {
		return drawMultilineText(g, texts, bounds, font, overflow, TextHorizontalAlign.LEFT, aligns);
	}
	
	public static Rectangle drawMultilineText(Graphics2D g, String allTexts, Rectangle bounds, Font font, boolean overflow, TextHorizontalAlign horizAlign, Alignment... aligns) {
		String[] texts = allTexts.split("\n", -1); //limit -1, so empty strings will be included
		return drawMultilineText(g, texts, bounds, font, overflow, horizAlign, aligns);
	}
	
	/**
	 * Draws a multiline text with bounds and alignments inside the bounds.
	 * @param g
	 * @param texts
	 * @param font
	 * @param bounds
	 * @param overflow Determines if the text overflows (is visible) from the bottom of the bounds (true) or is hidden (false). 
	 * @param horizAlign When multiline text has shorter rows than others, they will be aligned with the rest with this enum. Default is LEFT.
	 * @param aligns
	 * @return Returns render area.
	 */
	public static Rectangle drawMultilineText(Graphics2D g, String[] texts, Rectangle bounds, Font font, boolean overflow, TextHorizontalAlign horizAlign, Alignment... aligns) {
		font = checkIfFontIsNull(font);
		g.setFont(font);
		
		AffineTransform old = g.getTransform();
		g.setTransform(new AffineTransform()); //This is here to get the metrics correct while at is rotated.
		
		FontMetrics metrics = g.getFontMetrics(font);
		
		ArrayList<String> lines = createAllLines(g, texts, font, bounds);
		Dimension neededSpace = getNeededSpace(g, lines, font, bounds);
		
		Point XYOffset = HelperFunctions.xAndYOffsetInsideBoundsFromAlignment(bounds, neededSpace, aligns);
		
		int drawPosY = bounds.y + metrics.getAscent() + XYOffset.y;
		float lineHeight = getFontLineInterval(g, font);
		
		g.setTransform(old);
		
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			
			if (!overflow && drawPosY > bounds.y + bounds.height) {
				break;
			}
			
			if (horizAlign == TextHorizontalAlign.LEFT) {
				g.drawString(line, bounds.x + XYOffset.x, drawPosY);
			} else {
				int lineWidth = RenderText.getStringWidth(g, font, line);
				int lineXOff = HelperFunctions.getXOffsetFromAlignment(neededSpace, lineWidth, new Margin(), horizAlign.toAlignment(), ReferenceType.INSIDE);
				
				g.drawString(line, bounds.x + XYOffset.x + lineXOff, drawPosY);
			}
			
			if (i != lines.size() - 1) {
				drawPosY += lineHeight;
			}
		}
		
		return new Rectangle(bounds.x + XYOffset.x, bounds.y + XYOffset.y, neededSpace.width, neededSpace.height);
	}
	
	/**
	 * Counts actual lines separated by '\n' characters.
	 * @param allTexts
	 * @return 
	 */
	public static int countLines(String allTexts) {
		String[] texts = allTexts.split("\n", -1);
		return texts.length;
	}
	
	/**
	 * Counts all rendered lines. On top of the lines separated by '\n' characters,
	 * the text will be wrapped because otherwise it crosses the bounds.
	 * @param allTexts
	 * @param font
	 * @param bounds
	 * @return 
	 */
	public static int countAllLines(String allTexts, Font font, Rectangle bounds) {
		return countAllLines(getFakeGraphics(), allTexts, font, bounds);
	}
	
	public static int countAllLines(Graphics2D g, String allTexts, Font font, Rectangle bounds) {
		String[] texts = allTexts.split("\n", -1);
		ArrayList<String> lines = createAllLines(g, texts, font, bounds);
		
		return lines.size();
	}
	
	public static ArrayList<String> getAllLines(Graphics2D g, String allTexts, Font font, Rectangle bounds) {
		String[] texts = allTexts.split("\n", -1);
		ArrayList<String> lines = createAllLines(g, texts, font, bounds);
		
		return lines;
	}
	
	private static int longestLineWidth(Graphics2D g, ArrayList<String> lines, Font font) {
		int longest = 0;
		int res;
		
		for (String text : lines) {
			res = getStringWidth(g, font, text);
			if (res > longest) {
				longest = res;
			}
		}
		
		return longest;
	}
	
	/**
	 * Returns the actual space needed to render the text inside given bounds.
	 * Rectangle x and y will be the location of the top left corner of this area,
	 * and width and height will be the size of the area.
	 * @param allTexts
	 * @param font
	 * @param bounds What are the bounds where this text should be fitted. Might overflow from below if too much text.
	 * @param aligns
	 * @return 
	 */
	public static Rectangle getRenderedBounds(String allTexts, Font font, Rectangle bounds, Alignment... aligns) {
		return getRenderedBounds(getFakeGraphics(), allTexts, font, bounds, aligns);
	}
	
	public static Rectangle getRenderedBounds(Graphics2D g, String allTexts, Font font, Rectangle bounds, Alignment... aligns) {
		Dimension neededSpace = getNeededSpace(g, allTexts, font, bounds);
		Point XYOffset = HelperFunctions.xAndYOffsetInsideBoundsFromAlignment(bounds, neededSpace, aligns);
		
		return new Rectangle(bounds.x + XYOffset.x, bounds.y + XYOffset.y, neededSpace.width, neededSpace.height);
	}
	
	public static Dimension getNeededSpace(String allTexts, Font font, Rectangle bounds) {
		return getNeededSpace(getFakeGraphics(), allTexts, font, bounds);
	}
	
	public static Dimension getNeededSpace(Graphics2D g, String allTexts, Font font, Rectangle bounds) {
		String[] texts = allTexts.split("\n", -1); //limit -1, so empty strings will be included
		font = checkIfFontIsNull(font);
		g.setFont(font);
		return getNeededSpace(g, createAllLines(g, texts, font, bounds), font, bounds);
	}
	
	public static Dimension getNeededSpace(Graphics2D g, ArrayList<String> lines, Font font, Rectangle bounds) {
		int width = longestLineWidth(g, lines, font);
		
		float lineHeight = getFontLineInterval(g, font);
		int height = (int) (lineHeight * lines.size());
		
		//width shouldn't be larger than bounds.width, unless single character cant fit inside bounds.width
		//height could be bigger, then we return bounds.height. Overflow will decide if it gets rendered below bounds.
		return new Dimension(Math.min(width, bounds.width), Math.min(height, bounds.height));
	}
	
	private static ArrayList<String> createAllLines(Graphics2D g, String[] texts, Font font, Rectangle bounds) {
		ArrayList<String> lines = new ArrayList<>();
		
		for (int i = 0; i < texts.length; i++) {
			String paragraph = texts[i];
			
			lines.addAll(getLines(g, font, paragraph, bounds.width));
		}
		
		return lines;
	}
	
	private static ArrayList<String> getLines(Graphics2D g, Font font, String paragraph, int maxWidth) {
		FontMetrics metrics = g.getFontMetrics(font);
		ArrayList<String> lines = new ArrayList<>();
		
		String[] paragraphs = paragraph.split("\n", -1);
		
		for (String para : paragraphs) {
			if (para.isEmpty()) {
				para = " ";
			}
			for	(int i = 1; i <= para.length(); i++) {
				String sub = para.substring(0, i);

				if (i == para.length()) {
					lines.add(para);
					break;

				} else if (metrics.stringWidth(sub) > maxWidth) {
					int index = sub.lastIndexOf(' ');
					if (index == -1) {
						index = Math.max(0, i - 2);
					}
					lines.add(para.substring(0, index + 1)); //let the space be here in case there is no space at all, so we dont skip a character
					para = para.substring(index + 1);
					i = 0;
				}
			}
		}
		
		if (lines.isEmpty()) {
			lines.add(paragraph);
		}
		
		//Replaces lines with only one space char with fully empty string
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.length() == 1 && line.charAt(0) == ' ') {
				lines.set(i, "");
			}
		}
		
		return lines;
	}
}
