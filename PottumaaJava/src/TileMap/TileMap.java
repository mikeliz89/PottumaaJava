package TileMap;

import java.awt.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

import Main.GameOptions;
import Main.GamePanel;

public class TileMap {
	
	// position
	private double x;
	private double y;
	
	// bounds
	private int xMin;
	private int yMin;
	private int xMax;
	private int yMax;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numberOfRows;
	private int numberOfColumns;
	private int width;
	private int height;
	
	private int type;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesX;
	private int numTilesY;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;

	private String mapName;
	private String tileSetName;
	
	public TileMap(int tileSize, String tileSetName, String mapName) {
		this.tileSize = tileSize;
		this.tileSetName = tileSetName;
		this.mapName = mapName;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
	}
	
	public void loadTiles() {
		
		try {
			tileset = ImageIO.read(
				getClass().getResourceAsStream(tileSetName)
			);
			numTilesX = tileset.getWidth() / tileSize;
			numTilesY = tileset.getHeight() / tileSize;
			
			tiles = new Tile[numTilesY][numTilesX];
			
			int index = 0;
			BufferedImage subImage;
			for(int row = 0; row < numTilesY; row++) {
				for(int col = 0; col < numTilesX; col++) {
					subImage =
						tileset.getSubimage(
							col * tileSize,
							row * tileSize,
							tileSize,
							tileSize
						);

					int tileType = getTileType(index);
					int tileFrictionType = getFrictionType(index);

					Tile newTile = new Tile(subImage, tileType, tileFrictionType);
					tiles[row][col] = newTile;
					
					index ++;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private int getFrictionType(int index) {
		int tileFrictionType = Tile.TILE_FRICTION_TYPE_GRASS;
		if (type == Tile.TILE_TYPE_GROUND &&
				(index == 32 ||
				index == 33 ||
				index == 34)) {
			tileFrictionType = Tile.TILE_FRICTION_TYPE_ICE;
		}
		return tileFrictionType;
	}

	private int getTileType(int index) {
		int tileType = Tile.TILE_TYPE_GROUND;

		if(type == Tile.TILE_TYPE_OBSTACLE) {
			if(index == 0) {
				tileType = Tile.TILE_TYPE_GROUND;
			} else {
				tileType = Tile.TILE_TYPE_OBSTACLE;
			}
		}
		return tileType;
	}

	public void loadMap() {
		
		try {
			
			InputStream in = getClass().getResourceAsStream(mapName);
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);

			String delimeter = ";";
			String firstRowValue = br.readLine();
			String secondRowValue = br.readLine();
			
			numberOfColumns = Integer.parseInt(firstRowValue.substring(0, firstRowValue.indexOf(delimeter)));
			numberOfRows = Integer.parseInt(secondRowValue.substring(0, secondRowValue.indexOf(delimeter)));
			
			map = new int[numberOfRows][numberOfColumns];
			width = numberOfColumns * tileSize;
			height = numberOfRows * tileSize;
			
			xMin = GamePanel.WIDTH - width;
			xMax = 0;
			yMin = GamePanel.HEIGHT - height;
			yMax = 0;
			
			for(int row = 0; row < numberOfRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delimeter, -1);
				for(int col = 0; col < numberOfColumns; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getTileSize() { return tileSize; }
	public int getType() { return this.type; }
	public BufferedImage getTileSet() { return tileset; }
	public Tile[][] getTiles() { return tiles; }

	public int getColumnTile() {
		return (int)y / tileSize;
	}

	public int getRowTile() {
		return (int)x / tileSize;
	}
	
	public int getTileType(int row, int col) {
		row = makeRowValueSafe(row);
		col = makeColumnValueSafe(col);
		int rc = map[row][col];
		int r = rc / numTilesX;
		int c = rc % numTilesX;
		return tiles[r][c].getTileType();
	}
	
	public int getTileFrictionType(int row, int col) {
		row = makeRowValueSafe(row);
		col = makeColumnValueSafe(col);
		int rc = map[row][col];
		int r = rc / numTilesX;
		int c = rc % numTilesX;
		var tile = tiles[r][c];
		return tile.getFrictionType();
	}

	private int makeColumnValueSafe(int col) {
		if(col >= numberOfColumns)
			col = numberOfColumns -1;

		if(col <= 0)
			col = 0;
		return col;
	}

	private int makeRowValueSafe(int row) {
		if(row <= 0)
			row = 0;

		if(row >= numberOfRows)
			row = numberOfRows -1;
		return row;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}
	
	public void setType(int type) { this.type = type; }
	
	public void setPosition(double x, double y) {
		this.x += (x - this.x);
		this.y += (y - this.y);
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	
	private void fixBounds() {
		if(x < xMin) x = xMin;
		if(y < yMin) y = yMin;
		if(x > xMax) x = xMax;
		if(y > yMax) y = yMax;
	}
	
	public void draw(Graphics2D g) {
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			
			if(row >= numberOfRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numberOfColumns) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesX;
				int c = rc % numTilesX;
				
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);
				
				drawDebugRectangle(g, col, row);
			}
		}
	}

	private void drawDebugRectangle(Graphics2D g, int col, int row) {

		if(GameOptions.IS_DEBUG_MODE == false)
			return;

		//collision rectangles revealed
		g.setColor(Color.WHITE);
		g.drawRect((int)x + col * tileSize, (int)y + row * tileSize, tileSize, tileSize);
	}
	
}



















