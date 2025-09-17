package uilibrary.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import uilibrary.arrangement.Margin;
import uilibrary.enums.Alignment;
import uilibrary.interfaces.Draggable;
import uilibrary.interfaces.Focusable;
import uilibrary.interfaces.Typable;
import uilibrary.interfaces.Updateable;
import uilibrary.util.HelperFunctions;
import uilibrary.util.LooperAction;
import uilibrary.util.RenderMultilineText;
import uilibrary.util.RenderText;

public class TextField extends InteractableElement implements Focusable, Updateable, Typable, Draggable { //TODO: doesn't wrap exactly correctly, a character can go over the bounds, especially over padding.
	private String text = "";
	
	private boolean focused = false;
	private LooperAction cursorBlinking;
	private boolean cursorVisible = true;
	private int cursorIndex; //Where cursor is located in text. If 0, it's before first character. It's equal to text length when after last character.
	private int highlightCursorOther;
	private boolean highlighting = false; //TODO: select words by double clicking
	private boolean multiLine; //TODO: use this variable when it's false the line doesn't automatically go to next line.
	
	private int textPadding = 10;
	private static final Font defaultFont = new Font("Serif", Font.BOLD, 30);
	
	public TextField(int width, int fps) { //No height, so no multiline
		this(width, RenderText.getFontLineInterval(defaultFont) + 20, fps, false);
	}
	
	public TextField(int width, int height, int fps, boolean multiLine) {
		super(width, height);
		
		cursorBlinking = new LooperAction(o -> toggleBlink(), fps, 3.0);
		this.multiLine = multiLine;
	}
	
	@Override
	public void update() {
		cursorBlinking.update();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		
		if (highlighting && focused) {
			highlightBetween(g, Math.min(cursorIndex, highlightCursorOther), Math.max(cursorIndex, highlightCursorOther));
		}
		
		renderText(g);
		
		renderCursor(g);
	}
	
	private Rectangle renderText(Graphics2D g) {
		g.setColor(HelperFunctions.getDarkerColor(Color.decode("#747474"), 70));
		
		Rectangle bounds = getTextBounds();
		
		return RenderMultilineText.drawMultilineText(g, text, bounds, defaultFont, true, Alignment.TOP, Alignment.LEFT);
	}
	
	private void renderCursor(Graphics2D g) {
		if (!cursorVisible || !focused) {
			return;
		}
		
		g.setColor(Color.BLACK);
		
		Point p = getCursorLocation(g, cursorIndex);
		int h = getCursorHeight(g);
		g.fillRect(p.x, p.y, 2, h);
	}
	
	@Override
	public boolean isFocused() {
		return focused;
	}
	
	@Override
	public void focus() {
		if (!focused) {
			resetBlinking();
		}
		
		focused = true;
	}
	
	@Override
	public void unfocus() {
		focused = false;
	}
	
	@Override
	public boolean hover(int x, int y) {
		if (isInside(x, y)) {
			//cursor.type = Cursor.TEXT_CURSOR; //TODO: handle cursor stuff
			return true;
		}
		return false;
	}
	
	@Override
	public boolean click(int x, int y) {
		highlighting = false;
		if (isInside(x, y)) {
			focus();
			setCursorIndex(getClosestCursorIndex(x, y));
			return true;
		}
		
		unfocus();
		return false;
	}
	
	@Override
	public boolean mouseDragged(int x, int y) {
		if (isInside(x, y)) {
			highlightCursorOther = getClosestCursorIndex(x, y);
			highlighting = (cursorIndex != highlightCursorOther);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void mouseReleased() {
		if (highlighting) {
			int temp = cursorIndex;
			cursorIndex = highlightCursorOther;
			highlightCursorOther = temp;
		}
		resetBlinking();
	}
	
	private void toggleBlink() {
		cursorVisible = !cursorVisible;
	}
	
	@Override
	public boolean keyTyped(KeyEvent e) {
		if (!focused) {
			return false;
		}
		
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_UNDEFINED) {
			key = (int) e.getKeyChar(); //keyTyped event doesn't have keyCode. So if it was undefined, use keyChar.
			if (key == KeyEvent.CHAR_UNDEFINED || key == KeyEvent.VK_UNDEFINED) {
				return false;
			}
		}
		
		int length = text.length();
		
		switch (key) {
			case KeyEvent.VK_LEFT:
				if (highlighting) {
					if (!e.isShiftDown()) {
						setCursorIndex(Math.max(0, cursorIndex - 1));
					} else {
						highlightCursorOther = Math.max(0, highlightCursorOther - 1);
					}
				} else {
					if (e.isShiftDown()) {
						highlightCursorOther = cursorIndex;
						highlighting = true;
					}
					setCursorIndex(Math.max(0, cursorIndex - 1));
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (highlighting) {
					if (!e.isShiftDown()) {
						setCursorIndex(Math.min(length, cursorIndex + 1));
					} else {
						highlightCursorOther = Math.min(length, highlightCursorOther + 1);
					}
				} else {
					if (e.isShiftDown()) {
						highlightCursorOther = cursorIndex;
						highlighting = true;
					}
					setCursorIndex(Math.min(length, cursorIndex + 1));
				}
				break;
			case KeyEvent.VK_UP:
				if (e.isShiftDown() && !highlighting) {
					highlightCursorOther = cursorIndex;
					highlighting = true;
				}
				
				moveCursorUp(e.isShiftDown());
				break;
			case KeyEvent.VK_DOWN:
				if (e.isShiftDown() && !highlighting) {
					highlightCursorOther = cursorIndex;
					highlighting = true;
				}
				
				moveCursorDown(e.isShiftDown());
				break;
			case KeyEvent.VK_BACK_SPACE:
				removeCharacter();
				break;
			case KeyEvent.VK_DELETE:
				deleteCharacter();
				break;
			case KeyEvent.VK_END:
				setCursorIndex(length);
				break;
			case KeyEvent.VK_HOME:
				setCursorIndex(0);
				break;
			case KeyEvent.VK_TAB:
				addCharacter('\t');
				break;
			case KeyEvent.VK_ENTER:
				addCharacter('\n');
				break;
			default:
				if (key >= KeyEvent.VK_SPACE) {
					addCharacter((char) key);
				}
				break;
		}
		
		return length != text.length();
	}
	
	private void addCharacter(char c) {
		if (highlighting) {
			deleteHighlightedPart();
		}
		
		String newText = text.substring(0, cursorIndex) + c + text.substring(cursorIndex);
		setCursorIndex(cursorIndex + 1);
		text = newText;
	}
	
	private void removeCharacter() {
		if (text.isEmpty()) {
			return;
		}
		
		if (highlighting) {
			deleteHighlightedPart();
			return;
		}
		
		String newText = text.substring(0, cursorIndex - 1) + text.substring(cursorIndex);
		setCursorIndex(cursorIndex - 1);
		text = newText;
	}
	
	private void deleteCharacter() {
		if (text.isEmpty()) {
			return;
		}
		
		if (highlighting) {
			deleteHighlightedPart();
			return;
		}
		
		if (cursorIndex != text.length()) {
			cursorIndex++;
			removeCharacter();
		}
	}
	
	private void deleteHighlightedPart() {
		if (!highlighting) return;
		
		int start = Math.min(cursorIndex, highlightCursorOther);
		int end = Math.max(cursorIndex, highlightCursorOther);
		
		String newText = text.substring(0, start) + text.substring(end);
		setCursorIndex(start);
		text = newText;
		
		highlighting = false;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		setCursorIndex(text.length());
	}
	
	private void setCursorIndex(int i) {
		cursorIndex = i;
		resetBlinking();
	}
	
	private int getClosestCursorIndex(int x, int y) {
		Graphics2D g = RenderText.getFakeGraphics();
		int closestI = -1;
		double minD = Double.MAX_VALUE;
		
		int lineHeight = RenderText.getFontLineInterval(g, defaultFont);
		for (int i = 0; i <= text.length(); i++) {
			Point p = getCursorLocation(g, i);
			if (y >= p.y && y < p.y + lineHeight) {
				p.y += lineHeight / 2;
				double d = HelperFunctions.distance(p.x, p.y, x, y);
				if (d < minD) {
					minD = d;
					closestI = i;
				}
			}
		}
		
		if (closestI == -1) {
			if (y <= getCursorLocation(g, 0).y) {
				closestI = 0;
			} else {
				closestI = text.length();
			}
		}
		return closestI;
	}
	
	private void resetBlinking() {
		cursorBlinking.reset();
		cursorVisible = true;
	}
	
	private Point getCursorLocation(int cursorIndex) {
		return getCursorLocation(RenderText.getFakeGraphics(), cursorIndex);
	}
	
	private Point getCursorLocation(Graphics2D g, int cursorIndex) {
		String textBeforeCursor = getText().substring(0, cursorIndex);
		
		int lineHeight = RenderMultilineText.getFontLineInterval(g, defaultFont);
		
		Rectangle renderedBounds = RenderMultilineText.getRenderedBounds(g, textBeforeCursor, defaultFont, getTextBounds(), Alignment.TOP, Alignment.LEFT);
		
		ArrayList<String> lines = RenderMultilineText.getAllLines(g, textBeforeCursor, defaultFont, getTextBounds());
		int w = RenderText.getStringWidth(g, defaultFont, lines.get(lines.size() - 1));
		int h = getCursorHeight(g);
		
		return new Point(renderedBounds.x + w, renderedBounds.y + lineHeight * lines.size() - h);
	}
	
	private int getCursorLineNumber(Graphics2D g, int cursorIndex) {
		ArrayList<String> lines = RenderMultilineText.getAllLines(g, getText(), defaultFont, getTextBounds());
		
		int count = 0;
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (cursorIndex >= count && cursorIndex <= count + line.length()) {
				return i;
			}
			count += line.length() + 1; //+1 because lines doesn't contain \n, not sure if it's supposed to go to the if as well
		}
		return 0;
	}
	
	private int getCursorHeight() {
		return RenderText.getFontLineInterval(defaultFont);
	}
	
	private int getCursorHeight(Graphics2D g) {
		return RenderText.getFontLineInterval(g, defaultFont) - 2;
	}
	
	public Rectangle getTextBounds() {
		Rectangle bounds = getBounds();
		new Margin(textPadding).shrinkRectWithMargin(bounds);
		bounds.y -= textPadding;
		return bounds;
	}
	
	public void clear() {
		setText("");
	}
	
	private void highlightBetween(Graphics2D g, int minIndex, int maxIndex) {
		g.setColor(new Color(51, 144, 255));
		
		Point first = getCursorLocation(g, 0);
		Point p = getCursorLocation(g, minIndex);
		Point p2 = getCursorLocation(g, maxIndex);
		int h = getCursorHeight(g);
		if (p.y == p2.y) {
			g.fillRect(p.x, p.y, p2.x - p.x, h);
		} else {
			ArrayList<String> lines = RenderMultilineText.getAllLines(g, getText(), defaultFont, getTextBounds());
			int s = getCursorLineNumber(g, minIndex);
			int e = getCursorLineNumber(g, maxIndex);
			for (int i = s; i <= e; i++) {
				if (i != s && i != e) {
					highlightLine(g, i, lines);
				} else if (i == s) {
					String line = lines.get(i);
					g.fillRect(p.x, p.y, RenderText.getStringWidth(g, defaultFont, line) - (p.x - first.x), h);
				} else if (i == e) {
					g.fillRect(first.x, p2.y, p2.x - first.x, h);
				}
			}
		}
	}
	
	private void highlightLine(Graphics2D g, int line, ArrayList<String> lines) {
		int h = getCursorHeight(g);
		Point p = getCursorLocation(g, 0);
		g.fillRect(p.x, p.y + line * h, RenderText.getStringWidth(g, defaultFont, lines.get(line)), h);
	}
	
	private void moveCursorUp(boolean isShiftDown) {
		Point p = getCursorLocation(isShiftDown ? highlightCursorOther : cursorIndex);
		int h = getCursorHeight();
		int i = getClosestCursorIndex(p.x, p.y - h);
		
		if (isShiftDown) {
			highlightCursorOther = i;
		} else {
			setCursorIndex(i);
		}
	}
	
	private void moveCursorDown(boolean isShiftDown) {
		Point p = getCursorLocation(isShiftDown ? highlightCursorOther : cursorIndex);
		int h = getCursorHeight();
		int i = getClosestCursorIndex(p.x, p.y + h);
		
		if (isShiftDown) {
			highlightCursorOther = i;
		} else {
			setCursorIndex(i);
		}
	}
}
