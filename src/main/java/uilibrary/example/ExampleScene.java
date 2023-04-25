package uilibrary.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import uilibrary.arrangement.AbsoluteReference;
import uilibrary.enums.Alignment;
import uilibrary.enums.ReferenceType;
import static uilibrary.enums.ReferenceType.*;
import uilibrary.menu.Margin;

public class ExampleScene {
	private ArrayList<Box> boxes = new ArrayList<>();
	
	public ExampleScene() {
		Box greenBox = new Box(300, 200, Color.GREEN);
		greenBox.arrange().setReference(new AbsoluteReference(50, 20)).align(Alignment.TOP, Alignment.LEFT);
		boxes.add(greenBox);
		
		Box redBox = new Box(200, 200, Color.RED);
		redBox.arrange().setReference(280, 300).align(Alignment.TOP, Alignment.LEFT);
		boxes.add(redBox);
		
		Box blueBox = new Box(100, 100, Color.BLUE);
		blueBox.arrange().setReference(redBox, INSIDE, greenBox, INSIDE).align(Alignment.RIGHT, Alignment.BOTTOM);
		boxes.add(blueBox);
		
		Box orangeBox = new Box(100, 100, Color.ORANGE);
		orangeBox.arrange().setReference(greenBox, OUTSIDE).align(Alignment.BOTTOM, Alignment.LEFT).setMargin(0, 20);
		boxes.add(orangeBox);
		
		Box pinkBox = new Box(100, 100, Color.PINK);
		pinkBox.arrange().setReference(redBox, INSIDE);
		boxes.add(pinkBox);
		
		Box cyanBox = new Box(120, 120, Color.CYAN);
		cyanBox.arrange().setReference(greenBox, OUTSIDE).align(Alignment.RIGHT).setMargin("-0.5w2", 0);
		boxes.add(cyanBox);
		
		Box grayBox = new Box(50, 50, Color.GRAY);
		grayBox.arrange().setReference(cyanBox, INSIDE).setMargin("0.5w", "0.5h");
		boxes.add(grayBox);
		
		Box red2Box = new Box(50, 50, Color.RED);
		red2Box.arrange().setReference(blueBox, OUTSIDE_CORNER).align(Alignment.TOP, Alignment.RIGHT);
		boxes.add(red2Box);
	}
	
	public void render(Graphics2D g) {
		for (Box box : boxes) {
			box.render(g);
		}
	}
}
