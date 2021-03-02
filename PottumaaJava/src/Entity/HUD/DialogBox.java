package Entity.HUD;

import Main.GamePanel;

import java.awt.*;

public class DialogBox {

    private String text;
    private String title;

    public DialogBox(String title,String text) {
        this.text = text;
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void draw(Graphics2D g) {
        Rectangle shape = getShape();

        drawRectangle(g, shape);

        drawOuterLines(g, shape);

        drawHeader(g, shape);

        drawText(g, text, shape.x + 20, shape.y + 20);

        drawText(g, "Press J to exit dialog", shape.x + 120, shape.y + 190);
    }

    private Rectangle getShape() {
        var x = 100;
        var y = GamePanel.HEIGHT - 210;
        var width = GamePanel.WIDTH - 200;
        var height = 200;
        var shape = new Rectangle(x, y, width, height);
        return shape;
    }

    private void drawRectangle(Graphics2D g, Rectangle shape) {
        int alpha = 127; // 50% transparent
        Color semiTransParentColor = new Color(180, 150, 150, alpha);
        g.setColor(semiTransParentColor);
        g.fill(shape);
    }

    private void drawText(Graphics2D g, String text, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawString(text, x, y);
    }

    private void drawHeader(Graphics2D g, Rectangle shape) {
        drawText(g, title, shape.x, shape.y);
    }

    private void drawOuterLines(Graphics2D g, Rectangle shape) {
        g.setColor(Color.BLACK);
        g.drawRect(shape.x, shape.y, shape.width, shape.height);
    }
}
