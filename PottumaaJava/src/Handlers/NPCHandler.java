package Handlers;

import Entity.NPCs.NPC;
import Entity.Player.Player;

import java.awt.*;
import java.util.ArrayList;

public class NPCHandler {

    private Player player;
    private ArrayList<NPC> NPCs;
    private boolean playerIsCloseEnoughToNPC = false;

    public NPCHandler(Player player) {
        this.player = player;
        NPCs = new ArrayList<>();
    }

    public void add(NPC npc) {
        NPCs.add(npc);
    }

    public boolean playerIsCloseEnoughToNPC() {
        return playerIsCloseEnoughToNPC;
    }

    public void draw(Graphics2D g) {
        for(NPC npc : NPCs) {
            npc.draw(g);
        }
    }

    public void update() {

        for (NPC npc : NPCs) {
            npc.update();
        }

        isPlayerCloseEnoughToTalkToNPC();
    }

    private void isPlayerCloseEnoughToTalkToNPC() {
        NPC npcToTalkTo = getNPCToTalkTo();
        player.setNPCToTalkTo(npcToTalkTo);
        if(npcToTalkTo != null) {
            playerIsCloseEnoughToNPC = true;
            return;
        }
        playerIsCloseEnoughToNPC = false;
    }

    private NPC getNPCToTalkTo() {
        NPC npcToTalkTo = null;
        for(NPC npc : NPCs) {
            if(npc.intersects(player)) {
                npcToTalkTo = npc;
                npc.stopMoving();
            }
        }
        return npcToTalkTo;
    }
}
