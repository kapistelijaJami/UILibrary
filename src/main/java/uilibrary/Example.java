package uilibrary;

import java.awt.Graphics2D;

public class Example extends GameLoop {
	private Window window;
	
	public static void main(String[] args) {
		Example example = new Example(60);
		example.start();
	}
	
	public Example(int fps) {
		super(fps);
		
		int width = 1280;
		int height = 720;
		this.window = new Window(width, height, "My Title");
	}
	
	@Override
	protected void init() { //Init will be called right before the game loop starts.
		//Initialize member variables, or set up listeners etc.
		
		//You can add mouse- and keyListeners etc. to the canvas with:
		//Canvas canvas = window.getCanvas();
		//canvas.addMouseListener(yourMouseListener);
	}
	
	@Override
	protected void update() { //Update will be called 'fps' times per second
		//Update your game
	}
	
	@Override
	protected void render() { //Render will be called after update
		Graphics2D g = window.getGraphics2D();
		
		//Render graphics to the window with g
		
		window.display(g);
	}
}
