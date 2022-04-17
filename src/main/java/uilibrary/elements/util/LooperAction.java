package uilibrary.elements.util;

import java.util.function.Consumer;

/**
 * Constantly calls an assigned function using a counter.
 */
public class LooperAction {
	private Counter counter;
	private Consumer<Object> action;
	private Object input;
	
	/**
	 * Creates a looper that counts up from start to end in each update, and then runs the action.
	 * Both start and end is inclusive.
	 * @param action
	 * @param start
	 * @param end 
	 */
	public LooperAction(Consumer<Object> action, int start, int end) {
		this.action = action;
		this.counter = new Counter(start, end, true);
	}
	
	/**
	 * Creates a looper that runs the action fps times per second if update is called every frame.
	 * Depends on gameFPS, which is the fps of the game.
	 * @param action 
	 * @param gameFPS 
	 * @param fps 
	 */
	public LooperAction(Consumer<Object> action, double gameFPS, double fps) {
		this.action = action;
		this.counter = new Counter(0, Math.max(1, (int) (gameFPS / fps)), true);
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
