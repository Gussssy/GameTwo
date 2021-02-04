package com.gussssy.gametwo.game.objects.projectile;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.audio.SoundClip;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.particles.ExplosionEmitter;
import com.gussssy.gametwo.game.physics.BlastDamage;
import com.gussssy.gametwo.game.physics.Physics;

public class Grenade extends Projectile implements ExplosiveObject{

	// The grenade image, have another option in resources
	private ImageTile grenadeTileImage = new ImageTile("/grenade1.png", 6,6);

	// Beeping sound the grenade makes (with increasing frequency). 
	private SoundClip beep = new SoundClip("/audio/beep1.wav");

	// velocity - declared in projectile 
	//float vx, vy;

	// hit box (storing this here for easy access)
	//AABBComponent hitBox; moved to Projectile Class
	int tileSize = GameManager.TS;

	// WIP new silly hacky object to make temporary hitboxes for tiles
	// MOVED TO PROJECTILE CLASS
	//TileObject tileCollider = new TileObject();

	// explosion emitter
	private ExplosionEmitter explosion = new ExplosionEmitter(0, 0, dead);

	// light 
	Light light = new Light(10, 0xffff0000);


	// timing
	float time = 0;	// I wanted to record time taken to explode
	int beeps = 0;	// The number of beep

	// how many beeps before the grenade explodes
	static int maxBeeps = 30;
	
	public static boolean sound = true;
	private boolean print = false;
	
	float preCollX, preCollY;
	
	// for calculating blast damage
	BlastDamage blastDamage;
	
	private boolean willDie = false; 




	/**
	 * GRENADE CONSTRUCTOR 
	 **/
	public Grenade(float posX, float posY, float vx, float vy, GameManager gm){



		// General Game Object setup 
		this.tag = "grenade";
		this.posX = posX;
		this.posY = posY;
		this.tileX = (int)posX/GameManager.TS;
		this.tileY = (int)posY/GameManager.TS;
		this.width = 6;
		this.height= 6;


		System.out.println("NEW GRENADE: \n\ttileX: " + tileX + "\n\ttileY: " + tileY);

		// add the hitbox so it is updated each frame
		hitBox = new AABBComponent(this, 1);
		addComponent(hitBox);

		// set velocities given 
		this.vx = vx;
		this.vy = vy;

		// give the grenade a slow initial animation speed, it will ramp up over time. 
		animationSpeed = 2;

		// add explosin emiter to game objects so it is updatwe
		gm.addObject(explosion);

		// the hitbox that will be modified when tiles are collided with
		tileHitBox1 = new AABBComponent(tileCollider);
		
		// set how 'bouncy' the grenade is
		elasticity = 0.95f;
		
		blastDamage = new BlastDamage(gm, this, 10);

	}


	// Level Tiles that will be checked by each corner point of the aabb
	// MOVED TO PROJECTILE
	//LevelTile topLeft, topRight, bottomLeft, bottomRight;
	//Boolean tlc, trc, blc, brc;



	/**
	 * GRENADE UPDATE 
	 **/
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		// this is here to give the grenade 1 extra frame of life so blast damage can be rendered
		if(willDie){
			dead = true;
			return;
		}

		if(print)System.out.println("\n\nGrenade Update: New Frame");

		// effect of gravity (this seems to weak and I have no idea why)
		vy += 10 * dt * 15;

		// movement
		posX += vx * dt;
		posY += vy * dt;
		tileX = (int)posX/GameManager.TS;
		tileY = (int)posY/GameManager.TS;
		
		// record position before tile collision
		preCollX = posX;
		preCollY = posY;

		// AABB -> LevelTile collison
		tileCollision(gm);


		// ANIMATION AND BEEPING
		// Increasing the speed of animations and therfore beeping
		animationSpeed += 0.02;
		time += dt;
		DebugPanel.message1 = "Time passed: " + time;
		DebugPanel.message1 = "Beeps: " + beeps;

		// Animation and beeping
		if(animationSpeed * dt < 2)animationState += dt * animationSpeed;
		if(animationState >= 2){
			animationState -=2;
			if(sound)beep.play();
			beeps++;
		}

		// EXPLODE? 
		// Grenade explodes after a certain number of beeps
		if(beeps >= maxBeeps){
			explode();
		}

		// update components (just hit box atm)
		updateComponents(gc, gm, dt);

	}



	/**
	 * GRENADE RENDER 
	 **/
	@Override
	public void render(GameContainer gc, Renderer r) {

		
		
		// render the grenade 
		if(!willDie){
			r.drawImageTile(grenadeTileImage, (int)posX, (int)posY, (int)animationState, 0);

			// render a red light around the grenade when the red bit is showing 
			if(animationState > 1){
				r.drawLight(light, (int)posX +5-3, (int)posY+5-3);
			}

			// show the position of the grenade before modification by tile collisions
			if(GameManager.showDebug)r.drawRect((int)preCollX, (int)preCollY, width, height, 0xffffffff);
			
			//render components
			renderComponents(gc, r);

			// render the temporary tile hitbox

			if(tileHitBox1 != null)tileHitBox1.render(gc,r);
			//System.out.println("rendering tileHitBox1 at: x = " + tileHitBox1.getStartX() + ", y = " + tileHitBox1.getStartY());


			//r.drawFillRect((int)posX, (int)posY, 1, 1, 0xff000000);
			
		}
			
		// draw blast damage debug
		blastDamage.render(r);

	}

	//AABBComponent tileHitBox1;
	//AABBComponent tileHitBox2;
	boolean collidingWithTile = false;

	@SuppressWarnings("unused")
	protected void tileCollision(GameManager gm){

		// Tile Collision
		// 4 corners method
		// as the grenade is 6*6, it can only contact as much as two tiles with any adjacent corner point pairs
		// there cannot possibly be an unchecked tile between two adjacent points, in fact most of the time adjacent points will be within the same tile

		// reset tiles and collisions every frame
		// reset tiles
		topLeft = null;
		topRight = null;
		bottomLeft = null;
		bottomRight = null;
		// reset collisions 
		tlc = false;
		trc = false;
		blc = false;
		brc = false;

		// Determine tiles to check via hitbox - this seems to be slighly inaccurate so trying below...: 
		// set the tiles to be checked based on the tile the hitbox corners are within
		//topLeft = gm.getLevelTile((hitBox.getStartX())/tileSize, (hitBox.getStartY())/tileSize);
		//topRight = gm.getLevelTile((hitBox.getStartX() + width)/tileSize, (hitBox.getStartY())/tileSize);
		//bottomLeft = gm.getLevelTile((hitBox.getStartX())/tileSize, (hitBox.getStartY() + height)/tileSize);
		//bottomRight = gm.getLevelTile((hitBox.getStartX() + width)/tileSize, (hitBox.getStartY() + height)/tileSize);
		
		
		// Determine tiles to check using posX/posY and width and height
		topLeft = gm.getLevelTile((int)posX/tileSize, (int)posY/tileSize);
		topRight = gm.getLevelTile(((int)posX + width) / tileSize, (int)posY / tileSize);
		bottomLeft = gm.getLevelTile((int)posX / tileSize, ((int)posY + height) / tileSize);
		bottomRight = gm.getLevelTile(((int)posX + width) / tileSize, ((int)posY + height) / tileSize);

		// check for tile collision on each corner of the grenade hit box
		if(topLeft != null) {
			//topLeft.checked = true;
			tlc = topLeft.isCollision();
			topLeft.checked = true;
			topLeft.colliding = topLeft.isCollision();
		} //else System.out.println("top left null");

		if(topRight != null) {
			trc = topRight.isCollision();
			topRight.checked = true;
			topRight.colliding = topRight.isCollision();
		}//else System.out.println("top right null");

		if(bottomLeft != null) {
			blc = bottomLeft.isCollision();
			bottomLeft.checked = true;
			bottomLeft.colliding = bottomLeft.isCollision();
		}//else System.out.println("bottom left null");

		if(bottomRight != null) {
			brc = bottomRight.isCollision();
			bottomRight.checked = true;
			bottomRight.colliding = bottomRight.isCollision();
		}//else System.out.println("bottom right null");


		// print out corner tile collision
		//System.out.println("\n Start New Frame");
		//System.out.println("tlc: " + tlc);
		//System.out.println("trc: " + trc);
		//System.out.println("blc: " + blc);
		//System.out.println("brc: " + brc);

		// Print out details of the tiles being checked
		if(false){
			System.out.println("TopLeft: \n" + topLeft.toString() );
			System.out.println("TopRight: \n" + topRight.toString() );
			System.out.println("BottomLeft: \n" + bottomLeft.toString() );
			System.out.println("BottomRight: \n" + bottomRight.toString() );
		}


		//tileCollision();


		// when travelling down ONLY we care about bottom left and bottom right
		// collision with bottom edge
		if(vy > 0){
			// TRAVELLING DOWN
			if(blc && brc){
				
				tileHitBox1.modifyTileAABBComponent( bottomLeft.getTileX(), bottomRight.getTileX(), bottomLeft.getTileY(), bottomLeft.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				//collision(tileCollider, tileHitBox1);
				if(print)System.out.println("TILE COLLISION: blc && brc");
				if(print)System.out.println(tileHitBox1.toString());
				
				// chek for triple collison point
				/*if(tlc && vx < 0 && (topLeft.getTileY() != bottomLeft.getTileX())  && (bottomLeft.getTileX() != bottomRight.getTileX())){
					tileHitBox2.modifyTileAABBComponent( topLeft.getTileX(), bottomLeft.getTileX(), topLeft.getTileY(), bottomLeft.getTileY());
					Physics.addAABBComponent(tileHitBox2);
					System.out.println("TILE COLLISION: Triple point blc && brc && tlc, all unique tiles, travelling left");
					System.out.println(tileHitBox2.toString());
				}*/

				
			}else if(blc){

				// if we are on a tile boundary we need to consider top left
				
				if(tlc && vx < 0){ //(well also should we assure we are going left vx < 0)
					// if tlc also colliding we need to include it
					tileHitBox1.modifyTileAABBComponent(bottomLeft.getTileX(), bottomLeft.getTileX(), topLeft.getTileY(), bottomLeft.getTileY());
					Physics.addAABBComponent(tileHitBox1);
					//collision(tileCollider, tileHitBox1);
					if(print)System.out.println("TILE COLLISION: blc && tlc && vx < 0");
					if(print)System.out.println(tileHitBox1.toString());
					
				}else {
					// no need to worry about tlc, it isnt colliding
					tileHitBox1.modifyTileAABBComponent(bottomLeft.getTileX(), bottomLeft.getTileX(), bottomLeft.getTileY(), bottomLeft.getTileY());
					Physics.addAABBComponent(tileHitBox1);
					//collision(tileCollider, tileHitBox1);
					if(print)System.out.println("TILE COLLISION: blc");
					if(print)System.out.println(tileHitBox1.toString());
					
				}
				
				
			}else if(brc){
				
				// on tile boundary, do we need to worry about trc? , need to consider / assert vx > 0 ?? 
				if(trc && vx > 0){
					// top right is colliding
					tileHitBox1.modifyTileAABBComponent(bottomRight.getTileX(), bottomRight.getTileX(), topRight.getTileY(), bottomRight.getTileY());
					Physics.addAABBComponent(tileHitBox1);
					//collision(tileCollider, tileHitBox1);
					if(print)System.out.println("TILE COLLISION: brc && trc");
					if(print)System.out.println(tileHitBox1.toString());
					
				}else{
					tileHitBox1.modifyTileAABBComponent(bottomRight.getTileX(), bottomRight.getTileX(), bottomRight.getTileY(), bottomRight.getTileY());
					Physics.addAABBComponent(tileHitBox1);
					//collision(tileCollider, tileHitBox1);
					if(print)System.out.println("TILE COLLISION: brc only");
					if(print)System.out.println(tileHitBox1.toString());
					
				}
				

				
			}

			// NOTE: this deals with the missing third corner, this will likely lead to double collisions if the new second tile hitbox overlaps the first
			// deal with the missing third corner that can collide in fairly rare but significant circumstances
			// going right?
			/**if(vx > 0){
				// going right and down, the top right edge is never considered so: 
				if(trc){
					System.out.println("TILE COLLISION: THIRD CORNER: trc");
					tileHitBox2.modifyTileAABBComponent(topRight.getTileX(), topRight.getTileX(), topRight.getTileY(), topRight.getTileY());
					Physics.addAABBComponent(tileHitBox2);
				}
			}else{
				// going down and left, the top left edge hasnt been checked and can collide
				if(tlc){
					System.out.println("TILE COLLISION: THIRD CORNER: tlc");
					tileHitBox2.modifyTileAABBComponent(topLeft.getTileX(), topLeft.getTileX(), topLeft.getTileY(), topLeft.getTileY());
					Physics.addAABBComponent(tileHitBox2);

				}
			}*/


		}else{
			// Travelling UP
			if(tlc && trc){
				
				// Top Edge
				
				if(print)System.out.println("TILE COLLISION: tlc && trc");
				tileHitBox1.modifyTileAABBComponent(topLeft.getTileX(), topRight.getTileX(), topLeft.getTileY(), topRight.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				//collision(tileCollider, tileHitBox1);
				if(print)System.out.println(tileHitBox1.toString());
				
				// triple collison - right hand side
				// need to be certain it is triple, because we CANT have overlapping hitboxes
				if(brc && vx > 0 && (topRight.getTileX() != bottomRight.getTileX())){
					// I DONT THIN KTHIS WORKS IT NEEDS TO GO, SINGLE HITBOXES SEEM TO WORK, MULTIPLE CAUSES THE WORST
					if(print)System.out.println("TILE COLLISION: TRIPLE tlc && trc && brc (vx > 0)");
					tileHitBox2.modifyTileAABBComponent(bottomRight.getTileX(), bottomRight.getTileX(), bottomRight.getTileY(), bottomRight.getTileY());
					Physics.addAABBComponent(tileHitBox2);
					SoundManager.error2.play();
					//collision(tileCollider, tileHitBox2);
				
				}
				
				
				/*System.out.println("TILE COLLISION: tlc && trc");
				tileHitBox1.modifyTileAABBComponent(topLeft.getTileX(), topRight.getTileX(), topLeft.getTileY(), topRight.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				System.out.println(tileHitBox1.toString());
				System.out.println(tileHitBox1.toString());*/

			}else if(tlc){
				
				
				// just a tile? 
				if(print)System.out.println("TILE COLLISION: tlc ");
				tileHitBox1.modifyTileAABBComponent(topLeft.getTileX(), topLeft.getTileX(), topLeft.getTileY(), topLeft.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				//collision(tileCollider, tileHitBox1);
				if(print)System.out.println(tileHitBox1.toString());
				
				// or the whole edge? 
				// is adjacent bottom left a unique tile && colliding ? if so then we need to check both tiles by expanding the hitbox to cover both
				if(blc && bottomLeft.getTileY() != topLeft.getTileY()){
					tileHitBox1.modifyTileAABBComponent(topLeft.getTileX(), bottomLeft.getTileX(), topLeft.getTileY(), bottomLeft.getTileY());
				}


			}else if(trc){
				// just colling on top right,
				if(print)System.out.println("TILE COLLISION: trc");
				tileHitBox1.modifyTileAABBComponent(topRight.getTileX(), topRight.getTileX(), topRight.getTileY(), topRight.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				//collision(tileCollider, tileHitBox1);
				if(print)System.out.println(tileHitBox1.toString());
				
				
				// or is there collision along two tiles on the right edge
				// is the horizontally adjacent bottom right occupying a unique tile and colliding? 
				// TODO: 

			}

			// NOTE: this deals with the missing third corner, this will likely lead to double collisions if the new second tile hitbox overlaps the first
			// deal with the missing third corner that can collide in fairly rare but significant circumstances
			// going right?
			/*if(vx > 0){
				// going right and down, the top right edge is never considered so: 
				if(brc){
					System.out.println("TILE COLLISION: THIRD CORNER: brc");
					tileHitBox2.modifyTileAABBComponent(bottomRight.getTileX(), bottomRight.getTileX(), bottomRight.getTileY(), bottomRight.getTileY());
					Physics.addAABBComponent(tileHitBox2);
				}
			}else{
				// going down and left, the top left edge hasnt been checked and can collide
				if(blc){
					System.out.println("TILE COLLISION: THIRD CORNER: blc");
					tileHitBox2.modifyTileAABBComponent(bottomLeft.getTileX(), bottomLeft.getTileX(), bottomLeft.getTileY(), bottomLeft.getTileY());
					Physics.addAABBComponent(tileHitBox2);

				}
			}*/

		}

		// CASE MISSING: When you hit that third corner that I dont check for - will be very noticable with a large object		

	}

	// the distance the object has penetrated the hitbox
	int topDistance, botDistance, leftDistance, rightDistance;
	// the 'time' taken to acheive this distance if only velocity on the respective axis is considered
	float topTime, botTime, leftTime, rightTime;
	// whether or not a collision on the respective side is possible
	boolean topPossible, botPossible, leftPossible, rightPossible;


	/**
	 * GRENADE COLLISION 
	 * - if the grenade collides with another gameobject it will explode 
	 **/
	@Override
	public void collision(GameObject other, AABBComponent otherHitBox){

		topPossible = false;
		botPossible = false;
		leftPossible = false;
		rightPossible = false;

		topTime = 9999;
		botTime = 9999;
		leftTime = 9999;
		rightTime = 9999;

		// this only works for stationary platforms
		if(other.getTag() == "tile"){

			collisionHandler.collisionWithTileHitBox(otherHitBox);
			//if(sound)SoundManager.doing1.play(); // this causes crashes

			if(true)return;

			


		} else if(other.getTag() == "platform"){

			collisionHandler.collisionWithStationary(other, otherHitBox);
			if(sound)SoundManager.doing1.play();

		} else {
			
			if(other.getTag() == "grenade")return;
			
			// collision with any other aabb
			
			//explode();
			
			if(other.getTag() == "laserBullet" || other.getTag() == "player"){
				explode();
			}
			
		}





	}

	@Override
	public void explode() {
		// avoid multi explosions.. even though they are cool
		if(dead == true || willDie == true)return;

		//dead = true;
		// grenade will die a in one frame. it will live for one more frame so blast damage can be rendered for debugging
		willDie = true;
		explosion.setPosX(this.posX);
		explosion.setPosY(this.posY);
		explosion.emit();
		explosion.setWillDie(true);
		
		blastDamage.applyBlastDamage();
		System.out.println("GRENADE WILL EXPLODE");

	}

	public void setPosition(){

		// If player is more than half into neighbouring below tile....
		if(offY > GameManager.TS / 2){
			tileY++; //increment to the new tile
			offY -= GameManager.TS;
		}

		// If player is more than half into neighbouring above tile....
		if(offY < -GameManager.TS / 2){
			tileY--; //decrement to the new tile
			offY += GameManager.TS;
		}

		// If player is more than half into neighbouring right tile....
		if(offX > GameManager.TS / 2){
			tileX++; //increment to the new tile
			offX -= GameManager.TS;
		}

		// If the player is more than half into neighbouring left tile...
		if(offX < -GameManager.TS / 2){
			tileX--; //decrement to the new tile
			offX += GameManager.TS;
		}

		// Update player to new position
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
	}


	public static void setMaxBeeps(int maxBeeps) {
		Grenade.maxBeeps = maxBeeps;
	}

}
