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
        var x = 100;
        var y = GamePanel.HEIGHT - 210;
        var width = GamePanel.WIDTH - 200;
        var height = 200;
        var shape = new Rectangle(x, y, width, height);
        int alpha = 127; // 50% transparent
        Color semiTransParentColor = new Color(180, 150, 150, alpha);
        g.setColor(semiTransParentColor);
        g.fill(shape);

        //Piirrä ääriviivat
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        //Piirrä otsikko
        g.setColor(Color.BLACK);
        g.drawString(title, shape.x, shape.y);

        //Piirrä tekstiä
        g.setColor(Color.BLACK);
        g.drawString(text, shape.x + 20, shape.y + 20);
    }
}
