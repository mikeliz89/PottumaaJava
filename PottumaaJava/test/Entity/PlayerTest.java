package Entity;

import Entity.Obstacles.Obstacle;
import Entity.Player.Player;
import Entity.Player.PlayerSettings;
import GameState.ISaveManager;
import TileMap.TileMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    //Todo: Lisää joku Mocking -library, esim EasyMock ja toteuta sillä saveManager.load -metodi jotta testataan myös player.load
    private ISaveManager saveManager;
    private final ArrayList<TileMap> myTileMaps;
    private final ArrayList<Obstacle> myObstacles;
    Player myPlayer;

    public PlayerTest() {
        myTileMaps = new ArrayList<>();
        myObstacles = new ArrayList<>();
        myPlayer = new Player(myTileMaps, myObstacles, saveManager);
    }

    @Test
    void getHealth() {
        var health = myPlayer.getHealth();
        assertEquals(PlayerSettings.PLAYER_START_HEALTH, health);
    }

    @Test
    void getMaxHealth() {
        var maxHealth = myPlayer.getMaxHealth();
        assertEquals(PlayerSettings.PLAYER_START_HEALTH, maxHealth);
    }

    @Test
    void WhenPlayerGetsHitZeroDamageHealthShouldBeFive() {
        myPlayer.hit(0);
        var health = myPlayer.getHealth();
        assertEquals(PlayerSettings.PLAYER_START_HEALTH, health);
    }

    @Test
    void WhenPlayerGetsHitDamageMoreThanMaxHealthPlayerHealthShouldBeZero() {
        myPlayer.hit(PlayerSettings.PLAYER_START_HEALTH+1);
        var health = myPlayer.getHealth();
        assertEquals(0, health);
    }

}