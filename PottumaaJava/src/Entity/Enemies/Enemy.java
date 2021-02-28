package Entity.Enemies;

import Entity.Obstacles.Obstacle;
import TileMap.*;

import java.util.ArrayList;

public abstract class Enemy extends Entity.Character {

	protected int moneyGainedWhenKilled;
	protected int experienceGainedWhenKilled;
	protected int enemyType;

	private static final int WALKING = 0;
	
	public Enemy(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles,
				 int maxHealth, String spriteSheetName, int width, int height) {
		super(tileMaps, obstacles, maxHealth, spriteSheetName, width, height);
		this.tileMaps = tileMaps;
		this.obstacles = obstacles;
		init();
	}

	private void init() {
		flinchDurationInMilliseconds = 400;
		animation.setFrames(sprites.get(WALKING));
		animation.setDelay(300);
	}

	public int getEnemyType() {
		return enemyType;
	}
	
	public void update() {
		super.update();
		checkWallHits();
	}

	@Override
	protected void setCurrentAction() {

		var currentAction = getCurrentAction();
		if(currentAction != WALKING) {
			setCurrentAction(WALKING);
			animation.setFrames(sprites.get(WALKING));
			animation.setDelay(50);
		}
	}

	//if it hits a wall, go other direction
	private void checkWallHits() {
		//right and left
		if(right && getVectorX() == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && getVectorX() == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		//up and down
		if(up && getVectorY() == 0) {
			up = false;
			down = true;
			facingUp = false;
		}
		else if(down && getVectorY() == 0) {
			up = true;
			down = false;
			facingUp = true;
		}
	}

	public int getMoneyGainedWhenKilled() {
		return moneyGainedWhenKilled;
	}

	public int getExperienceGainedWhenKilled() { return experienceGainedWhenKilled; }

}