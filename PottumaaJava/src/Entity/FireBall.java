package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class FireBall extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public FireBall(ArrayList<TileMap> tileMaps, boolean right) {
		
		this.tileMaps = tileMaps;
		
		facingRight = right;
		
		moveSpeed = 2.0;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		collisionBoxWidth = 14;
		collisionBoxHeight = 14;

		loadSprites();
	}

	private void loadSprites() {
		try {

			BufferedImage spriteSheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/fireball.gif"
				)
			);

			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}

			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spriteSheet.getSubimage(
					i * width,
					height,
					width,
					height
				);
			}

			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {

		checkTileMapCollisions();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) {
			setHit();
		}

		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}

	private void checkTileMapCollisions() {
		for (TileMap tm : tileMaps) {
			checkTileMapCollision(tm);
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
	
}


















