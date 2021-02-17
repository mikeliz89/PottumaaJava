package GameState;

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

	private ArrayList<TileMap> tileMaps;
	private ArrayList<Integer> keysPressed;
	private double tileMapTween = 0.11;
	private int tileSize = 30;
	private int numTilesX;
	private int numTilesY;

	// tileset
	private BufferedImage tileset;
	private Tile[] tiles;

	//selection
	private int currentSelection = 0;

	public MapEditorState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {

		loadTiles("/Tilesets/grasstileset.png");
		keysPressed = new ArrayList<>();
	}

	void loadTiles(String s) {
		try {
			tileset = ImageIO.read(
					getClass().getResourceAsStream(s)
			);

			tiles = new Tile[2];

			numTilesX = tileset.getWidth() / tileSize;
			numTilesY = tileset.getHeight() / tileSize;

			var row = 4;
			var column = 0;
			BufferedImage subimage;
			subimage =
					tileset.getSubimage(
							column * tileSize,
							row * tileSize,
							tileSize,
							tileSize
					);
			Tile newTile = new Tile(subimage, 1, 1);
			tiles[0] = newTile;

			column = 0;
			row = 1;
			subimage =
					tileset.getSubimage(
							column * tileSize,
							row * tileSize,
							tileSize,
							tileSize
					);
			Tile newTile2 = new Tile(subimage, 1, 1);
			tiles[1] = newTile2;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void populateTileMaps() { 
		tileMaps = new ArrayList<>();
		
		// tiles: ground
		TileMap tileMapGround = new TileMap(tileSize);
		tileMapGround.loadTiles("/Tilesets/grasstileset.png", false);
		tileMapGround.loadMap("/Maps/map1.csv");
		tileMapGround.setPosition(0, 0);
		tileMapGround.setType(TileMap.GROUND);
		tileMapGround.setTween(tileMapTween);
		
		tileMaps.add(tileMapGround);
	}

	public void update() {
	}
	
	public void draw(Graphics2D g) {

		DrawBackground(g);

		DrawTileSets(g);

		DrawRectangles(g);

		DrawSelection(g);

		DrawTexts(g);

		DrawSelectedTile(g);
	}

	private int helpTextsLocationX = 400;
	private int helpTextsLocationY = 0;

	private void DrawSelectedTile(Graphics2D g) {

		var x = helpTextsLocationX;
		var y = helpTextsLocationY;

		g.setColor(Color.WHITE);
		var myString = "Currently selected tile";
		g.drawString(myString, x, y+40);

		//ArrayIndexOutOfBoundsException check
		if(tiles.length <= currentSelection) {
			g.setColor(Color.BLACK);
			g.fillRect(x, y, tileSize, tileSize);
			g.setColor(Color.WHITE);
			g.drawRect(x,y, tileSize,tileSize);
			return;
		}

		//null check
		if(tiles[currentSelection] == null)
			return;

		g.drawImage(tiles[currentSelection].getImage(),
				x, y, null);
		g.setColor(Color.WHITE);
		g.drawRect(x,y, tileSize,tileSize);
	}

	private void DrawTexts(Graphics2D g) {
		var font = new Font("Arial", Font.PLAIN, 12);
		g.setFont(font);
		g.setColor(Color.BLUE);
		if(GridSelection) {
			g.drawString("Press Enter to select", helpTextsLocationX, helpTextsLocationY + 80);
		} else {
			g.drawString("Press Enter start drawing", helpTextsLocationX, helpTextsLocationY + 80);
		}
		g.drawString("Press S to save map", helpTextsLocationX, helpTextsLocationY + 100);
		g.drawString("Press Backspace to exit to menu", helpTextsLocationX, helpTextsLocationY + 120);
	}

	private void DrawTileSets(Graphics2D g) {
		int rowIndex = 0;
		g.drawImage(
				tiles[rowIndex].getImage(),
				0,
				0,
				null
		);

		g.drawImage(
				tiles[rowIndex+1].getImage(),
				0,
				tileSize,
				null
		);
	}

	private void DrawBackground(Graphics2D g) {

		//Piirrä koko ruudun kokoinen rectangle taustaksi
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		//Piirrä alue johon kenttä suunnitellaan
		int width = 240;
		int height = 240;
		g.setColor(Color.WHITE);
		g.fillRect(tileSize, 0, width, height);
	}

	private void DrawRectangles(Graphics2D g) {

		var columnCount = tileSize;
		var rowCount = tileSize;

		g.setColor(Color.BLACK);
		for(int col = 0 ; col < columnCount; col++) {
			for(int row = 0; row < rowCount; row++) {
				g.drawRect(col * columnCount, row * rowCount, tileSize, tileSize);
			}
		}

	}

	private void DrawSelection(Graphics2D g) {
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
			gsm.setState(GameStateManager.MENUSTATE);
		}

		if(k == KeyEvent.VK_ENTER) {
			ToggleGridSelection();
		}

		//Up and down keys
		if(k == KeyEvent.VK_UP)
			ChangeSelection(true);
		if(k == KeyEvent.VK_DOWN)
			ChangeSelection(false);

		//S = Save
		if(k == KeyEvent.VK_S)
			SaveMap();
	}

	private boolean GridSelection = false;
	private void ToggleGridSelection() {
		GridSelection = !GridSelection;
	}

	private void SaveMap() {
		//todo: koodaa mapin tallennus CSV:ksi
		System.out.println("Map saved!");
	}

	private void ChangeSelection(boolean goingUp) {
		if(goingUp && currentSelection == 0)
			currentSelection = 0;
		if(goingUp && currentSelection > 0)
			currentSelection--;
		if(!goingUp && currentSelection >= 0 && currentSelection < 15)
			currentSelection++;
	}
	
	public void keyReleased(int k) {
		
		// remove released keys from keysPressed arrayList
		if(keysPressed.contains(k) == true) {
			keysPressed.remove(keysPressed.indexOf(k));
		}
		
	}

	public void stopBackGroundMusic() {

	}
	
}












