package com.gussssy.gametwo.game.objects.projectile;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.weapon.Weapon;

public class Bullet extends GameObject{
	
	private float speed = 500;
	private int direction;
	private int damage = 10;
	
	private float offX, offY;
	private int tileX, tileY;
	
	private Image bulletImage;
	
	private Weapon weapon;
	
	public Bullet(Weapon weapon, int tileX, int tileY, float offX, float offY, int direction){
		
		this.weapon = weapon;
		bulletImage = new Image("/laserBullet.png");
		bulletImage.setAlpha(true);
		this.tag = "bullet";
		this.tileX = tileX;
		this.tileY = tileY;
		this.offX = offX;
		this.offY = offY;
		this.direction = direction;
		this.posX = tileX * GameManager.TS + offX;
		this.posY = tileY * GameManager.TS + offY;
		//this.width = 4;
		//this.height = 4;
		this.width = bulletImage.getWidth();
		this.height = bulletImage.getHeight();
		this.addComponent(new AABBComponent(this));
		
		
		
		// Sound now controlled by the weapn class that fires the bullet
		//SoundManager.bulletFire.play();
		
		//System.out.println("\nNew Bullet" + "\nposX: " + posX + "\nposY: " + posY + "\ntileX: " + tileX + "\ntileY: " + tileY + "\noffX: " + offX + "\noffY: " +offY );
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt){
		
		/*switch(direction){
		case 0: offY -= speed * dt; break;
		case 1: offY += speed * dt; break;
		case 2: offX -= speed * dt; break;
		case 3: offX += speed * dt; break;
		}*/
		
		switch(direction){
		case 0: 
			// fire to right
			offX += speed * dt;
			break;
		case 1: 
			// fire to left
			offX -= speed * dt;
			break;
		}
		
		
		
		// TILE TRACKING
		
		// If object has moved entirely into lower tile
		if(offY > GameManager.TS){
			tileY++; //increment to the new tile
			offY -= GameManager.TS;
			//System.out.println("Object moved to lower tile, tileY: " + tileY);
		}

		// If object has moved entirely into above tile
		if(offY < 0){
			tileY--; //decrement to the new tile
			offY += GameManager.TS;
			//System.out.println("Object moved to upper tile, tileY: " + tileY);
		}

		// If object is now entirely into right tile
		if(offX > GameManager.TS){
			tileX++; //increment to the new tile
			offX -= GameManager.TS;
			//System.out.println("Object moved to next right tile, tileX: " + tileX);
		}

		// If the object is now entirely in left tile
		if(offX < 0){
			tileX--; //decrement to the new tile
			offX += GameManager.TS;
			//System.out.println("Object moved to next left tile, tileX: " + tileX);
		}
		
		if(gm.getLevelTileCollision(tileX, tileY)){
			this.dead = true;
			//SoundManager.sizzle.play();
		}
		
		
		// Update player to new position
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
		
		this.updateComponents(gc, gm, dt);
		
		
	}

	@Override
	public void render(GameContainer gc, Renderer r){
		
		//r.drawFillRect((int)posX, (int)posY, width, height, 0xff0000ff);
		r.drawImage(bulletImage, (int)posX, (int)posY);
		
		this.renderComponents(gc, r);
		
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox){
		
		
		// ig nore when hiotting player atm, as always hits play when fiored to right
		if(other.getTag() == "player")return;
		
		// ignore bullet bullet collision
		if(other.getTag() == "bullet")return;
		
		
		// not sure why but whenthe bullet hits bot bot.... it hits twice for some reason
		if(dead){
			System.out.println("!!! bullet tried to collide again - something is wrong !!!");
			return;
		}
		
		
		// APLPLY BULLET DAMAGE HERE not in the character classes...
		
		// when a bullet collides with an AABBComponent will die
		dead = true;
		
		
		
		

		
	}

	public int getDamage() {
		return damage;
	}

	public Weapon getWeapon() {
		return weapon;
	}

}
