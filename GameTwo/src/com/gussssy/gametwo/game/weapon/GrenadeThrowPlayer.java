package com.gussssy.gametwo.game.weapon;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.player.Player;
import com.gussssy.gametwo.game.objects.projectile.Grenade;

public class GrenadeThrowPlayer extends Weapon {
	
	private boolean throwingGrenade = false;
	private int power = 0;
	
	private Player player; 
	
	
	public GrenadeThrowPlayer(Player player){
		
		this.player = player;
		this.tag = "grenade";
		
	}
	
	/**
	 * Currently update is only relevant for the player, not NPCs. NPCs don't need there weapons updated each frame.
	 * - Not sure how ti fix this but I really would like Player and NPC to use the same weapon classes.
	 * - Starting to think that the weapon does not need to be a component. 
	 * - The player can have the weapon updated each frame, the weapon will implement playerWeapon which will have the playerWeapon update method
	 * - The weapon will also implement npcWeapon protocol which contains the npcUseWeapon method
	 * - THIS SYSTEM IS OK FOR NOW BUT NOT FOREVER
	 */
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		// throw a grenade? 
		if(gc.getInput().isKeyDown(KeyEvent.VK_F)){
			throwingGrenade = true;
			SoundManager.engineIdle1.play();
		}
		
		if(throwingGrenade && gc.getInput().isKey(KeyEvent.VK_F)){
			power++;
		}else if(throwingGrenade){
			
			double vx, vy;
			
			int mouseX = gc.getInput().getMouseX();
			int mouseY = gc.getInput().getMouseY();
			
			int xDiff = mouseX - (int)player.getPosX() + gc.getRenderer().getCamX();
			int yDiff = mouseY - (int)player.getPosX() + gc.getRenderer().getCamY();
			System.out.println("xDiff: " + xDiff + "\t yDiff: " + yDiff);
			
			
			// angle: tan-1(xDiff/yDiff)
			double theta = Math.atan(xDiff/yDiff);
			
			// vy: sin(theta) * power
			vy = Math.sin(theta) * power;
			
			// vx: cos(theta) * power
			vx = Math.cos(theta) * power;
			
			
			// throw grenade
			gm.addObject(new Grenade(player.getPosX(), player.getPosY(), (float)vx, (float)vy, gm));
			SoundManager.whoosh1.play();
			
			// reset for next time
			SoundManager.engineIdle1.stop();
			throwingGrenade = false; 
			power = 0;
			
		}
		
		
		
		
		
	}

	@Override
	public void render(Renderer r) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * For when an NPC uses this weapon. NPC DOESNT USE THIS WEAPON ...???
	 * - this weapon is really supposed to be targetted.
	 **/
	@Override
	public void useWeapon(GameManager gm, int direction) {
		
		System.out.println("! ! ! GrenadeThrow.useWeapon: this weapon isnt used in this way. It shpuld be targetted. (Even though that hasnt been implemented yet )");
		SoundManager.error2.play();
		
	}

	
	/**
	 * TODO: this needs to be implemented or this weapon can't be used by npcs 
	 **/
	@Override
	public void fireAtTarget(GameManager gm, GameObject target) {
		// TODO Auto-generated method stub
		
	}

}
