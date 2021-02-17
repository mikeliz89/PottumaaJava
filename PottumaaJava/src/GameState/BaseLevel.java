package GameState;

import Audio.AudioPlayer;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD.HUD;
import Entity.NPC;
import Entity.Player.Player;
import Main.GameOptions;
import Main.GamePanel;
import MapPoint.MapPoint;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class BaseLevel extends GameState  {
    protected ArrayList<TileMap> tileMaps;
    private Player player;
    protected ArrayList<Enemy> enemies;
    protected ArrayList<NPC> NPCs;
    private ArrayList<Explosion> explosions;
    protected ArrayList<MapPoint> mapPoints;
    private HUD hud;
    private AudioPlayer bgMusic;
    private ArrayList<Integer> keysPressed;

    //muuttuvat
    private String groundTileSetName;
    private String obstacleTileSetName;
    private String groundTileMapName;
    private String obstacleTileMapName;
    private String bgMusicSoundFileName;

    public BaseLevel(GameStateManager gameStateManager, int playerStartingPositionX, int playerStartingPositionY,
                     String groundTileSetName, String obstacleTileSetName,
                     String groundTileMapName, String obstacleTileMapName,
                     String bgMusicSoundFileName) {
        this.gsm = gameStateManager;
        this.groundTileSetName = groundTileSetName;
        this.obstacleTileSetName = obstacleTileSetName;
        this.groundTileMapName = groundTileMapName;
        this.obstacleTileMapName = obstacleTileMapName;
        this.bgMusicSoundFileName = bgMusicSoundFileName;
        init(playerStartingPositionX, playerStartingPositionY);
    }

    public void init() {

    }

    public void init(int playerStartingPositionX, int playerStartingPositionY) {

        mapPoints = new ArrayList<>();
        explosions = new ArrayList<>();
        keysPressed = new ArrayList<>();
        enemies = new ArrayList<>();
        NPCs = new ArrayList<>();

        populateTileMaps();
        populateMapPoints();

        createPlayer(playerStartingPositionX, playerStartingPositionY);

        populateEnemies();
        populateNPCs();
        populateObstacles();

        //Note: Hud has to be created after Player!
        hud = new HUD(player);

        playMusic();
    }

    private void createPlayer(int playerStartingPositionX, int playerStartingPositionY) {
        player = new Player(tileMaps);
        player.setPosition(playerStartingPositionX, playerStartingPositionY);
    }

    private void playMusic() {
        if(!GameOptions.IS_PLAY_MUSIC_ON)
            return;

        bgMusic = new AudioPlayer(bgMusicSoundFileName);
        bgMusic.play();
    }

    private void populateTileMaps() {
        tileMaps = new ArrayList<>();
        double tileMapTween = 0.11;

        // tiles: ground
        TileMap tileMapGround = new TileMap(30);
        tileMapGround.loadTiles(groundTileSetName, false);
        tileMapGround.loadMap(groundTileMapName);
        tileMapGround.setPosition(0, 0);
        tileMapGround.setType(TileMap.GROUND);
        tileMapGround.setTween(tileMapTween);

        // tiles: obstacles
        TileMap tileMapObstacles = new TileMap(30);
        tileMapObstacles.loadTiles(obstacleTileSetName, true);
        tileMapObstacles.loadMap(obstacleTileMapName);
        tileMapObstacles.setPosition(0, 0);
        tileMapObstacles.setType(TileMap.OBSTACLE);
        tileMapObstacles.setTween(tileMapTween);

        tileMaps.add(tileMapGround);
        tileMaps.add(tileMapObstacles);
    }

    protected void populateMapPoints() {}

    protected void populateObstacles() {}

    protected void populateEnemies() {}

    protected void populateNPCs() {}

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
                updateQuests(e);
                givePlayerRewards(e);
                enemies.remove(i);
                i--;
                explosions.add(
                        new Explosion(e.getX(), e.getY()));
            }
        }
    }

    private void updateQuests(Enemy e) {
        hud.KillOneEnemy(e.getEnemyType());
    }

    private void givePlayerRewards(Enemy e) {
        player.addExperience(e.getExperienceGainedWhenKilled());
        player.addMoney(e.getMoneyGainedWhenKilled());
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

    protected void DrawObstacles(Graphics2D g) {}

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
        if(k == KeyEvent.VK_R) player.setScratching();
        if(k == KeyEvent.VK_F) player.setFiring();

        if(k == KeyEvent.VK_I) hud.ToggleInventory();
        if(k == KeyEvent.VK_M) hud.ToggleMap();
        if(k == KeyEvent.VK_J) hud.ToggleDialogBox();
        if(k == KeyEvent.VK_Q) hud.ToggleQuestLog();

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

        // remove released keys from keysPressed arrayList
        if(keysPressed.contains(k) == true) {
            keysPressed.remove(keysPressed.indexOf(k));
        }

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
