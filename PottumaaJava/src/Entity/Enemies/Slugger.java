package Entity.Enemies;

import Audio.AudioPlayer;
import Entity.Obstacles.Obstacle;
import TileMap.TileMap;
import java.util.ArrayList;

public class Slugger extends Enemy {

	public Slugger(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles, int maxHealth) {

		super(tileMaps, obstacles, maxHealth, "/Sprites/Enemies/slugger.gif",
				30, 30);

		moveSpeed = EnemySettings.SLUGGER_MOVE_SPEED;
		maxSpeed = originalMaxSpeed = EnemySettings.SLUGGER_MOVE_SPEED;
		fallSpeed = 0.2;
		stopSpeed = originalStopSpeed = 0.3;
		maxFallSpeed = 10.0;

		collisionBoxWidth = 20;
		collisionBoxHeight = 20;

		damage = 1;

		right = true;
		facingRight = true;

		moneyGainedWhenKilled = EnemySettings.SLUGGER_MONEY_GAINED_WHEN_KILLED;
		experienceGainedWhenKilled = EnemySettings.SLUGGER_EXP_GAINED_WHEN_KILLED;

		enemyType = EnemySettings.ENEMY_TYPES_SLUGGER;

	}

	@Override
	protected void setSoundEffects() {
		sfx.put("death", new AudioPlayer("/SFX/sluggerDeathCry.wav"));
		sfx.put("idle", new AudioPlayer("/SFX/slimy.wav"));
		sfx.put("hit", new AudioPlayer("/SFX/sluggerHit.wav"));
	}

	@Override
	protected int[] getAnimationFrames() {
		return new int[]{
				1, 2, 3
		};
	}
}