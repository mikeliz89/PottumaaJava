package Entity.Player;

import Audio.AudioPlayer;
import Entity.*;
import GameState.GameStateManager;
import Main.GameOptions;
import TileMap.MapPoint;
import TileMap.Tile;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

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
	private int experiencePoints;
	
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	// scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		2, 8, 1, 2, 4, 2, 5
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	//private static final int JUMPING = 2;
	//private static final int FALLING = 3;
	//private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	
	private HashMap<String, AudioPlayer> sfx;

	private Wallet wallet;
	
	public Player(ArrayList<TileMap> tileMaps) {
		this.tileMaps = tileMaps;
		width = 30;
		height = 30;
		collisionBoxWidth = 20;
		collisionBoxHeight = 20;
		
		moveSpeed = 0.1;
		maxSpeed = originalMaxSpeed = 0.6;
		stopSpeed = originalStopSpeed = 0.4; //grass
		fallSpeed = 0.10;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		chargeSpeed = 2.4;
		
		facingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 2500;

		experiencePoints = PlayerSettings.PLAYER_START_EXP_AMOUNT;
		
		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<>();
		
		scratchDamage = 8;
		scratchRange = 40;

		loadSprites();

		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);

		createWallet();

		setSoundEffects();
	}

	private void createWallet() {
		wallet = new Wallet(PlayerSettings.PLAYER_START_MONEY_AMOUNT);
	}

	public int getMoneyInWallet() {
		return wallet.GetMoneyAmount();
	}

	public void addMoney(int money) {
		wallet.AddMoney(money);
	}

	public boolean hasLowHealth () {
		if(health <= 2 && maxHealth >= 5)
			return true;
		else if(health == 1 && maxHealth < 5)
			return true;
		return false;
	}

	public void addExperience(int exp) {
		experiencePoints += exp;
	}

	public int getExperiencePointsAmount() {
		return experiencePoints;
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
	public void setGliding(boolean b) { 
		gliding = b;
	}
	private void checkIce() {

		//check all grounds for ice
		for (TileMap tileMapGround : tileMaps) {

			// On ice player goes faster and breaks slower
			if (tileMapGround.getType() == TileMap.GROUND) {
				if (tileMapGround.getTileFrictionType(bottomTile, leftTile) == Tile.ICE
						|| tileMapGround.getTileFrictionType(bottomTile, rightTile) == Tile.ICE) {
					this.stopSpeed = originalStopSpeed / 100; //TODO: take these values to the tile class..
					this.maxSpeed = originalMaxSpeed * 1.2; //TODO: take these values to the tile class..
				} else {
					this.stopSpeed = originalStopSpeed;
					this.maxSpeed = originalMaxSpeed;
				}
			}
		}
	}
	
	public void checkLevelPoints(MapPoint levelPoint, GameStateManager gsm) {
	
//		System.out.println("player x " + x);
//		System.out.println("player y " + y);
//		System.out.println("x " + levelPoint.getx());
//		System.out.println("y " + levelPoint.gety());
		
		if(x > levelPoint.getX() &&
				x < levelPoint.getX() + levelPoint.getImage().getWidth() &&
				y > levelPoint.getY() &&
				y < levelPoint.getY() + levelPoint.getImage().getHeight()) {
			gsm.setState(levelPoint.getGotoLevel());
		}
//		if(intersects(e)) {
//			hit(e.getDamage());
//		}
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for (Enemy e : enemies) {

			// scratch attack
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
		if (intersects(e)) {
			hit(e.getDamage());
		}
	}

	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();

		playSoundEffect("playerGetsHit");
	}
	
	private void getNextPosition() {
		
		checkIce();
		checkCharging();
		
		// movement
		
		// up and down
		if(up) {
			dy -= moveSpeed;
			if(dy < -maxSpeed) {
				dy = -maxSpeed;
			}
		} 
		else if(down) {
			dy += moveSpeed;
			if(dy > maxSpeed) {
				dy = maxSpeed;
			}
		} 
		else {
			// up and down
			if(dy > 0) {
				dy -= stopSpeed;
				if(dy < 0) {
					dy = 0;
				}
			}
			else if(dy < 0) {
				dy += stopSpeed;
				if(dy > 0) {
					dy = 0;
				}
			}
		}
		
		// left and right
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
		else {

			// left and right
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if(
		(currentAction == SCRATCHING || currentAction == FIREBALL) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			playSoundEffect("jump");
			dy = jumpStart;
			falling = true;	
		}
		
		// falling
		if(falling) {
			
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) dy = maxFallSpeed;
			
		}
		
	}

	private void playSoundEffect(String soundEffectName) {
		sfx.get(soundEffectName).play();
	}
	
	public void update() {
		
		// update position
		getNextPosition();

		for (TileMap tm : tileMaps) {
			checkTileMapCollision(tm);
		}
		setPosition(xtemp, ytemp);

		checkAttackHasStopped();

		fireBallAttack();

		updateFireBalls();

		// check done flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}

		setAnimation();

		animation.update();

		updateDirection();
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
		if(scratching) { //scratch
			if(currentAction != SCRATCHING) {
				playSoundEffect("scratch");
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) { //shooting
			if(currentAction != FIREBALL) {
				playSoundEffect("fireball");
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy > 0) { //going down
			//System.out.println("going down");
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0) { //going up
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(left || right) { // left or right
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
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

	private void updateDirection() {
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}

	public void draw(Graphics2D g) {
		
		setMapPosition();

		DrawFireBalls(g);

		//Player is not drawn when flinching etc.
		if (DrawPlayer()) return;

		DrawDebugArea(g);

		super.draw(g);
		
	}

	private void DrawDebugArea(Graphics2D g) {

		if(GameOptions.ISDEBUGMODE == false)
			return;

		Font titleFont = new Font(
				"Century Gothic",
				Font.PLAIN,
				12);
		g.setColor(Color.BLACK);
		g.setFont(titleFont);
		g.drawString("X: " + this.x, 400, 13);
		g.drawString("Y: " + this.y, 400, 28);
		g.drawString("MaxSpeed: " + this.maxSpeed, 600, 13);
		g.drawString("StopSpeed: " + this.stopSpeed, 600, 28);
		g.setColor(Color.BLUE);
		g.drawRect((int) (x + xmap - width / 2),
				(int) (y + ymap - height / 2),
				width,
				height);
	}

	private boolean DrawPlayer() {
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return true;
			}
		}
		return false;
	}

	private void DrawFireBalls(Graphics2D g) {
		for (FireBall fireBall : fireBalls) {
			fireBall.draw(g);
		}
	}

}

















