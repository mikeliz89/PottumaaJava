package Entity.Obstacles;

import Entity.Animation;
import Entity.MapObject;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Obstacle extends MapObject {

    private String imageName;
    public Obstacle(ArrayList<TileMap> tileMaps, int width, int height, String imageName) {
        this.tileMaps = tileMaps;
        this.width = width;
        this.height = height;
        this.imageName = imageName;
        this.collisionBoxHeight = height;
        this.collisionBoxWidth = width;
        loadSprites();
    }

    private void loadSprites() {
        try {
            BufferedImage image = ImageIO.read(
                    getClass().getResourceAsStream(imageName)
            );

            var sprites = new BufferedImage[1];
            Arrays.fill(sprites, image);

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(7000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
