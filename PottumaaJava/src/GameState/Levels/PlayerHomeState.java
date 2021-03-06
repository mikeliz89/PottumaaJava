package GameState.Levels;

import GameState.*;
import MapPoint.MapPoint;

public class PlayerHomeState extends BaseLevel {

	public PlayerHomeState(GameStateManager gsm, int previousState) {
		super(gsm,
				"/Tilesets/small-house.png",
				"/Tilesets/obstacles.png",
				"/Maps/map3.csv",
				"/Maps/map3_obstacles.csv",
				"/Music/happymusic.wav"
				);

		player.setPosition(450, 480);
	}

	@Override
	protected void populateMapPoints() {
		MapPoint mapPoint = new MapPoint(MapPoint.MAP_POINT_TYPE_ARROW_DOWN, "/SFX/homeDoorClose.wav");
		mapPoint.setPosition(450, 490);
		mapPoint.setGotoLevel(GameStateManager.STATE_LEVEL_1);
		addMapPoint(mapPoint);
	}

	@Override
	protected void populateEnemies() {

	}

	@Override
	protected void populateNPCs() {

	}

	@Override
	protected void populateItems() {

	}

	@Override
	protected void populateObstacles() {

	}

}












