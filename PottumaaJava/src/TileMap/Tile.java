package TileMap;

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
	public double x;
	public double y;
	
	// dimensions
//	private double width;
//	private double height;
	
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
	
//	public void setDimensions(double width, double height) {
//		this.width = width;
//		this.height = height;
//	}
	
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
	
	public void draw(Graphics2D g) {
		g.drawImage(
			this.image,
				(int)x,
				(int)y,
				null
			);
	}

}
