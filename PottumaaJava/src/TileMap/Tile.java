package TileMap;

import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image;
	private int type;

	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	
	private int frictionType; 
	
	public static final int GRASS = 0;
	public static final int ICE = 1;

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
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setFrictionType(int frictionType) {
		this.frictionType = frictionType;
	}

}
