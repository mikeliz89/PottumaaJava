package GameState.Menu;

import Entity.Player.PlayerSettings;
import GameState.GameStateManager;
import GameState.SaveData;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class NewGameState extends BaseMenuState {

    private int playerNameMaxLength = 20;
    private int playerNameMinLength = 3;
    private ArrayList<String> validationErrors;
    private boolean invalidInputsGiven = false;
    private String text = "";
    Rectangle customTextField = new Rectangle(200,200,300,30);

    public NewGameState(GameStateManager gsm) {
        super(gsm, new String[]{
                "Start new game"
        }, "/Backgrounds/menubg.png");
        validationErrors = new ArrayList<String>();
    }

    protected void select() {
        if(currentChoice == 0) {
            gsm.setState(GameStateManager.STATE_MAIN_MENU);
        }
    }

    @Override
    public void draw(Graphics2D g) {

        super.draw(g);

        drawPlayerNameTextField(g);
        drawHelpTexts(g);

        if(invalidInputsGiven)
            drawValidationErrors(g);
    }

    private void drawValidationErrors(Graphics2D g) {
        for(String text : validationErrors) {
            g.setColor(Color.RED);
            g.drawString(text, 220, 260);
        }
    }

    private void drawPlayerNameTextField(Graphics2D g) {
        g.setColor(Color.blue);
        g.fillRect(customTextField.x, customTextField.y, customTextField.width, customTextField.height);
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(text, customTextField.x, customTextField.y + customTextField.height - 9);
    }

    private void drawHelpTexts(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.BLACK);
        g.drawString("Type name:", 180, 180);
        g.drawString("Press Enter to start the game",  220, 310);
        g.drawString("Press Escape to cancel",  220, 330);
    }

    @Override
    public void keyPressed(int k) {
        var event = gsm.getLastKeyEvent();
        var keyChar= event.getKeyChar();

        if(k == KeyEvent.VK_ENTER) {
            validateInputs();
            if(invalidInputsGiven) {
                return;
            }
            saveGame();
            gsm.setState(GameStateManager.STATE_LEVEL_1);
        }

        if(keyChar == KeyEvent.VK_BACK_SPACE) {
            if(text.length() >= 1) {
                text = removeLastChar(text);
                validateInputs();
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
                    validateInputs();
                }
            }
        }
    }

    private void validateInputs() {
        if(text.length() < playerNameMinLength) {
            invalidInputsGiven = true;
            validationErrors.add("Name must be at least " + playerNameMinLength +" characters long");
            return;
        }
        invalidInputsGiven = false;
    }

    private void saveGame() {
        var saveData = new SaveData();
        saveData.name = text;
        saveData.health = PlayerSettings.PLAYER_START_HEALTH;
        saveData.money = PlayerSettings.PLAYER_START_MONEY;
        saveData.experience = PlayerSettings.PLAYER_START_EXP;
        saveData.fire = PlayerSettings.PLAYER_START_FIRE;
        saveData.level = GameStateManager.STATE_LEVEL_1;
        try {
            saveManager.save(saveData, "1.save");
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

    @Override
    public void keyReleased(int k) {

    }

}
