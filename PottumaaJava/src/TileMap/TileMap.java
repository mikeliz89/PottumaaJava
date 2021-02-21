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
	
	private double tween;
	
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
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public void loadTiles(String s, boolean allBlocked) {
		
		try {
			tileset = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			numTilesX = tileset.getWidth() / tileSize;
			numTilesY = tileset.getHeight() / tileSize;
			
			tiles = new Tile[numTilesY][numTilesX];
			
			int count = 0;
			BufferedImage subimage;
			for(int row = 0; row < numTilesY; row++) {
				for(int col = 0; col < numTilesX; col++) {
					subimage = 
						tileset.getSubimage(
							col * tileSize,
							row * tileSize,
							tileSize,
							tileSize
						);
					int tileFrictionType = Tile.TILE_TYPE_GROUND;
					int type = Tile.TILE_TYPE_GROUND;
					
					if(allBlocked == true) {
						if(count != 0) {
							type = Tile.TILE_TYPE_OBSTACLE;
						}
					} 
					else {
						if(
							count == 32 ||
							count == 33 ||
							count == 34) {
							tileFrictionType = Tile.TILE_FRICTION_TYPE_ICE;
						}
						if (count == 20 || count == 21 || count == 22 || count == 29 || count == 13) {
							type = Tile.TILE_TYPE_OBSTACLE;
						}
					}
					Tile newTile = new Tile(subimage, type, tileFrictionType);
					tiles[row][col] = newTile;
					
					count ++;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s) {
		
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);

			String delimeters = ";";
			String firstRowValue = br.readLine();
			String secondRowValue = br.readLine();
			
			numberOfColumns = Integer.parseInt(firstRowValue.substring(0, firstRowValue.indexOf(';')));
			numberOfRows = Integer.parseInt(secondRowValue.substring(0, secondRowValue.indexOf(';')));
			
			map = new int[numberOfRows][numberOfColumns];
			width = numberOfColumns * tileSize;
			height = numberOfRows * tileSize;
			
			xMin = GamePanel.WIDTH - width;
			xMax = 0;
			yMin = GamePanel.HEIGHT - height;
			yMax = 0;
			
			for(int row = 0; row < numberOfRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delimeters, -1);
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
	public double getTween() { return tween; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getTileSize() { return tileSize; }
	public int getType() { return this.type; }
	public BufferedImage getTileSet() { return tileset; }
	public Tile[][] getTiles() { return tiles; }
	
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
	
	public void setTween(double d) { tween = d; }
	
	public void setType(int type) { this.type = type; }
	
	public void setPosition(double x, double y) {
		this.x += (x - this.x); // * tween;
		this.y += (y - this.y); // * tween;
		
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



















