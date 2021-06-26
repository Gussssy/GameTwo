package com.gussssy.gametwo.game.objects.environment;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

public class GameImage extends GameObject {
	
	
	private Image image;
	
	private String path;
	
	
	
	
	public GameImage(String path, int tileX, int tileY){
		
		image = new Image(path);
		
		this.tileX = tileX;
		this.tileY = tileY;
		
		
		posX = tileX * GameManager.TS;
		posY = tileY * GameManager.TS;
		
		this.path = path;
		
		System.out.println("New Game Image Created: \n" + toString());
		
	}
	
	

	
	public GameImage(String path, float posX, float posY){
		
		image = new Image(path);
		
		this.posX = posX;
		this.posY = posY;
		
		tileX = (int) (posX/16);
		tileY = (int) (posY/16);
		
		offX = posX%16;
		offY = posY%16;
	
		
		this.path = path;
		
		System.out.println("New Game Image Created: \n" + toString());
		
	}


	
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub
		
		

	}

	@Override
	public void render(Renderer r) {
		
		r.drawImage(image, (int)posX, (int)posY);

	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub

	}
	
	
	public String toString(){
		
		return "\nGameImage: \n\tPath: " + path + "\n\tTileX: " + tileX  + "\n\tTileY: " + tileY 
				+ "\n\toffX: " + offX+ "\n\toffY: " + offY 
				+ "\n\tposX: " + posX + "\n\tposY: " + posY;
		
	}

}
