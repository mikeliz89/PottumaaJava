package Entity.NPCs;

import Entity.Obstacles.Obstacle;
import TileMap.TileMap;

import java.util.ArrayList;

public class MrPotatoGuy extends NPC {

    public MrPotatoGuy(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles, int maxHealth) {
        super(tileMaps, obstacles, maxHealth,
                "/Sprites/NPC/mrpotatoguy.gif", 60, 84);
        collisionBoxWidth = 20;
        collisionBoxHeight = 20;
        damage = 100;
        right = true;
        facingRight = true;
        name = "Mr. Potatoguy";
        profession = "Fruitseller";
    }

    @Override
    protected int[] getAnimationFrames() {
        return new int[]{
                1, 2, 3
        };
    }
}
