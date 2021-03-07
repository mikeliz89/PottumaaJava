package Entity.NPCs;

import Audio.AudioPlayer;
import Entity.HUD.DialogBox;
import Entity.Obstacles.Obstacle;
import TileMap.TileMap;

import java.util.ArrayList;

public class MrPotatoGuy extends NPC {

    public MrPotatoGuy(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles, int maxHealth) {
        super(tileMaps, obstacles, maxHealth,
                "/Sprites/NPC/mrpotatoguy.gif", 60, 84);
        collisionBoxWidth = 45;
        collisionBoxHeight = 70;
        damage = 100;
        name = "Mr. Potatoguy";
        profession = "Fruitseller";

        //speed
        moveSpeed = NPCSettings.MR_POTATOGUY_MOVE_SPEED;
        maxSpeed = originalMaxSpeed = NPCSettings.MR_POTATOGUY_MOVE_SPEED;
        stopSpeed = originalStopSpeed = NPCSettings.MR_POTATOGUY_STOP_SPEED;

        startMovingLeft();
    }

    private void startMovingLeft() {
        left = true;
        facingRight = false;
    }

    @Override
    public void update() {
        super.update();
        if(this.getX() <= 400) {
            left = false;
            stopGoingLeft();
            //todo: Idle animation
            animation.setDelay(10000);
        }

        if(this.getY() == 300) {
            up = false;
        }
    }

    @Override
    protected int[] getAnimationFrames() {
        return new int[]{
                1, 2, 3
        };
    }

    public DialogBox getDialogBox() {
        return new DialogBox("Welcome",
                "Mr.PotatoGuy: Welcome to the land of the pottu");
    }

    @Override
    protected void setSoundEffects() {
        sfx.put("talk", new AudioPlayer("/SFX/mrPotatoguyTalk.wav"));
    }
}
