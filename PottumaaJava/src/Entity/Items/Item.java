package Entity.Items;

import Entity.MapObject;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Item extends MapObject {

    BufferedImage image;
    private String name;
    private String imageName;

    public Item(String name, String imageName, ArrayList<TileMap> tileMaps) {
        this.tileMaps = tileMaps;
        this.name = name;
        this.imageName = imageName;
        init();
    }

    private void init() {
        collisionBoxWidth = 14;
        collisionBoxHeight = 14;
        setWidth(14);
        setHeight(14);
        getImage();
    }

    private void getImage() {
        try {
            image = ImageIO.read(
                    getClass().getResourceAsStream(imageName)
            );
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g) {

        BufferedImage before = image;
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(0.2, 0.2);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(before, after);

        g.drawImage(
                after,
                getX(),
                getY(),
                null
        );
    }

    @Override
    protected void updatePosition() {

    }

}
