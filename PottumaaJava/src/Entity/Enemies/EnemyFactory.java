package Entity.Enemies;

import Entity.Enemy;
import TileMap.TileMap;
import java.util.ArrayList;

public class EnemyFactory {

    ArrayList<TileMap> tileMaps;

    public EnemyFactory(ArrayList<TileMap> tileMaps) {
        this.tileMaps = tileMaps;
    }

    public Enemy getEnemy(int enemyType, int xCoordinate, int yCoordinate, int maxHealth) {
        if(enemyType <= 0) {
            return null;
        }

        if(enemyType == EnemySettings.ENEMY_TYPES_SLUGGER) {
            var slugger = new Slugger(tileMaps, maxHealth);
            slugger.setPosition(xCoordinate, yCoordinate);
            return slugger;
        }

        return null;
    }
}
