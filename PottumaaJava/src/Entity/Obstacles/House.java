package Entity.Obstacles;

import Entity.Animation;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class House extends Obstacle {

    private BufferedImage[] sprites;

    public House(java.util.ArrayList<TileMap> tileMaps) {
        super(tileMaps);

        width = 180;
        height = 120;

        loadSprites();
    }

    private void loadSprites() {
        try {

            BufferedImage image = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/House/obstacles-new-house.gif")
            );

            sprites = new BufferedImage[1];
            Arrays.fill(sprites, image);

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
