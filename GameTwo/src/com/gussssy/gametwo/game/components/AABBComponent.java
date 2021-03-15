package com.gussssy.gametwo.game.components;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.testObjects.TileObject;
import com.gussssy.gametwo.game.physics.Physics;

/**
 * Axis aligned bounding box. Rectangular shaped hit-box used to detect collision between two GameObjects.
 * <p>
 * As a component, an AABBComponent is attached to its parent GameObject. Following its parents movement and detecting collisions with other AABBComponents. 
 * <p>
 *  Different types of AABBComponents/hit-boxes behave and are treated different. 
 *  <ul>
 *  <li>Type 0: Initial implementation. For GameObjects with padding
 *  <li>Type 1: For GameObjects with no padding. 
 *  <li>Type 2: For testing purposes with CollisionTesterObject as the parent GameObject. Collisions are simulated without actual motion.
 *  <li>Type 3: LevelTile hit-boxes. Used for rectangle to rectangle collision between GameObects and LevelTiles. These hit-boxes are strange.
 *  They don't have a proper GameObject parent instead use the a placeholder TileObject (not a real GameObject).  
 *  </ul>
 *  
 *  @see com.gussssy.gametwo.game.objects.testObjects.CollisionTesterObject
 */
public class AABBComponent extends Component{
	
	
	// when true, will print debug information to the console.
	private boolean print = false;
	
	
	
	// Variables held and updated by AABBComponent that are used by Physics to detect collisions
	
	// the x and y locations of the center of the hit-box
	private int centerX, centerY;
	
	// the x and y locations of the center of the hit-box LAST FRAME
	private int lastCenterX, lastCenterY;
	
	// width and height of the parent GameObject
	private int width, height;					
	
	// half the width/height of the hit-box
	private int halfWidth, halfHeight;
	
	// x and y locations of where the hit-box starts and finishes.
	private int startX, startY, stopX, stopY;	 
	
	
	/**
	 *  Whether or not this hit-box is currently colliding with another hit-box
	 */
	public boolean colliding = false;
	
	// The type of AABBComponent. TODO: These should be separate extending classes in the near future 16/2/21.
	private int type;
	
	// The color of this AABBComponent when NOT colliding (green)
	private int color = 0xff00ff00;
	
	// The color of this AABBComponent when colliding (red)
	private int collidingColor = 0xffff0000;
	

	
	
	/**
	 * Constructs a new AABBComponent for the parent GameObject.
	 * 
	 * As no type is specified, type will be 0. For type 0 hit-boxes, padding must be considered.
	 **/
	public AABBComponent(GameObject parent){
		
		this.parent = parent;
		this.tag = "aabb";
		this.type = 0;
		
		width = parent.getWidth();
		height = parent.getHeight();
		
		if(print)System.out.println("AABBComponent construcotor. Type 0 parent: " + parent.getTag());
		
		// Set up all necessary values.
		startX = (int)parent.getPosX() + parent.getLeftRightPadding();
		startY = (int)parent.getPosY() + parent.getTopPadding();
		stopX = startX + width;
		stopY = startY + height;
		centerX = (int)(parent.getPosX() + (parent.getWidth() / 2) + parent.getLeftRightPadding()); // must account for left right padding
		centerY = (int)(parent.getPosY() + (parent.getHeight() / 2) + (parent.getTopPadding())); //must account for padding at top of player
		halfWidth = parent.getWidth()/2;
		halfHeight = parent.getHeight()/2; 
		
	}
	
	
	
	
	/**
	 * Constructs a new AABBComponent for the parent GameObject of the specified type.
	 * <p>
	 * Type 1: For small projectiles with no padding 
	 * Type 2: For CollisionTesterObjects, no padding AND the parent object isn't actually moving but we want to simulate motion. Position last frame must be set manually, not in the AABB Update
	 * 			- used to simulate a collision without actually moving
	 * 
	 * @param parent The parent GameObject.
	 * @param type the type of hit-box to be constructed
	 * @see game.objects.testObject.CollisionTesterObject
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
	 * Constructs a new LevelTile hit-box.
	 * <p>
	 *  These hit-boxes are strange as they do not really have a parent GameObject, they use a TileObject. A TileObject is really just a  workaround/placeholder
	 *  for a parent GameObject which is required for all components. The only purpose of the TileObject is to fulfill that requirement and 
	 *  provide a means to inform colliding objects that it has hit a tile, so it can process the collision accordingly.
	 *  <p>
	 *  
	 *  @param parent TileObject is a placeholder, not a real object in the game. Allows collisions with this hit-box to be processed appropriately. 
	 **/
	public AABBComponent(TileObject parent){
		
		this.tag = "aabb";
		this.parent = parent;
		type = 3;
		
		// colliding and non colliding colors for this type of hit-box are different
		color = 0xff00ffff;
		collidingColor = 0xffffaa00;
	}
	

	
	
	/**
	 * Update the hit-box to reflect the current position of its parent GameObject.
	 * 
	 * Once recalculated, send the hit-box to Physics for collision detection with every other hit-box. 
	 * 
	 *  @param gc The GameContainer.
	 *  @param gm The GameManager.
	 *  @param dt The time passed since the last update.
	 */
	@Override
	public void update(GameContainer gc, GameManager gm, float dt){
		
		// set colliding to false, this will be reset later by Physics if a collision occurs.
		colliding = false;
		
		// How each type of hit-box is updated depends on it's type. 
		switch(type){
			
		
		case 0: // normal hit-box, parent GameObject has padding
				
				lastCenterX = centerX;
				lastCenterY = centerY;
				startX = (int)parent.getPosX() + parent.getLeftRightPadding();
				startY = (int)parent.getPosY() + parent.getTopPadding();
				stopX = startX + width; // these value arent used to detect the collison but are used when processing? THESE ARE WRONG AS PADDING ON RHS IS NOT CONSIDERED
				stopY = startY + height;
				centerX = (int)(parent.getPosX() + (parent.getWidth() / 2) + parent.getLeftRightPadding()); // must account for left right padding
				centerY = (int)(parent.getPosY() + (parent.getHeight() / 2) + (parent.getTopPadding())); //must account for padding at top of player 
				
				// Add this hit-box to physics for collision detection
				Physics.addAABBComponent(this);
				break;
			
				
				
			case 1: // no padding
				
				lastCenterX = centerX;
				lastCenterY = centerY;
				startX = (int)parent.getPosX();
				startY = (int)parent.getPosY();
				stopX = startX + width;
				stopY = startY + height;
				centerX = (int)(parent.getPosX() + halfWidth);
				centerY = (int)(parent.getPosY() + halfHeight);
				Physics.addAABBComponent(this);
				break;
			
				
				
			case 2: // parent object is actually stationary but motion is being simulated
				
				// This is set manually by CollisionTesterObject as the object isn't actually moving
				//lastCenterX = centerX;
				//lastCenterY = centerY;
				
				// This is set automatically like type 0 and t hitboxes.
				startX = (int)parent.getPosX();
				startY = (int)parent.getPosY();
				stopX = startX + width;
				stopY = startY + height;
				centerX = (int)(parent.getPosX() + halfWidth);
				centerY = (int)(parent.getPosY() + halfHeight);
				Physics.addAABBComponent(this);
				break;
				
				
				
			case 3: // LevelTile hit-box
				
				// The location of these type of hit-boxes is set by the GameObject that is likely going to collide with a LevelTle.
				// start/stop/center location of this hitbox is static
				// For now, these AABBs  are added to Physics by the class that made them.
				
				// TODO: this is a very messy process and could be a lot more simple and logical 16/2/21
				
				break;
		}
		
	}
	
	
	
	
	/**
	 * Modify a LevelTile (type = 3) hit-box.
	 * 
	 *   This is only for type 3 AABBs which come from tiles that will likely collide with a projectile. This is not guaranteed. 
	 *   
	 *   When a Grenade determines it will likely collide with a LevelTile, this method modifies an existing type 3 hit-box, 
	 *   
	 *   The result of this method is a hit-box that surrounds one or two consecutive level tiles. 
	 *   
	 *   NOTE: why don't I add this to physics right here
	 *   
	 *   @param startTileX
	 *   @param stopTileX
	 *   @param startTileY
	 *   @param stopTileY
	 **/
	public void modifyTileAABBComponent(int startTileX, int stopTileX, int startTileY, int stopTileY){
		
		if(type != 3){
			System.out.println("Tried to modify an AABBComponent that is not supposed to be modified. Type is : " + type + "\n (Only type 3 can be modified in this fasion)");
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
		
		// A good place to add to physics..? 
		
	}
	
	
	/**
	 * Updates an AABB after a collision has occurred. 
	 **/
	public void updateAfterCollision(){
		
		// This could lead to wrong calculations, it will think dy/dx are....
		lastCenterX = centerX;
		lastCenterY = centerY;
		centerX = (int)(parent.getPosX() + halfWidth);
		centerY = (int)(parent.getPosY() + halfHeight);
	}
	

	
	
	/**
	 * Renders the AABB. 
	 * 
	 * Will only render AABBs when the debug display is active. 
	 * @param r The renderer.  
	 */
	@Override
	public void render(Renderer r){
		
		if(GameManager.showDebug && DebugPanel.showHitBoxes){
			
			if(colliding){
				r.drawRect(centerX - halfWidth, centerY - halfHeight, halfWidth*2, halfHeight*2, collidingColor);
			}else {
				r.drawRect(centerX - halfWidth, centerY - halfHeight, halfWidth*2, halfHeight*2, color);
			}
			
			// the 'last position' used to calculate dy and dx. Rendered as a white rectangle.
			if(type == 2 || type == 1)r.drawRect(lastCenterX-halfWidth, lastCenterY-halfHeight,halfWidth*2, halfHeight*2, 0xffffffff);
		}	
	}
	
	
	
	
	/**
	 * Returns a String containing various attributes of the hit-box. 
	 * 
	 * @return a String containing various attributes of the hit-box.
	 */
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


	
	
	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	
	

	/**
	 * @param collidingColor the collidingColor to set
	 */
	public void setCollidingColor(int collidingColor) {
		this.collidingColor = collidingColor;
	}

}
