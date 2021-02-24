package Entity.HUD;

import java.awt.*;

public class InventorySlot {

    private InventoryItem inventoryItem;
    private Rectangle rectangle;
    private int width;
    private int height;
    private int identifierNumber;

    public InventorySlot(Rectangle rectangle, int width, int height, int identifierNumber) {
        this.rectangle = rectangle;
        this.width = width;
        this.height = height;
        this.identifierNumber = identifierNumber;
    }

    public void addItem(InventoryItem item) {
        if(inventoryItem != null)
            return;
        inventoryItem = item;
    }

    public boolean hasItem() {
        return inventoryItem == null ? false : true;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void draw(Graphics2D g) {
        var fillRectOffset = 2;
        var fillRectColor = new Color (153, 51, 0, 190);
        var textColor = new Color(255, 255, 255, 90);
        var rectangle = getRectangle();
        g.setColor(Color.BLACK);
        g.drawRect(rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height
        );
        g.setColor(fillRectColor);
        g.fillRect(rectangle.x + fillRectOffset,
                rectangle.y + fillRectOffset,
                rectangle.width - (fillRectOffset +1),
                rectangle.height - (fillRectOffset +1)
        );
        g.setColor(textColor);
        g.drawString(String.valueOf(identifierNumber),
                rectangle.x + (width / 2) - 5,
                rectangle.y + (height / 2)
        );

        if(hasItem()) {
            //todo: Piirrä inventorion ruutuun itemin kuva
            inventoryItem.draw(g);
            g.setColor(Color.WHITE);
            g.drawString("X", rectangle.x + (width / 2), rectangle.y + (height / 2) + 3);
        }
    }
}
