package com.gussssy.gametwo.game.level;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.Textures;
import com.gussssy.gametwo.game.debug.DebugPanel;

public abstract class Level {
	
	// set by constructor
	public Image levelMap;
	public Image levelBackground;
	
	// managed internally
	private LevelTile[] levelTiles;
	
	// Width and Height of the level. (In tiles not pixels). Set by load level.
	int levelWidth, levelHeight;
	
	// where the player will start in this level
	int playerTileX, playerTileY;
	
	
	
	
	
	
	// just to make the code easier to read... 
	int tileSize = GameManager.TS;
	
	// dont love this here asI dont so this anywhere else
	GameManager gm;
	
	
	
	/**
	 * Updates anything in the level that needs to be updated each frame e.g weather.  
	 * 
	 **/
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	
	
	
	/**
	 * Primary Level Construcor 
	 * 
	 * Needs to take the GameManager as Input so that GameManager can be sent the loaded level tiles.
	 * 
	 * Takes the paths for the level template image and the background and opens them.
	 * 
	 * Load the level using the level template image
	 **/
	public Level(String levelImagePath, String backgroundImagePath, GameManager gm){
		
		
		this.gm = gm;
		
		levelMap = new Image(levelImagePath);
		levelBackground = new Image(backgroundImagePath);
		
		
		loadLevel(levelMap);
	}
	
	
	/**
	 * Secondary Level Constructor. Experimenting with a more logical way of constructing levels. 
	 * - it doesnt make sense to pass in images paths when making a new level. These dont vary between instances of the same level. 
	 * - they vary between levels, each have thier own class 
	 * 
	 * LEADS TO UNRECOGNISED PIXEL COLOUR? 
	 * 
	 **/
	public Level(Image levelMap, Image levelBackground, GameManager gm){
		
		this.levelMap = levelMap;
		this.levelBackground = levelBackground;
		
		this.gm = gm;
		
		loadLevel(levelMap);
		
	}
	
	
	
	
	
	/**
	 * Load the level by iterating through the level template image and creating the corresponding level tile.
	 * 
	 * Once finished it sets the levelTiles in the GameManager.
	 * 	- in future I would like level tiles to be accessed via this class not the GameManager. 
	 **/
	public void loadLevel(Image levelImage){
		
		//Image levelImage = new Image(path);

		// Set the width and height of this level (in tiles not pixels)
		levelWidth = levelImage.getWidth();
		levelHeight = levelImage.getHeight();
		
		// create the array that will hols the level tiles
		levelTiles = new LevelTile[levelWidth * levelHeight];
		
		// Pixel int colour --> tile type
		int air = 0xffffffff;
		int grass = -16711936;
		int dirt = -16777216;
		int dirtNew = -8761812;
		int stone = -5592406;
		int brick = -8355840;
		int log = -12571632;
		int dirtBack =-5342120;
		int leaf = -16751616;
		int fence = -7964306;
		int dirtBackReinforced = -5342062;
		int water = -16776961;
		int snow = -4276525;
		int brickBack = -6382294;
		int marbleBrick = -1907493;
		int marblePillar = -2369839;
		int ice = -8941840;

		for(int y = 0; y < levelHeight; y++){
			for(int x = 0; x < levelWidth; x++){

				int tileIndex = x + y * levelWidth;

				//System.out.println("x: " + x + ", y: " + y);
				//System.out.println("Pixel from level image contains: " + levelImage.getPixels()[tileIndex]);

				if(levelImage.getPixels()[tileIndex] != air ){

					if(levelImage.getPixels()[tileIndex] == dirt || levelImage.getPixels()[tileIndex] == dirtNew ){
						// DIRT TILE
						levelTiles[x + y * levelWidth] = new LevelTile(1, x, y);
						//System.out.println("Setting level tile at index: " + (x + y * levelWidth) + " as a DIRT BLOCK" );

					}else if (levelImage.getPixels()[tileIndex] == grass){
						// GRASS TILE
						levelTiles[x + y * levelWidth] = new LevelTile(2, x, y);
						//System.out.println("Setting level tile at index: " + (x + y * levelWidth) + " as a GRASS BLOCK" );

					} else if(levelImage.getPixels()[tileIndex] ==  stone) {
						// STONE TILE
						levelTiles[x + y * levelWidth] = new LevelTile(3, x, y);
						//System.out.println("Setting level tile at index: " + (x + y * levelWidth) + " as a GRASS BLOCK" );

					}else if(levelImage.getPixels()[tileIndex] ==  brick){
						// BRICK TILE 
						levelTiles[x + y * levelWidth] = new LevelTile(4, x, y);
						
					}else if(levelImage.getPixels()[tileIndex] ==  log){
						// LOG TILE 
						levelTiles[x + y * levelWidth] = new LevelTile(5, x, y);
						
					}else if(levelImage.getPixels()[tileIndex] ==  dirtBack){
						// Dirt Back
						levelTiles[x + y * levelWidth] = new LevelTile(-1, x, y);
						
					} else if(levelImage.getPixels()[tileIndex] ==  leaf){
						// Leaf
						levelTiles[x + y * levelWidth] = new LevelTile(6, x, y);
						
					} else if(levelImage.getPixels()[tileIndex] ==  fence) {
						levelTiles[x + y * levelWidth] = new LevelTile(7, x, y);
						
					}else if(levelImage.getPixels()[tileIndex] ==  dirtBackReinforced){
						levelTiles[x + y * levelWidth] = new LevelTile(-2, x, y);
						
					}else if(levelImage.getPixels()[tileIndex] ==  water){
						levelTiles[x + y * levelWidth] = new LevelTile(-3, x, y);
					
					}else if(levelImage.getPixels()[tileIndex] ==  snow){
						levelTiles[x + y * levelWidth] = new LevelTile(8, x, y);
						
					}else if(levelImage.getPixels()[tileIndex] ==  brickBack){
						levelTiles[x + y * levelWidth] = new LevelTile(-4, x, y);
						
					}else if(levelImage.getPixels()[tileIndex] ==  marbleBrick){
						levelTiles[x + y * levelWidth] = new LevelTile(9, x, y);
						
					}else if(levelImage.getPixels()[tileIndex] ==  marblePillar){
						levelTiles[x + y * levelWidth] = new LevelTile(-5, x, y);
						
					}else if(levelImage.getPixels()[tileIndex] ==  ice){
						levelTiles[x + y * levelWidth] = new LevelTile(10, x, y);
					}else { 
						// UNREGONISED PIXEL FROM TEMPLATE IMAGE
						System.out.println("Unrecognised input pixel colour: " + levelImage.getPixels()[tileIndex]);
					}

				} else {
					
					// AIR TILE 
					// else false, level tile will be empty
					levelTiles[x + y * levelWidth] = new LevelTile(0,x,y);
					//System.out.println("Setting level tile at index: " + (x + y * levelWidth) + " as an AIR BLOCK" );
				}

			}
		}
		
		
		// set level tiles in the gm..... this seems stupid.. will change this in future 31/10/20
		gm.setLevelTiles(levelTiles);
		gm.setLevelWidth(levelWidth);
		gm.setLevelHeight(levelHeight);
		
		
		
	}
	
	
	/**
	 * Renders the level: the background image and the level tiles 
	 **/
	public void render(Renderer r){
		
		// Draw the background first
		r.setzDepth(-1);
		r.drawImage(levelBackground, r.getCamX(), r.getCamY());
		r.setzDepth(0);
		
		for(int y = 0; y < levelHeight; y++){
			for(int x = 0; x < levelWidth; x++){

				//System.out.println("levelTiles size: " + levelTiles.length);
				//System.out.println("x: " + x + ", y: " + y);
				//System.out.println("Trying to access position: " + (x + y * levelWidth));
				
				
				
				
				
				// DIRT TILE
				if(levelTiles[x + y * levelWidth].getType() == 1){

					r.drawImage(Textures.dirtBack, x* tileSize, y * tileSize);
					if(levelTiles[x+y*levelWidth].accessible == true && GameManager.showDebug)r.drawImage(Textures.marker, x * tileSize, y * tileSize);

				// GRASS TILE
				}else if(levelTiles[x + y * levelWidth].getType() == 2){

					r.drawImage(Textures.grass, x* tileSize, y * tileSize);
					
					// prints numbers on grass blocks if debug mode is on
					if(GameManager.showDebug)r.drawText(Integer.toString(x), x*tileSize, y*tileSize, 0xff000000);
					
					if(levelTiles[x+y*levelWidth].accessible == true && GameManager.showDebug)r.drawImage(Textures.marker, x * tileSize, y * tileSize);

				// STONE TILE
				} else if(levelTiles[x + y * levelWidth].getType() == 3){

					r.drawImage(Textures.stone, x* tileSize, y * tileSize);
					if(levelTiles[x+y*levelWidth].accessible == true && GameManager.showDebug)r.drawImage(Textures.marker, x * tileSize, y * tileSize);
				
				// BRICK TILE
				}else if(levelTiles[x + y * levelWidth].getType() == 4) {
					
					r.drawImage(Textures.brick, x* tileSize, y * tileSize);
					
				// LOG TILE
				}else if(levelTiles[x + y * levelWidth].getType() == 5){
					r.drawImage(Textures.log, x* tileSize, y * tileSize);
					
					
				} else if(levelTiles[x + y * levelWidth].getType() == -1){
					r.drawImage(Textures.dirt, x* tileSize, y * tileSize);
					
				} else if(levelTiles[x + y * levelWidth].getType() == 6){
					r.drawImage(Textures.leaf, x* tileSize, y * tileSize);
					
				} else if(levelTiles[x + y * levelWidth].getType() == 7){
					r.drawImage(Textures.fence, x* tileSize, y * tileSize);
					
				} else if(levelTiles[x + y * levelWidth].getType() == -2){
					r.drawImage(Textures.dirtBackReinforced, x* tileSize, y * tileSize);
				}else if(levelTiles[x + y * levelWidth].getType() == -3){
					r.drawImage(Textures.water, x* tileSize, y * tileSize);
					
				}else if(levelTiles[x + y * levelWidth].getType() == 8){
					
					r.drawImage(Textures.snow, x* tileSize, y * tileSize);
					
				}else if(levelTiles[x + y * levelWidth].getType() == -4){
					r.drawImage(Textures.brickBack, x* tileSize, y * tileSize);
					
					
				}else if(levelTiles[x + y * levelWidth].getType() == 9){
					r.drawImage(Textures.marbleBrick, x* tileSize, y * tileSize);
				}else if(levelTiles[x + y * levelWidth].getType() == -5){
					r.drawImage(Textures.marblePillar, x* tileSize, y * tileSize);
				}else if(levelTiles[x + y * levelWidth].getType() == 10){
					r.drawImage(Textures.ice, x* tileSize, y * tileSize);
				}else{
					// AIR TILE
					// Must be an air tile
					if(levelTiles[x+y*levelWidth].accessible == true && GameManager.showDebug && DebugPanel.showPathMap)r.drawImage(Textures.marker, x * tileSize, y * tileSize);
				}
				
				// Tile Augments that apply to all tile types: 
				
				// ? 
				if(GameManager.renderPathFinding){
					if(levelTiles[x+y*levelWidth].visited == true)r.drawImage(Textures.marker, x * tileSize, y * tileSize);
					if(levelTiles[x+y*levelWidth].accessible == true)r.drawImage(Textures.node, x * tileSize, y * tileSize);
				}
				
				if(DebugPanel.showAwareness){
					if(levelTiles[x+y*levelWidth].checked)r.drawImage(Textures.tileFrame, x * tileSize, y * tileSize);
				}
				
				if(DebugPanel.showTileCollision){
					if(levelTiles[x+y*levelWidth].checked)r.drawImage(Textures.tileFrame, x * tileSize, y * tileSize);
					if(levelTiles[x+y*levelWidth].colliding)r.drawImage(Textures.tileFrameRed, x * tileSize, y * tileSize);
					
				}
				
			}
		}
		
	}
	

}
