package com.gussssy.gametwo.game.objects;

import java.util.ArrayList;
import java.util.UUID;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.Component;
import com.gussssy.gametwo.game.physics.CollisionType;

public abstract class GameObject{
	
	protected String tag;
	public ObjectType objectType;
	public CollisionType collisionType;
	public String id;
	protected int tileX;
	protected int tileY;
	protected float posX, posY;
	protected float offX, offY;
	protected int width, height;
	protected int leftRightPadding = 0; 
	protected int topPadding = 0;
	public boolean dead = false;
	protected ArrayList<Component> components = new ArrayList<Component>();
	
	// animation variables
	protected int direction = 0;	// this shouldnt really be here, only npcs and player have direction so far
	public float animationState;
	public float animationSpeed = 8;
	public int frames = 4;			// by default animated objects have 4 frames, however this is not always the case.
	
	
	// Abstract Methods
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	public abstract void render(GameContainer gc, Renderer r);
	public abstract void collision(GameObject other, AABBComponent otherHitBox);
	
	
	
	/**
	 * GameObject constructor.
	 * - gives the obnject a unique id String 
	 **/
	public GameObject(){
		
		id = UUID.randomUUID().toString();
	}
	
	
	
	public void updateComponents(GameContainer gc, GameManager gm, float dt){
		
		for(Component c : components){
			c.update(gc, gm, dt);
		}
	}
	
	
	
	public void renderComponents(GameContainer gc, Renderer r){
		
		for(Component c : components){
			c.render(gc, r);
		}
	}
	
	public void addComponent(Component c){
		
		if(c.getTag() == null){
			System.out.println("Tried to add component to: " + tag + " without a tag. Component not added." );
			return;
		}
		
		components.add(c);
	}
	
	public void removeComponent(String tag){
		
		for(int i = 0; i < components.size(); i++){
			if(components.get(i).getTag().equals(tag)){
				components.remove(i);
			}
		}
	}
	
	public Component findComponent(String tag){
		for(int i = 0; i < components.size(); i++){
			if(components.get(i).getTag().equals(tag)){
				return components.get(i);
			}
		}
		return null;
		
	}
	
	
	public void incrementOffX(float amount){
		offX += amount;
	}
	
	public void incrementOffY(float amount){
		offY += amount;
	}
	
	public void incrementPosX(float amount){
		posX += amount;
	}
	public void incrementPosY(float amount){
		posY += amount;
	}
	
	
	public String getTag(){
		return tag;
	}
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

}
