package Entity;

import Entity.Obstacles.Obstacle;
import Entity.Player.Player;
import Entity.Player.PlayerSettings;
import GameState.GameStateManager;
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
    void getHealth_WhenPlayerGetsHitZeroDamageHealthShouldBeStartHealth() {
        myPlayer.hit(0);
        var health = myPlayer.getHealth();
        assertEquals(PlayerSettings.PLAYER_START_HEALTH, health);
    }

    @Test
    void getHealth_WhenPlayerGetsHitDamageMoreThanMaxHealthPlayerHealthShouldBeZero() {
        myPlayer.hit(PlayerSettings.PLAYER_START_HEALTH+1);
        var health = myPlayer.getHealth();
        assertEquals(0, health);
    }

    @Test
    void getExperience_ShouldBeZeroAtFirst() {
        var experience = myPlayer.getExperience();
        assertEquals(0, experience);
    }

    @Test
    void getDamage() {
        var damage = myPlayer.getDamage();
        assertEquals(0, damage);
    }

    @Test
    void getCurrentLevel_Then_setCurrentLevel_Then_getCurrentLevel_Again_LevelShouldBeChanged() {
        assertEquals(0, myPlayer.getCurrentLevel());
        myPlayer.setCurrentLevel(GameStateManager.STATE_LEVEL_2);
        assertEquals(GameStateManager.STATE_LEVEL_2, myPlayer.getCurrentLevel());
    }

    @Test
    void increaseMana_WhenPlayerManaIsMax_ShouldNotIncreaseManaAnymore() {
        var oldMana = myPlayer.getMana();
        myPlayer.increaseMana();
        var newMana = myPlayer.getMana();
        assertEquals(oldMana, newMana);
    }

    @Test()
    void decreaseMana_ShouldRemoveManaPoints() {
        var oldMana = myPlayer.getMana();
        myPlayer.decreaseMana(5); //FIREBALL
        var newMana = myPlayer.getMana();
        assertTrue(newMana < oldMana);
    }

    @Test
    void increaseMana_WhenPlayerManaIsNotMax_ShouldIncreaseManaByOne() {
        myPlayer.decreaseMana(5); //FIREBALL
        var oldMana = myPlayer.getMana();
        myPlayer.increaseMana();
        var newMana = myPlayer.getMana();
        assertEquals(oldMana+1, newMana);
    }

    @Test
    void hasEnoughMana_getManaCostByType_ShouldThrow_IllegalArgumentException_For_UnsupportedType() {
        assertThrows(IllegalArgumentException.class, () -> {
            myPlayer.hasEnoughMana(1000);
        });
    }

    @Test
    void getName_ShouldReturnNull() {
        var name = myPlayer.getName();
        assertNull(name);
    }

}