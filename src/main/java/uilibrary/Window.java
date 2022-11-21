package uilibrary;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 * Basic window with a canvas where you can draw with Graphics2D object.
 * Ask for graphics2D with getGraphics2D() method, then render with it, and finally call display(g).
 */
public class Window extends JFrame {
	private Canvas canvas;
	private boolean fullscreen;
	private Color background = Color.black;
	
	public Window(int width, int height, String title) {
		this(width, height, title, true, 0, false);
	}
	
	public Window(int width, int height, String title, boolean resizable) {
		this(width, height, title, resizable, 0, false);
	}
	
	public Window(int width, int height, String title, int screen, boolean fullscreen) {
		this(width, height, title, true, screen, fullscreen);
	}
	
	public Window(int width, int height, String title, boolean resizable, int screen, boolean fullscreen) {
		super(title);
		canvas = new Canvas();
		canvas.setFocusTraversalKeysEnabled(false);		//disables focus traversal with tab key, so it wont get consumed and keyListener can fire on it
		Toolkit.getDefaultToolkit().setDynamicLayout(false); //resizaa componentit vasta kun ikkunan resize on valmis, ei vilku niin paljoa
		
		//tää vaikuttaa vaan ikkunaan, parempi laittaa canvasiin, niin toimii paremmin
		//super.setPreferredSize(new Dimension(width, height));
		
		canvas.setSize(width, height); //laitetaan canvasin koko (ikkunan koko on vähän isompi, kun siinä on reunat) (setVisible ei saa olla setSize ja pack välissä)
		super.setMinimumSize(new Dimension(256, 144));
		
		
		handleCloseOperation();
		
		super.setResizable(resizable);		//voiko ikkunaa venyttää, oletus oli true
		super.setLocationRelativeTo(null);	//ikkuna syntyy näytön keskelle
		super.add(canvas);					//lisätään peli ikkunaan
		super.pack();						//pakkaa addatut tavarat ikkunaan, ja ikkunasta tulee siten oikean kokoinen
		
		super.setVisible(true);				//laitetaan ikkuna näkyväksi
		
		setScreenPriv(screen);
		
		
		//FULLSCREEN STUFF
		this.fullscreen = fullscreen;
		if (fullscreen) {
			//this might not resize everything if all the items are not fully initialized inside the window.
			//To quarantee it works, call setFullscreen later, or set width and height based on the screen size.
			//(Might need to call setFullscreen(false) before calling setFullscreen(true))
			setFullscreen(true);
			/*super.setExtendedState(JFrame.MAXIMIZED_BOTH);
			super.setUndecorated(true);*/
		}
		
		canvas.requestFocus();
	}
	
	private void handleCloseOperation() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//ikkuna sulkeutuu rastista
		
		super.addWindowListener(new WindowAdapter() {	//ikkunan voi laittaa sulkeutumaan rastista ja pystyy kutsua myös metodia
			@Override
			public void windowClosing(WindowEvent e) {
				//closing
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				//minimized
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				//back from minimized
			}
		});
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public int getCanvasWidth() {
		return canvas.getWidth();
	}
	
	public int getCanvasHeight() {
		return canvas.getHeight();
	}
	
	public void setCanvasBackground(Color color) {
		background = color;
	}
	
	/**
	 * Returns the bounds of the canvas with location relative to the screen,
	 * not to the window, like canvas.getBounds() would.
	 * @return 
	 */
	public Rectangle getCanvasBounds() {
		Insets inset = getInsets();
		return new Rectangle(getX() + inset.left + canvas.getX(), getY() + inset.top + canvas.getY(), canvas.getWidth(), canvas.getHeight());
	}
	
	public Dimension getCanvasSize() {
		return new Dimension(canvas.getWidth(), canvas.getHeight());
	}
	
	public void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	public void setScreen(int screen) {
		setScreenPriv(screen);
	}
	
	private void setScreenPriv(int screen) {
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		
		GraphicsDevice monitor;
		
		if (screen >= 0 && screen < devices.length) {
			monitor = devices[screen];
		} else if (devices.length > 0) {
			monitor = devices[0];
		} else {
			throw new RuntimeException("No Screens Found");
		}
		
		Rectangle gcBounds = monitor.getDefaultConfiguration().getBounds();
		
		Dimension windowSize = super.getSize();
		Point centerPoint = new Point((int) gcBounds.getCenterX(), (int) gcBounds.getCenterY());
		int dx = centerPoint.x - windowSize.width / 2;
		int dy = centerPoint.y - windowSize.height / 2;
		
		
		// Avoid being placed off the edge of the screen:
		if (dy + windowSize.height > gcBounds.y + gcBounds.height) { //bottom
			dy = gcBounds.y + gcBounds.height - windowSize.height;
		}
		
		if (dy < gcBounds.y) { //top
			dy = gcBounds.y;
		}
		
		if (dx + windowSize.width > gcBounds.x + gcBounds.width) { //right
			dx = gcBounds.x + gcBounds.width - windowSize.width;
		}
		
		if (dx < gcBounds.x) { //left
			dx = gcBounds.x;
		}
		
		super.setLocation(dx, dy);
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fullscreen ? this : null);
	}
	
	public Graphics2D getGraphics2D() {
		BufferStrategy bs = canvas.getBufferStrategy();
		while (bs == null) {
			canvas.createBufferStrategy(3);
			bs = canvas.getBufferStrategy();
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		setGraphicsRenderingHints(g);
		
		g.setColor(background);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		return g;
	}
	
	public static void setGraphicsRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); //for image scaling to look sharp, might not be good for all images
	}
	
	public void display(Graphics2D g) {
		g.dispose();
		canvas.getBufferStrategy().show();
	}

	public boolean isFullscreen() {
		return fullscreen;
	}
}
