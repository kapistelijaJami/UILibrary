package uilibrary.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import uilibrary.elements.Element;
import uilibrary.elements.InteractableElement;
import uilibrary.interfaces.Clickable;
import uilibrary.interfaces.Hoverable;
import uilibrary.interfaces.Interactable;

public abstract class InteractableMenu extends Menu implements Interactable {
	protected List<InteractableElement> interactableElements = new ArrayList<>();
	
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
		
		for (Hoverable element : interactableElements) {
			if (element.hover(x, y)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean click(int x, int y) {
		if (!active) {
			return false;
		}
		
		for (Clickable element : interactableElements) {
			if (element.click(x, y)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected void renderElements(Graphics2D g) {
		if (!active) {
			return;
		}
		
		for (Element element : interactableElements) {
			element.render(g);
		}
	}
	
	//TODO: see if you need resetSelected() and toggleElementSelection for resetting selected tab or button etc.
	
	public void addElement(InteractableElement element) {
		interactableElements.add(element);
	}
}
