package com.gussssy.gametwo.game.objects.projectile;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.HealthBar;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;

/**
 * LaserBullet is a projectile fired by the Aimed Pistol.  
 **/
public class LaserBullet extends Projectile {
	
	private boolean print = false;

	// Regualr size:
	/**
	// image dimensions
	private int imageWidth = 5;
	private int imageHeight = 5;
	
	// integer array containing pixel colour values foir the laser bullet image
	private int[] pixels = new int[imageWidth * imageHeight];
	
	// contains values used to modify correspnding pixels in the pixel array
	private float[] alphaModifications = {0.1f, 0.3f, 0.8f, 0.3f, 0.1f};
	*/
	
	// larger dimensions
	private int imageWidth = 9;
	private int imageHeight = 9;
	
	// integer array containing pixel colour values foir the laser bullet image
	private int[] pixels = new int[imageWidth * imageHeight];
	
	// contains values used to modify correspnding pixels in the pixel array
	private float[] alphaModifications = {0.1f, 0.2f, 0.3f, 0.5f, 0.8f, 0.5f, 0.3f, 0.2f, 0.1f};
	
	
	
	
	
	
	
	// the laser bullet image
	private Image image;
	
	// how much damage a laser bullet will do to a charachter
	int damage = 10;
	
	/**
	 * The GameObject that fired this bullet
	 * */
	private GameObject source;

	
	// A small red light surrounding the laser bullet
	Light light = new Light(imageHeight, 0xffff0000);
	

	
	
	/**
	 * LaserBullet Constructor
	 * 
	 **/
	public LaserBullet(float posX, float posY, float vx, float vy, GameObject source){
		
		this.posX = posX;
		this.posY = posY;
		this.vx = vx;
		this.vy = vy;
		this.tag = "laserBullet";
		this.source = source;
		
		
		image = constructImage();
		
		width = imageWidth;
		height = imageHeight;
		
		hitBox = new AABBComponent(this,1);
		//System.out.println(hitBox.toString());
		addComponent(hitBox);
		
		
		
	}
	
	
	/**
	 * Constructs the image for the bullet by modidying alpha values of a 5*5 pixel redsquare
	 * - more central pixels retain a higher alpha value
	 * 
	 * @return this bullets image
	 * 
	 * NOTE: this really could be done once statically, rather then each time a new laser bullet is created
	 **/
	private Image constructImage(){
		
		int x, y;
		int alpha = 255;

		for(int i = 0 ; i < pixels.length; i++){
			alpha = 255;

			x = i%imageWidth;
			y = i/imageWidth;

			alpha = (int)((float)alpha * alphaModifications[y] * alphaModifications[x]);

			pixels[i] = (alpha <<24 | 255 << 16 | 0 << 8 | 0 );
			//System.out.println("x: " + x +", y: " + y);

		}
		
		Image image = new Image(pixels, imageWidth, imageHeight);
		image.setAlpha(true);
		return image;
	}

	
	
	
	/**
	 *  TODO: create a temporary tile hitbox
	 *  Create AABBComponent hit  boxes with tiles or groups of tiles when collision has likely occured. 
	 *  	- (when the projectile touches or enters a solid tile)
	 *  	- NOTE: with the Grenade class not all tile AABBComponents hitbvoxes lead to an actual collision when processsed by physcis. 
	 *  Then pass these hitboxes to the Physcis class to detect collisions. 
	 * */
	@Override
	protected void tileCollision(GameManager gm) {
		// TODO Auto-generated method stub

	}

	
	/**
	 * Laser Bullet update.
	 * 	- update to new position 
	 * 	- call updateComponents 
	 **/
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		posX += vx*dt;
		posY += vy*dt;
		
		// set tile position for tile collision checking
		tileX = (int)posX/GameManager.TS;
		tileY = (int)posY/GameManager.TS;
		
		// make sure laser bullet hasnt left the game. ... WAIT this not neccesary because everyuthing outside of the level bounds is solid anyway. 
		/*if(tileX < 0 || tileY < 0 || tileX > GameManager.getGameWidth() || tileY > GameManager.getGameHeight()){
			
		}*/
		
		// check for level collision or out of level bounds (looking for a tile outside of level bounds retuns solid tile / collison)
		if(gm.getLevelTileCollision(tileX, tileY)){
			dead = true;
			System.out.println("Laser Bullet hit tile or OOB");
		}
		
		// tile collision ? 
		// this is likely more expensive then it need to be
		
		/*if(gm.getLevelTileCollision((int)posX%GameManager.TS, (int)posY%GameManager.TS)){
			dead = true;
		}*/
		
		updateComponents(gc,gm, dt);

	}

	
	
	@Override
	public void render(Renderer r) {
		
		r.drawImage(image, (int)posX, (int)posY);
		
		r.drawLight(light, (int)posX, (int)posY);

	}

	
	
	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		//System.out.println("Laser Bullet Collision");
		//System.out.println("\t Hit: " + other.getTag());
		
		
		// Collisions with platforms
		if(other.getTag() == "platform"){
			
			//System.out.println("Processing collision: laser bullet with platform ");
			collisionHandler.collisionWithStationary(other, otherHitBox);
		
		
		// Collisions with tiles / tile hit boxes
		}else if(other.getTag() == "tile"){
			
			// TODO: process a tile collison, need to implement tile collison (above) first....
			
		
			
		// NOTE: Shouldnt this call an apply damage method ehich belongs to damaging projectile interface.. im having to repeat this code elsewhere
		
		// Collisions with Charachters - applying damage
		}else if(other.getObjectType() == ObjectType.NPC || other.getObjectType() == ObjectType.PLAYER ){
			
			
			
			// 1. Prevent Erroneous Collisions / damage
			
			// avoid multiple collisions
			//		- if the bullet is dead, it has already collided this frame.
			if(dead == true){
				// this will happen no big deal
				//System.out.println("!!! Leaserbullet.collision: tried to collidfe with a character twice !!!");
				return;
			}
			
			// Ignore collisions with the GameObject that fired the bullet 
			if(other.id == source.id){
				return;
			}
			
			
			
			
			// 2. Apply damage to the charachter that has been hit.
			
			// NOTE: friendly fire will occur
			
			if(print)System.out.println(other.getTag() + " Has been hit by a laser bullet");
			
			
			// get the health bar of the charchater that was hit
			HealthBar healthBar  = (HealthBar)other.findComponent("hp");
			healthBar.healthChanged(-10);
			
			// the bullet has hit a target, it is dead now
			dead = true;
			
			//SoundManager.sizzle.play();
			
		}
		
	}

}
