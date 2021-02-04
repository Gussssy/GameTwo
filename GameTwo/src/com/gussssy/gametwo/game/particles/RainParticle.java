package com.gussssy.gametwo.game.particles;


import com.gussssy.gametwo.game.GameManager;

public class RainParticle extends Particle {
	
	float vx = 20;
	float vy = 150;
	
	public int width = 1;
	public int height = 3;
	public int color = 0x552255ff;
	
	
	
	public RainParticle(float posX, float vx, float vy){
		
		this.posX = posX;
		this.posY = -16;
		this.vx = vx; 
		this.vy = vy;
		
		
		
	}
	
	public void update(GameManager gm, float dt){
		
		posX += vx * dt;
		posY += vy * dt;
		
		tileX = (int)posX/GameManager.TS;
		tileY = (int)posY/GameManager.TS;
		
		if(gm.getLevelTileCollision(tileX, tileY)){
			dead = true;
		}
	}
	
	

}
