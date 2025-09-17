package uilibrary.example;

import java.awt.Color;
import java.awt.Graphics2D;
import uilibrary.GameLoop;
import uilibrary.Window;
import uilibrary.interfaces.HasWindow;

public class Example extends GameLoop implements HasWindow {
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
	public Window getWindow() {
		return window;
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
	public void render(Graphics2D g) { //Render will be called after update
		//Render graphics to the window with the provided Graphics2D
		
		exampleScene.render(g);
		
		//Graphics will be automatically displayed if HasWindow is implemented.
	}
	
	
	// This wont be called if the class implements HasWindow interface.
	/*@Override
	protected void render() { //Render will be called after update
		Graphics2D g = window.getGraphics2D();
		
		//Render graphics to the window with Graphics2D
		
		exampleScene.render(g);
		
		window.display(g);
	}
	*/
}
