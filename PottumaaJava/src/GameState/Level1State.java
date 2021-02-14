package GameState;

import Entity.NPCs.MrPotatoGuy;
import Entity.Obstacles.House;
import Entity.Player.Player;
import Main.GameOptions;
import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Level1State extends GameState {
	
	private ArrayList<TileMap> tileMaps;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<NPC> NPCs;
	private ArrayList<Explosion> explosions;
	private ArrayList<MapPoint> mapPoints;
	private HUD hud;
	private AudioPlayer bgMusic;
	private ArrayList<Integer> keysPressed;
	private House playerHome;

	public Level1State(GameStateManager gsm, int PlayerStartingPositionX, int PlayerStartingPositionY) {
		this.gsm = gsm;
		init(PlayerStartingPositionX, PlayerStartingPositionY);
	}

	public void init() {

	}
	
	public void init(int PlayerStartingPositionX, int PlayerStartingPositionY) {
		
		populateTileMaps();
		
		populateMapPoints();
		
		player = new Player(tileMaps);
		player.setPosition(PlayerStartingPositionX, PlayerStartingPositionY);
		
		populateEnemies();
		populateNPCs();
		populateObstacles();
		
		explosions = new ArrayList<>();
		keysPressed = new ArrayList<>();
		hud = new HUD(player);

		playMusic();
	}

	private void playMusic() {

		if(!GameOptions.IS_PLAY_MUSIC_ON)
			return;

		bgMusic = new AudioPlayer("/Music/happymusic.wav");
		bgMusic.play();
	}
	
	private void populateTileMaps() { 
		tileMaps = new ArrayList<>();
		double tileMapTween = 0.11;
		
		// tiles: ground
		TileMap tileMapGround = new TileMap(30);
		tileMapGround.loadTiles("/Tilesets/grasstileset.png", false);
		tileMapGround.loadMap("/Maps/map1.csv");
		tileMapGround.setPosition(0, 0);
		tileMapGround.setType(TileMap.GROUND);
		tileMapGround.setTween(tileMapTween);
		
		// tiles: obstacles
		TileMap tileMapObstacles = new TileMap(30);
		tileMapObstacles.loadTiles("/Tilesets/obstacles.png", true);
		tileMapObstacles.loadMap("/Maps/map1_obstacles.csv");
		tileMapObstacles.setPosition(0, 0);
		tileMapObstacles.setType(TileMap.OBSTACLE);
		tileMapObstacles.setTween(tileMapTween);
		
		tileMaps.add(tileMapGround);
		tileMaps.add(tileMapObstacles);
	}
	
	private void populateMapPoints() {
		mapPoints = new ArrayList<>();
		try { 
			BufferedImage tileset = ImageIO.read(
				getClass().getResourceAsStream("/Tiles/arrows.png")
			);

			BufferedImage rightArrow =  tileset.getSubimage(30, 0, 30, 30);
			//BufferedImage upArrow =  tileset.getSubimage(0, 0, 30, 30);
			//BufferedImage downArrow =  tileset.getSubimage(60, 0, 30, 30);
			//BufferedImage leftArrow =  tileset.getSubimage(90, 0, 30, 30);
			
			MapPoint level2Point = new MapPoint(rightArrow);
			level2Point.setPosition(885, 585);
			level2Point.setGotoLevel(GameStateManager.LEVEL2STATE);
			mapPoints.add(level2Point);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void populateObstacles() {
		playerHome = new House(tileMaps);
		playerHome.setPosition(500, 260);
	}
	
	private void populateEnemies() {
		
		enemies = new ArrayList<>();
		
		Slugger s;
		Point[] sluggerPoints = new Point[] {
			new Point(130, 50),
			new Point(330, 100),
			new Point(250, 100),
			new Point(700, 100),
			new Point(100, 500),
		};
		for (Point sluggerPoint : sluggerPoints) {
			s = new Slugger(tileMaps, EnemySettings.SLUGGER_MAX_HEALTH);
			s.setPosition(sluggerPoint.x, sluggerPoint.y);
			enemies.add(s);
		}
		
	}

	private void populateNPCs() {
		NPCs = new ArrayList<>();

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
	
	public void update() {
		
		// update player
		player.update();

		for (MapPoint mapPoint : mapPoints) {
			player.checkLevelPoints(mapPoint, gsm);
		}

		for (TileMap tm : tileMaps) {
			tm.setPosition(
					GamePanel.WIDTH / 2 - player.getx(),
					GamePanel.HEIGHT / 2 - player.gety()
			);
		}
		
		// attack enemies
		player.checkAttack(enemies);

		UpdateEnemies();

		UpdateNPCs();

		UpdateExplosions();
	}

	private void UpdateNPCs() {
		for (NPC npc : NPCs) {
			npc.update();
		}
	}

	private void UpdateEnemies() {
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(
					new Explosion(e.getx(), e.gety()));
			}
		}
	}

	private void UpdateExplosions() {
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	}

	public void draw(Graphics2D g) {

		DrawTileMaps(g);

		DrawMapPoints(g);

		DrawObstacles(g);

		DrawNPCs(g);

		DrawPlayer(g);

		DrawEnemies(g);

		DrawExplosions(g);

		DrawHUD(g);
	}

	private void DrawObstacles(Graphics2D g) {
		playerHome.draw(g);
	}

	private void DrawHUD(Graphics2D g) {
		hud.draw(g);
	}

	private void DrawExplosions(Graphics2D g) {
		for (Explosion explosion : explosions) {
			explosion.setMapPosition(
					(int) tileMaps.get(0).getx(), (int) tileMaps.get(0).gety());
			explosion.draw(g);
		}
	}

	private void DrawEnemies(Graphics2D g) {
		for (Enemy enemy : enemies) {
			enemy.draw(g);
		}
	}

	private void DrawNPCs(Graphics2D g) {
		for(NPC npc : NPCs) {
			npc.draw(g);
		}
	}

	private void DrawPlayer(Graphics2D g) {
		player.draw(g);
	}

	private void DrawMapPoints(Graphics2D g) {
		for (MapPoint mp : mapPoints) {
			//use only tileMap[0](ground tiles)
			mp.setMapPosition((int) tileMaps.get(0).getx(), (int) tileMaps.get(0).gety());
			mp.draw(g);
		}
	}

	private void DrawTileMaps(Graphics2D g) {
		for (TileMap tm : tileMaps) {
			tm.draw(g);
		}
	}

	public void keyPressed(int k) {
		if(keysPressed.contains(k) == false) {
			keysPressed.add(k);
		}
		
		//have to press running before can charge
		if(keysPressed.contains(KeyEvent.VK_LEFT) == true ||
				keysPressed.contains(KeyEvent.VK_RIGHT) == true ||
				keysPressed.contains(KeyEvent.VK_UP) == true || 
				keysPressed.contains(KeyEvent.VK_DOWN) == true) {
			if(keysPressed.contains(KeyEvent.VK_SHIFT)) {
				player.setCharging(true);
			}
		}
		
		if(k == KeyEvent.VK_LEFT) { 
			player.setLeft(true);
		}
		if(k == KeyEvent.VK_RIGHT) { 
			if(keysPressed.contains(KeyEvent.VK_RIGHT) == true) {
				if(k == KeyEvent.VK_SHIFT) {
					player.setCharging(true);
				}
			}
			player.setRight(true);
		}
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
//		if(k == KeyEvent.VK_W) player.setJumping(true);
//		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching();
		if(k == KeyEvent.VK_F) player.setFiring();

		if(k == KeyEvent.VK_I) hud.ToggleInventory();
	}
	
	public void keyReleased(int k) {
		
		if(k == KeyEvent.VK_SHIFT) {
			player.setCharging(false);
		}
		if(k == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if(k == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
//		if(k == KeyEvent.VK_W) player.setJumping(false);
//		if(k == KeyEvent.VK_E) player.setGliding(false);
		
		// remove released keys from keysPressed arrayList
		if(keysPressed.contains(k) == true) {
			keysPressed.remove(keysPressed.indexOf(k));
		}
		
	}

	public void stopBackGroundMusic() {
		bgMusic.stop();
	}
	
}












