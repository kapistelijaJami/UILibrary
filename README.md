# UILibrary
UILibrary is a UI library for java, where you can easily create windows, interactable UI elements and game loops to get your app up and running fast.

You can place buttons, divide the window into panels, or align text however you want relative to some bounds, or automatically wrap the text to multiple lines inside the bounds. See Javadoc of Alignment enum for more information on how to use Alignments, and use StringArrangement to render and position text (see RenderText and RenderMultilineText on how the positioning works). Any of your own UI elements should extend Element class, which will provide some methods that are used within this library, width and height variables, and an Arrangement object, which contains the information how to position the Element.

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
        //See Javadoc for DragAndDrop for more information.
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

See the Javadoc for GameLoop and Window for more information.


## How to position objects
To create objects that you can position with this library and use as a reference, the object should extend Element class or InteractableElement for extra hover and click methods.
The Element class will provide some methods that are used within this library, width and height variables, and an Arrangement object, which contains the information on how to position the Element.
You will have to implement render method, and call super() in the constructor with the width and height values.

Here is an example of a Box object:

```Java
public class Box extends Element {
    private final Color color;
    
    public Box(int width, int height, Color color) {
        super(width, height);
        
        this.color = color;
    }
    
    @Override
    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillRect(getX(), getY(), width, height);
    }
}
```

To position these boxes, you just call arrange() method on it, then you can use setter chaining to set the reference, alignments and margin.

In this example we will render multiple Boxes that are positioned relative to each other. The boxes are added to a list, and rendered to the screen with the Graphics2D object. It also renders couple text objects inside some of the boxes.

```Java
public class ExampleScene {
    private ArrayList<Box> boxes = new ArrayList<>();
    private ArrayList<StringArrangement> texts = new ArrayList<>();
    
    public ExampleScene() {
        //You can position elements very easily relative to each other however you want.
        //Here are couple examples:
        
        //Green box of size 300x200, positioned relative to an absolute reference, the point (50, 20).
        //And aligned top left, which means the top left corner of the box touches the point.
        Box greenBox = new Box(300, 200, Color.GREEN);
        greenBox.arrange().setReference(new AbsoluteReference(50, 20)).align(TOP, LEFT);
        boxes.add(greenBox);
        
        //Red box of size 200x200. Also absolute position, same way relative to point (280, 300).
        //Just a shorter way of specifying the absolute location.
        //You can also set the absolute reference coordinates in the arrange()
        //method as well for an even shorter way.
        Box redBox = new Box(200, 200, Color.RED);
        redBox.arrange().setReference(280, 300).align(TOP, LEFT);
        boxes.add(redBox);
        
        //Blue box of size 100x100. This example uses two references, one for horizontal, and one for vertical.
        //Positioned relative to the red box horizontally, and to the green box vertically.
        //It's aligned bottom right and inside both references, which means it will be positioned horizontally
        //so that right edges of blue and red are touching (the x coordinate of the right edges will match),
        //and bottom edges of blue and green are touching.
        Box blueBox = new Box(100, 100, Color.BLUE);
        blueBox.arrange().setReference(redBox, INSIDE, greenBox, INSIDE).align(RIGHT, BOTTOM);
        boxes.add(blueBox);
        
        //Orange box of size 100x100. It is positioned relative to the green box.
        //It's aligned bottom left, but outside of the reference.
        //Here the alignment order matters, bottom comes first, which positions the box below the
        //green box, and aligns it so that the left sides are touching.
        //There is also a 20px vertical margin, so it will be 20 pixels below the green box.
        Box orangeBox = new Box(100, 100, Color.ORANGE);
        orangeBox.arrange().setReference(greenBox, OUTSIDE).align(BOTTOM, LEFT).setMargin(0, 20);
        boxes.add(orangeBox);
        
        //Pink box of size 100x100. It is inside and relative to the red box.
        //Default alignment is center, so it will be perfectly in the middle of the red box.
        Box pinkBox = new Box(100, 100, Color.PINK);
        pinkBox.arrange().setReference(redBox, INSIDE);
        boxes.add(pinkBox);
        
        //Gray box of size 120x120. It's aligned outside the green box, right hand side.
        //But it has a margin, that uses the 'w2' variable, which is the width of the reference. (Here green box width is 300)
        //And it has a multiplier -0.5. So it will be positioned with the margin of -150.
        //This effectively puts it right side of the green box and moves it so the left side is at the center of the green box.
        //Same can be achieved for example with .setReference(greenBox, INSIDE).setMargin("0.5w", 0);
        Box grayBox = new Box(120, 120, Color.GRAY);
        grayBox.arrange().setReference(greenBox, OUTSIDE).align(RIGHT).setMargin("-0.5w2", 0);
        boxes.add(grayBox);
        
        //Cyan box of size 50x50. Positioned relative to the gray box. It's placed inside with the margin ("0.5w", "0.5h").
        //This places the top left corner to the middle of the gray box.
        Box cyanBox = new Box(50, 50, Color.CYAN);
        cyanBox.arrange().setReference(grayBox, INSIDE).setMargin("0.5w", "0.5h");
        boxes.add(cyanBox);
        
        //Another red box, this time size 50x50. Placed relative to the blue box.
        //The ReferenceType is OUTSIDE_CORNER, and aligned top right, which puts it to the top right corner of the blue box.
        //We also set the reference straight in the arrange method. Just a shorter way to do it without setReference() method.
        Box redBox2 = new Box(50, 50, Color.RED);
        redBox2.arrange(blueBox, OUTSIDE_CORNER).align(TOP, RIGHT);
        boxes.add(redBox2);
        
        
        //This creates a text element and places it same way relative to the red box.
        //It aligns the text bottom center, and has a vertical margin of 10px.
        //We did not provide this text element a size, which means it will be one line only, no matter how many characters.
        StringArrangement redBoxText = new StringArrangement("Text inside red", Color.BLACK);
        redBoxText.arrange().setReference(redBox).align(BOTTOM).setMargin(0, 10);
        texts.add(redBoxText);
        
        //Another text element (with font size of 15), but we provide a size this time. The size is from the orange box.
        //The text will be wrapped so that it fits inside this area. It's also placed relative to the orange box.
        //Default ReferenceType is INSIDE, and it's aligned top left with a margin of 5 to both directions.
        //It will fit inside the orange box, but will overflow from below if there are too many characters,
        //unless you use setOverflow(false) -method to disable overflow, which won't render
        //the rest that are outside of the bounds.
        StringArrangement orangeBoxText = new StringArrangement("Text inside orange, that splits to multiple lines.", orangeBox.getSize(), Color.BLACK);
        orangeBoxText.setFontSize(15);
        orangeBoxText.arrange().setReference(orangeBox).align(TOP, LEFT).setMargin(5, 5);
        texts.add(orangeBoxText);
    }
    
    public void render(Graphics2D g) {
        for (Box box : boxes) {
            box.render(g);
        }
        
        for (StringArrangement text : texts) {
            text.render(g);
        }
    }
}
```