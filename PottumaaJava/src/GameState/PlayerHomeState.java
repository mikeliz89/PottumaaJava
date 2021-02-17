package GameState;

import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import Entity.NPCs.MrPotatoGuy;
import Entity.Obstacles.House;
import Main.GamePanel;
import MapPoint.MapPoint;
import TileMap.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerHomeState extends BaseLevel {

	private Background bg;

	// TODO Piirrä talon sisätilat ja vaihda oikea tiedosto tähän.
	public PlayerHomeState(GameStateManager gsm, int playerStartingPositionX, int playerStartingPositionY) {
		super(gsm, playerStartingPositionX, playerStartingPositionY,
				"/Tilesets/grasstileset.png",
				"/Tilesets/obstacles.png",
				"/Maps/map1.csv",
				"/Maps/map1_obstacles.csv",
				"/Music/happymusic.wav"
				);
	}

	@Override
	protected void populateMapPoints() {
		try {
			BufferedImage tileset = ImageIO.read(
					getClass().getResourceAsStream("/Tiles/arrows.png")
			);

			BufferedImage downArrow =  tileset.getSubimage(60, 0, 30, 30);
			MapPoint mapPoint = new MapPoint(downArrow);
			mapPoint.setPosition(300, 300);
			mapPoint.setGotoLevel(GameStateManager.LEVEL1STATE);
			mapPoints.add(mapPoint);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}












