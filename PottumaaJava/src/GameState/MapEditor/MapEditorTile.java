package GameState.MapEditor;

import TileMap.Tile;
import java.awt.image.BufferedImage;

public class MapEditorTile extends Tile {

    private int index;
    public MapEditorTile (BufferedImage image, int type, int frictionType) {
        super(image, type, frictionType);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
