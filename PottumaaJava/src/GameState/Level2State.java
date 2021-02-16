package GameState;

import Entity.Enemies.EnemySettings;
import Entity.Enemies.Slugger;
import TileMap.MapPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level2State extends BaseLevel {

	public Level2State(GameStateManager gsm, int playerStartingPositionX, int playerStartingPositionY) {
		super(gsm, playerStartingPositionX, playerStartingPositionY,
				"/Tilesets/grasstileset.png",
				"/Tilesets/obstacles.png",
				"/Maps/map2.csv",
				"/Maps/map2_obstacles.csv",
				"/Music/happymusic.wav");
	}

	@Override
	protected void populateMapPoints() {
		try {
			BufferedImage tileset = ImageIO.read(
					getClass().getResourceAsStream("/Tiles/arrows.png")
			);

			BufferedImage leftArrow =  tileset.getSubimage(90, 0, 30, 30);
			MapPoint mapPoint = new MapPoint(leftArrow);
			mapPoint.setPosition(leftArrow.getWidth() / 2, 585);
			mapPoint.setGotoLevel(GameStateManager.LEVEL1STATE);
			mapPoints.add(mapPoint);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void populateEnemies() {
		Slugger s;
		Point[] sluggerPoints = new Point[] {
				new Point(130, 50),
				new Point(700, 700),
				new Point(330, 200),
				new Point(250, 400),
				new Point(100, 500),
		};
		for (Point sluggerPoint : sluggerPoints) {
			//little bit tougher enemy in level 2
			s = new Slugger(tileMaps, EnemySettings.SLUGGER_MAX_HEALTH + 3);
			s.setPosition(sluggerPoint.x, sluggerPoint.y);
			enemies.add(s);
		}
	}
	
}












