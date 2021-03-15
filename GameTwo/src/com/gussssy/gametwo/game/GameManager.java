package com.gussssy.gametwo.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.gussssy.gametwo.engine.AbstractGame;
import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.debug.Log;
import com.gussssy.gametwo.game.level.Arena2;
import com.gussssy.gametwo.game.level.Arena5;
import com.gussssy.gametwo.game.level.Arena6;
import com.gussssy.gametwo.game.level.CollisionWorld;
import com.gussssy.gametwo.game.level.IceSpike;
import com.gussssy.gametwo.game.level.Level;
import com.gussssy.gametwo.game.level.LevelTile;
import com.gussssy.gametwo.game.level.MyPalace;
import com.gussssy.gametwo.game.level.RabbitHills;
import com.gussssy.gametwo.game.level.RedPlanet;
import com.gussssy.gametwo.game.level.SnowMap1;
import com.gussssy.gametwo.game.level.SpaceStationMap;
import com.gussssy.gametwo.game.level.WaterSiege;
import com.gussssy.gametwo.game.level.WaterTestMap;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.player.Player;
import com.gussssy.gametwo.game.particles.TileDestruction;
import com.gussssy.gametwo.game.physics.Physics;
import com.gussssy.gametwo.game.ui.HUD;
import com.gussssy.gametwo.game.ui.TabMenu;

/**
 * GameManger plays a central role in the operation of the game:
 * <ul>
 * <li>Contains the objects list, calling update and render on each contained object each frame.
 * <li>Holds the events list, UI such as the tab menu and the HUD. Updates and renders these too.
 * <li>Holds the currently loaded level and provides other objects access to it.
 * <li>Calls update on Physics which detects collisions.
 * </ul>
 **/
@SuppressWarnings("unused")
public class GameManager extends AbstractGame{

	/**
	 * The size of the tiles on the x and y axis, in pixels. 
	 * <p>
	 * The Game has been developed using 16*16 tiles. This could change in the future.  
	 */
	public static final int TS = 16;

	// Game Components
	private Camera camera;
	
	// Renderer - used to set the ambient colour via the GameManager
	private static Renderer r;

	// Cursor Image
	private Image cursor = new Image("/target_cursor_grey.png");

	// HUD
	private HUD hud = new HUD();

	// TAB MENU 
	private TabMenu tabMenu = new TabMenu();
	
	/**
	 * The Payer. GameObject that is controlled by user input. 
	 */
	public static Player player = new Player(1,11);

	// Level Variables
	private LevelTile[] levelTiles;			// need to remove this from here
	private int levelWidth, levelHeight;	// need to remove this from here
	//private float gravity = 10;				// Should this be here? Maybe a variable of Level?
	/** The currently loaded level */
	public Level level;						// The currently loaded level

	// Contains all the GameObejects currently active in the game
	private ArrayList<GameObject> objects = new ArrayList<GameObject>(); 
	
	//private ArrayList<Event> events = new ArrayList<Event>();
	// events will be managed by separate class Event Manager (is static for now)
	

	// Debugging Variables
	/** When true the debug messages will be rendered*/
	public static boolean showDebug = false;
	public static boolean renderPathFinding = false;
	
	/** Logs error messages to file */
	public static Log log = new Log();
	
	//Enable or Disable friendly fire
	public static boolean friendlyFire = false; //(would like to move this to a more appropriate place)

	// Update Speed testing variables
	private long lastUpdateTime; 
	private long totalUpdateTime = 0;
	private int updates;
	private boolean testUpdateTime = false; 

	
	
	
	
	// WIP 
	// volume test - apprently i cant exceed a value of  
	private float volume = 5f;
	
	private Image spaceXLogo = new Image("/spacex_logo.png");

	
	/**
	 * GameManager Constructor.
	 * 
	 *  Initializes the game. 
	 **/
	public GameManager(){

		// INIT TEXTURES
		Textures.init();
		SoundManager.init();

		// INIT PLAYER 
		objects.add(player);

		// LEVEL LOADING
		//level = new Arena2("/Arena2.png", "/Area5_bg1.png", this);
		//level = new Arena5("/Arena5_v3.png", "/Area5_bg1.png", this);
		//level = new WaterTestMap("/watermap1.png","/Area5_bg1.png", this);
		level = new SnowMap1("/snowmap1.png","/nightsky_bg.png",this);
		//level= new CollisionWorld("/CollisionTestMap.png","/Area5_bg1.png", this);
		//level = new Arena6("/Arena6.png","/Area5_bg1.png", this);
		//level = new MyPalace("/Palace1.png", "/Area5_bg1.png", this);
		//level = new IceSpike("/level/icespikemap.png","/Area5_bg1.png",this);
		//level = new RabbitHills(this);
		//level = new WaterSiege(this);
		//level = new FlatMap(this);
		//level = new RedPlanet(this);
		//level = new SpaceStationMap(this);

		// CURSOR
		cursor.setAlpha(true);

		// CAMERA
		camera = new Camera("player");
	}




/**
 * GameManager update. 
 * <p>
 * Call update on the GameObjects, the level, HUD, Physics, log, camera and debug panel.
 * 
 * @param gc the GameContainer.
 * @param dt the time passed since last update 
 **/
	@Override
	public void update(GameContainer gc, float dt){

		// Test update time. Records start time of this update of enabled.
		if(testUpdateTime){
			lastUpdateTime = System.nanoTime();
		}

		// UPDATING THE GAME 

		// level update
		level.update(gc, this, dt);

		// update the log: 
		log.update();

		// Update GameObjects
		for(int i = 0; i < objects.size(); i++){

			// Update game object
			objects.get(i).update(gc, this, dt);


			// If the gameobject is now dead, remove it
			if(objects.get(i).isDead()){
				//System.out.println("Removing: " + objects.get(i).getTag());
				objects.remove(i);
				i--;	// decrement i so next game objects is not skipped
			}
		}

		
		// hud update
		hud.update(gc, dt);

		//Update the camera position
		camera.update(gc, this, dt);

		// Collision Detection
		Physics.update();
		
		
		// Update Events
		EventManager.update(gc, this, dt);
		
		
		
		// TILE DESTRUCTION
		// initial testing of tile destruction
		if(gc.getInput().isButtonDown(1) && gc.getInput().isKey(KeyEvent.VK_E)){
			
			// REALLY WANT THE CURSOR CLASS HERE
			// get the level tile - wont work when cam is offset 
			int tileX = (gc.getInput().getMouseX() + gc.getRenderer().getCamX())/TS;
			int tileY = (gc.getInput().getMouseY() + gc.getRenderer().getCamY())/TS;
			LevelTile tile = getLevelTile(tileX, tileY);
			EventManager.addEvent(new TileDestruction(tile));
		}
		
		
		
		// Debug 
		
		// update debug display if showDebug is true
		if(showDebug)DebugPanel.update(gc);
		
		// toggle debug display by pressing F3
		if(gc.getInput().isKeyDown(KeyEvent.VK_F3)){
			if(showDebug == false) {
				showDebug = true;
			} else {
				showDebug = false;
			}
		}



		// Modify Player Movement

		// Toggle Player Free Movement be pressing 0
		if(gc.getInput().isKeyDown(KeyEvent.VK_0)){

			if(player.isFreeMovementEnabled()){
				// turn off free movement
				player.setFreeMovementEnabled(false);
				SoundManager.turnOff.play();

			}else{
				// turn on free movement
				player.setFreeMovementEnabled(true);
				SoundManager.enable.play();

			}
		}

		
		// Testing Loading Different Levels 22/2/21
		if(gc.getInput().isKey(KeyEvent.VK_CONTROL)){
			
			System.out.println("CONTROL PRESSED");
			
			//level = new FlatMap(this);
			//level = new RedPlanet(this);
			//level = new SpaceStationMap(this);
			
			if(gc.getInput().isKeyDown(KeyEvent.VK_1)){
				
				unloadLevel();
				level = new Arena2("/Arena2.png", "/Area5_bg1.png", this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_2)){
				
				unloadLevel();
				level = new Arena5("/Arena5_v3.png", "/Area5_bg1.png", this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_3)){
				
				unloadLevel();
				level = new WaterTestMap("/watermap1.png","/Area5_bg1.png", this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_4)){
				
				unloadLevel();
				level = new SnowMap1("/snowmap1.png","/nightsky_bg.png",this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_5)){
				
				unloadLevel();
				level= new CollisionWorld("/CollisionTestMap.png","/Area5_bg1.png", this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_6)){
				
				unloadLevel();
				level = new Arena6("/Arena6.png","/Area5_bg1.png", this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_7)){
				
				unloadLevel();
				level = new MyPalace("/Palace1.png", "/Area5_bg1.png", this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_8)){
				
				unloadLevel();
				level = new IceSpike("/level/icespikemap.png","/Area5_bg1.png",this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_9)){
				
				unloadLevel();
				level = new RabbitHills(this);
				
			}else if(gc.getInput().isKeyDown(KeyEvent.VK_MINUS)){
				
				unloadLevel();
				level = new WaterSiege(this);
				
			}
			
		}


		// -------------------------------------------------------------------------------------------------------
		// 											CAMERA MODIFICATIONS

		// Set camera to follow the first botbot it finds in the game objects - no good if there are no botbots
		if(gc.getInput().isKeyDown(KeyEvent.VK_7)){

			camera.setTarget(getObject("botbot"));
		}


		// Set the camera to follow the player
		if(gc.getInput().isKeyDown(KeyEvent.VK_8)){

			camera.setTarget(player);
		}

		// --------------------------------------------------------------------------------------------------------



		// WIP VOLUME CONTROLLS

		// NOT USING VOLUME CONTROLS  VERY BUGGY

		/**
		// Testing music volume control...  WORKS, 1 is a small step
		if(gc.getInput().isKeyDown(KeyEvent.VK_L)){
			volume += 1f;
			SoundManager.music.setVolume(volume);
		}*/

		

		// Attempt at controlling volume
		if(gc.getInput().isKeyDown(KeyEvent.VK_K)){
			volume -= 1f;
			//SoundManager.music.setVolume(volume);
		}


		// Testing Update time. Get time at end of update. 
		if(testUpdateTime){
			lastUpdateTime = System.nanoTime() - lastUpdateTime;
			totalUpdateTime += lastUpdateTime;
			updates++;

			if(updates > 60){
				System.out.println("Ave update time for last 60 frames: " + (totalUpdateTime/updates));
				updates = 0;
				totalUpdateTime = 0;
			}
		}


	}
	
	
	
	/**
	 * Purges the GameObjects in the objects list and re adds the player so a new level can be loaded. 
	 */
	private void unloadLevel(){
		
		// purge the objects list
		objects.clear();
		
		EventManager.purge();
		
		objects.add(player);
	}


	/**
	 * Determines if the tile located at (input) x and (input) y coords will collide with a game object
	 * 
	 * @param tileX x location of the tile to be checked for collision
	 * @param tileY y location of the tile to be checked for collision
	 * @return wether or not the specified tile is solid or not and therefore if it collides
	 */
	public boolean getLevelTileCollision(int tileX, int tileY){

		// Check if in bounds
		if(tileX < 0 || tileX >= levelWidth || tileY < 0 || tileY >= levelHeight) {
			//System.out.println("Tried to look for collision outside level bounds");
			return true; // everything outside of the level is solid, so collision = true
		}

		// Return collision for this tile
		// return levelTileCollisions[tileX + tileY * levelWidth];
		return levelTiles[tileX + tileY * levelWidth].isCollision();
	}


	/**
	 * Returns the specified level tile. 
	 * 
	 *  However if the tile does not exist returns null. BEWARE
	 *  
	 * @param tileX x location of the tile 
	 * @param tileY y location of the tile 
	 * @return the tile specified by tileX and tileY parameters
	 **/
	public LevelTile getLevelTile(int tileX, int tileY){

		// Check if in bounds
		if(tileX < 0 || tileX >= levelWidth || tileY < 0 || tileY >= levelHeight)
			return null; // everything outside of the level is solid, so collision = true

		return levelTiles[tileX + tileY * levelWidth];
	}



	/**
	 * Adds a GameObject to the objects list so it is updated and rendered each frame  
	 * 
	 * @param obj the GameObject to add to the objects list
	 **/
	public void addObject(GameObject obj){
		objects.add(obj);
	}


	/**
	 * Gets a GameObject from the list of currently active game objects
	 * 
	 * @param the tag of the desired GameObject
	 * @return the first GameObject from the objects list that has a matching tag
	 **/
	public GameObject getObject(String tag){

		for(GameObject obj : objects){

			if(obj.getTag().equals(tag)){
				return obj;
			}

		}

		return null;
	}


	/**
	 * MAIN METHOD. Program entry point. 
	 * 
	 * @param args (not used)
	 * */
	public static void main(String[] args){

		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth(gameWidth);
		gc.setHeight(gameHeight);
		gc.setScale(scale);
		gc.start();

	}


	/**
	 * Performs some initialisation tasks that cannot be completed untill after GameManager has been constructed.
	 * 
	 *   @param gc, the GameContainer
	 */
	@Override
	public void init(GameContainer gc){

		// Sets ambient colour to full brightness
		gc.getRenderer().setAmbientColor(-1);
		
		//GameManager.gc = gc;
		GameManager.r = gc.getRenderer();

	}

	/**
	 * Set the ambient colour 
	 * 
	 * @param newAmbientColor, int representing RGB colour. 
	 **/
	public void setAmbientColor(int newAmbientColor){
		r.setAmbientColor(newAmbientColor);
	}

	
	// testing something I spent ages drawing.. looks average
	Image window = new Image("/observation_window.png");

	
	
	/**
	 * Renders the game.
	 * 
	 * Calls Render on:
	 * - the currently loaded level
	 * - all GameObjects
	 * - HUD
	 * - Tab Menu / inventory
	 * - Debug display
	 * 
	 * - Renders text containing basic player position/state varibales etc
	 * - Renders Debug Messages 1-12
	 * - Renders the cursor (last so nothing if drawn on top of it.) 
	 * 
	 * @param gc The GameContainer, used to access the Input class to check for Keyboard input.
	 * @param r The renderer
	 */
	@Override
	public void render(GameContainer gc, Renderer r){

		
		
		// Render the level
		level.render(r);
		
		// testing observation window
		//r.drawImage(window, 152*16, 21*16);
		//r.drawImage(window, 31*16, 18*16);

		// Render Game Objects
		for(GameObject o : objects ){
			o.render(r);
		}
		
		//Render Events
		EventManager.render(r);

		// render the HUD
		//hud.render(gc, r);

		// render if the tabMenu if tab is down
		if(gc.getInput().isKey(KeyEvent.VK_TAB)){
			tabMenu.render(gc,r);
		}

		// render Debug display if active 
		if(showDebug){
			DebugPanel.render(gc,r);
		
		}
		
		

		// Render the cursor. This must be last. 
		r.drawImage(cursor, gc.getInput().getMouseX()+r.getCamX(), gc.getInput().getMouseY()+r.getCamY());

	}

	
	/**
	 * Gets the width of the level in tiles
	 * 
	 * @return the width of the level in tiles
	 */
	public int getLevelWidth(){
		return levelWidth;
	}

	/**
	 * Gets the height of the level in tiles
	 * 
	 * @return the height of the level in tiles 
	 */
	public int getLevelHeight(){
		return levelHeight;
	}

	/**
	 * Gets the GameObjects list
	 * 
	 * @return The GameObjects list
	 */
	public ArrayList<GameObject> getObjects() {
		return objects;
	}


	/**
	 * Gets the currently loaded LevelTiles
	 * 
	 * @return the currently loaded LevelTiles
	 */
	public LevelTile[] getLevelTiles() {
		return levelTiles;
	}


	/**
	 * Sets all the level tiles accessibility to true or false
	 * 
	 * The accessible field of LevelTile is used for NPC pathfinding. 
	 * When a tile accessible is set to false, an NPC wont try and path to it. 
	 * 
	 * @param accessible, wether all level tiles are to be set to accessible or not accessible
	 */
	public void setAllTilesAccessible(boolean accessible){
		for(LevelTile t : levelTiles){
			t.accessible = accessible;
		}
	}

	/**
	 * Sets all the level tiles visited field to true or false
	 * 
	 * The visited field of LevelTile is used for NPC pathfinding. 
	 * When a tiles visible field is set to true, pathfinding will not revist this tile. 
	 * 
	 * @param accessible, wether all level tiles are to be set to accessible or not accessible
	 */
	public void setAllTilesVisited(boolean visited){
		for(LevelTile t : levelTiles){
			t.visited = visited;
		}
	}

	
	public void setLevelTiles(LevelTile[] levelTiles) {
		this.levelTiles = levelTiles;
	}


	public void setLevelWidth(int levelWidth) {
		this.levelWidth = levelWidth;
	}


	public void setLevelHeight(int levelHeight) {
		this.levelHeight = levelHeight;
	}


	public static int getGameWidth() {
		return gameWidth;
	}


	public static int getGameHeight() {
		return gameHeight;
	}



	// WINDOW SIZE AND SCALE

	// FullScreen and Scale of 1, minimal mouse tracking error (background too small)
	// very low fps, sub ~20
	//private static int gameWidth = 2500; 
	//private static int gameHeight= 1400;
	//private static float scale = 1f;
	 

	// Full screen, minimal mouse tracking error, scale of 2. (background is too small for this size)
	// private static int gameWidth = 1250; 
	// private static int gameHeight= 700;
	// private static float scale = 2f;
	 

	// Full screen and minimal error in mouse tracking
	private static int gameWidth = 833; 
	private static int gameHeight= 467;
	private static float scale = 3f;


	// Window size and Game Scale

	// testing to find dimensions with least screw ups on mouse tracing
	//private static int gameWidth = 560;
	//private static int gameHeight= 310;
	//private static float scale = 5f;

	
	// BEST SiZE FOR DEBUGGING
	// Normal Settings
	//private static int gameWidth = 560;
	//private static int gameHeight= 310;
	//private static float scale = 4.0f;
	//private static float scale = 3f;
	//private static float scale = 1f;

	// Large - (doesnt work at all with arena 2)
	//private static int gameWidth = 960;
	//private static int gameHeight= 540;
	//private static float scale = 3f;
	//private static float scale = 2f;

	// large but minimised mouse error - this works well NO MASSIVE ERROR
	//private static int gameWidth = 640;
	//private static int gameHeight= 360;
	//private static float scale = 3f;

	// above but large... 4f... 
	//private static int gameWidth = 533;
	//private static int gameHeight= 300;
	//private static float scale = 2f;

	// 1440p/3 ish but fitted to screen Larger then above 
	//private static int gameWidth = 782;
	//private static int gameHeight= 440;
	//private static float scale = 2f; // was 3

	// Squareish, with space to see console
	//private static int gameWidth = 550;
	//private static int gameHeight= 420;
	//private static float scale = 3f;

	// Huge Full screen
	//private static int gameWidth = 2560;
	//private static int gameHeight= 1440;
	//private static float scale = 1f;

}
