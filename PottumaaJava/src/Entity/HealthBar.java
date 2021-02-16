package Entity;

import java.awt.*;

public class HealthBar {

    int x;
    int y;
    int width;
    int maxWidth;
    int height;

    public HealthBar(int height, int maxWidth) {
        this.height = height;
        this.maxWidth = maxWidth;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setMaxWidth(int maxWidth) {

        this.maxWidth = maxWidth;
    }

    private final int maximumLength = 30;

    public void draw(Graphics2D g) {

        if(width > maximumLength) {
            width = maximumLength;
        }

        if(maxWidth > maximumLength) {
            maxWidth = maximumLength;
        }

        //piirrä healthbarin sisältö
        g.setColor(Color.RED);
        g.fillRect(x, y , width, height);

        //piirrä ääriviivat
        g.setColor(Color.BLACK);
        g.drawRect(x, y, maxWidth, height);
    }
}
