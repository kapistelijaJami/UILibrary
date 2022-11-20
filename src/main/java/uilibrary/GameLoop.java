package uilibrary;

/**
 * Game loop library for running update and render methods in a separate thread with specific fps.
 * Create a subclass that extends GameLoop.
 * Implement required methods. Update and render will be called back to back fps times a second.
 * Call start() for this object and you are good.
 * Override shutdown() to decide what to do when stopping the loop.
 */
public abstract class GameLoop implements Runnable {
	protected final int FPS;
	protected boolean running = true;
	
	public GameLoop(int fps) {
		this.FPS = fps;
	}
	
	protected abstract void update();
	protected abstract void render();
	protected abstract void init();
	protected abstract void updateTitle(int fps);
	
	/**
	 * Calls System.exit().
	 * Can be overridden to shut down other stuff as well (like window).
	 * You can decide if you want to call System.exit() as well.
	 */
	protected void shutdown() {
		System.exit(0);
	}
	
	public synchronized void start() { //not sure if need synchronized
		init();
		new Thread(this).start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	@Override
	public void run() {
		long now = System.nanoTime();
		long nsBetweenFrames = (long) (1e9 / FPS);
		
		long time = System.currentTimeMillis();
		int frames = 0;
		
		while (running) {
			if (now + nsBetweenFrames <= System.nanoTime()) {
				now += nsBetweenFrames;
				update();
				render();
				frames++;
				
				if (time + 250 < System.currentTimeMillis()) {
					time += 250;
					updateTitle(frames * 4);
					frames = 0;
				}
			}
		}
		shutdown();
	}
}
