package com.gussssy.gametwo.game.objects;

import java.util.ArrayList;
import java.util.UUID;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.audio.SoundClip;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.Component;
import com.gussssy.gametwo.game.physics.CollisionType;

/**
 * Base class for all of the objects in the Game. 
 * <p>
 * As long as a GameObject is in the objects list, GameManager will call update and render on it each frame.
 */
public abstract class GameObject{
	
	
	
	/**
	 * A unique string used to identify this GameObject instance specifically.  
	 */
	public String id;
	
	public ObjectType objectType;
	
	protected String tag;
	
	// LOCATION VARIABLES
	
	// The current tile location of this GameObject.
	protected int tileX;
	protected int tileY;
	
	// The x and y offset of this GameObject relative to the tile it's currently occupying. 
	protected float offX, offY;
	
	// The exact location of this GameObject on the screen. 
	protected float posX, posY;
	
	
	
	
	
	// DIMENSIONS and PADDING
	
	// Width and Height of this GameObject. This includes any padding.
	protected int width, height;
	
	// The empty space between the left and right edges and the actual region this GameObject occupies.
	// 		While the texture may spill over into the padding zone, for tile and hit-box collision, this is ignored.
	//		As not all GameObjects have padding, this is set to zero by default. 
	protected int leftRightPadding = 0; 
	
	// The empty space between the top edge and the actual region this GameObject occupies.
	//		As not all GameObjects have padding, this is set to zero by default. 
	protected int topPadding = 0;
	
	
	
	
	/**
	 * Set to true when the object dies. A dead object will be removed from the objects list next frame.
	 */
	public boolean dead = false;
	
	
	
	/**
	 * This GameObjects components. This will generally include it's hit-box, any weapons and any spell. 
	 * 
	 * Components in this list will be updated each frame and rendered as well if applicable.
	 */
	protected ArrayList<Component> components = new ArrayList<Component>();
	
	
	
	
	// ANIMATION VARIABLES
	// Only GameObjects using an ImageTile for their texture use these variables. 
	// These variables are used to control the cycling individual images within an ImageTile to animate walking/jumping and in some cases more.  
	
	// The direction the GameObject is facing. O = Left, 1 = Right.
	protected int direction = 0;	
	
	// The current tile within a tile image that will be rendered this frame. (Cast to an Integer for rendering) 
	protected float animationState;
	
	// The speed at which the GameObject changes it's animation state. 
	protected float animationSpeed = 8;
	
	// How many frames this object has for its animation. (The number of images in the ImageTile)
	protected int animationFrames = 4;			// by default animated objects have 4 frames, however this is not always the case.
	
	
	
	// Death Sound: 
	// This need to be moved to character
	public SoundClip deathSound = SoundManager.dead;
	
	
	
	// ABSTRACT METHODS
	
	/**
	 * Updates the GameObject.
	 * 
	 *  @param GameContainer used in this context to access any keyboard or mouse input that may have consequences for the outcome of the update.
	 *  @param GameManager used in this context mainly to interact with the level (collide with terrain etc)  
	 *  @param dt the time passed since the last update.
	 */
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	
	
	/**
	 * Renders the GameObject
	 * 
	 * @param r Renders the object.  
	 */
	public abstract void render(Renderer r);
	
	
	/**
	 * Process a collision between this GameObjects hit-box and another GameObjects hit-box.
	 * <p>
	 *  Collisions are detected by Physics which then calls this method. 
	 *  
	 *  @param other The GameObject that has collided with this GameObject.
	 *  @param otherHitBox The hit-box of the other GameObject that has collided with this Object.
	 */
	public abstract void collision(GameObject other, AABBComponent otherHitBox);
	
	
	
	
	
	
	
	
	/**
	 * GameObject constructor.
	 * <p>
	 * Assigns the GameObjects id, a unique string. 
	 */
	public GameObject(){
		
		id = UUID.randomUUID().toString();
	}
	
	
	
	/**
	 * Updates the GameObjects components. (hit-box, weapons, spells etc)
	 * 
	 * @param gc The GameContainer. Used in this context to respond to keyboard/mouse input. 
	 * @param gm The GameManager. GameManager used in this context mainly to interact with the level (collide with terrain etc)  
	 */
	public void updateComponents(GameContainer gc, GameManager gm, float dt){
		
		for(Component c : components){
			c.update(gc, gm, dt);
		}
	}
	
	
	/**
	 * Renders components that require rendering. 
	 * <p>
	 *  Some components are always rendered e.g the players pistol. 
	 *  Some are rendered only under certain circumstances e.g hit-boxes are only rendered when the DebugPanel is active. 
	 *  Some are not rendered at all. 
	 *  
	 * @param r Renders the component.  
	 */
	public void renderComponents(Renderer r){
		
		for(Component c : components){
			c.render(r);
		}
	}
	
	
	
	
	/**
	 * Adds the given component to the components list. 
	 * <p>
	 * Once a component has been added to the list, it will be updated and rendered each frame.
	 * 
	 * @param c The component to be added to the components list. 
	 */
	public void addComponent(Component c){
		
		if(c.getTag() == null){
			System.out.println("Tried to add component to: " + tag + " without a tag. Component not added." );
			return;
		}
		
		components.add(c);
	}
	
	
	
	
	/**
	 * Removes the first component found with a matching tag.
	 * <p>
	 * If no matching tag is found, nothing is changed. 
	 * 
	 *  @param tag String containing the tag of the desired component to be removed.
	 */
	public void removeComponent(String tag){
		
		for(int i = 0; i < components.size(); i++){
			if(components.get(i).getTag().equals(tag)){
				components.remove(i);
			}
		}
	}
	
	
	
	
	/**
	 * Returns the first component with a tag matching the given tag. 
	 * <p>
	 * If the given tag does not match any components in the list, null is returned.
	 * 
	 *  @param tag String containing the tag of the desired component. 
	 *  @return A component with a tag matching the argument tag. Returns null if there is not component in the list with a matching tag. 
	 */
	public Component findComponent(String tag){
		for(int i = 0; i < components.size(); i++){
			if(components.get(i).getTag().equals(tag)){
				return components.get(i);
			}
		}
		return null;
		
	}
	
	
	
	/**
	 * Increments offX by the argument amount
	 * 
	 *  @param amount the amount offX will be incremented
	 */
	public void incrementOffX(float amount){
		offX += amount;
	}
	
	
	/**
	 * Increments offY by the argument amount
	 * 
	 *  @param amount the amount offY will be incremented
	 */
	public void incrementOffY(float amount){
		offY += amount;
	}
	
	
	/**
	 * Increments posX by the argument amount
	 * 
	 *  @param amount the amount posX will be incremented
	 */
	public void incrementPosX(float amount){
		posX += amount;
	}
	
	
	/**
	 * Increments posY by the argument amount
	 * 
	 *  @param amount the amount posY will be incremented
	 */
	public void incrementPosY(float amount){
		posY += amount;
	}
	
	
	/**
	 * Returns the GameObjects tag.
	 * 
	 * @return A String, the objects tag.
	 */
	public String getTag(){
		return tag;
	}
	
	
	/**
	 * Sets the GameObjects tag
	 * 
	 *  @param The new tag. 
	 */
	public void setTag(String tag){
		this.tag = tag;
	}
	
	
	public float getPosX(){
		return posX;
	}
	
	
	public void setPosX(float posX){
		this.posX = posX;
	}
	
	
	public float getPosY(){
		return posY;
	}
	
	
	public void setPosY(float posY){
		this.posY = posY;
	}
	
	
	public int getWidth(){
		return width;
	}
	
	
	public void setWidth(int width){
		this.width = width;
	}
	
	
	public int getHeight(){
		return height;
	}
	
	
	public void setHeight(int height){
		this.height = height;
	}
	
	
	public boolean isDead(){
		return dead;
	}
	
	
	public void setDead(boolean dead){
		this.dead = dead;
	}
	
	
	public int getLeftRightPadding(){
		return leftRightPadding;
	}
	
	
	public void setLeftRightPadding(int padding){
		this.leftRightPadding = padding;
	}
	
	
	public int getTopPadding(){
		return topPadding;
	}
	
	
	public void setTopPadding(int paddingTop){
		this.topPadding = paddingTop;
	}
	
	
	public int getTileX() {
		return tileX;
	}
	
	
	public int getTileY() {
		return tileY;
	}
	
	
	public float getOffX() {
		return offX;
	}
	
	
	public float getOffY() {
		return offY;
	}
	
	
	public int getDirection() {
		return direction;
	}
	
	
	public float getAnimationState() {
		return animationState;
	}
	
	
	public ObjectType getObjectType() {
		return objectType;
	}
	
	
	public void setTileX(int tileX) {
		this.tileX = tileX;
	}
	
	
	public void setTileY(int tileY) {
		this.tileY = tileY;
	}
	
	
	public String getId() {
		return id;
	}
	
	
	public float getAnimationSpeed() {
		return animationSpeed;
	}
	
	
	public void setOffX(float offX) {
		this.offX = offX;
	}
	
	
	public void setOffY(float offY) {
		this.offY = offY;
	}
	
	
	public void setAnimationSpeed(int animationSpeed) {
		this.animationSpeed = animationSpeed;
	}
	
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	
	/**
	 * @return the frames
	 */
	public int getAnimationFrames() {
		return animationFrames;
	}
	
	
	/**
	 * @param animationState the animationState to set
	 */
	public void setAnimationState(float animationState) {
		this.animationState = animationState;
	}
	
	
	/**
	 * @param animationSpeed the animationSpeed to set
	 */
	public void setAnimationSpeed(float animationSpeed) {
		this.animationSpeed = animationSpeed;
	}

}
