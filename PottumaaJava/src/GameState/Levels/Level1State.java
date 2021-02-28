package GameState.Levels;

import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import Entity.Items.Item;
import Entity.NPCs.MrPotatoGuy;
import Entity.Obstacles.Obstacle;
import GameState.*;
import MapPoint.MapPoint;
import java.awt.*;
import java.util.ArrayList;

public class Level1State extends BaseLevel {

	ArrayList<Item> items;
	private Obstacle playerHome;
	public Level1State(GameStateManager gsm, int previousState) {
		super(gsm,
				"/Tilesets/grasstileset.png",
				"/Tilesets/obstacles.png",
				"/Maps/map1.csv",
				"/Maps/map1_obstacles.csv",
				"/Music/bensound-ukulele.wav"
				);
		changeBackgroundMusic(previousState);
		setPlayerStartingPosition(previousState);
		createObstacles();
		createItems();
	}

	private void createItems() {
		items = new ArrayList<>();
		var banana =  new Item("Banana",
				"/Images/Items/banana.png");
		banana.setPosition(new Point(300, 300));
		items.add(banana);
	}

	private void changeBackgroundMusic(int previousState) {
		if(previousState == GameStateManager.STATE_NEW_GAME)
			backgroundMusicShouldChange = true;
	}

	private void setPlayerStartingPosition(int previousState) {
		if(previousState == GameStateManager.STATE_LEVEL_2) {
			player.setPosition(845, 585);
			return;
		}
		player.setPosition(345, 200);
	}

	private void createObstacles() {
		playerHome = new Obstacle(tileMaps, 180, 120, "/Sprites/House/obstacles-new-house.gif");
		playerHome.setPosition(527 - 180, 247 - 120);
		playerHome.setCollisionBoxHeight(playerHome.getHeight() - 10);
		playerHome.setCollisionBoxWidth(playerHome.getWidth() - 40);
		addObstacle(playerHome);
	}

	@Override
	protected void populateMapPoints() {
		var level2 = new MapPoint(MapPoint.MAP_POINT_TYPE_ARROW_RIGHT, "");
		level2.setPosition(885, 585);
		level2.setGotoLevel(GameStateManager.STATE_LEVEL_2);
		mapPoints.add(level2);

		var playerHome = new MapPoint(MapPoint.MAP_POINT_TYPE_ARROW_UP, "/SFX/homeDoorOpen.wav");
		playerHome.setPosition(345, 195);
		playerHome.setGotoLevel(GameStateManager.STATE_PLAYER_HOME);
		mapPoints.add(playerHome);
	}

	@Override
	protected void populateNPCs() {
		MrPotatoGuy mrPotatoGuy;
		Point[] points = new Point [] {
				new Point(300, 300)
		};
		for(Point npcPoint : points) {
			mrPotatoGuy = new MrPotatoGuy(tileMaps, obstacles, 1000);
			mrPotatoGuy.setPosition(npcPoint.x, npcPoint.y);
			NPCs.add(mrPotatoGuy);
		}
	}

	@Override
	protected void populateEnemies() {
		var enemyFactory = new EnemyFactory(tileMaps, obstacles);
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
	protected void drawObstacles(Graphics2D g) {
		playerHome.draw(g);

		drawItems(g);
	}

	private void drawItems(Graphics2D g) {
		for(Item item : items) {
			item.draw(g);
		}
	}

}












