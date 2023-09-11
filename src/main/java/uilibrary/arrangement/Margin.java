package uilibrary.arrangement;

import java.awt.Rectangle;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import uilibrary.interfaces.HasSize;

public class Margin {
	private int x, y;
	
	private HasSize itself; //The object this margin affects
	private HasSize reference; //The object we use as a reference
	
	private Expression expressionX, expressionY;
	
	private boolean useMarginForCenter = true; //If both margins were set with one value, you probably don't want to use margin for the direction you aligned CENTER
	
	public Margin() {
		this(0, 0);
	}
	
	public Margin(int both) {
		this(both, both);
		useMarginForCenter = false;
	}
	
	public Margin(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Margin(String x, String y, HasSize itself) { //If you want to use w2 and h2, provide a reference as well.
		this(x, y, itself, null);
	}
	
	/**
	 * Takes in mathematical expressions as a String.
	 * You can use 'w' for width, and 'h' for height in the expressions.
	 * You can also use 'w2' for reference width and 'h2' for reference height in the expressions.
	 * @param x
	 * @param y 
	 * @param itself 
	 * @param reference 
	 */
	public Margin(String x, String y, HasSize itself, HasSize reference) {
		this.itself = itself;
		this.reference = reference;
		
		expressionX = new ExpressionBuilder(x).variables("w", "h", "w2", "h2").build();
		expressionY = new ExpressionBuilder(y).variables("w", "h", "w2", "h2").build();
	}
	
	public Margin(String x, int y, HasSize itself) { //If you want to use w2 and h2, provide a reference as well.
		this(x, y, itself, null);
	}
	
	public Margin(String x, int y, HasSize itself, HasSize reference) {
		this.itself = itself;
		this.reference = reference;
		
		expressionX = new ExpressionBuilder(x).variables("w", "h", "w2", "h2").build();
		this.y = y;
	}
	
	public Margin(int x, String y, HasSize itself) { //If you want to use w2 and h2, provide a reference as well.
		this(x, y, itself, null);
	}
	
	public Margin(int x, String y, HasSize itself, HasSize reference) {
		this.itself = itself;
		this.reference = reference;
		
		expressionY = new ExpressionBuilder(y).variables("w", "h", "w2", "h2").build();
		this.x = x;
	}
	
	public int getX() {
		if (expressionX == null) {
			return this.x;
		}
		
		expressionX.setVariable("w", itself.getWidth()).setVariable("h", itself.getHeight());
		
		if (reference == null) {
			expressionX.setVariable("w2", 0).setVariable("h2", 0);
		} else {
			expressionX.setVariable("w2", reference.getWidth()).setVariable("h2", reference.getHeight());
		}
		return (int) expressionX.evaluate();
	}
	
	public int getY() {
		if (expressionY == null) {
			return this.y;
		}
		
		expressionY.setVariable("w", itself.getWidth()).setVariable("h", itself.getHeight());
		
		if (reference == null) {
			expressionY.setVariable("w2", 0).setVariable("h2", 0);
		} else {
			expressionY.setVariable("w2", reference.getWidth()).setVariable("h2", reference.getHeight());
		}
		return (int) expressionY.evaluate();
	}
	
	public void setBoth(int both) {
		this.x = both;
		this.y = both;
		expressionX = null;
		expressionY = null;
		
		useMarginForCenter = false;
	}
	
	public void setX(int x) {
		this.x = x;
		expressionX = null;
		
		useMarginForCenter = true;
	}
	
	public void setX(String x, HasSize itself) {
		setX(x, itself, null);
	}
	
	public void setX(String x, HasSize itself, HasSize reference) {
		this.itself = itself;
		this.reference = reference;
		
		expressionX = new ExpressionBuilder(x).variables("w", "h", "w2", "h2").build();
	}
	
	public void setY(int y) {
		this.y = y;
		expressionY = null;
		useMarginForCenter = true;
	}
	
	public void setY(String y, HasSize itself) {
		setY(y, itself, null);
	}
	
	public void setY(String y, HasSize itself, HasSize reference) {
		this.itself = itself;
		this.reference = reference;
		
		expressionY = new ExpressionBuilder(y).variables("w", "h", "w2", "h2").build();
	}
	
	/**
	 * If both margins were set with one value, you probably
	 * don't want to use margin for the direction you aligned CENTER.
	 * @return 
	 */
	public boolean useMarginForCenter() {
		if (!useMarginForCenter) { //both were set using one value
			if (expressionX != null && expressionY != null) { //both expressions are in use
				return true;
			}
		}
		return useMarginForCenter;
	}
	
	public void shrinkRectWithMargin(Rectangle bounds) {
		bounds.x += getX();
		bounds.y += getY();
		bounds.width -= getX() * 2;
		bounds.height -= getY() * 2;
	}
	
	public void widenRectWithMargin(Rectangle bounds) {
		bounds.x -= getX();
		bounds.y -= getY();
		bounds.width += getX() * 2;
		bounds.height += getY() * 2;
	}
}
