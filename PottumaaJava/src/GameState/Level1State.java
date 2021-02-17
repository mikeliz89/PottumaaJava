package GameState;

import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import Entity.NPCs.MrPotatoGuy;
import Entity.Obstacles.House;
import MapPoint.MapPoint;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Level1State extends BaseLevel {

	private House playerHome;
	public Level1State(GameStateManager gsm, int playerStartingPositionX, int playerStartingPositionY) {
		super(gsm, playerStartingPositionX, playerStartingPositionY,
				"/Tilesets/grasstileset.png",
				"/Tilesets/obstacles.png",
				"/Maps/map1.csv",
				"/Maps/map1_obstacles.csv",
				"/Music/happymusic.wav"
				);
	}

	@Override
	protected void populateObstacles() {

		playerHome = new House(tileMaps);
		playerHome.setPosition(527 - 180, 247 - 120);
	}

	@Override
	protected void populateMapPoints() {
		try {
			BufferedImage tileset = ImageIO.read(
					getClass().getResourceAsStream("/Tiles/arrows.png")
			);

			BufferedImage rightArrow =  tileset.getSubimage(30, 0, 30, 30);
			MapPoint mapPoint = new MapPoint(rightArrow);
			mapPoint.setPosition(885, 585);
			mapPoint.setGotoLevel(GameStateManager.LEVEL2STATE);
			mapPoints.add(mapPoint);

			BufferedImage upArrow =  tileset.getSubimage(0, 0, 30, 30);
			MapPoint mapPoint2 = new MapPoint(upArrow);
			mapPoint2.setPosition(345, 195);
			mapPoint2.setGotoLevel(GameStateManager.INSIDEHOUSE);
			mapPoints.add(mapPoint2);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void populateNPCs() {
		MrPotatoGuy mr;
		Point[] points = new Point [] {
				new Point(300, 300)
		};
		for(Point npcPoint : points) {
			mr = new MrPotatoGuy(tileMaps, 1000);
			mr.setPosition(npcPoint.x, npcPoint.y);
			NPCs.add(mr);
		}
	}

	@Override
	protected void populateEnemies() {
		var enemyFactory = new EnemyFactory(tileMaps);
		Point[] sluggerPoints = new Point[] {
				new Point(130, 50),
				new Point(330, 100),
				new Point(250, 100),
				new Point(700, 100),
				new Point(100, 500),
		};
		for (Point sluggerPoint : sluggerPoints) {
			var s = enemyFactory.getEnemy(EnemySettings.ENEMY_TYPES_SLUGGER,
					sluggerPoint.x,
					sluggerPoint.y,
					EnemySettings.SLUGGER_MAX_HEALTH);
			enemies.add(s);
		}
		//hämähäkki
		var a = enemyFactory.getEnemy(EnemySettings.ENEMY_TYPES_ARACHNID,
				400, 400,
				EnemySettings.ARACHNID_MAX_HEALTH);
		enemies.add(a);
	}

	@Override
	protected void DrawObstacles(Graphics2D g) {
		playerHome.draw(g);
	}
	
}












