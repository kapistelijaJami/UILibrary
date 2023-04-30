package uilibrary.arrangement;

import java.awt.Rectangle;
import uilibrary.enums.Alignment;
import static uilibrary.enums.Alignment.*;
import uilibrary.enums.ReferenceType;
import uilibrary.interfaces.HasBounds;
import uilibrary.interfaces.HasLocation;
import uilibrary.interfaces.HasSize;
import uilibrary.menu.HelperFunctions;
import uilibrary.menu.Margin;

public class Arrangement implements HasLocation {
	private static long LATEST_LOCATION_UPDATE; //This is the timestamp of when the latest update happened that changed their location. Any Arrangement with smaller latestUpdate will get updated.
	
	private final Reference reference; //The reference we are positioning relative to
	
	private Margin margin;
	private final Alignments aligns;
	private final HasSize itself; //The object we are trying to position with this Arrangement
	
	private Location latestLocation; //The latest calculated location, so you don't have to calculate it every frame.
	private long latestUpdate; //If latestUpdate is less than LATEST_LOCATION_UPDATE, then the location will get updated.
	
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
		updateLocation(false);
		return this;
	}
	
	public Arrangement setReference(HasBounds horizontal, ReferenceType horType, HasBounds vertical, ReferenceType verType) {
		this.reference.setHorizontal(horizontal, horType);
		this.reference.setVertical(vertical, verType);
		updateLocation(false);
		return this;
	}
	
	public Arrangement setHorizontalReference(HasBounds horizontal, ReferenceType horType) {
		this.reference.setHorizontal(horizontal, horType);
		updateLocation(false);
		return this;
	}
	
	public Arrangement setVerticalReference(HasBounds vertical, ReferenceType verType) {
		this.reference.setVertical(vertical, verType);
		updateLocation(false);
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
		updateLocation(false);
		return this;
	}
	
	public Arrangement align(Alignment first, Alignment second) {
		aligns.setFirst(first);
		aligns.setSecond(second);
		updateLocation(false);
		return this;
	}
	
	public Arrangement setMargin(int x, int y) {
		margin.setX(x);
		margin.setY(y);
		updateLocation(false);
		return this;
	}
	
	public Arrangement setMargin(String x, int y) {
		margin.setX(x, itself, reference.getHorizontal());
		margin.setY(y);
		updateLocation(false);
		return this;
	}
	
	public Arrangement setMargin(int x, String y) {
		margin.setX(x);
		margin.setY(y, itself, reference.getVertical());
		updateLocation(false);
		return this;
	}
	
	public Arrangement setMargin(String x, String y) {
		margin.setX(x, itself, reference.getHorizontal());
		margin.setY(y, itself, reference.getVertical());
		updateLocation(false);
		return this;
	}
	
	public Arrangement setMargin(Margin margin) {
		this.margin = margin;
		updateLocation(false);
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
		if (latestLocation == null || latestUpdate < Arrangement.LATEST_LOCATION_UPDATE) {
			updateLocation(false);
		}
		
		return latestLocation;
	}
	
	/**
	 * Updates the location of this object.
	 * Will also set a timestamp if the location changed, so that other objects that were only
	 * updated before this timestamp will get updated as well, since they might reference this object.
	 * 
	 * If forceUpdateOthers is true, then others will be updated regardless. (This must be true when size changed,
	 * because that might not change the location of this object, but could affect other objects regardless.)
	 * @param forceUpdateOthers 
	 */
	public void updateLocation(boolean forceUpdateOthers) {
		Rectangle xBounds = reference.getHorizontal().getBounds();
		Rectangle yBounds = reference.getVertical().getBounds();
		
		Alignment horizontal = aligns.getHorizontal();
		int xOffset = HelperFunctions.getXOffsetFromAlignment(xBounds.getSize(), itself.getWidth(), margin, horizontal, reference.getTypeHorizontal(aligns.isFirst(horizontal)));
		
		Alignment vertical = aligns.getVertical();
		int yOffset = HelperFunctions.getYOffsetFromAlignment(yBounds.getSize(), itself.getHeight(), margin, vertical, reference.getTypeVertical(aligns.isFirst(vertical)));
		
		Location tempLoc = latestLocation;
		latestLocation = new Location(xBounds.x + xOffset, yBounds.y + yOffset);
		latestUpdate = System.nanoTime();
		
		if (tempLoc == null || forceUpdateOthers || !tempLoc.equals(latestLocation)) { //If the location changed, then we have to update other arrangements as well
			Arrangement.LATEST_LOCATION_UPDATE = latestUpdate;
		}
	}
	
	public Alignment[] getAligns() {
		return aligns.asArray();
	}
	
	public Margin getMargin() {
		return margin;
	}
}
