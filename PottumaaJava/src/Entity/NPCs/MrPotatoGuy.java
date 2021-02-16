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
        collisionBoxWidth = 20;
        collisionBoxHeight = 20;

        damage = 100;

        loadSprites();

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = true;
        name = "Mr. Potatoguy";
    }

    private void loadSprites() {
        try {

            BufferedImage spriteSheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/NPC/mrpotatoguy.gif"
                    )
            );

            sprites = new BufferedImage[4];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spriteSheet.getSubimage(
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
