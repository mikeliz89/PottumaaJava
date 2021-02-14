package Entity.NPCs;

import Entity.Animation;
import Entity.NPC;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MrPotatoGuy extends NPC {

    private BufferedImage[] sprites;

    public MrPotatoGuy(ArrayList<TileMap> tileMaps, int maxHealth) {

        super(tileMaps, maxHealth);

        width = 60;
        height = 84;
        cwidth = 20;
        cheight = 20;

        damage = 100;

        // load sprites
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/NPC/mrpotatoguy.gif"
                    )
            );

            sprites = new BufferedImage[4];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = true;

    }

    public void update() {

        // update animation
        animation.update();
    }

    public void draw(Graphics2D g) {

        //if(notOnScreen()) return;

        setMapPosition();

        super.draw(g);

    }
}
