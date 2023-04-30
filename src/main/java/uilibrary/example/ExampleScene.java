package uilibrary.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import uilibrary.arrangement.AbsoluteReference;
import uilibrary.arrangement.TextElement;
import static uilibrary.enums.Alignment.*;
import static uilibrary.enums.ReferenceType.*;

public class ExampleScene {
	private List<Box> boxes = new ArrayList<>();
	private List<TextElement> texts = new ArrayList<>();
	
	public ExampleScene() {
		//You can position elements very easily relative to each other however you want.
		//Here are couple examples:
		
		//Absolute positioning:
		
		//Green box of size 300x200, positioned relative to an absolute reference, the point (50, 20).
		//And aligned top left, which means the top left corner of the box touches the point.
		Box greenBox = new Box(300, 200, Color.GREEN);
		greenBox.arrange().setReference(new AbsoluteReference(50, 20)).align(TOP, LEFT);
		boxes.add(greenBox);
		
		//Red box of size 200x200. Also absolute position, same way relative to point (280, 300).
		//Just a shorter way of specifying the absolute location.
		Box redBox = new Box(200, 200, Color.RED);
		redBox.arrange().setReference(280, 300).align(TOP, LEFT);
		boxes.add(redBox);
		
		//White box of size 30x30. Even shorter way to specify the location.
		//Top left corner will be at point (60, 450).
		Box whiteBox = new Box(30, 30, Color.RED);
		whiteBox.arrange(60, 450).align(TOP, LEFT);
		boxes.add(whiteBox);
		
		//Magenta box of size 30x30. Uses another constructor that calls the constructor
		//of Element class with the location, which will automatically align it top left.
		//So top left corner will be at point (120, 450).
		boxes.add(new Box(120, 450, 30, 30, Color.MAGENTA));
		
		
		//Relative positioning:
		
		//Blue box of size 100x100. This example uses two references, one for horizontal, and one for vertical.
		//Positioned relative to the red box horizontally, and to the green box vertically.
		//It's aligned bottom right and inside both references, which means it will be positioned horizontally
		//so that right edges of blue and red are touching (the x coordinate of the right edges will match),
		//and bottom edges of blue and green are touching.
		Box blueBox = new Box(100, 100, Color.BLUE);
		blueBox.arrange().setReference(redBox, INSIDE, greenBox, INSIDE).align(RIGHT, BOTTOM);
		boxes.add(blueBox);
		
		//Orange box of size 100x100. It is positioned relative to the green box. It's aligned bottom left, but outside of the reference.
		//Here the alignment order matters, bottom comes first, which positions the box below the green box, and aligns it so that the left sides are touching.
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
		TextElement redBoxText = new TextElement("Text inside red", Color.BLACK);
		redBoxText.arrange().setReference(redBox).align(BOTTOM).setMargin(0, 10);
		texts.add(redBoxText);
		
		//Another text element (with font size of 15), but we provide a size this time. The size is the size of the orange box.
		//The text will be wrapped so that it fits inside this area. It's also placed relative to the orange box.
		//Default ReferenceType is INSIDE, and it's aligned top left with a margin of 5 to both directions.
		//It will fit inside the orange box, but will overflow from below if there are too many characters,
		//unless you use setOverflow(false) -method to disable overflow, which won't render the rest that are outside of the bounds.
		TextElement orangeBoxText = new TextElement("Text inside orange, that splits to multiple lines.", orangeBox.getSize(), Color.BLACK);
		orangeBoxText.setFontSize(15);
		orangeBoxText.arrange().setReference(orangeBox).align(TOP, LEFT).setMargin(5, 5);
		texts.add(orangeBoxText);
	}
	
	public void render(Graphics2D g) {
		for (Box box : boxes) {
			box.render(g);
		}
		
		for (TextElement text : texts) {
			text.render(g);
		}
	}
}
