package Handlers;

import Entity.Items.Item;
import Entity.Player.Player;

import java.awt.*;
import java.util.ArrayList;

public class ItemHandler {

    private Player player;
    private ArrayList<Item> items;

    public ItemHandler(Player player) {
        this.player = player;
        items = new ArrayList<>();
    }

    public void add(Item item) {
        items.add(item);
    }

    public void update() {

    }

    public void draw(Graphics2D g) {
        for(Item item : items) {
            item.draw(g);
        }
    }
}
