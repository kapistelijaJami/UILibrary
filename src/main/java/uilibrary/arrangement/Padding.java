package uilibrary.arrangement;

/**
 * Padding makes the text area itself smaller (the TextElement bounds stay the same),
 * while margin positions the TextElement bounds further from the reference.
 */
public class Padding {
	public int x, y;
	
	public Padding() {}
	
	public Padding(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
