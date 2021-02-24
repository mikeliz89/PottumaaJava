package Entity.NPCs;

import Entity.Animation;
import Entity.HealthBar;
import Entity.MapObject;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//todo: Yhteinen base class NPC/ENEMY/Player -luokille, esim Character-class?
public abstract class NPC extends MapObject {

	private int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean flinching;
	protected long flinchTimer;
	protected String name;
	protected String profession;
	protected BufferedImage[] sprites;
	HealthBar healthBar;

	public NPC(ArrayList<TileMap> tileMaps, int maxHealth) {
		this.tileMaps = tileMaps;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		healthBar = new HealthBar(5, maxHealth);
		animation = new Animation();
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

	public void draw(Graphics2D g) {

		super.draw(g);
		drawHealthBar(g);
		drawName(g);
	}

	private void drawHealthBar(Graphics2D g) {
		healthBar.setX((int)this.x -10 + (int) xMap);
		healthBar.setY((int)this.y -20 + (int) yMap);
		healthBar.setWidth(this.health);
		healthBar.draw(g);
	}

	private void drawName(Graphics2D g) {
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		g.setColor(Color.BLACK);
		g.drawString(name, (int)this.x-20 + (int) xMap, (int)this.y - 25 + (int) yMap);
		g.setColor(Color.CYAN);
		g.drawString(profession, (int)this.x-20 +(int) xMap, (int)this.y -10 + (int) yMap);
	}
	
}














