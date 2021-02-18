package Entity.Enemies;

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
        else if(enemyType == EnemySettings.ENEMY_TYPES_ARACHNID) {
            var arachnid = new Arachnid(tileMaps, maxHealth);
            arachnid.setPosition(xCoordinate, yCoordinate);
            return arachnid;
        }

        return null;
    }
}
