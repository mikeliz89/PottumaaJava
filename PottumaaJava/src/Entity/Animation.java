package Entity;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	private boolean playedOnce;
	
	public Animation() {
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	public void setDelay(long d) { delay = d; }
	public void setFrame(int i) { currentFrame = i; }
	
	public void update() {
		
		if(delay == -1) return;
		
		//how long since last frame came up. Current time minus start time divided by million (milliseconds)
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		
		//time to change to the next frame
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime(); // reset the timer
		}
		
		//dont go pass amount of frames there are
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
	}
	
	public int getFrame() { return currentFrame; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return playedOnce; }
}
