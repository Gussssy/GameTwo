package com.gussssy.gametwo.game.level;

/**
 * A tile within a level. The terrain of a level consists of a 2D grid of <code>LevelTiles</code>. 
 * <p>
 * <code>LevelTiles</code> may or may not collide with <code>GameObjects</code> and <code>Particles</code>. 
 * The value of integer <code>type</code> determines whether or not the <code>LevelTile</code> will collide with <code>GameObjects</code>.
 * <ul>
 * <li>For <code>type</code> values greater then 0, the <code>LevelTile</code> is solid, collision will occur. 
 * <li>For <code>type</code> values equal to or less then zero, the <code>LevelTile</code> is not solid, collision will not occur.
 * </ul>
 * <p>
 * <code>GameObjects</code> and <code>Particles</code> collide with <code>LevelTiles</code> by calling <code>isCollision</code> on the tile of interest.
 * <p>
 * Unlike <code>GameObject</code> to <code>GameObject</code> collision, <code>LevelTiles</code> do not collide with <code>GameObjects</code> via hit-boxes with the exception of grenades that generate temporary hit-boxes around <code>LevelTiles</code> when a collision is likely.
 * <p>
 * There are many different types of <code>LevelTile</code> such as air, dirt, stone, water etc.
 * 
 * @see Level
 */
public class LevelTile {
	
	
	// The coordinates of this tile within the grid
	private int tileX;
	private int tileY;
	
	
	// whether or not the tile will collide with game objects
	private boolean collision;
	
	
	/**
	 * Integer describing the type of tile
	 * <p>
	 * 0 = air,
	 * 1 = dirt,
	 * 2 = grass, 
	 * 3 = stone,
	 * 4 = brick
	 */
	public int type;
	
	
	/**
	 * Whether or not this tile has been visited during a iteration of path finding.
	 **/
	public boolean visited = false;
	

	/**
	 * Whether or not this tile is accessible to an NPC, e.g there is a valid path to this tile
	 */
	public boolean accessible = false;
	
	
	/**
	 * Whether or not this tile has been checked for vision blocking during an update of NPCVision
	 */
	public boolean checked = false;
	
	
	/**
	 * Whether or not this tile has collided with a GameObjects hit-box (only grenades currently interact with tiles in this fashion)
	 */
	public boolean colliding = false;

	
	
	
	/**
	 *  Constructs a new <code>LevelTile</code> object.
	 *  
	 *  @param type - the type of this LevelTile, air, dirt, stone etc
	 *  @param tileX - the x coordinate of this tiles location within the grid
	 *  @param tileY - the y coordinate of this tiles location within the grid
	 */
	public LevelTile(int type, int tileX, int tileY){
		setType(type);
		//this.type = type;
		this.tileX = tileX;
		this.tileY = tileY;
		
	}
	
	
	
	
	/**
	 *  Sets the type of this LevelTile. Also sets the appropriate value for <code>collision</code>.
	 * <p>
	 * 	If type value is greater then 0, tile will have collision.
	 *  If type value is less than or equal to 0, tile will not have collision.
	 *  
	 *  @param type - the new type this tile will be set too
	 **/
	public void setType(int type) {
		
		if(type <= 0)collision = false;
		else collision = true;
		
		this.type = type;
	}
	
	
	
	
	/**
	 *	Returns a string containing the x and y coordinates of the tile, the type and collision.
	 * 
	 *  @return string containing the x and y coordinates of the tile, the tile type and collision
	 */
	@Override
	public String toString(){
		return "ToString: [] Level Tile []\n\ttileX:" + tileX + "\n\ttileY: " + tileY + "\n\ttype: " + type + "\n\tcollision: " + collision + "\n[] [] []";
	}
	
	
	
	
	/**
	 * Gets the type of this LevelTile.
	 * 
	 * @return type the type of this LevelTile
	 */
	public int getType() {
		return type;
	}

	
	
	
	/**
	 * Returns true if this LevelTile will collide with GameObjects/Particles.
	 * 
	 * @return boolean representing whether or not this tile will collide with GameObjects
	 */
	public boolean isCollision() {
		
		// temporarily removing collision from logs
		if(type == 5){
			return false;	
		}
		
		// for all other level tile types, if type is 0 or less, no collision. 
		if(type <= 0) return false; 
				else return true;
	}




	/**
	 *  Gets the x coordinate of this LevelTile's position in the grid.
	 *  
	 * @return x coordinate of this tile within the tile grid 
	 */
	public int getTileX() {
		return tileX;
	}



	/** 
	 * Gets the y coordinate of this LevelTile's position in the grid.
	 * 
	 * @return y coordinate of this tile within the tile grid 
	 */
	public int getTileY() {
		return tileY;
	}


	
	/**
	 * Sets collision of this LevelTile to the value specified. 
	 * 
	 * NOTE: this doesn't work because collision is determined by the value of type. 15/2/21
	 * 
	 * @param collision whether or not this tile will collide with GameObjects and Particles
	 */
	public void setCollision(boolean collision) {
		this.collision = collision;
	}


	

}
