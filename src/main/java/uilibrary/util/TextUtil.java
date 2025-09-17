package uilibrary.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

/**
 * ALL ARE OFFSET TO BASELINE, OR ORIGIN (baseline is the line where the base characters sit, and origin is the point in baseline, where a character's origin is.)
 * See this for reference:
 * https://i.stack.imgur.com/crGOe.png
 */
public class TextUtil {
	private static final String allDefaultCharacters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZzÅåÄäÖö0123456789";
	
	/**
	 * Gets the FontMetrics used in the calculations.
	 * @param font
	 * @param g
	 * @return 
	 */
	public static FontMetrics getFontMetrics(Font font, Graphics g) {
		return g.getFontMetrics(font);
	}
	
	/**
	 * Gets the FontMetrics without having the Graphics object.
	 * Might not be exactly accurate, but probably good enough for most cases.
	 * @param font
	 * @return 
	 */
	public static FontMetrics getFakeFontMetrics(Font font) {
		return RenderText.getFakeFontMetrics(font);
	}
	
	/**
	 * How much any character can go above baseline. (Some can go higher)
	 * Needs to be negated from the baseline.
	 * @param fm
	 * @return 
	 */
	public static int getAscent(FontMetrics fm) {
		return fm.getAscent();
	}
	
	/**
	 * How much any character can go below baseline. (Some can go lower)
	 * Needs to be added to the baseline.
	 * @param fm
	 * @return 
	 */
	public static int getDescent(FontMetrics fm) {
		return fm.getDescent();
	}
	
	/**
	 * How much space there are between lines
	 * (From lowest possible character to the highest possible character on the next line).
	 * So space from descent to next line's ascent.
	 * @param fm
	 * @return 
	 */
	public static int getLeading(FontMetrics fm) {
		return fm.getLeading();
	}
	
	/**
	 * How thicc this character is.
	 * Origin of the character sits at the baseline,
	 * and next character's origin is advanced by the advancement value.
	 * @param fm
	 * @param c
	 * @return 
	 */
	public static int getAdvancement(FontMetrics fm, char c) {
		return fm.charWidth(c);
	}
	
	/**
	 * The string width.
	 * Sum of the advancements of all characters in the String.
	 * @param fm
	 * @param s
	 * @return 
	 */
	public static int getStringWidth(FontMetrics fm, String s) {
		return fm.stringWidth(s);
	}
	
	/**
	 * Distance between baselines.
	 * Ascent + Descent + Leading.
	 * @param fm
	 * @return 
	 */
	public static int getLineHeight(FontMetrics fm) {
		return fm.getHeight();
	}
	
	/**
	 * x-height is the height of the character 'x'.
	 * It's the height of the main part of usual lower case letters.
	 * Might be slower to execute. If performance is consideration, call once in initialization.
	 * @param font
	 * @return 
	 */
	public static int getXHeight(Font font) {
		FontRenderContext frc = new FontRenderContext(null, true, true);
		
		GlyphVector gvx = font.createGlyphVector(frc, "x");
        Rectangle2D boundsX = gvx.getVisualBounds();
		
        double xHeight = boundsX.getHeight();
		
		return (int) Math.ceil(xHeight);
	}
	
	/**
	 * Cap height is the height of the usual Capital case letters.
	 * Here the height of the letter 'H'.
	 * Might be slower to execute. If performance is consideration, call once in initialization.
	 * @param font
	 * @return 
	 */
	public static int getCapHeight(Font font) {
		FontRenderContext frc = new FontRenderContext(null, true, true);
		
		GlyphVector gvH = font.createGlyphVector(frc, "H");
        Rectangle2D boundsH = gvH.getVisualBounds();
		
        double capHeight = boundsH.getHeight();
		
		return (int) Math.ceil(capHeight);
	}
	
	/**
	 * Gets the height above the baseline that the highest character in the String reaches.
	 * Accented letters, like Ä, Á, Â, Ö etc. reach above the CapHeight, but below the Ascent line.
	 * Might be slower to execute. If performance is consideration, call once in initialization.
	 * @param text
	 * @param font
	 * @return 
	 */
	public static int getMaxAscentOfSpecificString(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(null, true, true);
		
		GlyphVector gv = font.createGlyphVector(frc, text);
		Rectangle2D bounds = gv.getVisualBounds();
		
        double aboveBaseline = Math.max(0.0, -bounds.getY()); //Bounds start at the baseline, so y will be negative when above it. Height includes below the baseline too, so that can't be used.
		
		int addedPixel = font.isBold() ? 1 : 0;
		return (int) Math.ceil(aboveBaseline) + addedPixel;
	}
	
	public static int getMaxHeightOfFont(Font font, Graphics2D g) {
		int maxAscent = getMaxAscentOfSpecificString(allDefaultCharacters, font);
		
		return getLeading(getFontMetrics(font, g)) + maxAscent;
	}
	
	public static int getFakeMaxHeightOfFont(Font font) {
		int maxAscent = getMaxAscentOfSpecificString(allDefaultCharacters, font);
		
		return getLeading(getFakeFontMetrics(font)) + maxAscent;
	}
}
