package TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MapPoint {

	private BufferedImage image;
	
	// position
	private double x;
	private double y;
	private double xmap;
	private double ymap;
	
	private int gotoLevel;
	
	public MapPoint(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public double getx() {
		return this.x;
	}
	
	public double gety() { 
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
		g.setColor(Color.RED);
		g.drawRect(
				(int)(x + xmap - this.image.getWidth() / 2),
				(int)(y + ymap - this.image.getHeight() / 2),
				this.image.getWidth(),
				this.image.getHeight()
		);
	}

}
