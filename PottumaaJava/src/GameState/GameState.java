package GameState;

import java.awt.Graphics2D;

public abstract class GameState {

	/* Members */
	protected GameStateManager gsm;
	protected boolean backgroundMusicShouldChange;
	/* Methods */
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
}
