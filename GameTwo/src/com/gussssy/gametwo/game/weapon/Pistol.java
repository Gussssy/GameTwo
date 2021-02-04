package com.gussssy.gametwo.game.weapon;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.audio.SoundClip;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.Bullet;
import com.gussssy.gametwo.game.objects.projectile.Grenade;

@SuppressWarnings("unused")
public class Pistol extends Weapon {
	
	SoundClip fire = new SoundClip("/audio/LaserGun.wav");
	
	public Pistol(GameObject wielder){
		
		super("/pistolTile.png", 7,4);
		this.wielder = wielder;
		this.tag = "pistol";
		
		
		// set cooldown
		cooldown = 5;
	}
	/**
	 * Updates the Pistol, currently only does stuff for the Player
	 * 
	 **/
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		if(onCooldown){
			if(counter == 0){
				onCooldown = false;
			}else {
				counter--;
				return;
			}
		}
		
		/*if(gc.getInput().isKeyDown(KeyEvent.VK_UP)){
			gm.addObject(new Bullet(wielder.getTileX(), wielder.getTileY(), wielder.getOffX() + wielder.getWidth()/2, wielder.getOffY() + wielder.getHeight()/2, 0)); // 0
		}
		if(gc.getInput().isKeyDown(KeyEvent.VK_DOWN)){
			gm.addObject(new Bullet(wielder.getTileX(), wielder.getTileY(), wielder.getOffX() + wielder.getWidth()/2, wielder.getOffY() + wielder.getHeight()/2, 1)); // 1
		}
		if(gc.getInput().isKeyDown(KeyEvent.VK_LEFT)){
			gm.addObject(new Bullet(wielder.getTileX(), wielder.getTileY(), wielder.getOffX() + wielder.getWidth()/2, wielder.getOffY() + wielder.getHeight()/2, 2)); // 2
		}
		
		if(gc.getInput().isKeyDown(KeyEvent.VK_RIGHT)){
			gm.addObject(new Bullet(wielder.getTileX(), wielder.getTileY(), wielder.getOffX() + wielder.getWidth()/2, wielder.getOffY() + wielder.getHeight()/2, 3));
		}*/
		
		if(gc.getInput().isKey(KeyEvent.VK_SPACE) && wielder.getTag() == "player"  /**gc.getInput().isButton(1) && wielder.getTag() == "player"*/){
			
			// want to adjust the location the bullet spawn, unfortunately how I made bullet is pretty messed up and needs fixing before I can make this work
			if(direction == 0){
				//gm.addObject(new Bullet(this, wielder.getTileX(), wielder.getTileY(), wielder.getOffX() + wielder.getWidth()/2, wielder.getOffY() + wielder.getHeight()/2, direction));
				//gm.addObject(new Bomb(wielder.getPosX(), wielder.getPosY(), direction, gm));
				gm.addObject(new Grenade(wielder.getPosX()+20, wielder.getPosY(),100, -30, gm));
				counter = cooldown;
				onCooldown = true;
				//fire.play();
			}else {
				//gm.addObject(new Bullet(this, wielder.getTileX(), wielder.getTileY(), wielder.getOffX() + wielder.getWidth()/2, wielder.getOffY() + wielder.getHeight()/2, direction));
				//gm.addObject(new Bomb(wielder.getPosX(), wielder.getPosY(), direction, gm));
				gm.addObject(new Grenade(wielder.getPosX()-20, wielder.getPosY(),-100, -30, gm));
				counter = cooldown;
				onCooldown = true;
				//fire.play();
			}
		}
		
	}

	@Override
	public void useWeapon(GameManager gm, int direction) {
		
		
		if(onCooldown)return;
		//gm.addObject(new Bullet(this, wielder.getTileX(), wielder.getTileY(), wielder.getOffX() + wielder.getWidth()/2, wielder.getOffY() + wielder.getHeight()/2, direction));
		//gm.addObject(new Bomb(wielder.getPosX(), wielder.getPosY(), direction, gm));
		onCooldown = true;
		counter = cooldown;
		//fire.play();
	}
	@Override
	public void fireAtTarget(GameManager gm, GameObject target) {
		
		System.out.println("!!! Pistol.fireAtTarget This weapon does not fire at a target. Need to fix this bad desing so you stop making these mistakes !!!");
		SoundManager.error2.play();
		
	}

	//int x,y, direction;
	
	/*
	@Override
	public void render(GameContainer gc, Renderer r) {
		
		// determine location to draw pistol
		if(wielder.getTag() == "player"){
			direction = wielder.getDirection();
			
			if(direction == 0){
				switch((int)wielder.getAnimationState()){
				case 0:
					x = (int)wielder.getPosX() + 12;
					y = (int)wielder.getPosY() + 12;
					break;
				case 1:
					x = (int)wielder.getPosX() + 14;
					y = (int)wielder.getPosY() + 8;
					break;
				case 2:
					x = (int)wielder.getPosX() + 11;
					y = (int)wielder.getPosY() + 12;
					break;
				case 3:
					x = (int)wielder.getPosX() + 9;
					y = (int)wielder.getPosY() + 8;
					break;
				}
				
			}else{
				switch((int)wielder.getAnimationState()){
				case 0:
					x = (int)wielder.getPosX() - 3; //12 .. 4 but need to -7 so -3
					y = (int)wielder.getPosY() + 12;
					break;
				case 1:
					x = (int)wielder.getPosX() - 6; //14 .. 1 but need -6 so -6
					y = (int)wielder.getPosY() + 8;
					break;
				case 2:
					x = (int)wielder.getPosX() - 3; //11
					y = (int)wielder.getPosY() + 12;
					break;
				case 3:
					x = (int)wielder.getPosX() - 2; //9 
					y = (int)wielder.getPosY() + 8;
					break;
				}
				
			}
			
			
		}else{
			direction = 0;
			x = (int)wielder.getPosX();
			y = (int)wielder.getPosY();
			
		}
		
		
		//r.drawImage(weaponImage, (int)wielder.getPosX() + 5, (int)wielder.getPosY() + 5);
		r.drawImageTile(weaponImage, x, y, 0, direction);
		
		
		
		
	}*/

}
