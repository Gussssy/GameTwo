package com.gussssy.gametwo.game.objects.projectile;

import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.level.LevelTile;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.testObjects.TileObject;
import com.gussssy.gametwo.game.physics.ProjectileCollision;

public abstract class Projectile extends GameObject {
	
	protected ProjectileCollision collisionHandler = new ProjectileCollision(this);
	protected AABBComponent hitBox;
	
	
	protected TileObject tileCollider = new TileObject();
	protected AABBComponent tileHitBox1 = new AABBComponent(tileCollider);
	protected AABBComponent tileHitBox2 = new AABBComponent(tileCollider);
	
	protected float vx, vy;
	
	// previous posX and previous posy - testing if this help collision detection
	protected float prevPosx, prePosY;
	
	protected float elasticity = 0.8f;
	
	
	// Tile Collision Variables
	// Level Tiles that will be checked by each corner point of the aabb
	protected LevelTile topLeft, topRight, bottomLeft, bottomRight;
	protected Boolean tlc, trc, blc, brc;
	protected int tileSize = GameManager.TS;
	
	
	
	protected abstract void tileCollision(GameManager gm);

	public void invertVX(){
		vx = -vx;
	}
	
	public void invertVY(){
		vy = -vy;
	}
	
	public float getVx() {
		return vx;
	}

	public void setVx(float vx) {
		this.vx = vx;
	}

	public float getVy() {
		return vy;
	}

	public void setVy(float vy) {
		this.vy = vy;
	}

	public float getElasticity() {
		return elasticity;
	}

	public void setElasticity(float elasticity) {
		this.elasticity = elasticity;
	}

	public TileObject getTileCollider() {
		return tileCollider;
	}
	
	

}
