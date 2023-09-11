package uilibrary.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import uilibrary.elements.Element;
import uilibrary.interfaces.Clickable;
import uilibrary.interfaces.Draggable;
import uilibrary.interfaces.Focusable;
import uilibrary.interfaces.HasSize;
import uilibrary.interfaces.Hoverable;
import uilibrary.interfaces.Interactable;
import uilibrary.interfaces.Typable;
import uilibrary.interfaces.Updateable;

public abstract class InteractableMenu extends Menu implements Interactable {
	protected List<Element> elements = new ArrayList<>();
	
	public InteractableMenu(Color backgroundColor, HasSize size) {
		super(backgroundColor, size);
	}
	
	public InteractableMenu(Color backgroundColor, int width, int height) {
		super(backgroundColor, width, height);
	}
	
	public InteractableMenu(Color backgroundColor, int x, int y, int width, int height) {
		super(backgroundColor, x, y, width, height);
	}
	
	@Override
	public boolean hover(int x, int y) {
		if (!active) {
			return false;
		}
		
		for (Element element : elements) {
			if (element instanceof Hoverable) {
				if (((Hoverable) element).hover(x, y)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean click(int x, int y) {
		if (!active) {
			return false;
		}
		
		resetFocuses(); //Every click resets focuses
		
		for (Element element : elements) {
			if (element instanceof Clickable) {
				if (((Clickable) element).click(x, y)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void update() {
		for (Element element : elements) {
			if (element instanceof Updateable) {
				((Updateable) element).update();
			}
		}
	}
	
	private void resetFocuses() {
		for (Element element : elements) {
			if (element instanceof Focusable) {
				((Focusable) element).unfocus();
			}
		}
	}
	
	protected void renderElements(Graphics2D g) {
		if (!active) {
			return;
		}
		
		for (Element element : elements) {
			element.render(g);
		}
	}
	
	//TODO: see if you need resetSelected() and toggleElementSelection for resetting selected tab or button etc.
	
	public void addElement(Element element) {
		elements.add(element);
	}
	
	public boolean keyTyped(KeyEvent e) {
		for (Element element : elements) {
			if (element instanceof Typable) {
				Typable typable = (Typable) element;
				if (typable.isFocused()) {
					if (typable.keyTyped(e)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public void mouseReleased() {
		for (Element element : elements) {
			if (element instanceof Draggable) {
				((Draggable) element).mouseReleased();
			}
		}
	}
	
	public boolean mouseDragged(int x, int y) {
		for (Element element : elements) {
			if (element instanceof Draggable) {
				Draggable draggable = (Draggable) element;
				if (draggable.isFocused() && draggable.mouseDragged(x, y)) {
					return true;
				}
			}
		}
		return false;
	}
}
