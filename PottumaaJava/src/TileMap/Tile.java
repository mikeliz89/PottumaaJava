package TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image;
	
	// tile types
	private int tileType;
	public static final int TILE_TYPE_GROUND = 0;
	public static final int TILE_TYPE_OBSTACLE = 1;
	
	// tile friction types
	private int frictionType; 
	public static final int TILE_FRICTION_TYPE_GRASS = 0;
	public static final int TILE_FRICTION_TYPE_ICE = 1;
	
	// position
	private double x;
	private double y;
	private double xMap;
	private double yMap;
	
	public Tile(BufferedImage image, int type, int frictionType) {
		this.image = image;
		this.tileType = type;
		
		this.frictionType = frictionType;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getTileType() {
		return tileType;
	}
	
	public int getFrictionType() {
		return frictionType;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setTileType(int tileType) {
		this.tileType = tileType;
	}
	
	public void setFrictionType(int frictionType) {
		this.frictionType = frictionType;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setMapPosition(int xMap, int yMap) {
		this.xMap = xMap;
		this.yMap = yMap;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(
			this.image,
			(int)(x + xMap - this.image.getWidth() / 2),
			(int)(y + yMap - this.image.getHeight() / 2),
				null
			);

		DrawRectangle(g);
	}

	private void DrawRectangle(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawRect(
				(int)(x + xMap - this.image.getWidth() / 2),
				(int)(y + yMap - this.image.getHeight() / 2),
				this.image.getWidth(),
				this.image.getHeight()
		);
	}

}
