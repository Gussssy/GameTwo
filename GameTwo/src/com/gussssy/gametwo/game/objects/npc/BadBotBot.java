package com.gussssy.gametwo.game.objects.npc;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.audio.SoundClip;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.components.HealthBar;
import com.gussssy.gametwo.game.debug.DebugPanel;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.projectile.Bullet;
import com.gussssy.gametwo.game.particles.ElectricEffect;
import com.gussssy.gametwo.game.weapon.Pistol;

/**
 * An NPC almost identical to BotBot but with red eyes and by default is on the enemy team.
 */
public class BadBotBot extends NPC{

	private Image image = new Image("/BadBotBot.png");
	
	Light light = new Light(32, 0xffff0000);
	
	ElectricEffect damagedEffect;

	public BadBotBot(int tileX, int tileY){

		this.tag = "badbotbot";
		this.tileX = tileX;
		this.tileY = tileY;
		this.posX = tileX * GameManager.TS;
		this.posY = tileY * GameManager.TS;
		
		// padding 
		this.leftRightPadding = 3;
		this.topPadding = 2;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		// hitbox
		hitBox = new AABBComponent(this);
		addComponent(hitBox);
		
		
		//actionType = NPCActionType.ATTACK;
		//actionType = NPCActionType.IDLE;
		
		weapon = new Pistol(this);
		components.add(weapon);
		team = 1;
		direction = 1;
		
		damagedEffect = new ElectricEffect((int)posX, (int)posY);
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {

		switch(actionType){
		case IDLE:
			idlePathing(gm);
			break;
		case PATH: 
			break;
		case ATTACK:
			aggroIdle(gm);
			//pathToTargetObjectAndAttack(gm);
		case FOLLOW:
			break;
		case WAIT:
			break;
		default:
			break;
		}

		npcMovement(gc, gm, dt);
		npcUpdate(gc, gm, dt);


		if(health < 50){
			damagedEffect.update(gc, gm, dt);
			damagedEffect.setLocationX((int)posX);
			damagedEffect.setLocationY((int)posY);
		}


	}

	@Override
	public void render(Renderer r) {

		r.drawImage(image, (int)posX, (int)posY);

		// dont like that tgis is here, need to add this to each new npc class...
		renderComponents(r);
		r.drawLight(light, (int)posX+14, (int)posY+14);
		
		// if sufficiently damaged this badbotbot will have an electric effect
		if(health < 50){
			damagedEffect.render(r);
		}
		
		
		if(GameManager.showDebug){
			vision.renderVision(r);
		}

	}

	int[] incursions = new int[4];
	int smallestIndex;
	int botIncursion, topIncursion, leftIncursion, rightIncursion;
	
	
	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		if(other.getTag().equals("bullet")){

			
			if(((Bullet)other).getWeapon().getWielder().getTag() != tag){
				// bullet was fired by npc of a different type
			}else{
				
				// dw just friendly fire, ignore this bullet
				return; 
			}
			
			HealthBar hp = (HealthBar)this.findComponent("hp");
			System.out.println("healthPixels before" + hp.getHealthPixels());


			System.out.println("Bullet Hit Bot Bot");
			System.out.println("Was the bullet dead? " + other.isDead());
			//hurt.play();
			System.out.println();
			hp.healthChanged(-10);

			if(health < 0){
				//SoundManager.dead.play();
				hp.healthChanged(100);
			}

		}
		
		if(other.getTag().equals("platform")){
			AABBComponent thisAABB = (AABBComponent) this.findComponent("aabb");
			AABBComponent otherAABB = (AABBComponent) other.findComponent("aabb");

			// how much the object has penetrated top, bottom, left and right
			topIncursion = thisAABB.getStopY() - otherAABB.getStartY();
			botIncursion = otherAABB.getStopY() - thisAABB.getStartY();
			leftIncursion = thisAABB.getStopX() - otherAABB.getStartX();
			rightIncursion = otherAABB.getStopX() -  thisAABB.getStartX();
			
			DebugPanel.message1 = "Not Colliding";
			DebugPanel.message2 = "Top Incursion: " + Integer.toString(topIncursion);
			DebugPanel.message3 = "Bottom Incursion: " + Integer.toString(botIncursion);
			DebugPanel.message4 = "Left Incursion: " + Integer.toString(leftIncursion);
			DebugPanel.message5 = "Right Incursion: " + Integer.toString(rightIncursion);
			
			incursions[0] = topIncursion;
			incursions[1] = botIncursion;
			incursions[2] = leftIncursion;
			incursions[3] = rightIncursion;
			
			
			smallestIndex = 0;
			//System.out.println("");
			for(int i = 1; i < incursions.length; i++){
				//System.out.println("Comparing current smallest: " + incursions[smallestIndex] + " with: " + incursions[i]);
				if(incursions[i] < incursions[smallestIndex]){
					smallestIndex = i;
					//System.out.println("Smaller, new smallest: " + incursions[i]);
					
				}else {
					//System.out.println("not smaller, current smallest: " + incursions[smallestIndex]);
				}
				
			}
			
			// Top Collision
			if(smallestIndex == 0){
				//System.out.println(" Top Collision");
				DebugPanel.message1 = "Top Collision";
				offY -= topIncursion;
				fallDistance = 0;
				onAABB = true;
				//onGround = true;
				onThisObject = otherAABB;
				//setPosition();
				
			} else if(smallestIndex == 1){
				//System.out.println(" Bottom Collision");
				DebugPanel.message1 = "Bottom Collision";
				offY += botIncursion;
				fallDistance = 0;
				//setPosition();
				
			} else if(smallestIndex == 2){
				//System.out.println(" Left Collision");
				DebugPanel.message1 = "Left Collision";
				offX -= leftIncursion;
				//stuckLeft = true;
				//stuckRight = false;
				//setPosition();
				
			} else {
				//System.out.println(" Right Collision");
				DebugPanel.message1 = "Right Collision";
				offX += rightIncursion;
				//stuckRight = true;
				//stuckLeft = false; 
				//setPosition();
				
			}
		}
		
		
		

	}

	@Override
	protected void attack(GameManager gm, int direction) {
		weapon.useWeapon(gm, direction);

	}

}
