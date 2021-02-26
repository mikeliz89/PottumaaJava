package Entity.Items;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Item  {

    BufferedImage image;
    private Point point;
    private String name;
    private String imageName;

    public Item(String name, String imageName) {
        this.name = name;
        this.imageName = imageName;
        try {
            image = ImageIO.read(
                    getClass().getResourceAsStream(imageName)
            );
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        point = new Point();
    }

    public void setPosition(Point point) {
        this.point.x = point.x;
        this.point.x = point.y;
    }

    public void draw(Graphics2D g) {

        BufferedImage before = image;
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(0.5, 0.5);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(before, after);

        g.drawImage(
                after,
                point.x,
                point.y,
                null
        );
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }
}
