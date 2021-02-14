package TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image;
	
	// tile types
	private int type;
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	// tile friction types
	private int frictionType; 
	public static final int GRASS = 0;
	public static final int ICE = 1;
	
	// position
	private double x;
	private double y;
	private double xmap;
	private double ymap;
	
	public Tile(BufferedImage image, int type, int frictionType) {
		this.image = image;
		this.type = type;
		
		this.frictionType = frictionType;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getType() {
		return type;
	}
	
	public int getFrictionType() {
		return frictionType;
	}
	
	public double getx() {
		return this.x;
	}
	
	public double gety() { 
		return this.y;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setFrictionType(int frictionType) {
		this.frictionType = frictionType;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setMapPosition(int xmap, int ymap) {
		this.xmap = xmap;
		this.ymap = ymap;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(
			this.image,
			(int)(x + xmap - this.image.getWidth() / 2),
			(int)(y + ymap - this.image.getHeight() / 2),
				null
			);

		DrawRectangle(g);
	}

	private void DrawRectangle(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawRect(
				(int)(x + xmap - this.image.getWidth() / 2),
				(int)(y + ymap - this.image.getHeight() / 2),
				this.image.getWidth(),
				this.image.getHeight()
		);
	}

}
