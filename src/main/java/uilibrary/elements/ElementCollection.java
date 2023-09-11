package uilibrary.elements;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * When you want to organize multiple elements inside one element.
 */
@Deprecated //TODO: remove if you can find a way to make it work
public class ElementCollection extends Element {
	private final List<Element> elements;

	public ElementCollection() {
		super(0, 0);
		elements = new ArrayList<>();
	}
	
	@Override
	public int getWidth() {
		if (elements == null || elements.isEmpty()) {
			return 0;
		} else if (elements.size() == 1) {
			return elements.get(0).getWidth();
		}
		
		return elements.get(0).getWidth();
		
		//TODO: You can't do this with multiple elements. Getting this X, and element's X requires already knowing the width, so this is infinite recursion.
		/*int x = getX();
		int reaches = x;
		for (Element element : elements) {
			int reach = element.getX() + element.getWidth();
			if (reach > reaches) {
				reaches = reach;
			}
		}
		
		return reaches - x;*/
	}
	
	@Override
	public int getHeight() {
		if (elements == null || elements.isEmpty()) {
			return 0;
		} else if (elements.size() == 1) {
			return elements.get(0).getHeight();
		}
		
		return elements.get(0).getHeight();
		
		//TODO: You can't do this with multiple elements. Getting this X, and element's X requires already knowing the width, so this is infinite recursion.
		/*int y = getY();
		int reaches = y;
		for (Element element : elements) {
			int reach = element.getY() + element.getHeight();
			if (reach > reaches) {
				reaches = reach;
			}
		}
		
		return reaches - y;*/
	}
	
	@Override
	public void render(Graphics2D g) {
		for (Element element : elements) {
			element.render(g);
		}
	}
	
	public void addElement(Element element) {
		this.elements.add(element);
	}
}
