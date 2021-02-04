package com.gussssy.gametwo.game.particles;


import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;

public class SnowParticle extends Particle {
	
	
	public Image snowFlakeImage;
	private int width = 5;
	private int height = 5;
	//private int[] alphaValues = {51,85,51,85,128,85,51,85,51}; 
	private int[] alphaValues = {85,51,85,51,85,
								 51,85,85,85,51,
								 85,85,128,85,85,
								 51,85,85,85,51,
								 85,51,85,51,85}; 
	private int[] pixels = new int[alphaValues.length];
	public float[] alphaModification = {0.4f,0.6f, 0.9f, 0.6f, 0.4f};
	//public float[] alphaHorizontal = {0.5f,0.70f, 1f, 0.7f, 0.5f};
	
	
	// velocity
	float vx = 20;
	float vy = 150;
	
	
	// Snow specific
	int snowOnGroundLifeTime = 360;
	private boolean willDie = false;
	
	
	public SnowParticle(float posX, float posY, float vx, float vy){
		this.posX = posX;
		this.posY = posY;
		this.vx = vx;
		this.vy = vy;
		
		int x, y;
		int alpha = 255;
		
		
		
		for(int i = 0 ; i < pixels.length; i++){
			//alpha = 255;
			
			x = i%width;
			y = i/width;
			
			alpha = (int)((float)alphaValues[i] * alphaModification[y] * alphaModification[x]);
			
			//pixels[i] = (alphaValues[i] <<24 | 255 << 16 | 255 << 8 | 255 );
			pixels[i] = (alpha <<24 | 255 << 16 | 255 << 8 | 255 );
			
			
			
			
			//System.out.println("x: " + x +", y: " + y);
			
		}
		
		snowFlakeImage = new Image(pixels, width, height);
		snowFlakeImage.setAlpha(true);
		
	}
	
	float offY;

	@Override
	public void update(GameManager gm, float dt) {
		
		if(willDie){
			snowOnGroundLifeTime--;
			if(snowOnGroundLifeTime == 0)dead = true;
			return;
		}
		
		posX += vx * dt;
		posY += vy * dt;
		
		tileX = (int)((posX)/GameManager.TS);
		tileY = (int)((posY)/GameManager.TS);
		
		if(gm.getLevelTileCollision(tileX, tileY)){
			
			// snow flake has collided with a level tile, it will die after sitting on the tile for a number of updates
			willDie = true;
			
			
			
			// prevent snow from settling entirely down the side of tiles
			offY = posY%GameManager.TS;
			if(offY > 3)dead = true;
			
			
			// prevent snow settling at the top of each level tile in a vertical stack
				// the above case still allows snow to settle inapproiately. ("*" no snow, "." is ok for snow)
				//
				//   ....
				//	 *[]*
				// 	 *[]*
				//
			if(gm.getLevelTileCollision(tileX, tileY-1))dead = true;
			
			
		}
		
	}
	
	

}
