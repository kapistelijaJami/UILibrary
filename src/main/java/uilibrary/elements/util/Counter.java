package uilibrary.elements.util;

public class Counter {
	private int counter;
	private int start;
	private int end;
	private boolean done = false;
	private boolean active = false;
	private boolean doneAskedWhenTrue = false; //changes to true when isDone is called and the done result was true
	
	public Counter(int start, int end) { //end is inclusive, when counter reaches end exactly it stops.
		this(start, end, false);
	}
	
	public Counter(int start, int end, boolean isActive) { //end is inclusive, when counter reaches end exactly it stops.
		this.start = start;
		this.counter = start;
		this.end = end;
		this.active = isActive;
	}
	
	public void advance() {
		if (!active || done) {
			return;
		}
		counter++;
		
		if (counter >= end) {
			done = true;
		}
	}
	
	public void reset() {
		counter = start;
		done = false;
		doneAskedWhenTrue = false;
	}
	
	public boolean isDone() {
		if (done) {
			doneAskedWhenTrue = true;
		}
		return done;
	}

	public boolean isActive() {
		return active;
	}
	
	public boolean isRunning() {
		return !done && active;
	}

	public boolean hasDoneAskedWhenTrue() {
		return doneAskedWhenTrue;
	}
	
	public void start() {
		active = true;
	}
	
	public void stop() {
		active = false;
	}

	public int getValue() {
		return counter;
	}

	public void setValue(int counter) {
		this.counter = counter;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return "[counter: " + counter + ", start: " + start + ", end: " + end + ", active: " + active + ", done: " + done + "]";
	}
}
