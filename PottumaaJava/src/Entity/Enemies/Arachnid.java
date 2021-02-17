package Entity.Enemies;

import Audio.AudioPlayer;
import Entity.Enemy;
import TileMap.TileMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Arachnid extends Enemy {

	private BufferedImage[] sprites;

	public Arachnid(ArrayList<TileMap> tileMaps, int maxHealth) {
		
		super(tileMaps, maxHealth);
		
		moveSpeed = 0.2;
		maxSpeed = originalMaxSpeed = 0.2;
		fallSpeed = 0.2;
		stopSpeed = originalStopSpeed = 0.3;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		collisionBoxWidth = 20;
		collisionBoxHeight = 20;

		damage = 2;

		loadSprites();

		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;

		moneyGainedWhenKilled = EnemySettings.ARACHNID_MONEY_GAINED_WHEN_KILLED;
		experienceGainedWhenKilled = EnemySettings.ARACHNID_EXP_GAINED_WHEN_KILLED;

		enemyType = EnemySettings.ENEMY_TYPES_ARACHNID;

		setSoundEffects();
	}

	private void loadSprites() {
		try {

			BufferedImage spriteSheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/arachnik.gif"
				)
			);

			sprites = new BufferedImage[1];
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

	@Override
	protected void updatePosition() {
		
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

	private void setSoundEffects() {
		sfx.put("deathCry", new AudioPlayer("/SFX/arachnidDeathCry.wav"));
		sfx.put("idleSound", new AudioPlayer("/SFX/eating.wav"));
	}
	
}











