package uilibrary.menu;

public class Margin {
	public int x, y;
	
	public Margin() {
		this(0, 0);
	}
	
	public Margin(int both) {
		this(both, both);
	}

	public Margin(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
