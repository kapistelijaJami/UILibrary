package uilibrary.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import uilibrary.enums.Alignment;
import uilibrary.enums.ReferenceType;

public class HelperFunctions {
	public static Color getDarkerColor(Color c, int amount) {
		int r = Math.max(40, c.getRed() - amount);
		int g = Math.max(40, c.getGreen() - amount);
		int b = Math.max(40, c.getBlue() - amount);
		return new Color(r, g, b);
	}
	
	public static String preZeros(int i, int numberOfDigits) {
		return String.format("%0" + numberOfDigits + "d", i);
	}
	
	public static int howManyDigits(int i) {
		return (int) Math.log10(i) + 1;
	}
	
	public static double dist(double a, double b) {
		return Math.abs(a - b);
	}
	
	public static double clamp(double val, double min, double max) {
		return Math.min(max, Math.max(min, val));
	}
	
	public static int clamp(int val, int min, int max) {
		return Math.min(max, Math.max(min, val));
	}
	
	public static Point xAndYOffsetInsideBoundsFromAlignment(Rectangle bounds, Dimension size, Alignment... aligns) {
		return xAndYOffsetInsideBoundsFromAlignment(bounds, size, new Margin(), aligns);
	}
	
	public static Point xAndYOffsetInsideBoundsFromAlignment(Rectangle bounds, Dimension size, Margin margin, Alignment... aligns) {
		return new Point(getXOffsetFromAlignment(bounds, size, margin, Alignment.getHorizontal(aligns), ReferenceType.INSIDE), getYOffsetFromAlignment(bounds, size, margin, Alignment.getVertical(aligns), ReferenceType.INSIDE));
	}
	
	public static int getXOffsetFromAlignment(Rectangle reference, Dimension size, Margin margin, Alignment align, ReferenceType type) {
		if (type == ReferenceType.INSIDE) {
			switch (align) {
				case LEFT:
					return margin.x;
				case RIGHT:
					return reference.width - size.width - margin.x;
			}
		} else {
			switch (align) {
				case LEFT:
					return reference.width + margin.x;
				case RIGHT:
					return 0 - size.width - margin.x;
			}
		}
		return reference.width / 2 - size.width / 2 + margin.x;
	}
	
	public static int getYOffsetFromAlignment(Rectangle reference, Dimension size, Margin margin, Alignment align, ReferenceType type) {
		if (type == ReferenceType.INSIDE) {
			switch (align) {
				case TOP:
					return margin.y;
				case BOTTOM:
					return reference.height - size.height - margin.y;
			}
		} else {
			switch (align) {
				case TOP:
					return reference.height + margin.y;
				case BOTTOM:
					return 0 - size.height - margin.y;
			}
		}
		return reference.height / 2 - size.height / 2 + margin.y;
	}
}
