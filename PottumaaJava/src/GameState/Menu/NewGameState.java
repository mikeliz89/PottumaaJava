package GameState.Menu;

import Entity.Player.PlayerSettings;
import GameState.GameStateManager;
import GameState.ResourceManager;
import GameState.SaveData;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class NewGameState extends BaseMenuState {

    private String text = "";
    Rectangle r = new Rectangle(200,200,250,30);

    public NewGameState(GameStateManager gsm) {
        super(gsm, new String[]{
                "Enter player name"
        }, "/Backgrounds/menubg.png");

        addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                text+=e.getKeyChar();
            }
        });
    }

    protected void select() {
        if(currentChoice == 0) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    @Override
    public void draw(Graphics2D g) {

        super.draw(g);

        g.setColor(Color.blue);
        g.fillRect(r.x, r.y, r.width, r.height);
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(text, r.x, r.y + r.height - 9);
        //help text
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Press Enter to start the game",  220, 320);
    }

    private int playerNameMaxLength = 20;

    @Override
    public void keyPressed(int k) {
        var event = gsm.getLastKeyEvent();
        var keyChar= event.getKeyChar();

        if(k == KeyEvent.VK_ENTER) {
            saveGame();
            gsm.setState(GameStateManager.LEVEL1STATE);
        }

        if(keyChar == KeyEvent.VK_BACK_SPACE) {
            if(text.length() >= 1) {
                text = removeLastChar(text);
            }
        } else {
            if(text.length() < playerNameMaxLength) {
                if(k != KeyEvent.VK_SHIFT &&
                    k != KeyEvent.VK_CAPS_LOCK &&
                    k != KeyEvent.VK_ESCAPE &&
                    k != KeyEvent.VK_CONTROL &&
                    k != KeyEvent.VK_ALT &&
                    k != KeyEvent.VK_ALT_GRAPH) {
                    text += keyChar;
                }
            }
        }
    }

    private void saveGame() {
        var saveData = new SaveData();
        saveData.name = text;
        saveData.hp = PlayerSettings.PLAYER_START_HEALTH;
        saveData.money = PlayerSettings.PLAYER_START_MONEY_AMOUNT;
        saveData.exp = PlayerSettings.PLAYER_START_EXP_AMOUNT;
        try {
            ResourceManager.save(saveData, "1.save");
            System.out.println("Save successful");
        } catch(Exception e) {
            System.out.println("Couldn't save: " + e.getMessage());
        }
    }

    public static String removeLastChar(String str) {
        return removeLastChars(str, 1);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }

}
