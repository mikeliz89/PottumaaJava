package GameState.MapEditor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapEditorSlot {

    private String tileSetIndex = "0";
    private int row;
    private int column;
    private Rectangle rectangle;
    private BufferedImage image;
    public MapEditorSlot(Rectangle rectangle, int row, int column) {
        this.rectangle = rectangle;
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getTileSetIndex() {
        return tileSetIndex;
    }

    public void setTileSetIndex(String tileSetIndex) {
        this.tileSetIndex = tileSetIndex;
    }

    public void setImage(BufferedImage newImage) {
        try {
            image = newImage;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, rectangle.x,
                rectangle.y, null);
        g.drawRect(rectangle.x,
                rectangle.y,
                rectangle.width, rectangle.height);
    }
}
