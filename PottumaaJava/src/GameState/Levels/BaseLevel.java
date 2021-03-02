package GameState.Levels;

import Entity.Enemies.Enemy;
import Entity.Enemies.EnemySettings;
import Entity.Explosion;
import Handlers.ExplosionHandler;
import Entity.HUD.HUD;
import Entity.Items.Item;
import Entity.NPCs.NPC;
import Entity.Obstacles.Obstacle;
import Entity.Player.Player;
import Entity.Quests.*;
import GameState.*;
import Handlers.KeyboardController;
import Handlers.MapPointHandler;
import Handlers.NPCHandler;
import Main.GamePanel;
import MapPoint.MapPoint;
import TileMap.*;

import java.awt.*;
import java.util.ArrayList;

public abstract class BaseLevel extends GameState  {

    protected abstract void populateMapPoints();
    protected abstract void populateEnemies();
    protected abstract void populateNPCs();

    protected ArrayList<TileMap> tileMaps;
    protected Player player;
    protected ArrayList<Enemy> enemies;
    private ArrayList<Item> items;

    private NPCHandler npcHandler;
    private ExplosionHandler explosionHandler;
    private MapPointHandler mapPointHandler;

    private ArrayList<Obstacle> obstacles;
    private HUD hud;
    private KeyboardController keyboardController;
    private String groundTileSetName;
    private String obstacleTileSetName;
    private String groundTileMapName;
    private String obstacleTileMapName;
    private String bgMusicSoundFileName;
    private QuestLog questLog;

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

        enemies = new ArrayList<>();
        obstacles = new ArrayList<>();
        items = new ArrayList<>();

        populateTileMaps();

        populateEnemies();

        createPlayer();
        createQuestLog();
        createHUD();

        npcHandler = new NPCHandler(player);

        populateNPCs();

        var groundTileMap = getGroundTileMap();
        explosionHandler = new ExplosionHandler(groundTileMap);
        mapPointHandler = new MapPointHandler(player, groundTileMap);
        keyboardController = new KeyboardController(gsm, player, hud, npcHandler, mapPointHandler);

        populateMapPoints();
    }

    protected void addNPC(NPC npc) {
        npcHandler.add(npc);
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
        mapPointHandler.add(mapPoint);
    }

    public void update() {

        updatePlayer();

        mapPointHandler.update();

        moveBackground();

        checkPlayerAttackingEnemies();

        updateEnemies();

        updateNPCs();

        explosionHandler.update();
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

    private void updateNPCs() {
       npcHandler.update();
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
                explosionHandler.addExplosion(new Explosion(e.getX(), e.getY()));
            }
        }
    }

    private void givePlayerRewardForKillingEnemy(Enemy e) {
        player.addExperience(e.getExperienceGainedWhenKilled());
        player.addMoney(e.getMoneyGainedWhenKilled());
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
        if(npcHandler.playerIsCloseEnoughToNPC())
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
        if(mapPointHandler.playerIsInMapPoint())
            g.drawString("Press E",
                    player.getX() + (int)player.getXMap(),
                    player.getY() + (int)player.getYMap());
    }

    private void drawExplosions(Graphics2D g) {
        explosionHandler.draw(g);
    }

    private void drawEnemies(Graphics2D g) {
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    private void drawNPCs(Graphics2D g) {
       npcHandler.draw(g);
    }

    private void drawPlayer(Graphics2D g) {
        player.draw(g);
    }

    private void drawMapPoints(Graphics2D g) {
        mapPointHandler.draw(g);
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
        keyboardController.keyPressed(k);
    }

    public void keyReleased(int k) {
        keyboardController.keyReleased(k);
    }
}
