package Entity.Player;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.Enemy;
import Entity.Obstacles.Obstacle;
import GameState.GameStateManager;
import GameState.ISaveManager;
import GameState.SaveManager;
import GameState.SaveData;
import Main.GameOptions;
import MapPoint.*;
import TileMap.Tile;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

//todo: Yhteinen base class NPC/ENEMY/Player -luokille, esim Character-class?
public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private boolean charging;
	private int experience;
	private double chargeSpeed;
	//name
	private String name;
	
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		2, 8, 1, 2, 4, 2, 5
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	private int currentAction;
	
	private HashMap<String, AudioPlayer> sfx;

	private Wallet wallet;
	private int currentLevel;
	private MapPoint mapPointForLevelChange;

	private ISaveManager saveManager;

	public Player(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles) {
		this(tileMaps, obstacles, new SaveManager());
	}

	public Player(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles, ISaveManager saveManager) {
		this.saveManager = saveManager;
		this.tileMaps = tileMaps;
		this.obstacles = obstacles;
		init();
		//damage & health
		experience = PlayerSettings.PLAYER_START_EXP;
		fireBallDamage = PlayerSettings.PLAYER_START_FIREBALL_DAMAGE;
		scratchDamage = PlayerSettings.PLAYER_START_SCRATCH_DAMAGE;
		health = maxHealth = PlayerSettings.PLAYER_START_HEALTH;
		fire = maxFire = PlayerSettings.PLAYER_START_FIRE;
		fireCost = 200;
		scratchRange = 40;
		//size
		width = 30;
		height = 30;
		collisionBoxWidth = 20;
		collisionBoxHeight = 20;
		//speed
		moveSpeed = PlayerSettings.PLAYER_MOVE_SPEED;
		maxSpeed = originalMaxSpeed = PlayerSettings.PLAYER_MAX_SPEED;
		stopSpeed = originalStopSpeed = 0.4; //grass
		fallSpeed = 0.10;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		chargeSpeed = 2.4;
		facingRight = true;
        //animation & sprites
		loadSprites();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		//other
		createWallet();
		//audio
		setSoundEffects();
		//load game
		loadGame();
	}

	private void init() {
		fireBalls = new ArrayList<>();
		animation = new Animation();
	}

	private void loadGame() {
		try {
			SaveData data = (SaveData) saveManager.load("1.save");
			health = data.health;
			name = data.name;
			experience = data.experience;
			fire = data.fire;
			wallet.addMoney(data.money);
			System.out.println("Loaded player. Name: " + data.name + ", health: " + data.health + ", level: " + data.level);
		} catch (Exception e) {
			System.out.println("Couldn't load save data: " + e.getMessage());
		}

		checkIfPlayerShouldBeDead();
	}

	public void setCurrentLevel(int level) {
		currentLevel = level;
	}

	public void saveGame() {
		var saveData = new SaveData();
		saveData.name = name;
		saveData.health = health;
		saveData.experience = experience;
		saveData.fire = fire;
		saveData.money = getMoneyInWallet();
		saveData.level = currentLevel;

		try {
			saveManager.save(saveData, "1.save");
			System.out.println("Save successful");
		} catch(Exception e) {
			System.out.println("Couldn't save: " + e.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	private void createWallet() {
		wallet = new Wallet();
	}

	public int getMoneyInWallet() {
		return wallet.getMoney();
	}

	public void addMoney(int money) {
		wallet.addMoney(money);
	}

	public boolean hasLowHealth () {
		if(health <= 2 && maxHealth >= 5)
			return true;
		else if(health == 1 && maxHealth < 5)
			return true;
		return false;
	}

	public void addExperience(int exp) {
		experience += exp;
	}

	public int getExperience() {
		return experience;
	}

 	private void loadSprites() {
		try {

			BufferedImage spriteSheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/playersprites.gif"
				)
			);

			sprites = new ArrayList<>();
			for(int i = 0; i < 7; i++) {

				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];

				for(int j = 0; j < numFrames[i]; j++) {

					if(i != SCRATCHING) {
						bi[j] = spriteSheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spriteSheet.getSubimage(
								j * width * 2,
								i * height,
								width * 2,
								height
						);
					}

				}

				sprites.add(bi);

			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void setSoundEffects() {
		sfx = new HashMap<>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.wav"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.wav"));
		sfx.put("fireball", new AudioPlayer("/SFX/fireball.wav"));
		sfx.put("playerGetsHit", new AudioPlayer("/SFX/playerGetsHit.wav"));
		sfx.put("playerDeath", new AudioPlayer("/SFX/playerDeath.wav"));
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	public void setFiring() { 
		firing = true;
	}
	public void setScratching() {
		scratching = true;
	}

	private void setSpeedAccordingToGroundType() {
		for (TileMap ground : getTileMaps(Tile.TILE_TYPE_GROUND)) {
			// On ice player goes faster and breaks slower
			if (ground.getTileFrictionType(bottomTile, leftTile) == Tile.TILE_FRICTION_TYPE_ICE ||
					ground.getTileFrictionType(bottomTile, rightTile) == Tile.TILE_FRICTION_TYPE_ICE) {
				this.stopSpeed = originalStopSpeed / 100; //TODO: take these values to the tile class..
				this.maxSpeed = originalMaxSpeed * 1.2; //TODO: take these values to the tile class..
			} else {
				this.stopSpeed = originalStopSpeed;
				this.maxSpeed = originalMaxSpeed;
			}
		}
	}

	public void setMapPointForLevelChange(MapPoint mapPointForLevelChange) {
		this.mapPointForLevelChange = mapPointForLevelChange;
	}

	public void changeLevel(GameStateManager gsm) {
		mapPointForLevelChange.playSoundEffect();
		var nextLevel = mapPointForLevelChange.getGotoLevel();
		setCurrentLevel(nextLevel);
		saveGame();
		gsm.setState(nextLevel);
	}

	public void checkAttack(ArrayList<Enemy> enemies) {

		for (Enemy e : enemies) {
			if (scratching) {
				if (facingRight) {
					if (
							e.getX() > x &&
									e.getX() < x + scratchRange &&
									e.getY() > y - height / 2 &&
									e.getY() < y + height / 2
					) {
						e.hit(scratchDamage);
					}
				} else {
					if (
							e.getX() < x &&
									e.getX() > x - scratchRange &&
									e.getY() > y - height / 2 &&
									e.getY() < y + height / 2
					) {
						e.hit(scratchDamage);
					}
				}
			}
			checkFireBallCollisions(e);
			checkEnemyCollision(e);
		}
	}

	private void checkFireBallCollisions(Enemy e) {
		for (FireBall fireBall : fireBalls) {
			if (fireBall.intersects(e)) {
				e.hit(fireBallDamage);
				fireBall.setHit();
				break;
			}
		}
	}

	private void checkEnemyCollision(Enemy e) {
		if(dead) return;

		if (intersects(e)) {
			hit(e.getDamage());
		}
	}

	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		checkIfPlayerShouldBeDead();
		flinching = true;
		flinchTimer = System.nanoTime();
		playSoundEffect("playerGetsHit");
	}

	private void checkIfPlayerShouldBeDead() {
		if(health < 0) health = 0;
		if(health == 0) die();
	}

	@Override
	protected void updatePosition() {

		moveUpOrDown();
		moveLeftOrRight();

		//cannot move left or right while attacking
		if(!PlayerSettings.PLAYER_CAN_MOVE_WHILE_ATTACKING && isAttacking()) {
			dx = 0;
		}

		jumping();
	}

	private void die() {
		dead = true;
		playSoundEffect("playerDeath");
	}

	public boolean isDead() {
		return dead;
	}

	public void setCharging(boolean isCharging) {
		charging = isCharging;
	}

	private void checkCharging() {
		if(charging) {
			this.setStopSpeed(this.stopSpeed / 3);
			this.setMaxSpeed(this.maxSpeed + chargeSpeed);
		}
		else {
			this.setMaxSpeed(this.originalMaxSpeed);
		}
	}

	private void jumping() {
		if(jumping) {
			playSoundEffect("jump");
			dy = jumpStart;
		}
	}

	private void moveLeftOrRight() {
		if(left) {
			moveLeft();
		}
		else if(right) {
			moveRight();
		}
		else {
			stopGoingRight();
			stopGoingLeft();
		}
	}

	private void stopGoingLeft() {
		if(goingLeft()) {
			dx += stopSpeed;
			if(goingRight()) {
				dx = 0;
			}
		}
	}

	private void stopGoingRight() {
		if(goingRight()) {
			dx -= stopSpeed;
			if(goingLeft()) {
				dx = 0;
			}
		}
	}

	private void moveRight() {
		dx += moveSpeed;
		if(dx > maxSpeed) {
			dx = maxSpeed;
		}
	}

	private void moveLeft() {
		dx -= moveSpeed;
		if(dx < -maxSpeed) {
			dx = -maxSpeed;
		}
	}

	private void moveUpOrDown() {
		if(up) {
			moveUp();
		}
		else if(down) {
			moveDown();
		}
		else {
			stopGoingDown();
			stopGoingUp();
		}
	}

	private void stopGoingUp() {
		if(goingUp()) {
			dy += stopSpeed;
			if(goingDown()) {
				dy = 0;
			}
		}
	}

	private void stopGoingDown() {
		if(goingDown()) {
			dy -= stopSpeed;
			if(goingUp()) {
				dy = 0;
			}
		}
	}

	private void moveDown() {
		dy += moveSpeed;
		if(dy > maxSpeed) {
			dy = maxSpeed;
		}
	}

	private void moveUp() {
		dy -= moveSpeed;
		if(dy < -maxSpeed) {
			dy = -maxSpeed;
		}
	}

	private void playSoundEffect(String soundEffectName) {
		sfx.get(soundEffectName).play();
	}
	
	public void update() {

		super.update();

		setSpeedAccordingToGroundType();

		checkCharging();

		updatePosition();

		checkAttackHasStopped();

		fireBallAttack();

		updateFireBalls();

		flinching();

		setAnimation();

		updateFacingDirection();

	}

	private void flinching() {
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
	}

	private void checkAttackHasStopped() {
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
	}

	private void fireBallAttack() {
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) {
				fire -= fireCost;
				FireBall fb = new FireBall(tileMaps, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
			}
		}
	}

	private void setAnimation() {
		if(scratching) {
			if(currentAction != SCRATCHING) {
				playSoundEffect("scratch");
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				playSoundEffect("fireball");
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(goingDown() ||
				goingUp() ||
				goingLeft() ||
				goingRight()) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else { //Everything else
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
	}

	private void updateFireBalls() {
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
	}

	private void updateFacingDirection() {
		if(!isAttacking()) {
			if(right) facingRight = true;
			if(left) facingRight = false;
			if(up) facingUp = true;
			if(down) facingUp = false;
		}
	}

	private boolean isAttacking() {
		return currentAction == SCRATCHING || currentAction == FIREBALL;
	}

	public void draw(Graphics2D g) {

		drawFireBalls(g);

		//Player is not drawn when flinching etc.
		if (drawPlayer()) return;

		drawDebugStuff(g);

		super.draw(g);
	}

	private void drawDebugStuff(Graphics2D g) {
		if(GameOptions.IS_DEBUG_MODE == false)
			return;
	}

	private boolean drawPlayer() {
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return true;
			}
		}
		return false;
	}

	private void drawFireBalls(Graphics2D g) {
		for (FireBall fireBall : fireBalls) {
			fireBall.draw(g);
		}
	}

	public int getFireBallDamage() {
		return fireBallDamage;
	}

	public int getScratchDamage(){
		return scratchDamage;
	}
}

















