package com.gussssy.gametwo.game.particles;

import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;

public class ClusterParticle extends Particle {
	
	protected Image clusterImage;
	
	public int type = 1;
	
	// seeing as we know each phase the cluster will go through before hand, it would make sense to generate a tileImage that all clustersParticles use
	// but for now this will do, even through expensive
	protected int height = 5;
	protected int width = 11;
	//public int[] alphaVertical = new int[height];
	//public int[] alphaHorizontal = new int[width];
	
	// specific to this original cluster particle which will be used for tornado like effects
	public float[] alphaVertical = {0.25f,0.50f,1.0f, 0.5f,0.25f};
	public float[] alphaHorizontal = {0.25f,0.5f,0.7f,0.8f,0.9f,1.0f,0.9f,0.8f,0.7f,0.5f,0.25f};
	public int baseColor = 0xffffffff;
	public int[] pixels = new int[5*11];
	
	public int deathHeight = 1;
	
	float radians = 0;
	
	
	public ClusterParticle(float posX, float posY){
		
		this.posX = posX;
		this.posY = posY;
		
		int x, y;
		int alpha = 255;
		
		radians = (float)(ThreadLocalRandom.current().nextDouble(0, 2*Math.PI));
		
		for(int i = 0 ; i < pixels.length; i++){
			alpha = 255;
			
			x = i%width;
			y = i/width;
			
			alpha = (int)((float)alpha * alphaVertical[y] * alphaHorizontal[x]);
			
			pixels[i] = (alpha <<24 | 255 << 16 | 255 << 8 | 255 );
			
			
			
			
			//System.out.println("x: " + x +", y: " + y);
			
		}
		
		clusterImage = new Image(pixels, width, height);
		getClusterImage().setAlpha(true);
		
		if(type == 2)deathHeight = 0;
		
	}
	
	float factor = 1.0f;
	float increment = 0.01f;

	@Override
	public void update(GameManager gm, float dt) {
		
		
		if(type == 1){
			//posX += 1;
			posY -= 1;
			
			posX += factor * Math.sin(radians);
			radians += 0.1f;
			factor += increment;
			
			// so particles dont last forever.. kill it if...
			if(posY < deathHeight)dead = true;
			
		}else if(type == 2){
			//posX += 1;
			posX -= 1;
			
			posY += factor * Math.sin(radians);
			radians += 0.1f;
			factor += increment;
			
			
			// so particles dont last forever.. kill it if...
			if(posX < deathHeight)dead = true;
			
		}
		
		
	}

	public Image getClusterImage() {
		return clusterImage;
	}

}
