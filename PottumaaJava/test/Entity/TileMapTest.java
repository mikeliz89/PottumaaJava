package Entity;

import TileMap.*;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileMapTest {

    private TileMap myTileMap;

    public TileMapTest() {
        myTileMap = new TileMap(30,
                "/Tilesets/grasstileset.png",
                "/Maps/map1.csv"
                );
        myTileMap.loadTiles();
        myTileMap.loadMap();
        myTileMap.setPosition(0, 0);
        myTileMap.setType(Tile.TILE_TYPE_GROUND);
    }

    @Test
    public void TileMapExistsAfterCreation() {
        assertNotNull(myTileMap);
    }

    @Test
    public void getTileType_DoesGiveGoodValueOnRowZeroColumnZero() {
        var tileType = myTileMap.getTileType(0, 0);
        assertEquals(Tile.TILE_TYPE_GROUND, tileType);
    }

    @Test
    public void getTileType_DoesGiveGoodValueOnRowTenColumnTen() {
        var tileType = myTileMap.getTileType(10, 10);
        assertEquals(Tile.TILE_TYPE_GROUND, tileType);
    }

    @Test
    public void getTileType_DoesGiveGoodValueOnRTooSmallRow() {
        var tileType = myTileMap.getTileType(-1, 0);
        assertEquals(Tile.TILE_TYPE_GROUND, tileType);
    }

    @Test
    public void getTileType_DoesNotThrowExceptionOnTooBigRow() {
        var tileType = myTileMap.getTileType(31, 10);
        assertEquals(Tile.TILE_TYPE_GROUND, tileType);
    }

    @Test
    public void getTileType_DoesNotThrowExceptionOnTooBigColumn() {
        var tileType = myTileMap.getTileType(10, 31);
        assertEquals(Tile.TILE_TYPE_GROUND, tileType);
    }
}