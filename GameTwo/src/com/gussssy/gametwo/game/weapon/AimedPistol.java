package com.gussssy.gametwo.game.weapon;


import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.LaserBullet;
import com.gussssy.gametwo.game.objects.tempobjects.TempLine;

public class AimedPistol extends Weapon{
	
	//GameObject wielder;
	
	float velocity = 600;
	
	
	public AimedPistol(GameObject wielder){
		this.wielder = wielder;
		this.tag = "laserGun";
		
	}

	
	// integers used to determine direction of a new laser bullet
	int mouseX, mouseY, camX, camY, wielderX, wielderY;
	
	
	
	/**
	 * Update is only used for the player 
	 **/
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		
		// TEMP work around so that update only occurs for player
		if(wielder.getTag() != "player")return;
		
		// click LMB to shoot laser bullet at the cursor
		if(gc.getInput().isButtonDown(1)){
			 
			mouseX = gc.getInput().getMouseX();
			mouseY = gc.getInput().getMouseY();
			
			camX = gc.getRenderer().getCamX();
			camY = gc.getRenderer().getCamY();
			
			wielderX = (int)wielder.getPosX();
			wielderY = (int)wielder.getPosY();
						
			// XDistance
			// if mouseX > posX : xDistance is positive, mouse is to the right of player
			int xDistance = mouseX - (wielderX -camX);
		
			
			// YDistance
			// if mouseY > posY
			int yDistance = mouseY - (wielderY - camY);
			
			
			
			// DEBUG RENDERING: render lines representing x and y distance and total distance if showDebug is true
			if(GameManager.showDebug){
				// render line between npc to mouseX
				gm.addObject(new TempLine(10, (int)wielder.getPosX(), (int)wielder.getPosY(), mouseX+camX , mouseY + camY, 0xffffffff));			
				
				// render xDistance
				gm.addObject(new TempLine(10, wielderX, wielderY, wielderX + xDistance , wielderY, 0xff00ff00));
				
				// render yDistance
				gm.addObject(new TempLine(10, wielderX, wielderY, wielderX , wielderY + yDistance, 0xffff00ff));
			}
			
			
			// Calculate vx and vy
			// use the ratio of x and y distance to set x and y velocity
			float vx = velocity * ((float)xDistance/ (float)(Math.abs(xDistance) + Math.abs(yDistance)));
			float vy = velocity * ((float)yDistance/ (float)(Math.abs(xDistance) + Math.abs(yDistance)));
			
			//System.out.println("vx: " + vx);
			//System.out.println("vy: " + vy);
			
			// create a laser bullet that will travel towards the psotion the mouse was clicked
			gm.addObject(new LaserBullet(wielder.getPosX(), wielder.getPosY(), vx, vy, wielder));
			
			// play a sound
			SoundManager.laserGunSounds.playRandom();
		}
		
	}

	
	/**
	 * For when NPC uses this weapon. This wont work for this weapon as it is 'aimed'. 
	 **/
	public void useWeapon(GameManager gm, int direction) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Shoots a bullet at the GameObject given as a parameter.
	 **/
	public void fireAtTarget(GameManager gm, GameObject target){
		
		int xDistance = (int)(target.getPosX() - wielder.getPosX());
		int yDistance = (int)(target.getPosY() - wielder.getPosY());
		
		// Calculate vx and vy
		// use the ratio of x and y distance to set x and y velocity
		float vx = velocity * ((float)xDistance/ (float)(Math.abs(xDistance) + Math.abs(yDistance)));
		float vy = velocity * ((float)yDistance/ (float)(Math.abs(xDistance) + Math.abs(yDistance)));
					
		//System.out.println("vx: " + vx);
		//System.out.println("vy: " + vy);
		
		gm.addObject(new LaserBullet(wielder.getPosX(), wielder.getPosY(), vx, vy, wielder));
		
		// play a sound
		SoundManager.laserGunSounds.playRandom();
		
	}


	
	/***
	 * TODO: should be the same as pistol
	 * - also only for player, dont render for botbots 
	 */
	@Override
	public void render(Renderer r) {
		// TODO Auto-generated method stub
		
	}

	

}
