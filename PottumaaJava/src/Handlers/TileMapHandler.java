package Handlers;

import TileMap.TileMap;
import TileMap.Tile;
import java.awt.*;
import java.util.ArrayList;

public class TileMapHandler extends BaseHandler {

    private ArrayList<TileMap> tileMaps;

    public TileMapHandler() {
        tileMaps = new ArrayList<>();
    }

    public void add(TileMap tileMap) {
        tileMaps.add(tileMap);
    }

    public ArrayList<TileMap> getTileMaps() {
        return this.tileMaps;
    }

    public TileMap getGroundTileMap() {
        TileMap groundTileMap = null;
        for(TileMap tileMap : tileMaps) {
            if(tileMap.getType() == Tile.TILE_TYPE_GROUND) {
                groundTileMap = tileMap;
            }
            break;
        }
        return groundTileMap;
    }

    public void draw(Graphics2D g) {
        for (TileMap tm : tileMaps) {
            tm.draw(g);
        }
    }

    public void update() {

    }
}
