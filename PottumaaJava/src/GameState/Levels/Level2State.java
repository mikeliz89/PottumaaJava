package GameState.Levels;

import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import GameState.*;
import MapPoint.MapPoint;
import java.awt.*;

public class Level2State extends BaseLevel {

	public Level2State(GameStateManager gsm, int previousState) {
		super(gsm,"/Tilesets/grasstileset.png",
				"/Tilesets/obstacles.png",
				"/Maps/map2.csv",
				"/Maps/map2_obstacles.csv",
				"/Music/happymusic.wav");
		setPlayerStartingPosition(previousState);
	}

	private void setPlayerStartingPosition(int previousState) {
		player.setPosition(40, 575);
	}

	@Override
	protected void populateMapPoints() {
		MapPoint mapPoint = new MapPoint(MapPoint.MAP_POINT_TYPE_ARROW_LEFT, "");
		mapPoint.setPosition(mapPoint.getImage().getWidth() / 2, 585);
		mapPoint.setGotoLevel(GameStateManager.STATE_LEVEL_1);
		addMapPoint(mapPoint);
	}

	@Override
	protected void populateEnemies() {
		var enemyFactory = new EnemyFactory(tileMaps, getObstacles());
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

	@Override
	protected void populateNPCs() {

	}

}












