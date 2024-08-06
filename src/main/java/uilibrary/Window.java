package uilibrary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import uilibrary.arrangement.Arrangement;

/**
 * Basic window with a canvas where you can draw with Graphics2D object.
 * Ask for graphics2D with getGraphics2D() method, then render with it, and finally call display() with the graphics object.
 * Add most of the listeners to the canvas. You can get the canvas object with getCanvas().
 * Added also focus method to focus on the canvas and bringing the window to front.
 * You can also get the InputMap and ActionMap objects to set keybindings with getInputMap() and getActionMap().
 * You can also use the Canvas object as a Reference for other objects as well.
 */
public class Window extends JFrame {
	private Canvas canvas;
	private boolean fullscreen;
	private JPanel keybindingPanel;
	private Runnable onClose;
	private Runnable onMinimized;
	private Runnable onUnminimized;
	private Runnable onMaximized;
	private Runnable onUnmaximized;
	private Runnable onResized;
	
	private boolean unmaximizedEvent;
	
	public Window(int width, int height, String title) {
		this(width, height, title, true, 0, false, true);
	}
	
	public Window(int width, int height, boolean addCanvas, String title) {
		this(width, height, title, true, 0, false, addCanvas);
	}
	
	public Window(int width, int height, String title, boolean resizable) {
		this(width, height, title, resizable, 0, false, true);
	}
	
	public Window(int width, int height, String title, boolean resizable, boolean addCanvas) {
		this(width, height, title, resizable, 0, false, addCanvas);
	}
	
	public Window(int width, int height, String title, int spawnScreen, boolean fullscreen) {
		this(width, height, title, true, spawnScreen, fullscreen, true);
	}
	
	public Window(int width, int height, String title, int spawnScreen, boolean fullscreen, boolean addCanvas) {
		this(width, height, title, true, spawnScreen, fullscreen, addCanvas);
	}
	
	public Window(int width, int height, String title, boolean resizable, int spawnScreen, boolean fullscreen) {
		this(width, height, title, resizable, spawnScreen, fullscreen, true);
	}
	
	/**
	 * Creates the window with the given parameters.
	 * @param width
	 * @param height
	 * @param title
	 * @param resizable
	 * @param spawnScreen Which screen/monitor to spawn the window. 0 is the default screen and 1 is the next etc.
	 * @param fullscreen 
	 * @param addCanvas 
	 */
	public Window(int width, int height, String title, boolean resizable, int spawnScreen, boolean fullscreen, boolean addCanvas) {
		super(title);
		Toolkit.getDefaultToolkit().setDynamicLayout(false);		//Resize components only after the window resize is ready, it doesn't flicker as much.
		
		if (!addCanvas) {
			super.setPreferredSize(new Dimension(width, height));	//If no canvas, then we need to set the size of the window.
		}
		super.setSize(new Dimension(width, height));				//For the canvas to just use these dimensions when/if we add it.
		super.setMinimumSize(new Dimension(256, 144));				//Minimum size of the window
		
		setWindowCloseOperation();
		
		keybindingPanel = (JPanel) getContentPane();	//JPanel that's between the window and canvas so you can add keybindings to the panel since it's JComponent.
		
		if (addCanvas) {
			addDefaultCanvas();
		}
		
		super.setResizable(resizable);			//Can the window be resized, default is true
		super.pack();							//Pack the added components inside the window, window will become correct size, and this makes it displayable.
		super.setLocationRelativeTo(null);		//Window will spawn in the middle of the screen
		
		super.setVisible(true);					//Makes the window visible
		
		addResizeListener();
		
		//FULLSCREEN STUFF
		this.fullscreen = fullscreen;
		if (fullscreen) {
			//This might not resize everything if all the items are not fully initialized inside the window.
			//To quarantee it works, call setFullscreen later, or set width and height based on the screen size.
			//(Might need to call setFullscreen(false) before calling setFullscreen(true))
			setFullscreen(true, spawnScreen);
			//super.setExtendedState(JFrame.MAXIMIZED_BOTH);
			//super.setUndecorated(true);
		} else {
			setScreen(spawnScreen);
		}
		
		focus();
	}
	
	private void setWindowCloseOperation() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Window and the whole application closes with X (default is HIDE_ON_CLOSE)
		
		super.addWindowListener(new WindowAdapter() {	//Window can be set to close from X and can also call a method
			@Override
			public void windowClosing(WindowEvent e) {
				//closing
				if (onClose != null) {
					onClose.run();
				}
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				//minimized
				System.out.println("minimized");
				if (onMinimized != null) {
					onMinimized.run();
				}
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				//back from minimized
				System.out.println("unminimized");
				if (onUnminimized != null) {
					onUnminimized.run();
				}
			}
		});
		
		super.addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if (isFlagMaximized(e.getOldState()) && !isFlagMaximized(e.getNewState())) { //Back from maximized
					unmaximizedEvent = true; //we run the function in componentResized, because it's after the event has passed (this is right before)
				}
			}
		});
		
		super.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (isFlagMaximized(Window.this.getExtendedState())) { //Maximized
					if (onMaximized != null) {
						onMaximized.run();
					}
				} else if (unmaximizedEvent) { //Back from maximized
					unmaximizedEvent = false;
					if (onUnmaximized != null) {
						onUnmaximized.run();
					}
				}
				
				if (onResized != null) { //TODO: do you always want to run this, or only when maximized functions weren't ran?
					onResized.run();
				}
			}
		});
	}
	
	private boolean isFlagMaximized(int flag) {
		return (flag & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
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
	
	public final void removeCanvas() { //TODO: might need to resize the window here or set preferred size before pack.
		super.remove(canvas);
		super.pack();
	}
	
	public final void addDefaultCanvas() {
		canvas = new Canvas();
		setCanvasBackground(Color.BLACK);
		
		//Set the canvas size based on width and height (the window itself is bit bigger, since it has borders) (setVisible cannot be between setSize and pack)
		canvas.setPreferredSize(new Dimension(getWidth(), getHeight()));
		
		canvas.setFocusTraversalKeysEnabled(false);	//Disables focus traversal with tab key, so it wont get consumed and keyListener can fire on it
		super.add(canvas);
		super.pack();
	}
	
	public void addCanvas(Canvas canvas) {
		this.canvas = canvas;
		super.add(canvas);
		super.pack();
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
	
	/**
	 * Sets the background color for the canvas.
	 * @param color 
	 */
	public final void setCanvasBackground(Color color) {
		if (this.canvas != null) {
			canvas.setBackground(color);
		}
	}
	
	/**
	 * Use setCanvasBackground() to set the background color.
	 * @param color
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void setBackground(Color color) {}
	
	/**
	 * Returns the bounds of the canvas with location relative to the monitor,
	 * not to the window location, like canvas.getBounds() would.
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
	
	/**
	 * Sets the location of the window to a given screen/monitor.
	 * 0 is the default screen and 1 is the next etc.
	 * @param screen 
	 */
	public final void setScreen(int screen) {
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
		
		int dx = gcBounds.x + (gcBounds.width - windowSize.width) / 2;
		int dy = gcBounds.y + (gcBounds.height - windowSize.height) / 2;
		
		if (dy < gcBounds.y) {
			dy = gcBounds.y;
		}
		
		super.setLocation(dx, dy);
	}
	
	public final void toggleFullscreen() {
		setFullscreen(!fullscreen, 0);
	}
	
	public final void setFullscreen(boolean fullscreen) {
		setFullscreen(fullscreen, 0);
	}
	
	public final void setFullscreen(boolean fullscreen, int screen) {
		this.fullscreen = fullscreen;
		
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		GraphicsDevice monitor;
		
		if (screen >= 0 && screen < devices.length) {
			monitor = devices[screen];
		} else if (devices.length > 0) {
			monitor = devices[0];
		} else {
			throw new RuntimeException("No Screens Found");
		}
		
		monitor.setFullScreenWindow(fullscreen ? this : null);
		focus();
	}
	
	/**
	 * This method will return Graphics2D object for the canvas which is used to render in the window.
	 * @return 
	 */
	public Graphics2D getGraphics2D() {
		if (canvas == null) {
			return null;
		}
		return getGraphics2D(canvas);
	}
	
	/**
	 * You can call this method with your own canvas to get Graphics2D object from it.
	 * @param c
	 * @return 
	 */
	public Graphics2D getGraphics2D(Canvas c) {
		BufferStrategy bs = c.getBufferStrategy();
		while (bs == null) {
			c.createBufferStrategy(2);
			bs = c.getBufferStrategy();
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		setGraphicsRenderingHints(g);
		
		g.setColor(c.getBackground());
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
		
		return g;
	}
	
	public static void setGraphicsRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); //for image scaling to look sharp, might not be good for all images
	}
	
	/**
	 * Call this with the Graphics2D object to show the rendered graphics in the window.
	 * @param g 
	 */
	public void display(Graphics2D g) {
		display(g, canvas);
	}
	
	/**
	 * Call this with the Graphics2D object and the canvas to show the rendered graphics in the canvas.
	 * @param g 
	 * @param c 
	 */
	public void display(Graphics2D g, Canvas c) {
		g.dispose();
		c.getBufferStrategy().show();
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}
	
	/**
	 * Request focus.
	 * Made to request focus for canvas, and to get
	 * the best possible chance of getting the focus.
	 */
	public final void focus() {
		toFront();
		if (canvas != null) {
			canvas.requestFocus();
		} else {
			requestFocus();
		}
	}
	
	/**
	 * Sets a function which will be executed right before the window closes.
	 * @param runnable 
	 */
	public void setOnClosingFunction(Runnable runnable) {
		this.onClose = runnable;
	}
	
	/**
	 * Sets a function which will be executed when the window is minimized.
	 * @param runnable 
	 */
	public void setOnMinimizedFunction(Runnable runnable) {
		this.onMinimized = runnable;
	}
	
	/**
	 * Sets a function which will be executed when the window is back from minimized.
	 * @param runnable 
	 */
	public void setOnUnminimizedFunction(Runnable runnable) {
		this.onUnminimized = runnable;
	}
	
	/**
	 * Sets a function which will be executed when the window is maximized.
	 * @param runnable 
	 */
	public void setOnMaximizedFunction(Runnable runnable) {
		this.onMaximized = runnable;
	}
	
	/**
	 * Sets a function which will be executed when the window is back from maximized.
	 * @param runnable 
	 */
	public void setOnUnmaximizedFunction(Runnable runnable) {
		this.onUnmaximized = runnable;
	}
	
	/**
	 * Sets a function which will be executed when the window is resized.
	 * @param runnable 
	 */
	public void setOnResizedFunction(Runnable runnable) {
		this.onResized = runnable;
	}
	
	/**
	 * If other objects position relative to the canvas, this will correctly
	 * change their locations when the window is resized.
	 */
	private void addResizeListener() {
		super.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Arrangement.updateAllArrangements();
			}
		});
	}
	
	public InputMap getInputMap() {
		return getInputMap(JComponent.WHEN_FOCUSED);
	}
	
	/**
	 * Get the input map.
	 * @param condition One of JComponent.WHEN_FOCUSED, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, JComponent.WHEN_IN_FOCUSED_WINDOW. Default: JComponent.WHEN_FOCUSED
	 * @return 
	 */
	public InputMap getInputMap(int condition) {
		return keybindingPanel.getInputMap(condition);
	}
	
	public ActionMap getActionMap() {
		return keybindingPanel.getActionMap();
	}
	//how to use keybindings:
	/*InputMap inputMap = keybindingPanel.getInputMap(JComponent.WHEN_FOCUSED);
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Pressed.UP"); //"Pressed.UP" string is arbitrary that links input to action, make it descriptive

	ActionMap actionMap = keybindingPanel.getActionMap();
	actionMap.put("Pressed.UP", new AbstractAction() { //You can create a class that extends AbstractAction or use anonymous class like here.
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("tuli");
		}
	});*/
}
