package Entity.Enemies;

import Audio.AudioPlayer;
import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Slugger extends Enemy {
	
	private BufferedImage[] sprites;

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

		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;

		moneyGainedWhenKilled = EnemySettings.SLUGGER_MONEY_GAINED_WHEN_KILLED;
		experienceGainedWhenKilled = EnemySettings.SLUGGER_EXP_GAINED_WHEN_KILLED;

		enemyType = EnemySettings.ENEMY_TYPES_SLUGGER;

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

	private void setSoundEffects() {
		sfx.put("deathCry", new AudioPlayer("/SFX/sluggerDeathCry.wav"));
		sfx.put("idleSound", new AudioPlayer("/SFX/slimy.wav"));
	}
	
}











