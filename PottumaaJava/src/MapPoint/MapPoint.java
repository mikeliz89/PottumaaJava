package MapPoint;

import Audio.AudioPlayer;
import Main.GameOptions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class MapPoint {

	private BufferedImage image;
	
	// position
	private double x;
	private double y;
	private double xmap;
	private double ymap;
	private int gotoLevel;
	private String mapPointSound;

	private Hashtable<String, AudioPlayer> sfx;
	
	public MapPoint(BufferedImage image, String mapPointSound) {
		this.image = image;
		this.mapPointSound = mapPointSound;
		sfx = new Hashtable<>();
		setSoundEffects();
	}

	private void setSoundEffects() {
		if(mapPointSound == "")
			return;
		sfx.put(mapPointSound, new AudioPlayer(mapPointSound));
	}

	public void playSoundEffect() {
		if(mapPointSound == "")
			return;
		sfx.get(mapPointSound).play();
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setMapPosition(int xmap, int ymap) {
		this.xmap = xmap;
		this.ymap = ymap;
	}
	
	public void setGotoLevel(int gotoLevel) { 
		this.gotoLevel = gotoLevel;
	}
	
	public int getGotoLevel() {
		return this.gotoLevel;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(
			this.image,
			(int)(x + xmap - this.image.getWidth() / 2),
			(int)(y + ymap - this.image.getHeight() / 2),
				null
			);

		DrawDebugRectangle(g);
	}

	private void DrawDebugRectangle(Graphics2D g) {
		if(GameOptions.IS_DEBUG_MODE == false)
			return;

		g.setColor(Color.RED);
		g.drawRect(
				(int)(x + xmap - this.image.getWidth() / 2),
				(int)(y + ymap - this.image.getHeight() / 2),
				this.image.getWidth(),
				this.image.getHeight()
		);
	}

}
