package uilibrary;

import java.awt.Graphics2D;
import uilibrary.interfaces.HasWindow;

/**
 * Game loop library for running update and render methods in a separate thread with specific fps.
 * <br>
 * <br>
 * USAGE:
 * Create a subclass that extends GameLoop. If you want to render to a Window, also implement HasWindow interface.
 * This will let the GameLoop call render(Graphics2D g) method with the Graphics2D object and dispose and display automatically.
 * Implement required methods. Update and render will be called back to back fps times a second.
 * Call start() for this object.
 * Override shutdown() to decide what to do when stopping the loop.
 * You can also override lazyUpdate(), which is called every 250ms, to maybe display fps
 * in title etc, or do something else which doesn't happen every update.
 */
public abstract class GameLoop implements Runnable {
	protected final int FPS;
	protected boolean running = true;
	private boolean infiniteFPS = false;
	
	public GameLoop(int fps) {
		this.FPS = fps;
	}
	
	protected abstract void init();
	protected abstract void update();
	
	protected void render() {} //Not forcing the implementation, but it can be overridden and automatically called even if subclass doesn't implement HasWindow.
	
	/**
	 * Called every 250ms.
	 * @param fps What is the fps right now.
	 */
	protected void lazyUpdate(int fps) {}
	
	/**
	 * Calls System.exit().
	 * Can be overridden to shut down other stuff as well (like window).
	 * You can decide if you want to call System.exit() as well or not,
	 * or just call super.shutdown() at the end.
	 */
	protected void shutdown() {
		System.exit(0);
	}
	
	/**
	 * Start the game loop in a new thread.
	 */
	public void start() {
		new Thread(this).start();
	}
	
	/**
	 * Start the game loop in a new thread and give the thread a name.
	 * @param threadName
	 */
	public void start(String threadName) {
		new Thread(this, threadName).start();
	}
	
	public void stop() {
		running = false;
	}
	
	public void setInfiniteFPS(boolean isInfinite) {
		infiniteFPS = isInfinite;
	}
	
	@Override
	public void run() {
		init();
		
		long now = System.nanoTime();
		long nsBetweenFrames = (long) (1e9 / FPS);
		
		long time = System.currentTimeMillis();
		int frames = 0;
		
		while (running) {
			if (infiniteFPS || now + nsBetweenFrames <= System.nanoTime()) {
				now += nsBetweenFrames;
				update();
				if (this instanceof HasWindow) {
					HasWindow windowed = (HasWindow) this;
					Window window = windowed.getWindow();
					Graphics2D g = window.getGraphics2D();
					windowed.render(g);
					window.display(g);
				} else {
					render();
				}
				frames++;
				
				if (time + 250 < System.currentTimeMillis()) {
					time += 250;
					lazyUpdate(frames * 4);
					frames = 0;
				}
			}
		}
		shutdown();
	}
}
