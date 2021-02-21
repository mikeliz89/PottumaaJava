package GameState.Levels;

import GameState.*;
import MapPoint.MapPoint;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class PlayerHomeState extends BaseLevel {

	// TODO Piirrä talon sisätilat ja vaihda oikea tiedosto tähän.
	public PlayerHomeState(GameStateManager gsm, int previousState) {
		super(gsm,
				"/Tilesets/grasstileset.png",
				"/Tilesets/obstacles.png",
				"/Maps/map3.csv",
				"/Maps/map1_obstacles.csv",
				"/Music/happymusic.wav"
				);

		player.setPosition(300, 300);
	}

	@Override
	protected void populateMapPoints() {
		MapPoint mapPoint = new MapPoint(MapPoint.MAP_POINT_TYPE_ARROW_DOWN, "/SFX/homeDoorClose.wav");
		mapPoint.setPosition(300, 300);
		mapPoint.setGotoLevel(GameStateManager.STATE_LEVEL_1);
		mapPoints.add(mapPoint);
	}

}












