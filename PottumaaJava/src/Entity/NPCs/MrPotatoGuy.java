package Entity.NPCs;

import TileMap.TileMap;

import java.util.ArrayList;

public class MrPotatoGuy extends NPC {

    public MrPotatoGuy(ArrayList<TileMap> tileMaps, int maxHealth) {
        super(tileMaps, maxHealth, "/Sprites/NPC/mrpotatoguy.gif");
        width = 60;
        height = 84;
        collisionBoxWidth = 20;
        collisionBoxHeight = 20;
        damage = 100;
        right = true;
        facingRight = true;
        name = "Mr. Potatoguy";
        profession = "Fruitseller";

        loadSprites();
        setAnimation();
    }
}
