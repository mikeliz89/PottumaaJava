package Entity;

import Audio.AudioPlayer;
import Entity.Enemies.Enemy;
import Entity.NPCs.NPC;
import Entity.Obstacles.Obstacle;
import Entity.Player.Player;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Character extends MapObject {

    private boolean flinching;
    protected long flinchTimer;
    protected int flinchDurationInMilliseconds;

    protected int damage;
    protected int maxHealth;
    protected int health;
    private boolean dead;

    protected ArrayList<BufferedImage[]> sprites;
    protected HashMap<String, AudioPlayer> sfx;
    private HealthBar healthBar;
    private String spriteSheetName;
    private int currentAction;
    // animations
    private final int SCRATCHING = 6;

    public Character(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles, int maxHealth,
                     String spriteSheetName, int width, int height) {
        this.tileMaps = tileMaps;
        this.obstacles = obstacles;
        this.health = this.maxHealth = maxHealth;
        this.spriteSheetName = spriteSheetName;
        this.setWidth(width);
        this.setHeight(height);
        init();
        setSoundEffects();
        loadSprites();
    }

    private void init() {
        sfx = new HashMap<>();
        animation = new Animation();
        healthBar = new HealthBar(3, this.maxHealth);
        sprites = new ArrayList<>();
    }

    protected abstract int[] getAnimationFrames();
    protected abstract void setCurrentAction();

    private void loadSprites() {

        try {

            BufferedImage spriteSheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            spriteSheetName
                    )
            );

            if(this instanceof Player) {

                var animationFrames = getAnimationFrames();
                var animationsCount = animationFrames.length;
                for (int row = 0; row < animationsCount; row++) {

                    BufferedImage[] images = new BufferedImage[animationFrames[row]];

                    for (int column = 0; column < animationFrames[row]; column++) {
                        var subImage = getSubImage(spriteSheet, row, column);
                        images[column] = subImage;
                    }
                    sprites.add(images);
                }
            } else if(this instanceof Enemy ||
                      this instanceof NPC) {
                var animationFrames = getAnimationFrames();
                var animationsCount = animationFrames.length;
                BufferedImage[] images =
                        new BufferedImage[animationFrames[animationsCount-1]];

                for (int column = 0; column < animationsCount; column++) {
                    var subImage = spriteSheet.getSubimage(
                            column * getWidth(),
                            0,
                            getWidth(),
                            getHeight()
                    );
                    images[column] = subImage;
                }

                sprites.add(images);
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    protected BufferedImage getSubImage(BufferedImage spriteSheet, int i, int j) {
        var width = getWidth();
        var height = getHeight();

        if (i != SCRATCHING) {
            var x = j * width;
            var y = i * height;
            var subImage = spriteSheet.getSubimage(x, y,
                    width, height
            );
            return subImage;
        } else {
            var x = j * width * 2;
            var y = i * height;
            var subImageWidth = width * 2;
            var subImage = spriteSheet.getSubimage(x,y,
                    subImageWidth, height
            );
            return subImage;
        }
    }

    public void hit(int damage) {
        if(isFlinching()) return;
        health -= damage;
        checkIfShouldBeDead();
        setFlinching();
        flinchTimer = System.nanoTime();
        playSoundEffect("hit");
    }

    public boolean isDead() {
        return dead;
    }

    protected void setDead() {
        dead = true;
        playSoundEffect("death");
    }

    protected boolean isFlinching() {
        return flinching;
    }

    protected void setFlinching() {
        flinching = true;
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }

    public int getDamage() { return damage; }

    protected void checkIfShouldBeDead() {
        if(health < 0) health = 0;
        if(health == 0) setDead();
    }

    protected void playSoundEffect(String soundEffectName) {
        sfx.get(soundEffectName).play();
    }

    protected void setSoundEffects() {

    }

    public void update() {
        super.update();
        updateFlinching();
        setCurrentAction();
    }

    public void draw(Graphics2D g) {
        if(!drawMe()) {
            return;
        }
        super.draw(g);
        drawHealthBar(g);
    }

    protected int getCurrentAction() {
        return currentAction;
    }

    protected void setCurrentAction(int action) {
        currentAction = action;
    }

    private boolean drawMe() {
        if(isFlinching()) {
            long elapsed =
                    (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0) {
                return false;
            }
        }
        return true;
    }

    private void drawHealthBar(Graphics2D g) {
        healthBar.setX((int)this.x -10 + (int) xMap);
        healthBar.setY((int)this.y -10 + (int) yMap);
        healthBar.setWidth(this.health);
        healthBar.draw(g);
    }

    private void updateFlinching() {
        if(flinching) {
            long elapsed =
                (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > flinchDurationInMilliseconds) {
                flinching = false;
            }
        }
    }

    @Override
    protected void updatePosition() {
        moveUpOrDown();
        moveLeftOrRight();
    }

    protected void moveUpOrDown() {
        if(up) {
            moveUp();
        }
        else if(down) {
            moveDown();
        }
    }

    protected void moveLeftOrRight() {
        if(left) {
            moveLeft();
        }
        else if(right) {
            moveRight();
        }
    }

}
