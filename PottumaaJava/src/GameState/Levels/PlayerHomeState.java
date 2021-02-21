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
		try {
			BufferedImage tileset = ImageIO.read(
					getClass().getResourceAsStream("/Tiles/arrows.png")
			);

			BufferedImage downArrow =  tileset.getSubimage(60, 0, 30, 30);
			MapPoint mapPoint = new MapPoint(downArrow, "/SFX/homeDoorClose.wav");
			mapPoint.setPosition(300, 300);
			mapPoint.setGotoLevel(GameStateManager.STATE_LEVEL_1);
			mapPoints.add(mapPoint);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}












