package com.gussssy.gametwo.game.components;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.testObjects.TileObject;
import com.gussssy.gametwo.game.physics.Physics;

/**
 * AABBComponent. Axis alligned bounding box. 
 *  
 *  Rectanglualr axis alligned hitbox for detecting collisions with other GameObjects. 
 */
public class AABBComponent extends Component{
	
	private boolean print = false;
	
	// variables held and updated by AABBComponent that are used by Physics to detect collisions
	private int centerX, centerY;
	private int halfWidth, halfHeight;
	private int lastCenterX, lastCenterY;		// location of the center last frame
	private int startX, startY, stopX, stopY;	// coords of the corners of the hitbox. Start is left, stop is right. 
	private int width, height;					// dimensions of the parent GameObject
	
	// Not currently in use. Was used for collision detection with particles. 
	public boolean particleCollisionDetectionEnabled = false;
	
	// Whether or not this AABBComponent is currently collising with another AABBComponent
	public boolean colliding = false;
	
	// The type of AABBComponent. These shoud be sperate extending classes in the near future.
	int type;
	
	// The color of this AABBComponent when NOT colliding (green)
	public int color = 0xff00ff00;
	
	// The color of this AABBComponent when colliding (red)
	public int collidingColor = 0xffff0000;
	

	
	
	/**
	 * Primary Constructor.
	 * - Initial implementation where objects have 'padding' 
	 **/
	public AABBComponent(GameObject parent){
		this.parent = parent;
		this.tag = "aabb";
		
		width = parent.getWidth();
		height = parent.getHeight();
		
		//System.out.println("AABBComponent construcotor. Type 0 parent: " + parent.getTag());
		
		
		// likely not required to do this here
		startX = (int)parent.getPosX() + parent.getLeftRightPadding();
		startY = (int)parent.getPosY() + parent.getTopPadding();
		stopX = startX + width;
		stopY = startY + height;
		centerX = (int)(parent.getPosX() + (parent.getWidth() / 2) + parent.getLeftRightPadding()); // must account for left right padding
		centerY = (int)(parent.getPosY() + (parent.getHeight() / 2) + (parent.getTopPadding())); //must account for padding at top of player
		halfWidth = parent.getWidth()/2;
		halfHeight = parent.getHeight()/2; 
		
		// initial tyope used for players npcs etc with padding
		type = 0;
		
	}
	
	
	/**
	 * Secondary constructor for making AABB with different conditions
	 * Type 1: no padding 
	 * Type 2: no padding AND this objects isnt actually moving so will set positon last frame manually, not in the AABB Update
	 * 			- used for Collision  Tester Objects which simulate collision wihtout actually moving
	 **/
	public AABBComponent(GameObject parent, int type){
		
		// set tag, type and parent
		this.tag = "aabb";
		this.parent = parent;
		this.type = type;
		
		// Set up dimensions of the hitbox
		if(type == 1 || type == 2){
			// type 1 = no padding
			width = parent.getWidth();
			height = parent.getHeight();
			startX = (int)parent.getPosX();
			startY = (int)parent.getPosY();
			stopX = (int)parent.getPosX() + parent.getWidth();
			stopX = (int)parent.getPosY() + parent.getHeight();
			halfWidth = parent.getWidth()/2;
			halfHeight = parent.getHeight()/2;
			if(print)System.out.println("AABB Constructor type 1: \n\t halfWidth: " + halfWidth + "\n\t halfHeight: " + halfHeight);
		}
		
	}
	
	/**
	 * AABB constructor for temporary/single frame tile hitboxes 
	 **/
	public AABBComponent(TileObject parent, int startTileX, int stopTileX, int startTileY, int stopTileY){
		
		this.parent = parent;
		this.tag = "aabb";
		this.type = 3;
		
		startX = startTileX * GameManager.TS;
		stopX = stopTileX * GameManager.TS + GameManager.TS;
		
		
		startY = startTileY * GameManager.TS;
		stopY = stopTileY * GameManager.TS + GameManager.TS;
		
		width = stopX - startX;
		height = stopY - startY;
		
		halfWidth = width/2;
		halfHeight = height/2;
		
		centerX = startX + halfWidth;
		centerY = startY + halfHeight;
		
		lastCenterX = centerX;
		lastCenterY = centerY;
		
	}
	
	/**
	 * AABBConstructor for when creating an AABBComponent that will be modiffied later for collision with tiles.
	 * This aabb will not work unless its variables are set by modifyAABB(); 
	 * Type is set to type 3: temporary AABB for tile collision
	 **/
	public AABBComponent(TileObject parent){
		this.tag = "aabb";
		this.parent = parent;
		type = 3;
		
		color = 0xff00ffff;
		collidingColor = 0xffffaa00;
		
		
	}
	

	/**
	 * AABBComponent Update. 
	 * 
	 * Recalculate the location of the hitbox for this frame and then add the AABB to Physics for collision detection. 
	 */
	@Override
	public void update(GameContainer gc, GameManager gm, float dt){
		
		// set colliding to false, this will be reset later by Physics if a collison occurs.
		colliding = false;
		
		switch(type){
			case 0: // Normal AABB (has padding)
				lastCenterX = centerX;
				lastCenterY = centerY;
				startX = (int)parent.getPosX() + parent.getLeftRightPadding();
				startY = (int)parent.getPosY() + parent.getTopPadding();
				stopX = startX + width; // these value arent used to detect the collison but are used when processing? THESE ARE WRONG AS PADDING ON RHS IS NOT CONSIDERED
				stopY = startY + height;
				centerX = (int)(parent.getPosX() + (parent.getWidth() / 2) + parent.getLeftRightPadding()); // must account for left right padding
				centerY = (int)(parent.getPosY() + (parent.getHeight() / 2) + (parent.getTopPadding())); //must account for padding at top of player
				//halfWidth = parent.getWidth()/2;
				//halfHeight = parent.getHeight()/2; 
				Physics.addAABBComponent(this);
				break;
			case 1: // No padding
				lastCenterX = centerX;
				lastCenterY = centerY;
				startX = (int)parent.getPosX();
				startY = (int)parent.getPosY();
				stopX = startX + width;
				stopY = startY + height;
				centerX = (int)(parent.getPosX() + halfWidth);
				centerY = (int)(parent.getPosY() + halfHeight);
				//halfWidth = parent.getWidth()/2;
				//halfHeight = parent.getHeight()/2; 
				Physics.addAABBComponent(this);
				break;
			case 2: // Stationary
				// This is set manually as the object is actually moving
				//lastCenterX = centerX;
				//lastCenterY = centerY;
				startX = (int)parent.getPosX();
				startY = (int)parent.getPosY();
				stopX = startX + width;
				stopY = startY + height;
				centerX = (int)(parent.getPosX() + halfWidth);
				centerY = (int)(parent.getPosY() + halfHeight);
				//halfWidth = parent.getWidth()/2;
				//halfHeight = parent.getHeight()/2; 
				Physics.addAABBComponent(this);
				break;
			case 3: // Tile Hitbox
				// Temporary Tile AABB
				// start/stop/center location of this hitbox is static
				// For now will add these AABBs to Physics in the class that made them
				//Physics.addAABBComponent(this);
				break;
		}
		
	}
	
	/**
	 * Modify a tileCollision AABB
	 * 
	 *   This is only for type 3 AABBs which come from tiles that have collided with a projectile. 
	 **/
	public void modifyTileAABBComponent(int startTileX, int stopTileX, int startTileY, int stopTileY){
		
		if(type != 3){
			System.out.println("Tried to modify an AABBComponent that is not supposed to be modified. Type is : " + type + "\n (Only type 3 can be modified)");
		}
		
		startX = startTileX * GameManager.TS;
		stopX = stopTileX * GameManager.TS + GameManager.TS;
		
		
		startY = startTileY * GameManager.TS;
		stopY = stopTileY * GameManager.TS + GameManager.TS;
		
		width = stopX - startX;
		height = stopY - startY;
		
		halfWidth = width/2;
		halfHeight = height/2;
		
		centerX = startX + halfWidth;
		centerY = startY + halfHeight;
		
		lastCenterX = centerX;
		lastCenterY = centerY;
		
	}
	
	
	/**
	 * Updates an AABB after a collision. 
	 **/
	public void updateAfterCollision(){
		
		// This could lead to wrong calculations, it will think dy/dx are
		lastCenterX = centerX;
		lastCenterY = centerY;
		centerX = (int)(parent.getPosX() + halfWidth);
		centerY = (int)(parent.getPosY() + halfHeight);
		//System.out.println("Hitbox Recalculating");// Maybe I would like to render previous hgitboxes I dont see...? 
		
		
	}
	

	/**
	 * Renders the AABB. 
	 * 
	 * Will only render AABBs when the debug display is active. 
	 * */
	@Override
	public void render(GameContainer gc, Renderer r){
		
		//if(type == 3)
		
		if(GameManager.showDebug){
			
			if(colliding){
				r.drawRect(centerX - halfWidth, centerY - halfHeight, halfWidth*2, halfHeight*2, collidingColor);
			}else {
				r.drawRect(centerX - halfWidth, centerY - halfHeight, halfWidth*2, halfHeight*2, color);
			}
			
			// the 'last position' used to calculate dy and dx
			if(type == 2 || type == 1)r.drawRect(lastCenterX-halfWidth, lastCenterY-halfHeight,halfWidth*2, halfHeight*2, 0xffffffff);
		}
			
		//System.out.println("x and y: " + (centerX-halfWidth) + " " + (centerY - halfHeight) + ", w h: " + (halfWidth*2) + ", " + halfHeight*2);
		
		
	}
	
	
	public String toString(){
		
		return "AABBComponent: type: " + type + "\n\tstartX: " + startX + "\t stopX: " + stopX + "\n\tstartY: " + startY + "\t stopY: " + stopY + "\n\twidth: " + width + "\theight:" + height + "\n\tcenterX: " + centerX + "\tcenterY:" + centerY;
	}

	
	
	public int getCenterX(){
		return centerX;
	}

	public void setCenterX(int centerX){
		this.centerX = centerX;
	}

	public int getCenterY(){
		return centerY;
	}

	public void setCenterY(int centerY){
		this.centerY = centerY;
	}

	public int getHalfWidth(){
		return halfWidth;
	}

	public void setHalfWidth(int halfWidth){
		this.halfWidth = halfWidth;
	}

	public int getHalfHeight(){
		return halfHeight;
	}

	public void setHalfHeight(int halfHeight){
		this.halfHeight = halfHeight;
	}

	public int getLastCenterX() {
		return lastCenterX;
	}

	public void setLastCenterX(int lastCenterX) {
		this.lastCenterX = lastCenterX;
	}

	public int getLastCenterY() {
		return lastCenterY;
	}

	public void setLastCenterY(int lastCenterY) {
		this.lastCenterY = lastCenterY;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getStopX() {
		return stopX;
	}

	public int getStopY() {
		return stopY;
	}

}
