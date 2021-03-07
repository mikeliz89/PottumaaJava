package Handlers;

import Entity.HUD.HUD;
import Entity.Player.Player;
import GameState.GameStateManager;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class KeyboardController {

    private ArrayList<Integer> keysPressed;
    private Player player;
    private HUD hud;
    private NPCHandler npcHandler;
    private GameStateManager gsm;
    private MapPointHandler mapPointHandler;

    public KeyboardController(GameStateManager gsm, Player player, HUD hud, NPCHandler npcHandler, MapPointHandler mapPointHandler) {
        this.player = player;
        this.hud = hud;
        this.npcHandler = npcHandler;
        this.gsm = gsm;
        this.mapPointHandler = mapPointHandler;
        keysPressed = new ArrayList<>();
    }

    public void keyPressed(int k) {
        if(keysPressed.contains(k) == false) {
            keysPressed.add(k);
        }

        //have to press running before can charge
        if(keysPressed.contains(KeyEvent.VK_LEFT) == true ||
                keysPressed.contains(KeyEvent.VK_RIGHT) == true ||
                keysPressed.contains(KeyEvent.VK_UP) == true ||
                keysPressed.contains(KeyEvent.VK_DOWN) == true) {
            if(keysPressed.contains(KeyEvent.VK_SHIFT)) {
                player.setCharging(true);
            }
        }

        if(k == KeyEvent.VK_LEFT) {
            player.setLeft(true);
        }
        if(k == KeyEvent.VK_RIGHT) {
            if(keysPressed.contains(KeyEvent.VK_RIGHT) == true) {
                if(k == KeyEvent.VK_SHIFT) {
                    player.setCharging(true);
                }
            }
            player.setRight(true);
        }
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_R) player.setScratching();
        if(k == KeyEvent.VK_F) player.setFiring();

        if(k == KeyEvent.VK_I) hud.toggleInventory();
        if(k == KeyEvent.VK_M) hud.toggleMap();
        if(k == KeyEvent.VK_J) hud.toggleDialogBox();
        if(k == KeyEvent.VK_Q) hud.toggleQuestLog();
        if(k == KeyEvent.VK_F5) hud.togglePauseMenu(); //F5 = Tallentaa pelin

        if(k == KeyEvent.VK_1) quickTravel(GameStateManager.STATE_LEVEL_1);
        if(k == KeyEvent.VK_2) quickTravel(GameStateManager.STATE_LEVEL_2);
        if(k == KeyEvent.VK_3) quickTravel(GameStateManager.STATE_PLAYER_HOME);

        if(mapPointHandler.playerIsInMapPoint()) {
            if(k == KeyEvent.VK_E) {
                player.changeLevel(gsm);
            }
        }

        if(npcHandler.playerIsCloseEnoughToNPC()) {
            if(k == KeyEvent.VK_E) {
                var npc = player.getNPCToTalkTo();
                var dialogBox = npc.getDialogBox();
                npc.talk();
                hud.setDialogBox(dialogBox);
                hud.toggleDialogBox();
            }
        }
    }

    public void keyReleased(int k) {
        if(k == KeyEvent.VK_SHIFT) {
            player.setCharging(false);
        }
        if(k == KeyEvent.VK_LEFT) {
            player.setLeft(false);
        }
        if(k == KeyEvent.VK_RIGHT) {
            player.setRight(false);
        }
        //NOTE: if next 2 are commented, player keeps going down or up
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);

        // remove released keys from keysPressed arrayList
        if(keysPressed.contains(k) == true) {
            keysPressed.remove(keysPressed.indexOf(k));
        }
    }

    private void quickTravel(int levelNumber) {
        gsm.setState(levelNumber);
    }
}
