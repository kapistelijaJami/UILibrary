# UILibrary
UILibrary is a UI library for java, where you can easily create windows, interactable ui elements and game loops to get your app up and running fast.

You can place buttons, divide the window into panels, or align text however you want relative to some other bounds, and even automatically render the text to multiple lines inside a rectangle, which will be aligned relative to some other bounds.

## Usage
Create a class which contains the main method and will start the loop:
```Java
public class Main {
    public static void main(String[] args) {
        Game game = new Game(60);
        game.start();
    }
}
```

Create a class for the game/engine that will extend GameLoop.
It keeps track of the window, updates the game and renders the game into the window.

```Java
import uilibrary.GameLoop;
import uilibrary.Window;
import java.awt.Graphics2D;

public class Game extends GameLoop {
    private Window window;
    
    public Game(int fps) {
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
		
		//You can also initialize drag and drop here with:
		//window.setTransferHandler(new DragAndDrop(this::openFile));
		//See JavaDoc for DragAndDrop for more information.
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
```

Now you will have a window with blank screen. You can easily start rendering with the Graphics2D object. Update and render will be called fps times per second (here 60). You can stop the execution of the loop by calling super.stop() inside the Game class.

See the JavaDoc for GameLoop and Window for more information.