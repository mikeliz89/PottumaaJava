package Entity.HUD;

import Entity.Player.Player;
import Main.GamePanel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Font font;
    private Player player;
    private HashMap<Integer, InventorySlot> inventorySlots;
    private final int inventoryXPosition = 10;
    private final int characterStatsXPosition = 480;
    private final int slotWidth = 30;
    private final int slotHeight = 30;

    public Inventory(Player player) {
        this.player = player;
        font = new Font("Arial", Font.PLAIN, 14);
        inventorySlots = new HashMap<>();
        createInventorySlots();

        testAddItems();
    }

    private void testAddItems() {
        addItemToSlot(new InventoryItem(), 3);
    }

    private void createInventorySlots() {
        var columns = 10;
        var rows = 8;

        var startingPositionX = 10;
        var startingPositionY = 50;

        var innerBox = getInnerBox(getOuterBox());

        var counter = 0;
        for(int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {

                var inventorySlotRectangle = new Rectangle(innerBox.x + startingPositionX + (column * slotWidth),
                        innerBox.y + startingPositionY + ( row * slotHeight),
                        slotWidth, slotHeight);
                var inventorySlot = new InventorySlot(inventorySlotRectangle, slotWidth, slotHeight, counter);
                inventorySlots.put(counter, inventorySlot);
                counter++;
            }
        }
    }

    public void draw(Graphics2D g) {
        var outerBox = getOuterBox();
        var innerBox = getInnerBox(outerBox);

        drawInventoryInnerBox(g, innerBox);

        drawOuterLines(g, outerBox);

        drawTitle(g, innerBox);

        drawInventorySlots(g);

        drawGold(g, innerBox);

        drawCharacterStatsTitle(g, innerBox);

        drawExperience(g, innerBox);

        drawAttackStats(g, innerBox);

        drawHealth(g, innerBox);
    }

    private void addItemToSlot(InventoryItem item, int slotNumber) {
        InventorySlot slot = null;
        for(Map.Entry<Integer, InventorySlot> entry : inventorySlots.entrySet()) {
            var key = entry.getKey();
            if(key == slotNumber) {
                slot = entry.getValue();
            }
        }

        if(slot == null)
            return;

        slot.addItem(item);
    }

    private Rectangle getOuterBox() {
        var x = 100;
        var y = 100;
        var width = GamePanel.WIDTH - 200;
        var height = GamePanel.HEIGHT - 160;
        return new Rectangle(x, y, width, height);
    }

    private Rectangle getInnerBox(Rectangle outerBox) {
        return new Rectangle(outerBox.x + 10, outerBox.y + 10,
                outerBox.width-20, outerBox.height-20);
    }

    private void drawInventoryInnerBox(Graphics2D g, Rectangle innerBox) {
        int alpha = 170; // vähemmän kuin 50% transparent
        Color semiTransParentColor = new Color(0, 0, 0, alpha);
        g.setColor(semiTransParentColor);
        g.fill(innerBox);
    }

    private void drawOuterLines(Graphics2D g, Rectangle rectangle) {
        g.setColor(Color.BLACK);
        g.drawRect(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
    }

    private void drawHealth(Graphics2D g, Rectangle innerBox) {
        g.setColor(Color.WHITE);
        g.drawString("HP : "+ player.getHealth() + " / " + player.getMaxHealth(), innerBox.x + characterStatsXPosition, innerBox.y + 100);
    }

    private void drawCharacterStatsTitle(Graphics2D g, Rectangle innerBox) {
        g.setColor(Color.WHITE);
        g.drawString("Character stats", innerBox.x + characterStatsXPosition, innerBox.y + 20);
    }

    private void drawExperience(Graphics2D g, Rectangle innerBox) {
        g.setColor(Color.CYAN);
        g.drawString("Exp: " + player.getExperience(), innerBox.x + characterStatsXPosition, innerBox.y + 40 );
    }

    private void drawGold(Graphics2D g, Rectangle innerBox) {
        g.setColor(Color.ORANGE);
        g.drawString("Gold: " + player.getMoneyInWallet(), innerBox.x + inventoryXPosition, innerBox.y + 40);
    }

    private void drawTitle(Graphics2D g, Rectangle innerBox) {
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("Inventory", innerBox.x + inventoryXPosition, innerBox.y + 20);
    }

    private void drawAttackStats(Graphics2D g, Rectangle innerBox) {
        g.setColor(Color.WHITE);
        g.drawString("Scratch dmg: " + player.getScratchDamage(), innerBox.x + characterStatsXPosition, innerBox.y + 60);
        g.drawString("Fireball dmg: " + player.getFireBallDamage(), innerBox.x + characterStatsXPosition, innerBox.y + 80);
    }

    private void drawInventorySlots(Graphics2D g) {
        for(Map.Entry<Integer, InventorySlot> entry : inventorySlots.entrySet()) {
           entry.getValue().draw(g);
        }
    }
}
