package uilibrary.arrangement;

import java.awt.Rectangle;
import uilibrary.enums.Alignment;
import static uilibrary.enums.Alignment.*;
import uilibrary.enums.Orientation;
import uilibrary.enums.ReferenceType;
import uilibrary.interfaces.HasBounds;
import uilibrary.interfaces.HasLocation;
import uilibrary.interfaces.HasSize;
import uilibrary.menu.HelperFunctions;
import uilibrary.menu.Margin;

public class Arrangement implements HasLocation {
	private static boolean LOCATIONS_HAVE_CHANGED = false; //TODO: see when you need to use this. If you move some reference, then they won't update on their own.
	
	private Reference reference; //The reference we are positioning relative to
	
	private Margin margin;
	private Alignments aligns;
	private HasSize itself; //The object we are trying to position with this Arrangement
	
	private Location latestLocation;
	private long latestUpdate;
	private final int minUpdateTime = 1000; //minimum update time in case game didnt update instantly. So it updates the bounds every second no matter what.
	
	public Arrangement(HasSize itself) {
		this.itself = itself;
		
		reference = new Reference(new AbsoluteReference(), ReferenceType.INSIDE);
		margin = new Margin();
		aligns = new Alignments();
	}
	
	public Arrangement setReference(int x, int y) {
		return setReference(new AbsoluteReference(x, y));
	}
	
	public Arrangement setReference(HasBounds reference) {
		return setReference(reference, ReferenceType.INSIDE);
	}
	
	public Arrangement setReference(HasBounds reference, ReferenceType type) {
		this.reference.setReference(reference, type);
		updateLocation();
		return this;
	}
	
	public Arrangement setReference(HasBounds horizontal, ReferenceType horType, HasBounds vertical, ReferenceType verType) {
		this.reference.setHorizontal(horizontal, horType);
		this.reference.setVertical(vertical, verType);
		updateLocation();
		return this;
	}
	
	public Arrangement setHorizontalReference(HasBounds horizontal, ReferenceType horType) {
		this.reference.setHorizontal(horizontal, horType);
		updateLocation();
		return this;
	}
	
	public Arrangement setVerticalReference(HasBounds vertical, ReferenceType verType) {
		this.reference.setVertical(vertical, verType);
		updateLocation();
		return this;
	}
	
	public Arrangement align(Alignment align) {
		if (align == CENTER) {
			return align(CENTER, CENTER);
		}
		
		if (align.isHorizontal()) {
			aligns.replaceHorizontalAlign(align);
		} else {
			aligns.replaceVerticalAlign(align);
		}
		updateLocation();
		return this;
	}
	
	public Arrangement align(Alignment first, Alignment second) {
		aligns.setFirst(first);
		aligns.setSecond(second);
		updateLocation();
		return this;
	}
	
	public Arrangement setMargin(int x, int y) {
		margin.setX(x);
		margin.setY(y);
		updateLocation();
		return this;
	}
	
	public Arrangement setMargin(String x, int y) {
		margin.setX(x, itself, reference.getHorizontal());
		margin.setY(y);
		updateLocation();
		return this;
	}
	
	public Arrangement setMargin(int x, String y) {
		margin.setX(x);
		margin.setY(y, itself, reference.getVertical());
		updateLocation();
		return this;
	}
	
	public Arrangement setMargin(String x, String y) {
		margin.setX(x, itself, reference.getHorizontal());
		margin.setY(y, itself, reference.getVertical());
		updateLocation();
		return this;
	}
	
	public Arrangement setMargin(Margin margin) {
		this.margin = margin;
		updateLocation();
		return this;
	}
	
	@Override
	public int getX() {
		return getLocation().x;
	}
	
	@Override
	public int getY() {
		return getLocation().y;
	}
	
	@Override
	public Location getLocation() {
		if (latestLocation == null || Arrangement.LOCATIONS_HAVE_CHANGED) { //TODO: see if you need timer based update as well
			updateLocation();
		}
		
		if (System.currentTimeMillis() - latestUpdate > 300) { //if 300ms has passed we can stop updating
			Arrangement.LOCATIONS_HAVE_CHANGED = false;
		}
		
		return latestLocation;
	}
	
	private void updateLocation() {
		Rectangle xBounds = reference.getHorizontal().getBounds();
		Rectangle yBounds = reference.getVertical().getBounds();
		
		Alignment horizontal = aligns.getHorizontal();
		int xOffset = HelperFunctions.getXOffsetFromAlignment(xBounds.getSize(), itself.getWidth(), margin, horizontal, reference.getTypeHorizontal(aligns.isFirst(horizontal)));
		
		Alignment vertical = aligns.getVertical();
		int yOffset = HelperFunctions.getYOffsetFromAlignment(yBounds.getSize(), itself.getHeight(), margin, vertical, reference.getTypeVertical(aligns.isFirst(vertical)));
		
		latestLocation = new Location(xBounds.x + xOffset, yBounds.y + yOffset);
		
		if (!Arrangement.LOCATIONS_HAVE_CHANGED) {
			latestUpdate = System.currentTimeMillis();
		}
		Arrangement.LOCATIONS_HAVE_CHANGED = true;
	}

	public Alignment[] getAligns() {
		return aligns.asArray();
	}
}
