package Entity.NPCs;

import Entity.HealthBar;
import Entity.MapObject;
import TileMap.TileMap;

import java.awt.*;

public abstract class NPC extends MapObject {

	private int health;
	private int maxHealth;

	protected boolean dead;
	protected int damage;
	protected boolean flinching;
	protected long flinchTimer;
	protected String name;

	HealthBar healthBar;

	public NPC(java.util.ArrayList<TileMap> tileMaps, int maxHealth) {
		this.tileMaps = tileMaps;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		healthBar = new HealthBar(5, maxHealth);
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

	public void draw(Graphics2D g) {
		super.draw(g);
		drawHealthBar(g);
		drawName(g);
	}

	private void drawHealthBar(Graphics2D g) {
		healthBar.setX((int)this.x -10 + (int)xmap);
		healthBar.setY((int)this.y -20 + (int)ymap);
		healthBar.setWidth(this.health);
		healthBar.draw(g);
	}

	private void drawName(Graphics2D g) {
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		g.setColor(Color.BLACK);
		g.drawString(name, (int)this.x-20 + (int)xmap, (int)this.y - 25 + (int) ymap);
	}
	
}














