package Entity.Enemies;

import Audio.AudioPlayer;
import Entity.Obstacles.Obstacle;
import TileMap.TileMap;
import java.util.ArrayList;

public class Arachnid extends Enemy {

	public Arachnid(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles, int maxHealth) {

		super(tileMaps, obstacles, maxHealth, "/Sprites/Enemies/arachnik.gif",
				30, 30);

		moveSpeed = EnemySettings.ARACHNID_MOVE_SPEED;
		maxSpeed = originalMaxSpeed = EnemySettings.ARACHNID_MOVE_SPEED;
		fallSpeed = 0.2;
		stopSpeed = originalStopSpeed = 0.3;
		maxFallSpeed = 10.0;

		collisionBoxWidth = 20;
		collisionBoxHeight = 20;

		damage = 2;

		//suunta on yhtäaikaa ylös ja oikealle eli menee yläviistoon. MovingBall? :D
		up = true;
		facingUp = true;
		right = true;
		facingRight = true;

		moneyGainedWhenKilled = EnemySettings.ARACHNID_MONEY_GAINED_WHEN_KILLED;
		experienceGainedWhenKilled = EnemySettings.ARACHNID_EXP_GAINED_WHEN_KILLED;

		enemyType = EnemySettings.ENEMY_TYPES_ARACHNID;

	}

	@Override
	protected void setSoundEffects() {
		sfx.put("death", new AudioPlayer("/SFX/arachnidDeathCry.wav"));
		sfx.put("idle", new AudioPlayer("/SFX/eating.wav"));
		sfx.put("hit", new AudioPlayer("/SFX/arachnidHit.wav"));
	}

	@Override
	protected int[] getAnimationFrames() {
		return new int[]{
				1
		};
	}

}