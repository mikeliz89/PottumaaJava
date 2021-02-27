package Entity.Enemies;

import Entity.Obstacles.Obstacle;
import TileMap.TileMap;
import java.util.ArrayList;

public class EnemyFactory {

    ArrayList<TileMap> tileMaps;
    ArrayList<Obstacle> obstacles;
    public EnemyFactory(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles) {
        this.tileMaps = tileMaps;
        this.obstacles = obstacles;
    }

    public Enemy getEnemy(int enemyType, int xCoordinate, int yCoordinate, int maxHealth) {
        if(enemyType <= 0) {
            return null;
        }

        if(enemyType == EnemySettings.ENEMY_TYPES_SLUGGER) {
            var slugger = new Slugger(tileMaps, maxHealth, obstacles);
            slugger.setPosition(xCoordinate, yCoordinate);
            return slugger;
        }
        else if(enemyType == EnemySettings.ENEMY_TYPES_ARACHNID) {
            var arachnid = new Arachnid(tileMaps, maxHealth, obstacles);
            arachnid.setPosition(xCoordinate, yCoordinate);
            return arachnid;
        }

        return null;
    }
}
