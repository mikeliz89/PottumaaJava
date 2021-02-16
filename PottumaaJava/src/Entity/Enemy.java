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

	HealthBar healthBar;

	private HashMap<String, AudioPlayer> sfx;
	
	public Enemy(java.util.ArrayList<TileMap> tileMaps, int maxHealth) {
		this.tileMaps = tileMaps;
		this.maxHealth = maxHealth;
		this.health = maxHealth;

		//todo: Tee vaihtuva healthBarin korkeus per enemy tyyppi
		healthBar = new HealthBar(3, this.maxHealth);

		setSoundEffects();
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
	
	public void update() {}

	private void Die() {
		dead = true;
		playSoundEffect("deathCry");
	}

	private void setSoundEffects() {
		sfx = new HashMap<>();
		sfx.put("deathCry", new AudioPlayer("/SFX/sluggerDeathCry.wav"));
	}

	private void playSoundEffect(String soundEffectName) {
		sfx.get(soundEffectName).play();
	}

	public int getMoneyGainedWhenKilled() {
		return moneyGainedWhenKilled;
	}

	public int getExperienceGainedWhenKilled() { return experienceGainedWhenKilled; }

	public void draw(Graphics2D g) {
		super.draw(g);
		drawHealthBar(g);
	}

	private void drawHealthBar(Graphics2D g) {
		healthBar.setX((int)this.x -10 + (int)xmap);
		healthBar.setY((int)this.y-10 + (int)ymap);
		healthBar.setWidth(this.health);
		healthBar.draw(g);
	}

}














