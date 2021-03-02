package GameState.Levels;

import Entity.Enemies.Enemy;
import Entity.Enemies.EnemySettings;
import Entity.Explosion;
import Entity.HUD.HUD;
import Entity.Items.Item;
import Entity.NPCs.NPC;
import Entity.Obstacles.Obstacle;
import Entity.Player.Player;
import Entity.Quests.*;
import GameState.*;
import Main.GamePanel;
import MapPoint.MapPoint;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class BaseLevel extends GameState  {

    protected abstract void populateMapPoints();
    protected abstract void populateEnemies();
    protected abstract void populateNPCs();

    protected ArrayList<TileMap> tileMaps;
    protected Player player;
    protected ArrayList<Enemy> enemies;
    protected ArrayList<NPC> NPCs;
    private ArrayList<Item> items;
    private ArrayList<Explosion> explosions;
    private ArrayList<MapPoint> mapPoints;
    private ArrayList<Obstacle> obstacles;
    private HUD hud;
    private ArrayList<Integer> keysPressed;
    private String groundTileSetName;
    private String obstacleTileSetName;
    private String groundTileMapName;
    private String obstacleTileMapName;
    private String bgMusicSoundFileName;
    private QuestLog questLog;

    private boolean playerIsInMapPoint = false;
    private boolean playerIsCloseEnoughToNPC = false;

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
        items = new ArrayList<>();

        populateTileMaps();
        populateMapPoints();
        populateEnemies();
        populateNPCs();

        createPlayer();
        createQuestLog();
        createHUD();
    }

    private void createPlayer() {
        player = new Player(tileMaps, obstacles);
        player.setCurrentLevel(gsm.getCurrentState());
    }

    private void createQuestLog() {
        questLog = new QuestLog();
        QuestFactory questFactory = new QuestFactory("Kill 5 Sluggers",
                "Sluggers are those little slimy \n" +
                        "spiky snails that keep terrorizing your farm.\n" +
                        "Fellow villagers are scared of them.\n" +
                        "Get rid of them", 5, EnemySettings.ENEMY_TYPES_SLUGGER);
        Quest killSluggersQuest = questFactory.getQuest(QuestSettings.QUEST_TYPE_KILLQUEST);
        questLog.addQuest(killSluggersQuest);
    }

    private void killOneEnemy(int EnemyType) {
        for(Quest quest : questLog.getKillQuests()) {
            if(quest instanceof KillQuest) {
                var killQuest = (KillQuest)quest;
                killQuest.killOneEnemy(EnemyType, player);
            }
        }
    }

    private void createHUD() {
        hud = new HUD(player, questLog);
    }

    private void populateTileMaps() {
        tileMaps = new ArrayList<>();
        addTileMap(groundTileSetName, groundTileMapName, Tile.TILE_TYPE_GROUND);
        addTileMap(obstacleTileSetName, obstacleTileMapName, Tile.TILE_TYPE_OBSTACLE);
    }

    private void addTileMap(String tileSetName, String mapName, int tileType) {
        TileMap tileMapGround = new TileMap(30, tileSetName, mapName);
        tileMapGround.setType(tileType);
        tileMapGround.loadTiles();
        tileMapGround.loadMap();
        tileMapGround.setPosition(0, 0);
        tileMaps.add(tileMapGround);
    }

    protected void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    protected void addItem(Item item) {
        items.add(item);
    }

    protected ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    protected void addMapPoint(MapPoint mapPoint) {
        mapPoints.add(mapPoint);
    }

    public void update() {

        updatePlayer();

        isPlayerCloseEnoughToTalkToNPC();
        isPlayerInMapPoint();

        moveBackground();

        checkPlayerAttackingEnemies();

        updateEnemies();

        updateNPCs();

        updateExplosions();
    }

    private void moveBackground() {
        for (TileMap tm : tileMaps) {
            tm.setPosition(
                    GamePanel.WIDTH / 2 - player.getX(),
                    GamePanel.HEIGHT / 2 - player.getY()
            );
        }
    }

    private void updatePlayer() {
        player.update();
    }

    private void checkPlayerAttackingEnemies() {
        player.checkAttack(enemies);
    }

    private void isPlayerCloseEnoughToTalkToNPC() {
        NPC npcToTalkTo = getNPCToTalkTo();
        player.setNPCToTalkTo(npcToTalkTo);
        if(npcToTalkTo != null) {
            playerIsCloseEnoughToNPC = true;
            return;
        }
        playerIsCloseEnoughToNPC = false;
    }

    private NPC getNPCToTalkTo() {
        NPC npcToTalkTo = null;
        for(NPC npc : NPCs) {
            if(npc.intersects(player)) {
                npcToTalkTo = npc;
                npc.stopMoving();
            }
        }
        return npcToTalkTo;
    }

    private void isPlayerInMapPoint() {
        MapPoint mapPointToChangeTo = getMapPointToChangeTo();
        player.setMapPointForLevelChange(mapPointToChangeTo);
        if(mapPointToChangeTo != null) {
            playerIsInMapPoint = true;
            return;
        }
        playerIsInMapPoint = false;
    }

    private MapPoint getMapPointToChangeTo() {
        MapPoint mapPointToChangeTo = null;
        for(MapPoint mapPoint : mapPoints) {
            Rectangle mapPointRectangle = mapPoint.getRectangle();
            Rectangle playerRectangle = player.getRectangle();
            if(playerRectangle.intersects(mapPointRectangle)) {
                mapPointToChangeTo = mapPoint;
            }
        }
        return mapPointToChangeTo;
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
                killOneEnemy(e.getEnemyType());
                givePlayerRewardForKillingEnemy(e);
                enemies.remove(i);
                i--;
                explosions.add(
                        new Explosion(e.getX(), e.getY()));
            }
        }
    }

    private void givePlayerRewardForKillingEnemy(Enemy e) {
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

        drawPlayer(g);

        drawNPCs(g);

        drawObstacles(g);

        drawItems(g);

        drawEnemies(g);

        drawExplosions(g);

        drawHUD(g);

        drawInLevelZoneText(g);

        drawTalkToNPCText(g);
    }

    private void drawTalkToNPCText(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.BLUE);
        if(playerIsCloseEnoughToNPC)
            g.drawString("Press E to talk",
                    player.getX() + (int)player.getXMap(),
                    player.getY() + (int)player.getYMap());
    }

    protected void drawObstacles(Graphics2D g) {
        for(Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
    }

    private void drawItems(Graphics2D g) {
        for(Item item : items) {
            item.draw(g);
        }
    }

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
        TileMap groundTileMap = null;
        for(TileMap tileMap : tileMaps) {
            if(tileMap.getType() == Tile.TILE_TYPE_GROUND) {
                groundTileMap = tileMap;
            }
            break;
        }
        return groundTileMap;
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

        if(k == KeyEvent.VK_I) hud.toggleInventory();
        if(k == KeyEvent.VK_M) hud.toggleMap();
        if(k == KeyEvent.VK_J) hud.toggleDialogBox();
        if(k == KeyEvent.VK_Q) hud.toggleQuestLog();
        if(k == KeyEvent.VK_F5) hud.togglePauseMenu(); //F5 = Tallentaa pelin

        if(k == KeyEvent.VK_1) quickTravel(GameStateManager.STATE_LEVEL_1);
        if(k == KeyEvent.VK_2) quickTravel(GameStateManager.STATE_LEVEL_2);
        if(k == KeyEvent.VK_3) quickTravel(GameStateManager.STATE_PLAYER_HOME);

        if(playerIsInMapPoint) {
            if(k == KeyEvent.VK_E) {
                player.changeLevel(gsm);
            }
        }

        if(playerIsCloseEnoughToNPC) {
            if(k == KeyEvent.VK_E) {
                var npc = player.getNPCToTalkTo();
                var dialogBox = npc.getDialogBox();
                hud.setDialogBox(dialogBox);
                hud.toggleDialogBox();
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
