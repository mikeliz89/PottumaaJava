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

	private HashMap<String, AudioPlayer> sfx;
	
	public Enemy(java.util.ArrayList<TileMap> tileMaps, int maxHealth) {
		this.tileMaps = tileMaps;
		this.maxHealth = maxHealth;
		this.health = maxHealth;

		setSoundEffects();
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

		//todo: Voisiko olla erillinen HealthBar-luokka josta luotaisiin olio initissä,
		//ja jolle annettaisiin tarvittavat parametrit, ja jonka drawia tässä kutsuttaisiin.

		var healthPositionX = (int)this.x -10 + (int)xmap;
		var healthPositionY = (int)this.y -10 + (int)ymap;
		var height = 3;

		//piirrä healthbarin sisältö
		g.setColor(Color.RED);
		g.fillRect(healthPositionX, healthPositionY , this.health , height);

		//piirrä ääriviivat
		g.setColor(Color.BLACK);
		g.drawRect(healthPositionX, healthPositionY, this.maxHealth, height);

	}

}














