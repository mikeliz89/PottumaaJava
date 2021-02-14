package Entity;

import TileMap.TileMap;

public abstract class NPC extends MapObject {

	private int health;
	private int maxHealth;

	protected boolean dead;
	protected int damage;
	protected boolean flinching;
	protected long flinchTimer;

	public NPC(java.util.ArrayList<TileMap> tileMaps, int maxHealth) {
		this.tileMaps = tileMaps;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
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
	
}














