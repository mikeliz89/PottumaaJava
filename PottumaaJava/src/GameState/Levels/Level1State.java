package GameState.Levels;

import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import Entity.Items.Item;
import Entity.NPCs.MrPotatoGuy;
import Entity.Obstacles.Obstacle;
import GameState.*;
import MapPoint.MapPoint;
import java.awt.*;

public class Level1State extends BaseLevel {

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
		var banana =  new Item("Banana",
				"/Images/Items/banana.png");
		banana.setPosition(new Point(300, 300));
		addItem(banana);
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
		var playerHome = new Obstacle(tileMaps, 180, 120, "/Images/Obstacles/playerHome.gif");
		playerHome.setPosition(527 - 180, 247 - 120);
		playerHome.setCollisionBoxHeight(playerHome.getHeight() - 10);
		playerHome.setCollisionBoxWidth(playerHome.getWidth() - 40);

		var aita = new Obstacle(tileMaps, 70, 35, "/Images/Obstacles/aita.gif");
		aita.setPosition(240, 220);
		aita.setCollisionBoxHeight(aita.getHeight());
		aita.setCollisionBoxWidth(aita.getWidth());

		var aita2 = new Obstacle(tileMaps, 70, 35, "/Images/Obstacles/aita.gif");
		aita2.setPosition(310, 220);
		aita2.setCollisionBoxHeight(aita2.getHeight());
		aita2.setCollisionBoxWidth(aita2.getWidth());

		addObstacle(playerHome);
		addObstacle(aita);
		addObstacle(aita2);
	}

	@Override
	protected void populateMapPoints() {
		var level2 = new MapPoint(MapPoint.MAP_POINT_TYPE_ARROW_RIGHT, "");
		level2.setPosition(885, 585);
		level2.setGotoLevel(GameStateManager.STATE_LEVEL_2);
		addMapPoint(level2);

		var playerHome = new MapPoint(MapPoint.MAP_POINT_TYPE_ARROW_UP, "/SFX/homeDoorOpen.wav");
		playerHome.setPosition(345, 195);
		playerHome.setGotoLevel(GameStateManager.STATE_PLAYER_HOME);
		addMapPoint(playerHome);
	}

	@Override
	protected void populateNPCs() {
		MrPotatoGuy mrPotatoGuy;
		Point[] points = new Point [] {
				new Point(800, 300)
		};
		for(Point npcPoint : points) {
			mrPotatoGuy = new MrPotatoGuy(tileMaps, getObstacles(), 1000);
			mrPotatoGuy.setPosition(npcPoint.x, npcPoint.y);
			addNPC(mrPotatoGuy);
		}
	}

	@Override
	protected void populateEnemies() {
		var enemyFactory = new EnemyFactory(tileMaps, getObstacles());
		Point[] sluggerPoints = new Point[] {
				new Point(130, 50),
				new Point(330, 100),
				new Point(250, 100),
				new Point(700, 100),
				new Point(100, 500),
		};
		for (Point sluggerPoint : sluggerPoints) {
			var slugger = enemyFactory.getEnemy(EnemySettings.ENEMY_TYPES_SLUGGER,
					sluggerPoint.x,
					sluggerPoint.y,
					EnemySettings.SLUGGER_MAX_HEALTH);
			addEnemy(slugger);
		}
		//hämähäkki
		var arachnid = enemyFactory.getEnemy(EnemySettings.ENEMY_TYPES_ARACHNID,
				400, 400,
				EnemySettings.ARACHNID_MAX_HEALTH);
		addEnemy(arachnid);
	}

}












