package GameState.Levels;

import Entity.Enemies.Enemy;
import Entity.Explosion;
import Entity.HUD.HUD;
import Entity.NPCs.NPC;
import Entity.Obstacles.Obstacle;
import Entity.Player.Player;
import GameState.*;
import Main.GamePanel;
import MapPoint.MapPoint;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class BaseLevel extends GameState  {
    protected ArrayList<Obstacle> obstacles;
    protected ArrayList<TileMap> tileMaps;
    protected Player player;
    protected ArrayList<Enemy> enemies;
    protected ArrayList<NPC> NPCs;
    private ArrayList<Explosion> explosions;
    protected ArrayList<MapPoint> mapPoints;
    private HUD hud;
    private ArrayList<Integer> keysPressed;

    //muuttuvat
    private String groundTileSetName;
    private String obstacleTileSetName;
    private String groundTileMapName;
    private String obstacleTileMapName;
    private String bgMusicSoundFileName;

    public BaseLevel(GameStateManager gameStateManager,
                     String groundTileSetName, String obstacleTileSetName,
                     String groundTileMapName, String obstacleTileMapName,
                     String bgMusicSoundFileName) {
        this.gsm = gameStateManager;
        this.groundTileSetName = groundTileSetName;
        this.obstacleTileSetName = obstacleTileSetName;
        this.groundTileMapName = groundTileMapName;
        this.obstacleTileMapName = obstacleTileMapName;
        this.bgMusicSoundFileName = bgMusicSoundFileName;
        init();
    }

    public void init() {

        gsm.setSongToPlay(bgMusicSoundFileName);

        mapPoints = new ArrayList<>();
        explosions = new ArrayList<>();
        keysPressed = new ArrayList<>();
        enemies = new ArrayList<>();
        NPCs = new ArrayList<>();
        obstacles = new ArrayList<>();

        populateTileMaps();
        populateMapPoints();

        populateEnemies();
        populateNPCs();
        populateObstacles();

        createPlayer();

        //Note: Hud has to be created after Player!
        hud = new HUD(player);
    }

    private void createPlayer() {
        player = new Player(tileMaps, obstacles);
        player.setCurrentLevel(gsm.getCurrentState());
    }

    private void populateTileMaps() {
        tileMaps = new ArrayList<>();
        double tileMapTween = 0.11;

        // tiles: ground
        TileMap tileMapGround = new TileMap(30);
        tileMapGround.loadTiles(groundTileSetName, false);
        tileMapGround.loadMap(groundTileMapName);
        tileMapGround.setPosition(0, 0);
        tileMapGround.setType(Tile.TILE_TYPE_GROUND);
        tileMapGround.setTween(tileMapTween);

        // tiles: obstacles
        TileMap tileMapObstacles = new TileMap(30);
        tileMapObstacles.loadTiles(obstacleTileSetName, true);
        tileMapObstacles.loadMap(obstacleTileMapName);
        tileMapObstacles.setPosition(0, 0);
        tileMapObstacles.setType(Tile.TILE_TYPE_OBSTACLE);
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

        checkMapPointCollisions();

        for (TileMap tm : tileMaps) {
            tm.setPosition(
                    GamePanel.WIDTH / 2 - player.getX(),
                    GamePanel.HEIGHT / 2 - player.getY()
            );
        }

        // attack enemies
        player.checkAttack(enemies);

        updateEnemies();

        updateNPCs();

        updateExplosions();
    }

    private boolean playerIsInMapPoint = false;

    private void checkMapPointCollisions() {
        MapPoint mapPointToChangeTo = null;
        for(MapPoint mapPoint : mapPoints) {
            Rectangle mapPointRectangle = mapPoint.getRectangle();
            Rectangle playerRectangle = player.getRectangle();
            if(playerRectangle.intersects(mapPointRectangle)) {
                mapPointToChangeTo = mapPoint;
            }
        }
        player.setMapPointForLevelChange(mapPointToChangeTo);
        if(mapPointToChangeTo != null) {
            playerIsInMapPoint = true;
            return;
        }
        playerIsInMapPoint = false;
    }

    private void updateNPCs() {
        for (NPC npc : NPCs) {
            npc.update();
        }
    }

    private void updateEnemies() {
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

    private void updateExplosions() {
        for(int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }
    }

    public void draw(Graphics2D g) {

        drawTileMaps(g);

        drawMapPoints(g);

        drawObstacles(g);

        drawNPCs(g);

        drawPlayer(g);

        drawEnemies(g);

        drawExplosions(g);

        drawHUD(g);

        drawInLevelZoneText(g);
    }

    protected void drawObstacles(Graphics2D g) {}

    private void drawHUD(Graphics2D g) {
        hud.draw(g);
    }

    private void drawInLevelZoneText(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.BLUE);
        if(playerIsInMapPoint)
            g.drawString("Press E",
                    player.getX() + (int)player.getXMap(),
                    player.getY() + (int)player.getYMap());
    }

    private void drawExplosions(Graphics2D g) {
        for (Explosion explosion : explosions) {
            TileMap groundMap = getGroundTileMap();
            explosion.setMapPosition((int) groundMap.getX(), (int) groundMap.getY());
            explosion.draw(g);
        }
    }

    private void drawEnemies(Graphics2D g) {
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    private void drawNPCs(Graphics2D g) {
        for(NPC npc : NPCs) {
            npc.draw(g);
        }
    }

    private void drawPlayer(Graphics2D g) {
        player.draw(g);
    }

    private void drawMapPoints(Graphics2D g) {
        for (MapPoint mapPoint : mapPoints) {
            TileMap groundMap = getGroundTileMap();
            mapPoint.setMapPosition((int) groundMap.getX(), (int) groundMap.getY());
            mapPoint.draw(g);
        }
    }

    private TileMap getGroundTileMap() {
        TileMap groundMap = null;
        for(TileMap tileMap : tileMaps) {
            if(tileMap.getType() == Tile.TILE_TYPE_GROUND) {
                groundMap = tileMap;
            }
            break;
        }
        return groundMap;
    }

    private void drawTileMaps(Graphics2D g) {
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
        if(k == KeyEvent.VK_F5) hud.TogglePauseMenu(); //F5 = Tallentaa pelin

        if(k == KeyEvent.VK_1) quickTravel(1);
        if(k == KeyEvent.VK_2) quickTravel(2);
        if(k == KeyEvent.VK_3) quickTravel(6);

        if(playerIsInMapPoint) {
            if(k == KeyEvent.VK_E) {
                player.changeLevel(gsm);
            }
        }
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
        gsm.setState(levelNumber);
    }
}
