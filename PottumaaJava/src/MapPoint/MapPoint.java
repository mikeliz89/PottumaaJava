package MapPoint;

import Audio.AudioPlayer;
import Main.GameOptions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class MapPoint {

	private BufferedImage image;
	
	// position
	private double x;
	private double y;
	private double xMap;
	private double yMap;
	private int width;
	private int height;
	private int gotoLevel;
	private String mapPointSound;
	private int mapPointType;
	private Hashtable<String, AudioPlayer> sfx;

	public static final int MAP_POINT_TYPE_ARROW_UP = 1;
	public static final int MAP_POINT_TYPE_ARROW_DOWN = 2;
	public static final int MAP_POINT_TYPE_ARROW_LEFT = 3;
	public static final int MAP_POINT_TYPE_ARROW_RIGHT = 4;
	
	public MapPoint(int mapPointType, String mapPointSound) {
		this.mapPointType = mapPointType;
		this.mapPointSound = mapPointSound;
		sfx = new Hashtable<>();
		setImage();
		setSoundEffects();
	}

	private void setImage() {
		try {
			var imageName = getImageName();
			BufferedImage tileset = ImageIO.read(
					getClass().getResourceAsStream(imageName)
			);

			BufferedImage subImage = getSubImage(tileset);

			image = subImage;

			width = image.getWidth();
			height = image.getHeight();

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private BufferedImage getSubImage(BufferedImage tileset) {
		switch(this.mapPointType) {
			case MAP_POINT_TYPE_ARROW_RIGHT:
				return tileset.getSubimage(30, 0, 30, 30);
			case MAP_POINT_TYPE_ARROW_DOWN:
				return tileset.getSubimage(60, 0, 30, 30);
			case MAP_POINT_TYPE_ARROW_LEFT:
				return tileset.getSubimage(90, 0, 30, 30);
			case MAP_POINT_TYPE_ARROW_UP:
				return tileset.getSubimage(0, 0, 30, 30);
			default: throw new IllegalStateException("ImageName not configured for mapPointType" + this.mapPointType);
		}
	}

	private String getImageName() {
		switch(this.mapPointType) {
			case MAP_POINT_TYPE_ARROW_DOWN:
			case MAP_POINT_TYPE_ARROW_LEFT:
			case MAP_POINT_TYPE_ARROW_RIGHT:
			case MAP_POINT_TYPE_ARROW_UP:
				return "/Tiles/arrows.png";
			default: throw new IllegalStateException("ImageName not configured for mapPointType" + this.mapPointType);
		}
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
	
	public void setMapPosition(int xMap, int yMap) {
		this.xMap = xMap;
		this.yMap = yMap;
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
			(int)(x + xMap - this.image.getWidth() / 2),
			(int)(y + yMap - this.image.getHeight() / 2),
				null
			);

		drawDebugRectangle(g);
	}

	public Rectangle getRectangle() {
		return new Rectangle(
				(int)(x + xMap - width / 2),
				(int)(y + yMap - height / 2),
				width,
				height);
	}

	private void drawDebugRectangle(Graphics2D g) {
		if(GameOptions.IS_DEBUG_MODE == false)
			return;

		g.setColor(Color.RED);
		var mapPointRectangle = getRectangle();
		g.drawRect(mapPointRectangle.x,
				mapPointRectangle.y,
				mapPointRectangle.width,
				mapPointRectangle.height
		);
	}

}
