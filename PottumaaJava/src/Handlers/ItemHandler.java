package Handlers;

import Entity.Enemies.Enemy;
import Entity.Items.Item;
import Entity.Player.Player;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

public class ItemHandler extends BaseHandler{

    private Player player;
    private ArrayList<Item> items;
    private ArrayList<TileMap> tileMaps;
    private boolean playerIsCloseEnoughToTakeItem = false;

    public ItemHandler(Player player, ArrayList<TileMap> tileMaps) {
        this.player = player;
        this.tileMaps = tileMaps;
        items = new ArrayList<>();
    }

    public void spawnNewItemFromEnemy(Enemy enemy) {
        Item item = new Item("Banana",
                "/Images/Items/banana.png", tileMaps);
        var enemyX = enemy.getX() + enemy.getXMap();
        var enemyY = enemy.getY() + enemy.getYMap();
        item.setPosition(enemyX, enemyY);
        add(item);
    }

    public void add(Item item) {
        items.add(item);
    }

    public void update() {
        for(Item item : items) {
            item.update();
        }
        isPlayerCloseEnoughToTakeItem();
    }

    private void isPlayerCloseEnoughToTakeItem() {
        Item itemToTake = getItemToTake();
        player.setItemToTake(itemToTake);
        if(itemToTake != null) {
            playerIsCloseEnoughToTakeItem = true;
            return;
        }
        playerIsCloseEnoughToTakeItem = false;
    }

    private Item getItemToTake() {
        Item itemToTake = null;
        for(Item item : items) {
            if(item.intersects(player)) {
                itemToTake = item;
                //npc.stopMoving();
            }
        }
        return itemToTake;
    }

    public void draw(Graphics2D g) {
        for(Item item : items) {
            item.draw(g);
        }
    }

    public boolean playerIsCloseEnoughToTakeItem() {
        return playerIsCloseEnoughToTakeItem;
    }
}
