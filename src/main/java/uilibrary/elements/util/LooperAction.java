package uilibrary.elements.util;

import java.util.function.Consumer;

/**
 * Constantly calls an assigned function when count is done.
 * Uses a Counter object.
 */
public class LooperAction {
	private Counter counter;
	private Consumer<Object> action;
	private Object input;
	
	
	/**
	 * Creates a looper that runs the action <code>timesASecond</code> times per second if update is called every frame.
	 * Depends on <code>gameFPS</code>, which is the fps of the game.
	 * @param action 
	 * @param gameFPS 
	 * @param timesASecond 
	 */
	public LooperAction(Consumer<Object> action, double gameFPS, double timesASecond) {
		this(action, 0, Math.max(1, (int) (gameFPS / timesASecond)));
	}
	
	/**
	 * Creates a looper that counts up from start to end in each update, and then runs the action.
	 * Both start and end is inclusive.
	 * Create consumer with Lambda Expression: (o -> functionToCall())
	 * or with method reference this::functionToCall (for non-static, also for specific object works) OR MyClass::functionToCall (for static)
	 * @param action
	 * @param start
	 * @param end 
	 */
	public LooperAction(Consumer<Object> action, int start, int end) {
		this.action = action;
		boolean isActive = true;
		this.counter = new Counter(start, end, isActive);
	}
	
	public void setInput(Object input) {
		this.input = input;
	}
	
	public void update() {
		counter.advance();
		if (counter.isDone()) {
			counter.reset();
			action.accept(input);
		}
	}
	
	public void reset() {
		counter.reset();
	}
}
