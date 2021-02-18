package Entity;

import Entity.Enemies.Enemy;
import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import TileMap.TileMap;

public class EnemyTest {

    //private final int MAX_HEALTH = 10;
    private final ArrayList<TileMap> myTileMaps;
    private final Enemy myEnemy;

    public EnemyTest () {
        myTileMaps = new ArrayList<>();
        var enemyFactory = new EnemyFactory(myTileMaps);
        myEnemy = enemyFactory.getEnemy(EnemySettings.ENEMY_TYPES_SLUGGER, 100, 100, EnemySettings.SLUGGER_MAX_HEALTH);
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