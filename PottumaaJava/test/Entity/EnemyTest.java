package Entity;

import Entity.Enemies.EnemySettings;
import Entity.Enemies.Slugger;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import TileMap.TileMap;

public class EnemyTest {

    //private final int MAX_HEALTH = 10;
    private ArrayList<TileMap> myTileMaps;
    private Slugger myEnemy;

    public EnemyTest () {
        myTileMaps = new ArrayList<>();
        myEnemy = new Slugger(myTileMaps, EnemySettings.SLUGGER_MAX_HEALTH);
    }

    @Test
    public void FirstTest() {
        assertTrue(1 == 1);
    }

    @Test
    public void EnemyExistsAfterCreation() {
        assertNotNull(myEnemy);
        assertEquals(myEnemy.tileMaps, myTileMaps);
    }

    @Test
    public void EnemyHealthIsReducedWhenTakingDamageButIsNotDead() {
        myEnemy.hit(1);
        var newHealth = myEnemy.getHealth();
        assertEquals(EnemySettings.SLUGGER_MAX_HEALTH - 1, newHealth);
        assertFalse(myEnemy.isDead());
    }

    @Test
    public void EnemyHealthIsZeroAndEnemyIsDeadWhenTakingDamageMoreThanMaxHealth() {
        myEnemy.hit(myEnemy.getMaxHealth()+1);
        var newHealth = myEnemy.getHealth();
        assertEquals(0, newHealth);
        assertTrue(myEnemy.isDead());
    }

    @Test
    public void WhenEnemyHealthGoesToZeroEnemyIsDead() {
        myEnemy.hit(EnemySettings.SLUGGER_MAX_HEALTH);
        var newHealth = myEnemy.getHealth();
        assertEquals(0, newHealth);
        assertTrue(myEnemy.isDead());
    }

    @Test
    public void EnemyIsHitWhenItIsAlreadyDeadShouldNotReduceHealth() {
        myEnemy.hit(EnemySettings.SLUGGER_MAX_HEALTH);
        var newHealth = myEnemy.getHealth();
        assertEquals(0, newHealth);
        assertTrue(myEnemy.isDead());
        myEnemy.hit(100);
        assertEquals(0, newHealth);
        assertTrue(myEnemy.isDead());
    }

}