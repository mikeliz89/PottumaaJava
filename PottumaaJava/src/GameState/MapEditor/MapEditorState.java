package GameState.MapEditor;

import GameState.*;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class MapEditorState extends GameState {

	private final int MAP_EDITOR_GRID_ROWS = 8;
	private final int MAP_EDITOR_GRID_COLUMNS = 8;
	private final int MAP_EDITOR_TOOL_ROWS = 13;

	private ArrayList<TileMap> tileMaps;
	private ArrayList<Integer> keysPressed;
	private int tileSize = 30;
	private int numTilesX;
	private int numTilesY;

	private int helpTextsLocationX = 400;
	private int helpTextsLocationY = 0;

	//grid
	private boolean gridSelection = false;
	private int currentGridRow;
	private int currentGridColumn;

	// tileset
	private BufferedImage tileset;
	private Tile[] tiles;

	private ArrayList<MapEditorSlot> mapEditorSlots;

	//selection
	private int currentSelection = 0;

	public MapEditorState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {

		loadTiles("/Tilesets/grasstileset.png");
		keysPressed = new ArrayList<>();
		currentGridColumn = 0;
		currentGridRow = 0;

		createMapEditorSlots();
	}

	void createMapEditorSlots() {
		mapEditorSlots = new ArrayList<>();
		var gridRectangle = getGridRectangle();
		var rows = MAP_EDITOR_GRID_ROWS;
		var columns = MAP_EDITOR_GRID_COLUMNS;
		for(int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				var rectangle = new Rectangle(gridRectangle.x + (column * tileSize),
						gridRectangle.y + ( row * tileSize),
						tileSize, tileSize);
				mapEditorSlots.add(new MapEditorSlot(rectangle, row, column));
			}
		}
	}

	void loadTiles(String s) {
		try {
			tileset = ImageIO.read(
					getClass().getResourceAsStream(s)
			);

			tiles = new Tile[3];

			numTilesX = tileset.getWidth() / tileSize;
			numTilesY = tileset.getHeight() / tileSize;

			BufferedImage subImage = getSubImage(4, 0);
			Tile tile = getNewTile(subImage);
			tiles[0] = tile;

			subImage = getSubImage(0, 1);
			tile = getNewTile(subImage);
			tiles[1] = tile;

			subImage = getSubImage(0, 2);
			tile = getNewTile(subImage);
			tiles[2] = tile;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Tile getNewTile(BufferedImage subImage) {
		return new Tile(subImage, 1, 1);
	}

	private BufferedImage getSubImage(int row, int column) {
		BufferedImage subImage =
			tileset.getSubimage(
					column * tileSize,
					row * tileSize,
					tileSize,
					tileSize
			);
		return subImage;
	}

	public void update() {
	}
	
	public void draw(Graphics2D g) {

		drawBackground(g);

		drawTileSets(g);

		drawGrid(g);

		drawMapGrid(g);

		drawTexts(g);

		if(!gridSelection)
			drawSelection(g);
		else
			drawSelectedGridTile(g);

		drawSelectedTile(g);
	}

	private void drawMapGrid(Graphics2D g) {
		drawMapGridOuterLines(g);
		drawMapEditorSlots(g);
	}

	private void drawMapGridOuterLines(Graphics2D g) {
		//Piirrä alue johon kenttä suunnitellaan
		var gridRectangle = getGridRectangle();

		g.setColor(Color.BLACK);
		g.fillRect(gridRectangle.x, gridRectangle.y,
				gridRectangle.width, gridRectangle.height);
	}

	private void drawMapEditorSlots(Graphics2D g) {
		g.setColor(Color.ORANGE);
		for(MapEditorSlot mapEditorSlot : mapEditorSlots) {
			mapEditorSlot.draw(g);
		}
	}

	private void drawSelectedGridTile(Graphics2D g) {
		var gridRectangle = getGridRectangle();
		g.setColor(Color.BLUE);
		g.drawRect(gridRectangle.x + (tileSize * currentGridColumn),
				gridRectangle.y +(tileSize * currentGridRow),
				tileSize, tileSize);
	}

	private void drawSelectedTile(Graphics2D g) {

		var x = helpTextsLocationX;
		var y = helpTextsLocationY;

		g.setColor(Color.BLACK);
		var myString = "Currently selected tile";
		g.drawString(myString, x, y+40);

		//ArrayIndexOutOfBoundsException check
		if(tiles.length - 1 <= currentSelection) {
			g.setColor(Color.WHITE);
			g.fillRect(x, y, tileSize, tileSize);
			g.setColor(Color.BLACK);
			g.drawRect(x,y, tileSize,tileSize);
			return;
		}

		//null check
		if(tiles[currentSelection] == null)
			return;

		g.drawImage(getSelectedImage(),
				x, y, null);
		g.setColor(Color.BLACK);
		g.drawRect(x,y, tileSize,tileSize);
	}

	private String getSelectedImageIndex() {
		var tile = tiles[currentSelection];
		return "8";
	}

	private BufferedImage getSelectedImage() {
		var tile = tiles[currentSelection];
		return tile.getImage();
	}

	private void drawTexts(Graphics2D g) {
		var font = new Font("Arial", Font.PLAIN, 12);
		g.setFont(font);
		g.setColor(Color.BLUE);
		if(gridSelection) {
			g.drawString("Press Enter to select", helpTextsLocationX, helpTextsLocationY + 80);
		} else {
			g.drawString("Press Enter start drawing", helpTextsLocationX, helpTextsLocationY + 80);
		}

		g.drawString("Press E to paint", helpTextsLocationX, helpTextsLocationY + 100);
		g.drawString("Press S to save map", helpTextsLocationX, helpTextsLocationY + 120);
		g.drawString("Press Backspace to exit to menu", helpTextsLocationX, helpTextsLocationY + 140);
	}

	private void drawTileSets(Graphics2D g) {

		for(int i = 0; i < tiles.length; i++) {
			g.drawImage(
					tiles[i].getImage(),
					0,
					tileSize * i,
					null
			);
		}
	}

	private void drawBackground(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}

	private Rectangle getGridRectangle() {
		int width = tileSize * MAP_EDITOR_GRID_COLUMNS;
		int height = tileSize * MAP_EDITOR_GRID_ROWS;
		return new Rectangle(tileSize, 0, width, height);
	}

	private void drawGrid(Graphics2D g) {

		var columnCount = tileSize;
		var rowCount = tileSize;

		g.setColor(Color.BLACK);
		for(int col = 0 ; col < columnCount; col++) {
			for(int row = 0; row < rowCount; row++) {
				g.drawRect(col * columnCount, row * rowCount, tileSize, tileSize);
			}
		}

	}

	private void drawSelection(Graphics2D g) {
		g.setColor(Color.RED);
		var y = currentSelection * tileSize;
		g.drawRect(0, y, tileSize, tileSize);
	}

	public void keyPressed(int k) {
		if(keysPressed.contains(k) == false) {
			keysPressed.add(k);
		}

		//back
		if(k == KeyEvent.VK_BACK_SPACE) {
			gsm.setState(GameStateManager.STATE_MAIN_MENU);
		}

		if(k == KeyEvent.VK_ENTER) {
			toggleGridSelection();
		}

		//Up and down keys
		if(k == KeyEvent.VK_UP)
			ChangeSelection(true, false, false, false);
		if(k == KeyEvent.VK_DOWN)
			ChangeSelection(false, true, false, false);
		if(k == KeyEvent.VK_LEFT)
			ChangeSelection(false, false, true, false);
		if(k == KeyEvent.VK_RIGHT)
			ChangeSelection(false, false,false, true);

		//E = draw
		if(k == KeyEvent.VK_E)
			drawImageToMapGrid();

		//S = Save
		if(k == KeyEvent.VK_S)
			saveMap();
	}

	private void drawImageToMapGrid() {
		var tileSetIndex = getSelectedImageIndex();
		var image = getSelectedImage();
		for(MapEditorSlot mapEditorSlot : mapEditorSlots) {
			if(mapEditorSlot.getRow() == currentGridRow &&
			   mapEditorSlot.getColumn() == currentGridColumn)
			mapEditorSlot.setImage(image);
			mapEditorSlot.setTileSetIndex(tileSetIndex);
		}
	}

	private void toggleGridSelection() {
		gridSelection = !gridSelection;
	}

	private void saveMap() {
		var csvFileWriter = new CsvFileWriter("mapEditorCreatedFile");
		ArrayList<String> rows = createRows(csvFileWriter);
		try {
			csvFileWriter.writeCsv(rows);
			System.out.println("Map saved!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private ArrayList<String> createRows(CsvFileWriter csvFileWriter) {
		var rows = new ArrayList<String>();
		addHeaderRows(csvFileWriter, rows);
		addTileMapRows(csvFileWriter, rows);
		return rows;
	}

	private void addTileMapRows(CsvFileWriter csvFileWriter, ArrayList<String> rows) {
		var rowValues = new ArrayList<String>();

		var lastRow = 0;
		for(MapEditorSlot mapEditorSlot : mapEditorSlots) {
			var row = mapEditorSlot.getRow();
			if(row != lastRow) {
				rowValues = new ArrayList<>();
				lastRow = row;
			}
			rowValues.add(mapEditorSlot.getTileSetIndex());
		}
		var row = csvFileWriter.convertArrayListIntoCommaSeparatedString(rowValues);
		for(int i = 0; i <  MAP_EDITOR_GRID_ROWS; i++) {
			rows.add(row);
		}
	}

	private void addHeaderRows(CsvFileWriter csvFileWriter, ArrayList<String> rows) {
		var headerRowCount = 2;
		for(int j = 0; j < headerRowCount; j++) {
			var rowValues = new ArrayList<String>();
			rowValues.add(Integer.toString(MAP_EDITOR_GRID_COLUMNS));
			for (int i = 0; i < MAP_EDITOR_GRID_COLUMNS - 1; i++) {
				rowValues.add("");
			}
			var headerRow = csvFileWriter.convertArrayListIntoCommaSeparatedString(rowValues);
			rows.add(headerRow);
		}
	}

	private void ChangeSelection(boolean goingUp, boolean goingDown,
								 boolean goingLeft, boolean goingRight) {

		if(!gridSelection) {
			if (goingUp && currentSelection == 0)
				currentSelection = 0;
			if (goingUp && currentSelection > 0)
				currentSelection--;
			if (goingDown && currentSelection >= 0 && currentSelection < (MAP_EDITOR_TOOL_ROWS))
				currentSelection++;
		} else {
			if(goingUp && currentGridRow == 0) {
				currentGridRow = 0;
			}
			if(goingUp && currentGridRow > 0)
				currentGridRow--;
			if(goingDown && currentGridRow >= 0 && currentGridRow < (MAP_EDITOR_GRID_ROWS - 1))
				currentGridRow++;

			if(goingLeft && currentGridColumn == 0)
				currentGridColumn = 0;
			if(goingLeft && currentGridColumn > 0)
				currentGridColumn--;
			if(goingRight && currentGridColumn >=0 && currentGridColumn < (MAP_EDITOR_GRID_COLUMNS - 1))
				currentGridColumn++;
		}
	}
	
	public void keyReleased(int k) {
		// remove released keys from keysPressed arrayList
		if(keysPressed.contains(k) == true) {
			keysPressed.remove(keysPressed.indexOf(k));
		}
	}
	
}