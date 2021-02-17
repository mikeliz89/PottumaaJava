package Entity;

import Audio.AudioPlayer;
import TileMap.TileMap;

import java.awt.*;
import java.util.HashMap;

public abstract class Enemy extends MapObject {

	private int health;
	private int maxHealth;
	protected int moneyGainedWhenKilled;
	protected int experienceGainedWhenKilled;
	protected boolean dead;
	protected int damage;
	protected boolean flinching;
	protected long flinchTimer;
	protected int enemyType;
	private HealthBar healthBar;
	protected HashMap<String, AudioPlayer> sfx;
	
	public Enemy(java.util.ArrayList<TileMap> tileMaps, int maxHealth) {
		this.tileMaps = tileMaps;
		this.maxHealth = maxHealth;
		this.health = maxHealth;

		//todo: Tee vaihtuva healthBarin korkeus per enemy tyyppi
		healthBar = new HealthBar(3, this.maxHealth);
		sfx = new HashMap<>();
		animation = new Animation();
	}

	public int getEnemyType() {
		return enemyType;
	}
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) Die();
		flinching = true;
		flinchTimer = System.nanoTime();
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}

	public int getHealth() {
		return this.health;
	}
	
	public void update() {
		updatePosition();

		checkCollisions();

		setPosition(xtemp, ytemp);

		updateFlinching();

		checkWallHits();

		updateAnimation();
	}

	//if it hits a wall, go other direction
	private void checkWallHits() {
		//right and left
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		//up and down
		if(up && dy == 0) {
			up = false;
			down = true;
			facingUp = false;
		}
		else if(down && dy == 0) {
			up = true;
			down = false;
			facingUp = true;
		}
	}

	private void updateFlinching() {
		if(flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
	}

	private void updateAnimation() {
		animation.update();
	}
	
	private void checkCollisions() {
		for (TileMap tm : tileMaps) {
			checkTileMapCollision(tm);
		}
	}

	protected void updatePosition() {

	}

	private void Die() {
		dead = true;
		playSoundEffect("deathCry");
	}

	private void playSoundEffect(String soundEffectName) {
		sfx.get(soundEffectName).play();
	}

	public int getMoneyGainedWhenKilled() {
		return moneyGainedWhenKilled;
	}

	public int getExperienceGainedWhenKilled() { return experienceGainedWhenKilled; }

	public void draw(Graphics2D g) {
		//Enemy is not drawn when flinching etc.
		if (drawEnemy()) return;

		super.draw(g);
		setMapPosition();
		drawHealthBar(g);
	}

	private boolean drawEnemy() {
		if(flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return true;
			}
		}
		return false;
	}

	private void drawHealthBar(Graphics2D g) {
		healthBar.setX((int)this.x -10 + (int)xmap);
		healthBar.setY((int)this.y-10 + (int)ymap);
		healthBar.setWidth(this.health);
		healthBar.draw(g);
	}

}














