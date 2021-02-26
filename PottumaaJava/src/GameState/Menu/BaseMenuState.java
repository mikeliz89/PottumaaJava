package GameState.Menu;

import GameState.*;
import Main.GamePanel;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class BaseMenuState extends GameState {

    private Color titleColor;
    private Font titleFont;
    private Font font;
    private Background bg;
    private String backgroundImageName;
    private String[] options;
    protected String titleText;
    protected int currentChoice = 0;
    protected ISaveManager saveManager;

    public BaseMenuState(GameStateManager gsm, String[] options, String backgroundImageName) {
        this(gsm, options, backgroundImageName, new SaveManager());
    }

    public BaseMenuState(GameStateManager gsm, String[] options, String backgroundImageName, ISaveManager saveManager) {
        this.saveManager = saveManager;
        this.gsm = gsm;
        this.options = options;
        this.backgroundImageName = backgroundImageName;
        init();
    }

    public void init() {
        try {
            //Background image
            bg = new Background(backgroundImageName, 0.1);
            bg.setVector(-0.1, 0);

            titleColor = new Color(220, 220, 220);
            titleFont = new Font(
                    "Century Gothic",
                    Font.PLAIN,
                    28);
            font = new Font("Arial", Font.PLAIN, 12);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        gsm.setSongToPlay("/Music/happymusic.wav");
    }

    public void update() {
        bg.update();
    }

    public void draw(Graphics2D g) {
        drawBackground(g);
        drawMenuOptions(g);
        drawTitle(g);
    }

    protected void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    private void drawBackground(Graphics2D g) {
        bg.draw(g);
    }

    private void drawTitle(Graphics2D g) {

        if(titleText == null)
            return;
        if(titleText == "")
            return;

        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString(titleText, GamePanel.WIDTH / 3, GamePanel.HEIGHT / 3);
    }

    protected void drawMenuOptions(Graphics2D g) {
        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.WHITE);
            }
            else {
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 175, 140 + i * 15);
        }
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if (k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if (currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }

    public void keyReleased(int k) {

    }

    protected void select() {

    }
}
