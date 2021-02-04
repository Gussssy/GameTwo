package com.gussssy.gametwo.game.level;

public class LevelTile {
	
	/**
	 * Does this tile collide with GameObjects? 
	 **/
	private boolean collision;
	
	//private LevelTileType type;
	
	/**
	 * Int describing the type of tile
	 * 0 = air
	 * 1 = dirt
	 * 2 = grass 
	 * 3 = stone
	 * 4 = brick
	 **/
	public int type;
	private int tileX;
	private int tileY;
	
	public boolean visited = false;
	public boolean accessible = false;
	public boolean checked = false;
	public boolean colliding = false;

	
	public LevelTile(int type, int tileX, int tileY){
		setType(type);
		//this.type = type;
		this.tileX = tileX;
		this.tileY = tileY;
		
	}
	
	
	
	

	/**
	 *  Sets the type of this level tile. Also sets the appropriate value of collision. 
	 *  Type = 0, Air Block, No collision
	 *  Type = 1|2 Dirt or Grass, solid so has Collision 
	 **/
	public void setType(int type) {
		
		if(type <= 0)collision = false;
		else collision = true;
		
		this.type = type;
	}
	
	@Override
	public String toString(){
		return "ToString: [] Level Tile []\n\ttileX:" + tileX + "\n\ttileY: " + tileY + "\n\ttype: " + type + "\n\tcollision: " + collision + "\n[] [] []";
	}
	
	public int getType() {
		return type;
	}

	public boolean isCollision() {
		return collision;
	}





	public int getTileX() {
		return tileX;
	}





	public int getTileY() {
		return tileY;
	}





	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	

}
