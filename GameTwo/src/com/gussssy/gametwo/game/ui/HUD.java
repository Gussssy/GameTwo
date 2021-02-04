package com.gussssy.gametwo.game.ui;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.player.Player;

public class HUD {
	
	// Will use these eventually
	//private int health = 100;
	//private int energy = 100;
	
	private int healthBarWidth = 500/3;
	private int healthBarHeight = 8;
	private int healthBarX = 500/2 - healthBarWidth/2;
	private int healthBarY = 8;
	
	public Player player;
	
	/*public HUD(Player player){
		this.player = player;
	}*/
	
	
	
	public HUD(){
		//GameManager.getGameWidth();
		
		healthBarX = GameManager.getGameWidth()/2 - healthBarWidth/2;
	}
	
	
	
	
	public void update(GameContainer gc, float dt){
		
		// HUD UPDATES
		
	}
	
	
	public void render(GameContainer gc, Renderer r){
		
		//location of the health bar: lets try top center for now
		// - 50px from top, so y = 50
		// - centered so x = 150 - half width
		// size of health bar, height = 16, length ~ 1/3 screen width so 166
		
		r.drawFillRect(healthBarX + r.getCamX(), healthBarY + r.getCamY(), healthBarWidth, healthBarHeight, 0xff00ff00);
		
	}

}
