package uilibrary.util;

/**
 * Counts up from start, when it reaches end, it is done.
 * Call advance to count up by one.
 */
public class Counter {
	private int counter;
	private int start;
	private int end;
	private boolean done = false;
	private boolean active = false;
	private boolean wasDoneWhenAsked = false; //true if isDone was called and it returned true, changes to true when isDone is called and the done result was true
	
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
		wasDoneWhenAsked = false;
	}
	
	public boolean isDone() {
		if (done) {
			wasDoneWhenAsked = true;
		}
		return done;
	}
	
	/**
	 * Counter keeps active if user hasn't called stop even if counter is done.
	 * @return 
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Counter doesn't stay running when counter is done, even if it's still active.
	 * @return 
	 */
	public boolean isRunning() {
		return !done && active;
	}

	public boolean wasDoneWhenAsked() {
		return wasDoneWhenAsked;
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
