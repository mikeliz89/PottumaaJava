package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.*;

import GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel 
	implements Runnable, KeyListener{
	
	// dimensions
	public static final int WIDTH = 820;
	public static final int HEIGHT = 480;
	public static final int SCALE = 2;
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// image
	private BufferedImage image;
	private Graphics2D graphics2D;

	// game state manager
	private GameStateManager gameStateManager;
	
	public GamePanel() {
		super();
		setPreferredSize(
			new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init() {
		createImage();
		createGraphics();
		running = true;
		createGameStateManager();
	}

	private void createGraphics() {
		graphics2D = (Graphics2D) image.getGraphics();
	}

	private void createGameStateManager() {
		gameStateManager = new GameStateManager();
	}

	private void createImage() {
		image = new BufferedImage(
					WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB
				);
	}

	public void run() {
		init();
		gameLoop();
	}

	private void gameLoop() {

		while(running) {

			long start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			long elapsed = getElapsed(start);
			long wait = getWait(elapsed);

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private long getElapsed(long start) {
		return System.nanoTime() - start;
	}

	private long getWait(long elapsed) {
		long wait = targetTime - elapsed / 1000000;
		if(wait < 0) wait = 5;
		return wait;
	}

	private void update() {
		gameStateManager.update();
	}
	private void draw() {
		gameStateManager.draw(graphics2D);
	}
	private void drawToScreen() {
		Graphics gamePanelGraphics = getGraphics();
		gamePanelGraphics.drawImage(image, 0, 0,
				WIDTH * SCALE, HEIGHT * SCALE,
				null);
		gamePanelGraphics.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		if(gameStateManager == null) return;
		gameStateManager.keyEventHappens(key);
		gameStateManager.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) {
		if(gameStateManager == null) return;
		gameStateManager.keyReleased(key.getKeyCode());
	}

}
















