package Entity.Player;

import Audio.AudioPlayer;
import Entity.*;
import Entity.Enemies.Enemy;
import Entity.NPCs.NPC;
import Entity.Obstacles.Obstacle;
import Entity.Player.Magic.FireBallHandler;
import GameState.GameStateManager;
import GameState.ISaveManager;
import GameState.SaveManager;
import GameState.SaveData;
import Main.GameOptions;
import MapPoint.*;
import TileMap.Tile;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;

public class Player extends Entity.Character {
	
	// player stuff
	private int mana;
	private int maxMana;
	private boolean charging;
	private int experience;
	private double chargeSpeed;
	//name
	private String name;

	private FireBallHandler fireBallHandler;
	
	// fireball
	private boolean firing;
	private int fireballManaCost;
	private int fireBallDamage;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	private Wallet wallet;
	private int currentLevel;
	private MapPoint mapPointForLevelChange;
	private NPC npcToTalkTo;

	private ISaveManager saveManager;

	public Player(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles) {
		this(tileMaps, obstacles, new SaveManager());
	}

	public Player(ArrayList<TileMap> tileMaps, ArrayList<Obstacle> obstacles, ISaveManager saveManager) {
		super(tileMaps, obstacles, PlayerSettings.PLAYER_START_HEALTH,
				"/Sprites/Player/playersprites.gif",
				30, 30);
		this.saveManager = saveManager;
		init(tileMaps);
		loadGame();
	}

	private void init(ArrayList<TileMap> tileMaps) {
		fireBallHandler = new FireBallHandler(this, tileMaps, FIREBALL);

		//damage & health
		experience = PlayerSettings.PLAYER_START_EXP;
		fireBallDamage = PlayerSettings.PLAYER_START_FIREBALL_DAMAGE;
		scratchDamage = PlayerSettings.PLAYER_START_SCRATCH_DAMAGE;
		mana = maxMana = PlayerSettings.PLAYER_START_FIRE;
		fireballManaCost = PlayerSettings.MAGIC_FIREBALL_MANA_COST;
		scratchRange = 40;
		//size
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
		flinchDurationInMilliseconds = 1000;
		setPlayerAction(IDLE, 400);
		//other
		createWallet();
	}

	private void loadGame() {
		try {
			SaveData data = (SaveData) saveManager.load("1.save");
			health = data.health;
			name = data.name;
			experience = data.experience;
			mana = data.mana;
			wallet.addMoney(data.money);
			System.out.println("Loaded player. Name: " + data.name + ", health: " + data.health + ", level: " + data.level);
		} catch (Exception e) {
			System.out.println("Couldn't load save data: " + e.getMessage());
		}

		checkIfShouldBeDead();
	}

	public void setCurrentLevel(int level) {
		currentLevel = level;
	}

	public void saveGame() {
		var saveData = new SaveData();
		saveData.name = name;
		saveData.health = health;
		saveData.experience = experience;
		saveData.mana = mana;
		saveData.money = getMoneyInWallet();
		saveData.level = currentLevel;

		try {
			saveManager.save(saveData, "1.save");
			System.out.println("Save successful");
		} catch(Exception e) {
			System.out.println("Couldn't save: " + e.getMessage());
		}
	}

	public void increaseMana() {
		mana += 1;
		if(mana > maxMana) {
			mana = maxMana;
		}
	}

	public void decreaseMana(int magicType) {
		mana -= getManaCostByType(magicType);
	}

	public boolean hasEnoughMana(int magicType) {
		if(mana > getManaCostByType(magicType))
			return true;
		return false;
	}

	private int getManaCostByType(int magicType) {
		switch(magicType) {
			case FIREBALL:
			return fireballManaCost;
			default: throw new IllegalArgumentException("Magictype " + magicType + " is not supported");
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

	@Override
	protected int[] getAnimationFrames() {
		//Comma = new row
		//2 idle animations, 8 walking animations etc.
		return new int[]{
				2, 8, 1, 2, 4, 2, 5
		};
	}

	@Override
	protected void setSoundEffects() {
		super.setSoundEffects();
		sfx.put("jump", new AudioPlayer("/SFX/jump.wav"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.wav"));
		sfx.put("fireball", new AudioPlayer("/SFX/fireball.wav"));
		sfx.put("hit", new AudioPlayer("/SFX/playerGetsHit.wav"));
		sfx.put("death", new AudioPlayer("/SFX/playerDeath.wav"));
	}

	public int getMana() { return mana; }
	public int getMaxMana() { return maxMana; }
	
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

	public void setNPCToTalkTo(NPC npcToTalkTo) {
		this.npcToTalkTo = npcToTalkTo;
	}

	public NPC getNPCToTalkTo() {
		return this.npcToTalkTo;
	}

	public void changeLevel(GameStateManager gsm) {
		mapPointForLevelChange.playSoundEffect();
		var nextLevel = mapPointForLevelChange.getGotoLevel();
		setCurrentLevel(nextLevel);
		saveGame();
		gsm.setState(nextLevel);
	}

	public void checkAttack(ArrayList<Enemy> enemies) {
		var height = getHeight();
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
			fireBallHandler.checkFireBallCollisions(e);
			checkEnemyCollision(e);
		}
	}

	private void checkEnemyCollision(Enemy e) {
		if(isDead()) return;

		if (intersects(e)) {
			hit(e.getDamage());
		}
	}

	@Override
	protected void updatePosition() {
		super.updatePosition();
		//cannot move left or right while attacking
		if(!PlayerSettings.PLAYER_CAN_MOVE_WHILE_ATTACKING && isAttacking()) {
			preventMovingInXDirection();
		}
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

	@Override
	protected void moveLeftOrRight() {
		super.moveLeftOrRight();
		stopGoingRight();
		stopGoingLeft();
	}

	@Override
	protected void moveUpOrDown() {
		super.moveUpOrDown();
		stopGoingDown();
		stopGoingUp();
	}

	public void update() {

		fireBallHandler.update();

		super.update();

		setSpeedAccordingToGroundType();

		checkCharging();

		checkAttackHasStopped();

		updateFacingDirection();
	}

	private void checkAttackHasStopped() {
		var currentAction = getCurrentAction();
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
	}

	protected void setCurrentAction() {
		var currentAction = getCurrentAction();
		if(scratching) {
			if(currentAction != SCRATCHING) {
				playSoundEffect("scratch");
				setPlayerAction(SCRATCHING, 50);
				setWidth(60);
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				playSoundEffect("fireball");
				setPlayerAction(FIREBALL, 100);
				setWidth(30);
			}
		}
		else if(goingDown() ||
				goingUp() ||
				goingLeft() ||
				goingRight()) {
			if(currentAction != WALKING) {
				setPlayerAction(WALKING, 100);
				setWidth(30);
			}
		}
		else { //Everything else
			if(currentAction != IDLE) {
				setPlayerAction(IDLE, 400);
				setWidth(30);
			}
		}
	}

	private void setPlayerAction(int action, int i) {
		setCurrentAction(action);
		animation.setFrames(sprites.get(action));
		animation.setDelay(i);
	}

	private void updateFacingDirection() {
		if(!isAttacking()) {
			if(right) facingRight = true;
			if(left) facingRight = false;
			if(up) facingUp = true;
			if(down) facingUp = false;
		}
	}

	public int getCurrentAction() {
		return super.getCurrentAction();
	}

	public boolean getFiring() {
		return this.firing;
	}

	private boolean isAttacking() {
		var currentAction = getCurrentAction();
		return currentAction == SCRATCHING || currentAction == FIREBALL;
	}

	public void draw(Graphics2D g) {

		drawFireBalls(g);
		drawDebugStuff(g);

		super.draw(g);
	}

	private void drawDebugStuff(Graphics2D g) {
		if(GameOptions.IS_DEBUG_MODE == false)
			return;
	}


	private void drawFireBalls(Graphics2D g) {
		fireBallHandler.draw(g);
	}

	public int getFireBallDamage() {
		return fireBallDamage;
	}

	public int getScratchDamage(){
		return scratchDamage;
	}
}