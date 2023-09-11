package uilibrary.elements;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import uilibrary.enums.Alignment;
import uilibrary.enums.Orientation;
import uilibrary.enums.ReferenceType;
import uilibrary.interfaces.Clickable;
import uilibrary.interfaces.Focusable;
import uilibrary.interfaces.Hoverable;

public class ElementList<T extends Element> extends InteractableElement { //TODO: should this extend InteractableMenu instead? Lot of similiar code.
	private List<T> elements;
	private int spaceBetween;
	private Orientation orientation;
	private Alignment elementAlignment; //Where to align elements relative to each other
	
	public ElementList(int spaceBetween) {
		this(Orientation.VERTICAL, spaceBetween);
	}
	
	public ElementList(Orientation orientation, int spaceBetween) {
		this(orientation, spaceBetween, Alignment.CENTER);
	}
	
	/**
	 * Creates an element list.
	 * 
	 * ElementAlignment has to be in the opposite axis of the orientation.
	 *	- If orientation is VERTICAL, elementAlignment has to be either CENTER, LEFT or RIGHT.
	 *	- If orientation is HORIZONTAL, elementAlignment has to be either CENTER, TOP or BOTTOM.
	 * 
	 * @param orientation Either VERTICAL or HORIZONTAL.
	 * @param spaceBetween How much space between elements in pixels.
	 * @param elementAlignment Where to align elements relative to each other. If LEFT, all elements will have their left side aligned.
	 */
	public ElementList(Orientation orientation, int spaceBetween, Alignment elementAlignment) {
		super(0, 0);
		elements = new ArrayList<>();
		this.orientation = orientation;
		this.spaceBetween = spaceBetween;
		this.elementAlignment = elementAlignment;
	}
	
	@Override
	public int getWidth() {
		if (elements == null || elements.isEmpty()) {
			return 0;
		}
		if (orientation == Orientation.VERTICAL) {
			return getWidestElementWidth(); //return width of widest element
		} else if (orientation == Orientation.HORIZONTAL) {
			//calculate list width
			int width = (elements.size() - 1 * spaceBetween);
			for (int i = 0; i < elements.size(); i++) {
				width += elements.get(i).getWidth();
			}
			return width;
		}
		
		return 0;
	}
	
	@Override
	public int getHeight() {
		if (elements == null || elements.isEmpty()) {
			return 0;
		}
		if (orientation == Orientation.VERTICAL) {
			//calculate list height //TODO: calculating every frame might not be the best idea.
			int height = (elements.size() - 1 * spaceBetween);
			for (int i = 0; i < elements.size(); i++) {
				height += elements.get(i).getHeight();
			}
			return height;
		} else if (orientation == Orientation.HORIZONTAL) {
			return getTallestElementHeight(); //return height of tallest element
		}
		
		return 0;
	}
	
	public void addElement(T element) {
		if (elements.isEmpty()) {
			if (orientation == Orientation.VERTICAL) {
				element.arrange(this, ReferenceType.INSIDE).align(Alignment.TOP, elementAlignment);
			} else if (orientation == Orientation.HORIZONTAL) {
				element.arrange(this, ReferenceType.INSIDE).align(elementAlignment, Alignment.LEFT);
			}
		} else {
			Element lastElement = elements.get(elements.size() - 1);
			element.arrange(lastElement, ReferenceType.OUTSIDE);
			
			if (orientation == Orientation.VERTICAL) {
				element.arrange().align(Alignment.BOTTOM, elementAlignment).setMargin(0, spaceBetween);
			} else if (orientation == Orientation.HORIZONTAL) {
				element.arrange().align(Alignment.RIGHT, elementAlignment).setMargin(spaceBetween, 0);
			}
		}
		
		elements.add(element);
	}
	
	public void remove(int index) {
		if (index == 0) { //remove first
			elements.remove(index);
			if (!elements.isEmpty()) {
				Alignment align = orientation == Orientation.VERTICAL ? Alignment.TOP : Alignment.LEFT;
				elements.get(0).arrange(this, ReferenceType.INSIDE).align(align, elementAlignment).setMargin(0);
			}
		} else if (index == elements.size() - 1) { //remove last
			elements.remove(index);
		} else { //remove from middle
			elements.remove(index);
			Alignment align = orientation == Orientation.VERTICAL ? Alignment.BOTTOM : Alignment.RIGHT;
			elements.get(index).arrange(elements.get(index - 1), ReferenceType.OUTSIDE).align(align, elementAlignment);
		}
		
	}
	
	public void remove(Element element) {
		int i = elements.indexOf(element);
		if (i != -1) {
			remove(i);
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			element.render(g);
		}
	}
	
	private int getWidestElementWidth() {
		int widest = 0;
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			if (element.getWidth() > widest) {
				widest = element.getWidth();
			}
		}
		
		return widest;
	}
	
	private int getTallestElementHeight() {
		int tallest = 0;
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			if (element.getHeight() > tallest) {
				tallest = element.getHeight();
			}
		}
		
		return tallest;
	}

	public List<T> getElements() {
		return elements;
	}

	@Override
	public boolean hover(int x, int y) {
		for (T element : elements) {
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
		resetFocuses(); //Every click resets focuses
		
		for (T element : elements) {
			if (element instanceof Clickable) {
				if (((Clickable) element).click(x, y)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void resetFocuses() {
		for (Element element : elements) {
			if (element instanceof Focusable) {
				((Focusable) element).unfocus();
			}
		}
	}
}
