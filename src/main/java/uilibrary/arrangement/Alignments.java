package uilibrary.arrangement;

import uilibrary.enums.Alignment;
import static uilibrary.enums.Alignment.*;

public class Alignments {
	private Alignment first;
	private Alignment second;
	
	public Alignments() {
		this(CENTER, CENTER);
	}
	
	public Alignments(Alignment first) {
		this(first, CENTER);
	}
	
	public Alignments(Alignment first, Alignment second) {
		this.first = first;
		this.second = second;
	}

	public void setFirst(Alignment first) {
		this.first = first;
	}

	public void setSecond(Alignment second) {
		this.second = second;
	}
	
	public Alignment getHorizontal() {
		if (first.isHorizontal()) return first;
		if (second.isHorizontal()) return second;
		
		return CENTER;
	}
	
	public Alignment getVertical() {
		if (first.isVertical()) return first;
		if (second.isVertical()) return second;
		
		return CENTER;
	}
	
	public boolean isFirst(Alignment align) {
		return first == align; //Can give a wrong answer when both are CENTER, but then the order doesn't matter anyway, both types are then INSIDE.
	}
	
	public boolean firstIsHorizontal() {
		return first == getHorizontal(); //Can give a wrong answer when both are CENTER, but then the order doesn't matter anyway, both types are then INSIDE.
	}
	
	public boolean firstIsVertical() {
		return first == getVertical(); //Can give a wrong answer when both are CENTER, but then the order doesn't matter anyway, both types are then INSIDE.
	}
	
	public boolean secondIsHorizontal() {
		return second == getHorizontal(); //Can give a wrong answer when both are CENTER, but then the order doesn't matter anyway, both types are then INSIDE.
	}
	
	public boolean secondIsVertical() {
		return second == getVertical(); //Can give a wrong answer when both are CENTER, but then the order doesn't matter anyway, both types are then INSIDE.
	}
	
	public void replaceHorizontalAlign(Alignment align) {
		if (first.isHorizontal()) {
			first = align;
		} else if (second.isHorizontal()) {
			second = align;
		} else {
			first = align; //Will replace first if both are CENTER
		}
	}
	
	public void replaceVerticalAlign(Alignment align) {
		if (first.isVertical()) {
			first = align;
		} else if (second.isVertical()) {
			second = align;
		} else {
			first = align; //Will replace first if both are CENTER
		}
	}

	public Alignment[] asArray() {
		return new Alignment[] {first, second};
	}
}
