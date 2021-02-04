package com.gussssy.gametwo.game.particles;

import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.game.GameManager;

public class ExplosionParticle extends Particle {
	
	public double vx, vy;
	public float posX, posY;
	public int tileX, tileY;
	public boolean dead = false;
	
	
	public int color = 0xffffffff;		// default colour is white, fully opaque. Currently set by the constructor so this is useless. Unless i make a simplified constructor.
	
	
	
	
	public int lifeTime;				// lifeTime must be set by the constructor
	public int size = 1;				// default size is 1
	
	
	public double velocityFallOff = 1; // velocity falloff is 1 by default
	
	// variables effecting colour change
	int distance = 0;
	double vt;
	int constant = 7;
	
	int alpha = 150;
	int red = 255;
	int green = 255;
	int blue = 255;
	int initialWait = 5;
	
	
	public ExplosionParticle(float posX, float posY, double vx, double vy, double vt, int lifeTime, int particleSize){ 
		
		this.posX = posX;
		this.posY = posY;
		this.tileX = (int) (posX / GameManager.TS);
		this.tileY = (int) (posY / GameManager.TS);
		this.vx = vx;
		this.vy = vy;
		//this.color = color;
		this.lifeTime = lifeTime;
		this.size = particleSize;
		
		red = 255 - ThreadLocalRandom.current().nextInt(0, 15);
		green = 255 - ThreadLocalRandom.current().nextInt(0, 25);
		blue = 255 - ThreadLocalRandom.current().nextInt(0, 25);
		alpha = 150 - ThreadLocalRandom.current().nextInt(0, 100);
		
		
		this.vt = vt;
	}
	
	
	
	public void update(GameManager gm, float dt){
		
		// white -> yellow --> red --> black/grey
		
		if(initialWait > 0){
			color = (alpha <<24 | red << 16 | green << 8 | blue);
			initialWait--;
			return;
		}
		
		distance = (int)(dt*vt*25);
		//System.out.println("Distance: " + distance);
		
		if(blue > 0){
			blue -= constant + distance;
			if(blue < 0)blue = 0;
		}else if(green > 100){
			green -= constant + distance;
			if(green < 0)green = 0;
		}else {
			green -= constant + distance;
			if(green < 0)green = 0;
			red -= constant + distance;
			if(red < 0)red = 0;
			alpha -= 0.5 * constant;
			if(alpha < 0){
				alpha = 0;
				dead = true;
			}
		}
		
		color = (alpha <<24 | red << 16 | green << 8 | blue);
		//System.out.println("Particle Color: " + Integer.toHexString(color));
		
		
		
		
		/*
		lifeTime--;
		if(lifeTime == 0)dead = true;
		
		// calculate effects of gravity
		//vy += dt * gm.getGravity();
		
		// velocity fall off
		vx = vx * velocityFallOff;
		vy = vy * velocityFallOff;
		
		// update position
		posX += vx;
		posY += vy;
		
		// determine tile position
		tileX = (int)posX/16;
		tileY = (int)posY/16;
		
		// use tile position to detect collisions with solid tiles
		if(gm.getLevelTileCollision(tileX, tileY)){
			dead = true;
		}
		*/
		
		
		
		
		
	}



	

}
