package uilibrary.example;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.SwingUtilities;
import uilibrary.GameLoop;
import uilibrary.Window;

public class Example extends GameLoop {
	private final Window window;
	private ExampleScene exampleScene;
	private final String TITLE = "My Title";
	
	public static void main(String[] args) {
		Example example = new Example(60);
		example.start();
	}
	
	public Example(int fps) {
		super(fps);
		
		int width = 1280;
		int height = 720;
		this.window = new Window(width, height, TITLE);
		window.setCanvasBackground(Color.GRAY);
	}
	
	@Override
	protected void init() { //Init will be called right before the game loop starts.
		//Initialize member variables, or set up listeners etc.
		
		//You can add mouse- and keyListeners etc. to the canvas with:
		//Canvas canvas = window.getCanvas();
		//canvas.addMouseListener(yourMouseListener);
		
		exampleScene = new ExampleScene();
	}
	
	@Override
	protected void lazyUpdate(int fps) {
		window.setTitle(TITLE + " - " + fps + " fps");
	}
	
	@Override
	protected void update() { //Update will be called 'fps' times per second
		//Update your game
	}
	
	@Override
	protected void render() { //Render will be called after update
		Graphics2D g = window.getGraphics2D();
		
		//Render graphics to the window with g
		
		exampleScene.render(g);
		
		window.display(g);
	}
}
