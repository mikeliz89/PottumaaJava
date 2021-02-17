package GameState.Levels;

import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import GameState.*;
import MapPoint.MapPoint;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

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
		var enemyFactory = new EnemyFactory(tileMaps);
		Point[] sluggerPoints = new Point[] {
				new Point(130, 50),
				new Point(700, 700),
				new Point(330, 200),
				new Point(250, 400),
				new Point(100, 500),
		};
		for (Point sluggerPoint : sluggerPoints) {
			//little bit tougher enemy in level 2
			var s = enemyFactory.getEnemy(EnemySettings.ENEMY_TYPES_SLUGGER,
					sluggerPoint.x,
					sluggerPoint.y,
					EnemySettings.SLUGGER_MAX_HEALTH + 2); //tougher slugger in level 2
			enemies.add(s);
		}
	}
	
}












