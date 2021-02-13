package GameState;

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

public class Level1State extends GameState {
	
	private ArrayList<TileMap> tileMaps;
//	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private ArrayList<MapPoint> mapPoints;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	private ArrayList<Integer> keysPressed;
	
//	private Tile level2Point;
//	private Tile level3Point;
	
	private double tileMapTween = 0.11;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		populateTileMaps();
		
//		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		
		populateMapPoints();
		
		player = new Player(tileMaps);
		player.setPosition(40, 100);
		
		populateEnemies(tileMaps);
		
		explosions = new ArrayList<Explosion>();
		
		keysPressed = new ArrayList<Integer>();
		
		hud = new HUD(player);
		
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
			bgMusic.play();
		
	}
	
	private void populateTileMaps() { 
		tileMaps = new ArrayList<TileMap>();
		
		// tiles: ground
		TileMap tileMapGround = new TileMap(30);
		tileMapGround.loadTiles("/Tilesets/grasstileset3.png", false);
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
		mapPoints = new ArrayList<MapPoint>();
		try { 
			BufferedImage tileset = ImageIO.read(
				getClass().getResourceAsStream("/Tiles/arrows.png")
			);
			
			BufferedImage upArrow =  tileset.getSubimage(0, 0, 30, 30);
			BufferedImage rightArrow =  tileset.getSubimage(30, 0, 30, 30);
			BufferedImage downArrow =  tileset.getSubimage(60, 0, 30, 30);
			BufferedImage leftArrow =  tileset.getSubimage(90, 0, 30, 30);
			
			MapPoint level2Point = new MapPoint(rightArrow);
			level2Point.setPosition(885, 585);
			level2Point.setGotoLevel(GameStateManager.LEVEL2STATE);
			MapPoint level3Point = new MapPoint(downArrow);
			level3Point.setPosition(250, 250);
			level3Point.setGotoLevel(GameStateManager.MENUSTATE);
			
			mapPoints.add(level2Point);
			mapPoints.add(level3Point);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void populateEnemies(ArrayList<TileMap> tileMaps) {
		
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] sluggerPoints = new Point[] {
			new Point(130, 50),
			new Point(330, 100),
			new Point(250, 100),
			new Point(700, 100),
			new Point(100, 500),
		};
		for(int i = 0; i < sluggerPoints.length; i++) {
			s = new Slugger(tileMaps, EnemySettings.SLUGGER_MAX_HEALTH);
			s.setPosition(sluggerPoints[i].x, sluggerPoints[i].y);
			enemies.add(s);
		}
		
	}
	
	public void update() {
		
		// update player
		player.update();
		for(int i = 0; i < mapPoints.size(); i++) { 
			player.checkLevelPoints(mapPoints.get(i), gsm);
		}
		
		for(int i = 0; i < tileMaps.size(); i++) {
			
			TileMap tm = tileMaps.get(i);
			
			tm.setPosition(
					GamePanel.WIDTH / 2 - player.getx(),
					GamePanel.HEIGHT / 2 - player.gety()
			);
		}
		
		// set background
//		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		player.checkAttack(enemies);
		
		// update all enemies
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
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
//		bg.draw(g);
		
		// draw tileMaps
		for(int i = 0; i < tileMaps.size(); i++) {
			TileMap tm = tileMaps.get(i);
			tm.draw(g);
		}
		
		// draw mapPoints
		for(int i = 0; i < mapPoints.size(); i++) { 
			MapPoint mp = mapPoints.get(i);
			//use only tileMap[0](ground tiles)
			mp.setMapPosition((int)tileMaps.get(0).getx(), (int)tileMaps.get(0).gety());
			mp.draw(g);
		}
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)tileMaps.get(0).getx(), (int)tileMaps.get(0).gety());
			explosions.get(i).draw(g);
		}
		
		// draw hud
		hud.draw(g);
		
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
	
}












