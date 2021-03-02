package Entity.Player.Magic;

import Entity.Enemies.Enemy;
import Entity.FireBall;
import Entity.Player.Player;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

public class FireBallHandler {

    private ArrayList<TileMap> tileMaps;
    private ArrayList<FireBall> fireBalls;
    private Player player;
    private int magicType;

    public FireBallHandler(Player player, ArrayList<TileMap> tileMaps, int magicType) {
        this.player = player;
        this.tileMaps = tileMaps;
        this.magicType = magicType;
        fireBalls = new ArrayList<>();
    }

    public void update() {
        player.increaseMana();
        fireBallAttack();
        updateFireBalls();
    }

    private void fireBallAttack() {
        var currentAction = player.getCurrentAction();
        if(player.getFiring() && currentAction != magicType) {
            if(player.hasEnoughMana(magicType)) {
                player.decreaseMana(magicType);
                FireBall fb = new FireBall(tileMaps, player.isFacingRight());
                fb.setPosition(player.getX(), player.getY());
                fireBalls.add(fb);
            }
        }
    }

    private void updateFireBalls() {
        for(int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).update();
            if(fireBalls.get(i).shouldRemove()) {
                fireBalls.remove(i);
                i--;
            }
        }
    }

    public void checkFireBallCollisions(Enemy enemy) {
        for (FireBall fireBall : fireBalls) {
            if (fireBall.intersects(enemy)) {
                enemy.hit(player.getFireBallDamage());
                fireBall.setHit();
                break;
            }
        }
    }

    public void draw(Graphics2D g) {
        for (FireBall fireBall : fireBalls) {
            fireBall.draw(g);
        }
    }
}
