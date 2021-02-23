package GameState.Levels;

import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import Entity.NPCs.MrPotatoGuy;
import Entity.Obstacles.House;
import GameState.*;
import MapPoint.MapPoint;
import java.awt.*;

public class Level1State extends BaseLevel {

	private House playerHome;
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
	}

	private void changeBackgroundMusic(int previousState) {
		if(previousState == GameStateManager.STATE_NEW_GAME)
			backgroundMusicShouldChange = true;
	}

	private void setPlayerStartingPosition(int previousState) {
		if(previousState == GameStateManager.STATE_LEVEL_2) {
			player.setPosition(880, 585);
			return;
		}
		player.setPosition(345, 200);
	}

	@Override
	protected void populateObstacles() {
		playerHome = new House(tileMaps);
		playerHome.setPosition(527 - 180, 247 - 120);
		obstacles.add(playerHome);
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
	protected void drawObstacles(Graphics2D g) {
		playerHome.draw(g);
	}
	
}












