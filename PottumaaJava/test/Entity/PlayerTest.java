package Entity;

import Entity.Obstacles.Obstacle;
import Entity.Player.Player;
import TileMap.TileMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private final ArrayList<TileMap> myTileMaps;
    private final ArrayList<Obstacle> myObstacles;
    Player myPlayer;

    public PlayerTest() {
        myTileMaps = new ArrayList<>();
        myObstacles = new ArrayList<>();
        myPlayer = new Player(myTileMaps, myObstacles);
    }

    @Test
    void getHealth() {
        var health = myPlayer.getHealth();
        assertEquals(5, health);
    }

    @Test
    void getMaxHealth() {
        var maxHealth = myPlayer.getMaxHealth();
        assertEquals(5, maxHealth);
    }

    @Test
    void WhenPlayerGetsHitZeroDamageHealthShouldBeFive() {
        myPlayer.hit(0);
        var health = myPlayer.getHealth();
        assertEquals(5, health);
    }

    @Test
    void WhenPlayerGetsHitDamageMoreThanMaxHealthPlayerHealthShouldBeZero() {
        myPlayer.hit(6);
        var health = myPlayer.getHealth();
        assertEquals(0, health);
    }

}