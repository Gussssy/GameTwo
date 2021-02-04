package com.gussssy.gametwo.game.objects.player;

import java.awt.event.KeyEvent;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.game.GameManager;

/**
 * Allows the player to move freely up down left and right in response to keyboard input 
 **/
public class PlayerFreeMovement {
	
	Player player;
	
	float speed = 300;

	/**
	 * FreeMovement constructor 
	 **/
	public PlayerFreeMovement(Player player){
	this.player = player;

	}
	
	public void movementUpdate(GameContainer gc, GameManager gm, float dt){
		
		if(gc.getInput().isKey(KeyEvent.VK_D)){

			player.setOffX(player.getOffX() + speed * dt );
			player.setDirection(0);

		}

		if(gc.getInput().isKey(KeyEvent.VK_A)){

			player.setOffX(player.getOffX() - speed * dt );
			player.setDirection(1);

		}

		if(gc.getInput().isKey(KeyEvent.VK_W)){

			player.setOffY(player.getOffY() - speed * dt );

		}

		if(gc.getInput().isKey(KeyEvent.VK_S)){

			player.setOffY(player.getOffY() + speed * dt );

		}

		player.setPosition();



	}

}
