package uilibrary.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import uilibrary.arrangement.AbsoluteReference;
import uilibrary.arrangement.StringArrangement;
import uilibrary.enums.Alignment;
import static uilibrary.enums.Alignment.*;
import uilibrary.enums.ReferenceType;
import static uilibrary.enums.ReferenceType.*;
import uilibrary.menu.Margin;

public class ExampleScene {
	private ArrayList<Box> boxes = new ArrayList<>();
	private ArrayList<StringArrangement> texts = new ArrayList<>();
	
	public ExampleScene() {
		Box greenBox = new Box(300, 200, Color.GREEN);
		greenBox.arrange().setReference(new AbsoluteReference(50, 20)).align(TOP, LEFT);
		boxes.add(greenBox);
		
		Box redBox = new Box(200, 200, Color.RED);
		redBox.arrange().setReference(280, 300).align(TOP, LEFT);
		boxes.add(redBox);
		
		Box blueBox = new Box(100, 100, Color.BLUE);
		blueBox.arrange().setReference(redBox, INSIDE, greenBox, INSIDE).align(RIGHT, BOTTOM);
		boxes.add(blueBox);
		
		Box orangeBox = new Box(100, 100, Color.ORANGE);
		orangeBox.arrange().setReference(greenBox, OUTSIDE).align(BOTTOM, LEFT).setMargin(0, 20);
		boxes.add(orangeBox);
		
		Box pinkBox = new Box(100, 100, Color.PINK);
		pinkBox.arrange().setReference(redBox, INSIDE);
		boxes.add(pinkBox);
		
		Box grayBox = new Box(120, 120, Color.GRAY);
		grayBox.arrange().setReference(greenBox, OUTSIDE).align(RIGHT).setMargin("-0.5w2", 0);
		boxes.add(grayBox);
		
		Box cyanBox = new Box(50, 50, Color.CYAN);
		cyanBox.arrange().setReference(grayBox, INSIDE).setMargin("0.5w", "0.5h");
		boxes.add(cyanBox);
		
		Box red2Box = new Box(50, 50, Color.RED);
		red2Box.arrange().setReference(blueBox, OUTSIDE_CORNER).align(TOP, RIGHT);
		boxes.add(red2Box);
		
		
		StringArrangement redBoxText = new StringArrangement("Text inside red", Color.BLACK);
		redBoxText.arrange().setReference(redBox).align(BOTTOM).setMargin(0, 5);
		texts.add(redBoxText);
		
		StringArrangement orangeBoxText = new StringArrangement("Text inside orange, that splits to multiple lines.", orangeBox.getWidth() - 10, orangeBox.getHeight() - 10, Color.BLACK);
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
