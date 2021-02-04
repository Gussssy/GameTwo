package com.gussssy.gametwo.game.objects.testObjects;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.Projectile;
import com.gussssy.gametwo.game.physics.Physics;

public class CollisionTesterShadow extends Projectile {
	
	int lifeTime = 600;
	//float vx, vy;
	//AABBComponent hitBox; moved to projectile class
	
	public CollisionTesterShadow(float posX, float posY, int width, int height, float vx, float vy){
		
		this.tag = "collision_tester_shadow";
		//this.parent = parent; 
		this.posX = posX;
		this.posY = posY;
		this.tileX = (int)posX/GameManager.TS;
		this.tileY = (int)posY/GameManager.TS;
		
		this.width = width;
		this.height = height;
		
		this.vx = vx;
		this.vy = vy;
		
		// hitbox type 1, no padding but actually moves
		hitBox = new AABBComponent(this, 1);
		addComponent(hitBox);
		
		tileHitBox2.color = 0xff000088;
		tileHitBox2.collidingColor = 0xffffff00;
		
		elasticity = 1.0f;
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		if(lifeTime == 0)dead= true;
		lifeTime--;
		
		posX += vx*dt;
		posY += vy*dt;
		
		tileCollision(gm);
		
		updateComponents(gc, gm, dt);
		
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect((int)posX, (int)posY, width, height, 0x44111188);
		renderComponents(gc, r);
		
		if(tileHitBox1 != null)tileHitBox1.render(gc, r);
		if(tileHitBox2 != null)tileHitBox2.render(gc, r);
		
	}
	
	// the distance the object has penetrated the hitbox
	int topDistance, botDistance, leftDistance, rightDistance;
	// the 'time' taken to acheive this distance if only velocity on the respective axis is considered
	float topTime, botTime, leftTime, rightTime;
	// whether or not a collision on the respective side is possible
	boolean topPossible, botPossible, leftPossible, rightPossible;

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		
		
		if(other.getTag() == "tile"){
			collisionHandler.collisionWithTileHitBox(otherHitBox);
		}
		
		
		// this only works for stationary platforms
		if(other.getTag() == "platform"){
			
			//colliding = true;
			collisionHandler.collisionWithStationary(other, otherHitBox);
			
		}
		
	}
	
	

	@SuppressWarnings("unused")
	@Override
	protected void tileCollision(GameManager gm) {
		
		// Tile Collision
		// 4 corners method

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

		// set the tiles to be checked based on the tile the hitbox corners are within
		topLeft = gm.getLevelTile((hitBox.getStartX())/tileSize, (hitBox.getStartY())/tileSize);
		topRight = gm.getLevelTile((hitBox.getStartX() + width)/tileSize, (hitBox.getStartY())/tileSize);
		bottomLeft = gm.getLevelTile((hitBox.getStartX())/tileSize, (hitBox.getStartY() + height)/tileSize);
		bottomRight = gm.getLevelTile((hitBox.getStartX() + width)/tileSize, (hitBox.getStartY() + height)/tileSize);
		
		/*
		topLeft = gm.getLevelTile((hitBox.getStartX())/tileSize, (hitBox.getStartY())/tileSize);
		topRight = gm.getLevelTile((hitBox.getStartX() + width)/tileSize, (hitBox.getStartY())/tileSize);
		bottomLeft = gm.getLevelTile((hitBox.getStartX())/tileSize, (hitBox.getStartY() + height)/tileSize);
		bottomRight = gm.getLevelTile((hitBox.getStartX() + width)/tileSize, (hitBox.getStartY() + height)/tileSize);*/

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
				System.out.println("TILE COLLISION: blc && brc");
				System.out.println(tileHitBox1.toString());
			}else if(blc){
				
				tileHitBox1.modifyTileAABBComponent(bottomLeft.getTileX(), bottomLeft.getTileX(), bottomLeft.getTileY(), bottomLeft.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				System.out.println("TILE COLLISION: blc");
				System.out.println(tileHitBox1.toString());
			}else if(brc){
				
				tileHitBox1.modifyTileAABBComponent(bottomRight.getTileX(), bottomRight.getTileX(), bottomRight.getTileY(), bottomRight.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				System.out.println("TILE COLLISION: brc");
				System.out.println(tileHitBox1.toString());
			}
			
			// NOTE: this deals with the missing third corner, this will likely lead to double collisions if the new second tile hitbox overlaps the first
			// deal with the missing third corner that can collide in fairly rare but significant circumstances
			// going right?
			/*if(vx > 0){
				// going right and down, the top right edge is never considered so: 
				if(trc & !brc){
					System.out.println("TILE COLLISION: THIRD CORNER: trc");
					tileHitBox2.modifyTileAABBComponent(topRight.getTileX(), topRight.getTileX(), topRight.getTileY(), topRight.getTileY());
					Physics.addAABBComponent(tileHitBox2);
				}
			}else{
				// going down and left, the top left edge hasnt been checked and can collide
				if(tlc & !blc){
					System.out.println("TILE COLLISION: THIRD CORNER: tlc");
					tileHitBox2.modifyTileAABBComponent(topLeft.getTileX(), topLeft.getTileX(), topLeft.getTileY(), topLeft.getTileY());
					Physics.addAABBComponent(tileHitBox2);
					
				}
			}*/

			
		}else{
			// Travelling UP
			if(tlc && trc){
				// if colliding on both top corners
				System.out.println("TILE COLLISION: tlc && trc");
				tileHitBox1.modifyTileAABBComponent(topLeft.getTileX(), topRight.getTileX(), topLeft.getTileY(), topRight.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				System.out.println(tileHitBox1.toString());
				System.out.println(tileHitBox1.toString());
				
			}else if(tlc){
				// just colling on top left
				System.out.println("TILE COLLISION: tlc ");
				tileHitBox1.modifyTileAABBComponent(topLeft.getTileX(), topLeft.getTileX(), topLeft.getTileY(), topLeft.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				System.out.println(tileHitBox1.toString());
				
				
			}else if(trc){
				// just colling on top right
				System.out.println("TILE COLLISION: trc");
				tileHitBox1.modifyTileAABBComponent(topRight.getTileX(), topRight.getTileX(), topRight.getTileY(), topRight.getTileY());
				Physics.addAABBComponent(tileHitBox1);
				System.out.println(tileHitBox1.toString());
				
			}
			
			// NOTE: this deals with the missing third corner, this will likely lead to double collisions if the new second tile hitbox overlaps the first
			// deal with the missing third corner that can collide in fairly rare but significant circumstances
			// going right?
			/*if(vx > 0){
				// going right and down, the top right edge is never considered so: 
				if(brc & !trc){
					System.out.println("TILE COLLISION: THIRD CORNER: brc");
					tileHitBox2.modifyTileAABBComponent(bottomRight.getTileX(), bottomRight.getTileX(), bottomRight.getTileY(), bottomRight.getTileY());
					Physics.addAABBComponent(tileHitBox2);
				}
			}else{
				// going down and left, the top left edge hasnt been checked and can collide
				if(blc & !tlc){
					System.out.println("TILE COLLISION: THIRD CORNER: blc");
					tileHitBox2.modifyTileAABBComponent(bottomLeft.getTileX(), bottomLeft.getTileX(), bottomLeft.getTileY(), bottomLeft.getTileY());
					Physics.addAABBComponent(tileHitBox2);

				}
			}*/

		}
		
		// CASE MISSING: When you hit that third corner that I dont check for - will be very noticable with a large object		
	}

}
