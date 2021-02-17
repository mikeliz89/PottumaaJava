package Entity.Obstacles;

import Entity.MapObject;
import TileMap.TileMap;

public abstract class Obstacle extends MapObject {

    public Obstacle(java.util.ArrayList<TileMap> tileMaps) {
        this.tileMaps = tileMaps;
    }

    public void update() {}
}
