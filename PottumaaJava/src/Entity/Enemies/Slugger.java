package Entity.Enemies;

import Audio.AudioPlayer;
import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashMap;

public class Slugger extends Enemy {
	
	private BufferedImage[] sprites;
	private HashMap<String, AudioPlayer> sfx;

	public Slugger(ArrayList<TileMap> tileMaps, int maxHealth) {
		
		super(tileMaps, maxHealth);
		
		moveSpeed = 0.3;
		maxSpeed = originalMaxSpeed = 0.3;
		fallSpeed = 0.2;
		stopSpeed = originalStopSpeed = 0.3;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		collisionBoxWidth = 20;
		collisionBoxHeight = 20;

		damage = 1;

		loadSprites();

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;

		moneyGainedWhenKilled = EnemySettings.SLUGGER_MONEY_GAINED_WHEN_KILLED;
		experienceGainedWhenKilled = EnemySettings.SLUGGER_EXP_GAINED_WHEN_KILLED;

		setSoundEffects();
	}

	private void loadSprites() {
		try {

			BufferedImage spriteSheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/slugger.gif"
				)
			);

			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
		
	}
	
	public void update() {

		//playSoundEffect("slimy");

		// update position
		getNextPosition();

		for (TileMap tm : tileMaps) {
			checkTileMapCollision(tm);
		}
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// if it hits a wall, go other direction
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
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g);
		
	}

	private void setSoundEffects() {
		sfx = new HashMap<>();
		sfx.put("slimy", new AudioPlayer("/SFX/slimy.wav"));
	}

	private void playSoundEffect(String soundEffectName) {
		sfx.get(soundEffectName).play();
	}
	
}











