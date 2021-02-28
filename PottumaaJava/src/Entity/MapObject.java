package Entity;

import Entity.Obstacles.Obstacle;
import Entity.Player.Player;
import Main.GameOptions;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.*;
import java.util.ArrayList;

public abstract class MapObject {
	
	// tile stuff
	protected ArrayList<TileMap> tileMaps;
	protected ArrayList<Obstacle> obstacles;
	protected double xMap;
	protected double yMap;

	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// dimensions
	private int width;
	private int height;
	
	// collision box
	protected int collisionBoxWidth;
	protected int collisionBoxHeight;
	
	// four-corner-collision detection
	protected int currentRow;
	protected int currentColumn;
	protected double xTemp;
	protected double yTemp;
	protected boolean topLeftCorner;
	protected boolean topRightCorner;
	protected boolean bottomLeftCorner;
	protected boolean bottomRightCorner;

	//tiles
	protected int bottomTile;
	protected int rightTile;
	protected int leftTile;
	protected int topTile;

	// animation
	protected Animation animation;
	protected boolean facingRight;
	protected boolean facingUp;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;

	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double originalMaxSpeed = maxSpeed;
	protected double stopSpeed;
	protected double originalStopSpeed = stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	// constructor
	public MapObject() {
		tileMaps = new ArrayList<>();
		obstacles = new ArrayList<>();
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getCollisionBoxRectangle();
		Rectangle r2 = o.getCollisionBoxRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getCollisionBoxRectangle() {
		return new Rectangle(
				(int)(x + xMap - collisionBoxWidth / 2),
				(int)(y + yMap - collisionBoxHeight / 2),
				collisionBoxWidth,
				collisionBoxHeight
		);
	}

	public Rectangle getRectangle() {
		return new Rectangle(
				(int)(x + xMap - width / 2),
				(int)(y + yMap - height / 2),
				width,
				height);
	}

	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCollisionBoxWidth() { return collisionBoxWidth; }
	public int getCollisionBoxHeight() { return collisionBoxHeight; }
	public void setCollisionBoxWidth(int collisionBoxWidth) {
		this.collisionBoxWidth = collisionBoxWidth;
	}
	public void setCollisionBoxHeight(int collisionBoxHeight) {
		this.collisionBoxHeight = collisionBoxHeight;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public double getStopSpeed() {
		return stopSpeed;
	}

	protected boolean getBottomLeftCorner() { return bottomLeftCorner; }
	protected boolean getBottomRightCorner() { return bottomRightCorner; }

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public void setStopSpeed(double stopSpeed) {
		this.stopSpeed = stopSpeed;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setMapPosition() {
		for (TileMap tileMap : getTileMaps(Tile.TILE_TYPE_GROUND)) {
			xMap = tileMap.getX();
			yMap = tileMap.getY();
		}
	}

	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }

	public boolean notOnScreen() {
		return x + xMap + width < 0 ||
				x + xMap - width > GamePanel.WIDTH ||
				y + yMap + height < 0 ||
				y + yMap - height > GamePanel.HEIGHT;
	}

	public double getXMap() {
		return xMap;
	}

	public double getYMap() {
		return yMap;
	}

	public void update() {
		updatePosition();
		//if(this instanceof Player){
			checkCollisions();
		//}
		setPosition(xTemp, yTemp);
		updateAnimation();
	}

	private void updateAnimation() {
		animation.update();
	}

	protected void updatePosition() {

	}

	protected ArrayList<TileMap> getTileMaps(int tileType) {
		var groundTileMaps = new ArrayList<TileMap>();
		for(TileMap tileMap : tileMaps) {
			if(tileMap.getType() == tileType) {
				groundTileMaps.add(tileMap);
			}
		}
		return groundTileMaps;
	}

	public void draw(Graphics2D g) {

		setMapPosition();

		if(notOnScreen()) return;

		if(!animation.hasFrames()) {
			return;
		}

		drawCollisionBox(g);

		drawAnimationImage(g);
	}

	public void checkCollisions() {
		checkObstacleCollisions();
		checkTileMapsCollisions();
	}

	private void checkObstacleCollisions() {
		Obstacle obstacleTemp = null;
		for(Obstacle obstacle : obstacles) {
			if (intersects(obstacle)) {
				if(GameOptions.IS_DEBUG_MODE)
					System.out.println("mapobject intersects obstacle");
				obstacleTemp = obstacle;
			}
		}
	}

	private void checkTileMapsCollisions() {
		for (TileMap tileMap : tileMaps) {

			calculateCurrentColumn(tileMap);
			calculateCurrentRow(tileMap);

			double xDestination = calculateDestinationX();
			double yDestination = calculateDestinationY();

			xTemp = x;
			yTemp = y;

			if(tileMap.getType() == Tile.TILE_TYPE_OBSTACLE) {
				upAndDown(tileMap, yDestination);
				leftAndRight(tileMap, xDestination);
			} else if(tileMap.getType() == Tile.TILE_TYPE_GROUND) {
				if(goingUp() || goingDown()) {
					moveInYDirection();
				}
				if (goingLeft() || goingRight()) {
					moveInXDirection();
				}
			}
		}
	}

	private void leftAndRight(TileMap tileMap, double xDestination) {
		calculateCornersForXDestination(xDestination, tileMap);
		if (goingLeft()) {
			if (leftCornersAreHitting()) {
				preventMovingInXDirection();
			} else {
				moveInXDirection();
			}
		} else if (goingRight()) {
			if (rightCornersAreHitting()) {
				preventMovingInXDirection();
			} else {
				moveInXDirection();
			}
		}
	}

	private void upAndDown(TileMap tileMap, double yDestination) {
		calculateCornersForYDestination(yDestination, tileMap);
		if (goingUp()) {
			if (topCornersAreHitting()) {
				preventMovingInYDirection();
			} else {
				moveInYDirection();
			}
		} else if (goingDown()) {
			if (bottomCornersAreHitting()) {
				preventMovingInYDirection();
			} else {
				moveInYDirection();
			}
		}
	}

	private void preventMovingInYDirection() {
		dy = 0;
	}

	private void preventMovingInXDirection() {
		dx = 0;
	}

	private void moveInXDirection() {
		xTemp += dx;
	}

	private void moveInYDirection() {
		yTemp += dy;
	}

	private boolean bottomCornersAreHitting() {
		return bottomLeftCorner || bottomRightCorner;
	}

	private boolean topCornersAreHitting() {
		return topLeftCorner || topRightCorner;
	}

	private boolean rightCornersAreHitting() {
		return topRightCorner || bottomRightCorner;
	}

	private boolean leftCornersAreHitting() {
		return topLeftCorner || bottomLeftCorner;
	}

	private void calculateCornersForXDestination(double xDestination, TileMap tileMap) {
		calculateCorners(xDestination, y, tileMap);
	}

	private void calculateCornersForYDestination(double yDestination, TileMap tileMap) {
		calculateCorners(x, yDestination, tileMap);
	}

	private void calculateCorners(double x, double y, TileMap tileMap) {
		var tileSize = tileMap.getTileSize();
		calculateTiles(x, y, tileSize);
		calculateFourCorners(tileMap);
	}

	private void calculateTiles(double x, double y, int tileSize) {
		leftTile = getLeftTile(x, tileSize);
		rightTile = getRightTile(x, tileSize);
		topTile = getTopTile(y, tileSize);
		bottomTile = getBottomTile(y, tileSize);
	}

	private void calculateFourCorners(TileMap tileMap) {
		int topLeftTileType = tileMap.getTileType(topTile, leftTile);
		int topRightTileType = tileMap.getTileType(topTile, rightTile);
		int bottomLeftTileType = tileMap.getTileType(bottomTile, leftTile);
		int bottomRightTileType = tileMap.getTileType(bottomTile, rightTile);

		topLeftCorner = topLeftTileType == Tile.TILE_TYPE_OBSTACLE;
		topRightCorner = topRightTileType == Tile.TILE_TYPE_OBSTACLE;
		bottomLeftCorner = bottomLeftTileType == Tile.TILE_TYPE_OBSTACLE;
		bottomRightCorner = bottomRightTileType == Tile.TILE_TYPE_OBSTACLE;
	}

	private int getBottomTile(double y, int tileSize) {
		return (int)(y + collisionBoxHeight / 2 - 1) / tileSize;
	}

	private int getTopTile(double y, int tileSize) {
		return (int)(y - collisionBoxHeight / 2) / tileSize;
	}

	private int getRightTile(double x, int tileSize) {
		return (int)(x + collisionBoxWidth / 2 - 1) / tileSize;
	}

	private int getLeftTile(double x, int tileSize) {
		return (int)(x - collisionBoxWidth / 2) / tileSize;
	}

	private double calculateDestinationY() {
		double yDestination = y + dy;
		return yDestination;
	}

	private double calculateDestinationX() {
		return x + dx;
	}

	private void calculateCurrentRow(TileMap tm) {
		currentRow = tm.getRowTile();
	}

	private void calculateCurrentColumn(TileMap tm) {
		currentColumn = tm.getColumnTile();
	}

	protected boolean goingRight() {
		return dx > 0;
	}

	protected boolean goingLeft() {
		return dx < 0;
	}

	protected boolean goingDown() {
		return dy > 0;
	}

	protected boolean goingUp() {
		return dy < 0;
	}

	private void drawCollisionBox(Graphics2D g) {

		if(GameOptions.IS_DEBUG_MODE == false)
			return;

		var collisionBoxRectangle = getCollisionBoxRectangle();

		if(this instanceof Player) {
			g.setColor(Color.magenta);
		} else {
			g.setColor(Color.yellow);
		}
		g.drawRect(collisionBoxRectangle.x,
				collisionBoxRectangle.y,
				collisionBoxRectangle.width,
				collisionBoxRectangle.height);
	}

	private void drawAnimationImage(Graphics2D g) {
		if(facingRight) {
			drawNormalAnimationImage(g);
		}
		else {
			//todo: tämä tarkoitti ennen vasemmalle menoa, mutta nykyään tarkoittaa vasemalle, ylös ja alas menoa.
			//todo: koodaa else iffit eri suuntiin menoille.
			drawFlippedAnimationImage(g);
		}
	}

	private void drawFlippedAnimationImage(Graphics2D g) {
		g.drawImage(
			animation.getImage(),
			(int)(x + xMap - width / 2 + width),
			(int)(y + yMap - height / 2),
			-width,
			height,
			null
		);
	}

	private void drawNormalAnimationImage(Graphics2D g) {
		g.drawImage(
			animation.getImage(),
			(int)(x + xMap - width / 2),
			(int)(y + yMap - height / 2),
			null
		);
	}
}