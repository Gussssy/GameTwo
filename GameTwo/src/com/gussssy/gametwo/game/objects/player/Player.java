package com.gussssy.gametwo.game.objects.player;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.Textures;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.Component;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.Charachter;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;
import com.gussssy.gametwo.game.spell.IceShardSpell;
import com.gussssy.gametwo.game.weapon.GrenadeThrowPlayer;
import com.gussssy.gametwo.game.weapon.AimedPistol;
import com.gussssy.gametwo.game.weapon.Pistol;

public class Player extends Charachter{

	// Location variables
	//private int tileX, tileY;	// in GameObject
	//private float offX, offY;	// in GameObject

	// Movement variables ALL MOVED TO CHARACHTRER
	//private float speed = 100;		moved to charachter
	//private float fallDistance = 0;		
	//private float gravity = 10;
	//private float jump = -4;
	//private float jump = -6;

	// platform collision variables
	//boolean onGround = false;			// moved to chartachter
	//boolean onAABB = false;
	//AABBComponent onThisObject = null;

	// This is player specific
	public boolean hasBoost = false;
	public float boostSpeed = -2;
	//private float boostSpeed = -10;
	
	// Player Movement for normal, water and free movement
	PlayerMovementBasic playerMovement;
	PlayerMovementWater waterMovement;
	PlayerFreeMovement freeMovement;
	
	public boolean freeMovementEnabled = false;


	// Animation variables
	private ImageTile playerImage = Textures.playerTile;

	
	// weapons
	private GrenadeThrowPlayer grenade = new GrenadeThrowPlayer(this);
	private AimedPistol aimedPistol = new AimedPistol(this);
	private Pistol pistol = new Pistol(this);
	
	// Spell
	//private Spell1 spell = new Spell1(this);
	//private Spell2 spell = new Spell2(this);
	//private Spell3 spell = new Spell3(this);
	private IceShardSpell spell = new IceShardSpell(this);
	
	
	// LIGHT
	Light playerLight = new Light(100, -1);

	// Debug variables
	int counter = 0;
	int framesPerPrint = 180;
	


	/**
	 * Player Constructor 
	 **/
	public Player(int tileX, int tileY){

		this.tag = "player";	

		// set players location
		this.tileX = tileX;
		this.tileY = tileY;
		this.offX = 0;
		this.offY = 0;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;

		// set player padding
		leftRightPadding = 4;
		topPadding = 2;
		this.width = GameManager.TS - (leftRightPadding *2);
		this.height = GameManager.TS - topPadding;

		// set AABB for collision detection
		hitBox = new AABBComponent(this);
		hitBox.particleCollisionDetectionEnabled = true;
		this.addComponent(hitBox);				// set players AABB 

		
		// set object type 
		this.objectType = ObjectType.PLAYER;
		
		// setting up player movement
		fallDistance = 0;
		gravity = 10;
		jump = -4;
		playerMovement = new PlayerMovementBasic(this);
		waterMovement = new PlayerMovementWater(this);
		freeMovement = new PlayerFreeMovement(this);
		
		// set weapon
		
		
		//equipWeapon(pistol);
		equipWeapon(aimedPistol);
		//equipWeapon(grenade);
		
		
		// add spell
		addComponent(spell);
		
		


	}




	@Override
	public void update(GameContainer gc, GameManager gm, float dt){
		
		DebugPanel.message1 = "Player tileX: " +  tileX;

		if(freeMovementEnabled){
			
			freeMovement.movementUpdate(gc, gm, dt);
			
		} else if(gm.getLevelTile(tileX, tileY).type == -3){
			waterMovement.movementUpdate(gc, gm, dt);
		}else {
			playerMovement.movementUpdate(gc, gm, dt);
		}
		
		
		// Update Components - AABB and HealthBar (coming soon)
		this.updateComponents(gc, gm, dt);
		
		
		// Temporary / Testing  Weapon toggling
		if(gc.getInput().isKeyDown(KeyEvent.VK_V)){
			toggleWeapon();
		}
		
		
		/**
		// Ground Friction
		// calculate velocity/momentum
		// if onGround, then cancel momentum at 500 kgm/s

		// dont apply friction if user is trying to move, is in the air, or has no momentum/velocity
		if(onGround && px != 0 && !gc.getInput().isKey(KeyEvent.VK_D) && !gc.getInput().isKey(KeyEvent.VK_A) ){
			// player on ground and with momentum, friction with ground will cancel momentum at rate of 500/s or

			//GameManager.debugMessage8 = "Applying friction";
			//System.out.println("Applying friction");
			//System.out.println("px: " + px);


			// is the x momentum '+' or '-'
			if(px > 0){
				// positive x momentum, momentum right

				px -= maxPx*dt* acceleration; // cold get frinction from the tile standing on... 
				if(px < 0)px = 0;

			}else{
				// negative x momentum
				px += maxPx*dt* acceleration;
				if(px > 0)px = 0;
			}
		} else {
			//System.out.println("No friction applied");
			//GameManager.debugMessage8 = "Not applying friction";
		}

		// now thhat we have sussed friction, calculate the resultant x velocity
		// vx = px/mass
		vx = px/mass;

		//GameManager.debugMessage6 = "Player offX: " + offX;
		//////////////////////////////////////////////////////////////////////////////////////////
		//								Move Left and Right										//

		// Respond to left and right movement input
		// pressing A and D doesnt not immediatly lead to left and right movement, instead it chnages the players velocity/momentum


		// Left Input
		if(gc.getInput().isKey(KeyEvent.VK_A)){
			// 'A' key is currently down

			// calculate resultant velocity from input 
			if(px > -maxPx){

				// apply left momentum
				px -= maxPx *dt * acceleration;

				// make sure momentum hasn't exceeded max self induced momentum 
				if(px < -maxPx)px = -maxPx;
			}

			//GameManager.debugMessage1 = "Player px = " + px;
			//GameManager.debugMessage2 = "Player vx = " + vx;

			//apply new momentum to velocity
			vx = px/mass;
		}

		// Right Input
		if(gc.getInput().isKey(KeyEvent.VK_D)){

			// 'D' key is currently down

			// calculate resultant velocity from input
			if(px < maxPx){

				px += maxPx *dt * acceleration;

				// make sure momentum hasn't exceeded max self induced momentum 
				if(px > maxPx) px = maxPx;
			}

			//GameManager.debugMessage1 = "Player px = " + px;
			//GameManager.debugMessage2 = "Player vx = " + vx;

			//apply new momentum to velocity
			vx = px/mass;
		}




		// MOVE LEFT / LEFT COLLISIONS
		if(vx < 0){

			// vx is negative, player will move left unless colliding with a tile

			// Is there a solid tile to the left?
			if(gm.getLevelTileCollision(tileX - 1 , tileY) || gm.getLevelTileCollision(tileX - 1 , tileY + (int)Math.signum((int)offY)) ){

				// there is a solid tile to the left but the player may not yet be touching it

				offX += dt * vx;
				//offX -= dt * speed;

				if(offX < -leftRightPadding) {
					// player has hit the the tile to the left and cannot move any further left
					offX = -leftRightPadding;
					// cancel all x momentum
					px = 0;

				}

			} else {

				// there is nothing solid to the left, player can move freely
				offX += dt * vx;

				//offX -= dt * speed;
			}
		}



		// MOVE RIGHT
		if(vx > 0){

			// vx is positve, player will move right unless colliding with a tile


			// Is there a solid tile to the right?
			if(gm.getLevelTileCollision(tileX + 1 , tileY)|| gm.getLevelTileCollision(tileX + 1 , tileY + (int)Math.signum((int)offY))){

				// there is a solid tile to the right but the player may not yet be touching it
				offX += dt * vx; 
				//offX += dt * speed; 


				if(offX > leftRightPadding){ 
					// player has hit the the tile to the right and cannot move any further right
					offX = leftRightPadding;
					// cancel all x momentum
					px = 0;
				}


			}else{
				// there is nothing solid to the right, player can move freely
				offX += dt * vx;
				//offX += dt * speed;
			}
		}

		//GameManager.debugMessage6 = "Player offX: " + offX;

		// LEFT RIGHT MOVING ANIMATION

		// Animating Left and Right Movement
		if(gc.getInput().isKey(KeyEvent.VK_A)){
			// left walking animation 
			direction = 1;
			animationState += dt * animationSpeed;
			if(animationState > 4)animationState = 0;
		} else if(gc.getInput().isKey(KeyEvent.VK_D)){
			// right walking animation
			direction = 0;
			animationState += dt * animationSpeed;
			if(animationState > 4)animationState = 0;
		} else {
			// stationary 
			animationState = 0;
		}


		//								End of Move Left and Right								//
		//////////////////////////////////////////////////////////////////////////////////////////





		//////////////////////////////////////////////////////////////////////////////////////////
		// 									Jump and Gravity									//

		if(counter > framesPerPrint && debugGravity){

			System.out.println("\n DEBUG GROUND COLLISION\noffX: " + offX + "\noffY: " + offY + "\npadding: " + leftRightPadding);
			System.out.println("Player is on TileX: " + tileX + ", TileY: " + tileY);
			int inputToSignum = Math.abs((int)offX) > leftRightPadding ? (int)offX : 0;
			System.out.println("input to signum: " + inputToSignum);
			int signumResult = (int)Math.signum(inputToSignum);
			System.out.println("Signum result: " + signumResult);
			System.out.println("Check1: Checking collision tile directly below with tileX: " + tileX + ", and tileY: " + (tileY + 1));
			System.out.println("Check2: Also Checking tileX: " + (tileX + signumResult) + ", and tileY: " + (tileY + 1));
		}


		// Check if the player is standing on an AABB of a platform
		if(onAABB){

			// Last frame the player was on the platform but is that still the case? 
			if(posX > (onThisObject.getParent().getPosX() - width - leftRightPadding) && posX < onThisObject.getParent().getPosX() + onThisObject.getParent().getWidth() - leftRightPadding){

				// Player still within the x range of the platforms surface
				onGround = true;
				hasBoost = false;

				// BUT is the player's y location still on top of the platform?
				if((onThisObject.getParent().getPosY() - posY) > height + topPadding + 1){

					// Player is no longer ontop of the platform as it has moved down faster then the player falls
					onGround = false;
					onAABB = false;
					onThisObject = null;
				} 

				// Player is still on the platform. Nothing needs to be changed. 

			} else {

				// Player is not within x range to be on the platform
				onAABB = false;
				onThisObject = null;
				onGround = false;
			}			
		}




		// CALCULATE EFFECT OF GRAVITY

		// fallDistane is increased by current fallSpeed * time passed since last update
		fallDistance += dt * gravity;




		// JUMPING

		// Double jump / boost 
		if(gc.getInput().isKeyDown(KeyEvent.VK_W) && hasBoost){
			fallDistance += boostSpeed;
			hasBoost = false;
			SoundManager.jetpack.play();
		}


		// Jump only if on the ground
		if(gc.getInput().isKeyDown(KeyEvent.VK_W) && onGround){
			fallDistance = jump;
			hasBoost = true;
			onGround = false;
			onAABB = false;
			SoundManager.jump.play();
			if(Debug.debugInputLoss)System.out.println(" PLAYER JUMP ");
		}


		// APPLY FALLING
		offY += fallDistance;



		// COLLISIONS ABOVE

		// Check for collisions above only if the player isnt falling down currently (negative value for falldistance)
		//		- positive values for falldistance mean the player is moving up therefore we need to check if there is something above
		if(fallDistance < 0){

			// if the absolute value off offX is greater then the padding, then get the signum of offX else get the signum of 0
			if((gm.getLevelTileCollision(tileX, tileY - 1) || gm.getLevelTileCollision(tileX + (int)Math.signum((int)Math.abs(offX) > leftRightPadding ? offX : 0), tileY - 1)) && offY < -topPadding ){
				// Collision with above tile detected
				fallDistance = 0;
				offY = -topPadding;	
			}
		}


		// COLLISIONS BELOW

		// Check for collision below if the player is falling (positve value for falldistance)
		// Checks tile directly below and adjacent below tile if player is between two tiles
		if(fallDistance > 0){
			if((gm.getLevelTileCollision(tileX, tileY + 1) || gm.getLevelTileCollision(tileX + (int)Math.signum((int)Math.abs(offX) > leftRightPadding ? offX : 0), tileY + 1)) && offY >= 0 ){

				// Collision with ground detected

				fallDistance = 0;	// dont fall this frame
				offY = 0;			// set the player on top of this tile
				onGround = true;	// player is on the ground
				hasBoost = false;	//player cant use boost when on ground, only when airbourne
				//jumping = false;	
			} else {
				onGround = false;
			}
		}

		// animating jump / falling
		if(fallDistance > 1){
			animationState = 1; 
		}


		//								End of Jump and Gravity									//
		//////////////////////////////////////////////////////////////////////////////////////////
		
		 
		setPosition();



		// debug info printing to console
		if(counter > framesPerPrint && debugPlayerMovement){
			System.out.println("\n DEBUG PLAYER MOVEMENT");
			System.out.println("onGround: " + onGround);
			//System.out.println("collidingWithAABB: " + collidingWithAABB);
			System.out.println("fallDistance: " + fallDistance);
			System.out.println("************************************\n\n");
		}

		//Debug counter control
		if(counter > framesPerPrint){
			counter = 0;
		}
		counter++;
		*/
		
		

		// Update Components - AABB and HealthBar (coming soon)
		//this.updateComponents(gc, gm, dt);
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



	int[] incursions = new int[4];
	float[] mins = new float[4];
	int smallestIndex;
	int botIncursion, topIncursion, leftIncursion, rightIncursion;
	float botMinimisation, topMinimisation, leftMinimisation, rightMinimisation;


	@Override
	public void collision(GameObject other, AABBComponent otherHitBox){


		if(other.getTag().equals("platform")){
			AABBComponent thisAABB = (AABBComponent) this.findComponent("aabb");
			AABBComponent otherAABB = (AABBComponent) other.findComponent("aabb");

			// 1. DETERMINE HOW MUCH THE PLAYER 'INCURRED' INTO THE OTHER HIT BOX

			// how much the object has penetrated top, bottom, left and right into the other objects hitbox 
			topIncursion = thisAABB.getStopY() - otherAABB.getStartY();
			botIncursion = otherAABB.getStopY() - thisAABB.getStartY();
			leftIncursion = thisAABB.getStopX() - otherAABB.getStartX();
			rightIncursion = otherAABB.getStopX() -  thisAABB.getStartX();
			
			System.out.println("\n\n\n PLATFORM COLLISION");
			System.out.println("Top Incursion: " + topIncursion);
			System.out.println("Bot Incursion: " + botIncursion);
			System.out.println("Left Incursion: " + leftIncursion);
			System.out.println("Right Incursion: " + rightIncursion);

			// The order matters
			// When left/right incursion ==  top/bottom incursion, the collision will be considered a left/right collision
			//  - because left and right is
			incursions[2] = topIncursion;
			incursions[3] = botIncursion;
			incursions[0] = leftIncursion;
			incursions[1] = rightIncursion;


			// 2. DETERMINE WHICH INCURSION IS THE SMALLEST

			/*
			smallestIndex = 0;
			for(int i = 1; i < incursions.length; i++){

				if(incursions[i] < incursions[smallestIndex]){
					smallestIndex = i;
				}
			}
			
			*/


			// 3. MOVE THE PLAYER THE SMALLEST POSSIBLE AMOUNT SO IT IS NOT COLLIDING ANYMORE
			// DOESNT WORK - will remove soon
			
			/*
			
			if(smallestIndex == 2){
				// Top Collision
				offY -= topIncursion;
				fallDistance = 0;
				onAABB = true;
				//onGround = true;
				playerOnThisPlatform = otherAABB;
				setPosition();

			} else if(smallestIndex == 3){
				// Top Collision
				offY += botIncursion;
				fallDistance = 0;
				setPosition();

			} else if(smallestIndex == 0){
				// Left Collision
				offX -= leftIncursion;
				setPosition();

			} else {
				// Right Collision
				offX += rightIncursion;
				setPosition();
			}
			
			*/




			// IMPLEMENTING FIXED COLLIUSION DETECTION: 

			// get x and y difference between thisframe and last frame
			int dx = thisAABB.getCenterX() - thisAABB.getLastCenterX(); 
			int dy = thisAABB.getCenterY() - thisAABB.getLastCenterY(); 
			
			System.out.println("dx: " + dx);
			System.out.println("dy: " + dy);

			// MINIMISATION
			// if the object is moving fast, using smallest incursion produces incorrect collisions
			// So now it takes into consideration the speed and direction of the object to determine collision
			// by 'moving' the object back along the reverse direction to the point where the collision actually occured
			// What side of the platform was hit first? - the smallest value = the side that was hit first

			// Top and Bot Minimisation
			if(dy != 0){
				// Minimise top and bot
				if(dy < 0){
					// Minimize bot - object is moving up as dy is neg
					botMinimisation = Math.abs((float)botIncursion / (float)dy);
					//botMinimisation = (float)botIncursion / (float)dy;
					topMinimisation = 1000;
					//System.out.println("botMinimisation: " + botMinimisation);

				}else{
					// Minimize top
					//topMinimisation = Math.abs((float)topIncursion / (float)dy);
					topMinimisation = (float)topIncursion / (float)dy;
					botMinimisation = 1000;
					//System.out.println("topMinimisation: " + topMinimisation);

				}
			}else{
				// there is no movement on the y axis, only left and right collisions are possible
				topMinimisation = 1000;
				botMinimisation = 1000;
			}

			// Left and Right Minimisation
			if(dx != 0){
				// Minimise top and bot
				if(dx < 0){
					// Minimize right - object is moving left as dx is negative
					rightMinimisation = Math.abs((float)rightIncursion / (float)dx);
					//rightMinimisation = (float)rightIncursion / (float)dx;
					leftMinimisation = 1000;
					//System.out.println("rightMinimisation: " + rightMinimisation);

				}else{
					// Minimize left - object is moving right as dx is positive
					leftMinimisation = Math.abs((float)leftIncursion / (float)dx);
					//leftMinimisation = (float)leftIncursion / (float)dx;
					rightMinimisation = 1000;
					//System.out.println("leftMinimisation: " + leftMinimisation);

				}
			}else{
				// there is no movement on the x axis, only top and bot collisions are possible
				leftMinimisation = 1000;
				rightMinimisation = 1000;
			}
			
			mins[0] = topMinimisation;
			mins[1] = botMinimisation;
			mins[2] = leftMinimisation;
			mins[3] = rightMinimisation;
			
			
			// DETERMINE SMALLEST MINIMISATION
			smallestIndex = 0;
			for(int i = 1; i < mins.length; i++){

				if(mins[i] < mins[smallestIndex]){
					smallestIndex = i;
				}
			}

			System.out.println("topMinimisation: " + topMinimisation);
			System.out.println("botMinimisation: " + botMinimisation);
			System.out.println("leftMinimisation: " + leftMinimisation);
			System.out.println("rightMinimisation: " + rightMinimisation);
				
			switch(smallestIndex){
			case 0:
				// top collision
				System.out.println("Top Collision");
				offY -= topIncursion;
				fallDistance = 0;
				py = 0;
				onAABB = true;
				//onGround = true;
				onThisObject = otherAABB;
				setPosition();
				break;
			case 1:
				// bottom collision
				System.out.println("Bottom Collision");
				offY += botIncursion;
				fallDistance = 0;
				py = 0;
				setPosition();
				break;
			case 2:
				// left collision
				System.out.println("Left Collision");
				offX -= leftIncursion;
				px = 0;
				setPosition();

				break;
			case 3:
				// right collision
				System.out.println("Right Collision");
				offX += rightIncursion;
				px = 0;
				setPosition();
				break;
			}













		}





		/**
		// Collision with platform
		if(other.getTag().equals("platform")){
			AABBComponent playerAABB = (AABBComponent) this.findComponent("aabb");
			AABBComponent otherAABB = (AABBComponent) other.findComponent("aabb");

			// how is was before
			// looked for what difference was bigger on the collision, but this only works for perfect squares
			//if(Math.abs(playerAABB.getCenterX() - otherAABB.getCenterX()) < Math.abs(playerAABB.getCenterY() - otherAABB.getCenterY())){


			// now we look at the last frame AABB positions of platform and player
			// How does using last frame change shit...
			// this just changes if we process a side or top/bot collision 
			if(Math.abs(playerAABB.getLastCenterX() - otherAABB.getLastCenterX()) < playerAABB.getHalfWidth() + otherAABB.getHalfWidth()){

				// Difference cX's is less than differences in cY's so colliding with top or bottom
				// TOP OR BOTTOM COLLISION
				//System.out.println("TOP OR BOTTOM COLLISION");


				// TOP COLLISION
				if(playerAABB.getCenterY() < otherAABB.getCenterY()){
					int distance = (playerAABB.getHalfHeight() + otherAABB.getHalfHeight()) - (otherAABB.getCenterY() - playerAABB.getCenterY());
					offY -= distance;
					fallDistance = 0;

					// set new position for player and reset the player AABB to match new positon 
					setPosition();
					onAABB = true;
					playerOnThisPlatform = otherAABB;
					//playerAABB.updateAfterCollision();
				}

				// BOTTOM COLLISION
				if(playerAABB.getCenterY() > otherAABB.getCenterY()){

					// flipped subtraction order when below platform, player cY is greater then platform cY
					int distance = (playerAABB.getHalfHeight() + otherAABB.getHalfHeight()) - (playerAABB.getCenterY() - otherAABB.getCenterY());
					offY += distance;
					// need to set fall distance to 0 or player will momentarily hover until fall distance peeks and becomes positive (positive = falling)
					fallDistance = 0;

					// set new position and update the players AABB
					setPosition();
					//playerAABB.updateAfterCollision();
				}
			}else{
				// SIDE COLLISION 
				//System.out.println("SIDE COLLISION");

				// LEFT COLLISION
				if(playerAABB.getCenterX() < otherAABB.getCenterX()){
					int distance = (playerAABB.getHalfWidth() + otherAABB.getHalfWidth()) - (otherAABB.getCenterX() - playerAABB.getCenterX());
					//System.out.println("Left Collision");
					offX -= distance;

					// set new position for player and reset the player AABB to match new positon 
					setPosition();
					//playerAABB.updateAfterCollision();
				}

				// RIGHT COLLISION
				if(playerAABB.getCenterX() > otherAABB.getCenterX()){

					//System.out.println("Right Collision");

					int distance = (playerAABB.getHalfWidth() + otherAABB.getHalfWidth()) - (playerAABB.getCenterX() - otherAABB.getCenterX());
					offX += distance;

					// set new position and update the players AABB
					setPosition();
					//playerAABB.updateAfterCollision();
				}	
			}
		}
		 */
	}

	

	@Override
	public void render(GameContainer gc, Renderer r){

		r.drawImageTile(playerImage, (int)posX, (int)posY, (int)animationState, direction);
		r.drawLight(playerLight, (int)posX, (int)posY);

		this.renderComponents(gc, r);

	}
	
	public void setPlayerLocation(int tileX, int tileY){
		this.tileX = tileX;
		this.tileY = tileY;
		
	}
	
	public void setPlayerLocation( float posX, float posY){
		this.posX = posX ;
		this.posY = posY;
		
		this.tileX = (int)posX/16;
		this.tileY = (int)posY/16;
		
		offX = posX%16;
		offY = posY%16;
		
		
	}
	
	
	private void toggleWeapon(){
		
		for(Component c : components){
			System.out.println(c.getTag());
		}
		
		if(weapon.getTag() == "pistol" ){
			equipWeapon(grenade);
			
		}else if(weapon.getTag() == "grenade"){
			equipWeapon(aimedPistol);
			
		}else if(weapon.getTag() == "laserGun"){
			equipWeapon(pistol);
			
		}
		
	}
	
	

	public boolean isOnGround() {
		return onGround;
	}

	public float getFallDistance() {
		return fallDistance;
	}

	public float getOffX() {
		return offX;
	}

	public float getOffY() {
		return offY;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}


}
