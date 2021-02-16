package GameState;

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

import javax.imageio.ImageIO;

public class Level2State extends GameState {
	
	private ArrayList<TileMap> tileMaps;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private ArrayList<MapPoint> mapPoints;
	private HUD hud;
	private AudioPlayer bgMusic;
	private ArrayList<Integer> keysPressed;
	
	public Level2State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		populateTileMaps();

		populateMapPoints();

		player = new Player(tileMaps);
		player.setPosition(30, 575);
		
		populateEnemies(tileMaps);
		
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
	
	private void populateMapPoints() {
		mapPoints = new ArrayList<>();
		try { 
			BufferedImage tileset = ImageIO.read(
				getClass().getResourceAsStream("/Tiles/arrows.png")
			);

			BufferedImage leftArrow =  tileset.getSubimage(90, 0, 30, 30);
			//BufferedImage upArrow =  tileset.getSubimage(0, 0, 30, 30);
			//BufferedImage rightArrow =  tileset.getSubimage(30, 0, 30, 30);
			//BufferedImage downArrow =  tileset.getSubimage(60, 0, 30, 30);

			MapPoint level1Point = new MapPoint(leftArrow);
			level1Point.setPosition(leftArrow.getWidth() / 2, 585);
			level1Point.setGotoLevel(GameStateManager.LEVEL1STATE);
			mapPoints.add(level1Point);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void populateTileMaps() {
		tileMaps = new ArrayList<>();
		
		// tiles: ground
		TileMap tileMapGround = new TileMap(30);
		tileMapGround.loadTiles("/Tilesets/grasstileset.png", false);
		tileMapGround.loadMap("/Maps/map2.csv");
		tileMapGround.setPosition(0, 0);
		tileMapGround.setType(TileMap.GROUND);
		tileMapGround.setTween(0.11);
		
		// tiles: obstacles
		TileMap tileMapObstacles = new TileMap(30);
		tileMapObstacles.loadTiles("/Tilesets/obstacles.png", true);
		tileMapObstacles.loadMap("/Maps/map1_obstacles.csv");
		tileMapObstacles.setPosition(0, 0);
		tileMapObstacles.setType(TileMap.OBSTACLE);
		tileMapObstacles.setTween(0.11);
		
		tileMaps.add(tileMapGround);
		tileMaps.add(tileMapObstacles);
	}
	
	private void populateEnemies(ArrayList<TileMap> tileMaps) {
		
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
			//little bit tougher enemy in level 2
			s = new Slugger(tileMaps, EnemySettings.SLUGGER_MAX_HEALTH + 3);
			s.setPosition(sluggerPoint.x, sluggerPoint.y);
			enemies.add(s);
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
					GamePanel.WIDTH / 2 - player.getX(),
					GamePanel.HEIGHT / 2 - player.getY()
			);
		}
		
		// attack enemies
		player.checkAttack(enemies);

		UpdateEnemies();

		UpdateExplosions();
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

	private void UpdateEnemies() {
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				givePlayerRewards(e);
				enemies.remove(i);
				i--;
				explosions.add(
					new Explosion(e.getX(), e.getY()));
			}
		}
	}

	private void givePlayerRewards(Enemy e) {
		player.addExperience(e.getExperienceGainedWhenKilled());
		player.addMoney(e.getMoneyGainedWhenKilled());
	}
	
	public void draw(Graphics2D g) {

		DrawTileMaps(g);

		DrawMapPoints(g);

		DrawPlayer(g);

		DrawEnemies(g);

		DrawExplosions(g);

		DrawHUD(g);
	}

	private void DrawHUD(Graphics2D g) {
		hud.draw(g);
	}

	private void DrawTileMaps(Graphics2D g) {
		for (TileMap tm : tileMaps) {
			tm.draw(g);
		}
	}

	private void DrawMapPoints(Graphics2D g) {
		for (MapPoint mp : mapPoints) {
			//use only tileMap[0](ground tiles)
			mp.setMapPosition((int) tileMaps.get(0).getx(), (int) tileMaps.get(0).gety());
			mp.draw(g);
		}
	}

	private void DrawExplosions(Graphics2D g) {
		for (Explosion explosion : explosions) {
			explosion.setMapPosition(
					(int) tileMaps.get(0).getx(), (int) tileMaps.get(0).gety());
			explosion.draw(g);
		}
	}

	private void DrawEnemies(Graphics2D g) {
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
	}

	private void DrawPlayer(Graphics2D g) {
		player.draw(g);
	}

	public void keyPressed(int k) {
		if(keysPressed.contains(k) == false) {
			keysPressed.add(k);
		}
		
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
		if(k == KeyEvent.VK_M) hud.ToggleMap();
		if(k == KeyEvent.VK_J) hud.ToggleDialogBox();
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
		//NOTE: if next 2 are commented, player keeps going down or up
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
//		if(k == KeyEvent.VK_W) player.setJumping(false);
//		if(k == KeyEvent.VK_E) player.setGliding(false);
		
		// remove released keys from keysPressed arrayList
		if(keysPressed.contains(k) == true) {
			keysPressed.remove(keysPressed.indexOf(k));
		}

		if(k == KeyEvent.VK_F1) {
			var currentVolume = bgMusic.getVolume();
			bgMusic.setVolume(currentVolume-0.1f);
		}
		if(k == KeyEvent.VK_F2) {
			var currentVolume = bgMusic.getVolume();
			bgMusic.setVolume(currentVolume+0.1f);
		}

		if(k == KeyEvent.VK_1) quickTravel(1);
		if(k == KeyEvent.VK_2) quickTravel(2);
		//if(k == KeyEvent.VK_3) quickTravel(3);
		
	}

	private void quickTravel(int levelNumber) {
		if(!GameOptions.ISDEBUGMODE)
			return;

		gsm.setState(levelNumber);
	}

	public void stopBackGroundMusic() {
		if(bgMusic != null)
			bgMusic.stop();
	}
	
}












