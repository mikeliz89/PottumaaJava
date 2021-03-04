package Entity;

import Entity.Enemies.Enemy;
import Entity.Enemies.EnemyFactory;
import Entity.Enemies.EnemySettings;
import Entity.Obstacles.Obstacle;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import TileMap.TileMap;

public class EnemyTest {

    private final ArrayList<TileMap> myTileMaps;
    private final ArrayList<Obstacle> obstacles;
    private final Enemy myEnemy;

    public EnemyTest () {
        myTileMaps = new ArrayList<>();
        obstacles = new ArrayList<>();
        var enemyFactory = new EnemyFactory(myTileMaps, obstacles);
        myEnemy = enemyFactory.getEnemy(EnemySettings.ENEMY_TYPES_SLUGGER, 100, 100, EnemySettings.SLUGGER_MAX_HEALTH);
    }

    @Test
    public void EnemyExistsAfterCreation() {
        assertNotNull(myEnemy);
        assertEquals(myEnemy.tileMaps, myTileMaps);
    }

    @Test
    public void GetHealth_EnemyHealthIsReducedWhenTakingDamageButIsNotDead() {
        myEnemy.hit(1);
        var newHealth = myEnemy.getHealth();
        assertEquals(EnemySettings.SLUGGER_MAX_HEALTH - 1, newHealth);
        assertFalse(myEnemy.isDead());
    }

    @Test
    public void GetHealth_EnemyHealthIsZeroAndEnemyIsDeadWhenTakingDamageMoreThanMaxHealth() {
        myEnemy.hit(myEnemy.getMaxHealth()+1);
        var newHealth = myEnemy.getHealth();
        assertEquals(0, newHealth);
        assertTrue(myEnemy.isDead());
    }

    @Test
    public void GetHealth_WhenEnemyHealthGoesToZeroEnemyIsDead() {
        myEnemy.hit(EnemySettings.SLUGGER_MAX_HEALTH);
        var newHealth = myEnemy.getHealth();
        assertEquals(0, newHealth);
        assertTrue(myEnemy.isDead());
    }

    @Test
    public void GetHealth_EnemyIsHitWhenItIsAlreadyDeadShouldNotReduceHealth() {
        myEnemy.hit(EnemySettings.SLUGGER_MAX_HEALTH);
        var newHealth = myEnemy.getHealth();
        assertEquals(0, newHealth);
        assertTrue(myEnemy.isDead());
        myEnemy.hit(100);
        assertEquals(0, newHealth);
        assertTrue(myEnemy.isDead());
    }

    @Test
    public void getMoneyGainedWhenKilled_slugger() {
        var moneyGained = myEnemy.getMoneyGainedWhenKilled();
        assertEquals(EnemySettings.SLUGGER_MONEY_GAINED_WHEN_KILLED, moneyGained);
    }

    @Test
    public void getExperienceGainedWhenKilled_slugger() {
        var experienceGained = myEnemy.getExperienceGainedWhenKilled();
        assertEquals(EnemySettings.SLUGGER_EXP_GAINED_WHEN_KILLED, experienceGained);
    }

}