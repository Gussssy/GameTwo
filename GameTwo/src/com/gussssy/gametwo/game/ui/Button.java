package com.gussssy.gametwo.game.ui;

import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;

public abstract class Button{
	
	// ints defining the clickable region of the button
	protected int startX, stopX, startY, stopY;
	
	// the image tile for the button - this is only really for a certain type of button though..
	protected ImageTile buttonImageTile;
	
	// is the buitton currently clicked/down 
	public boolean clicked = false;
	
	
	public Button(int x, int y, int width, int height, ImageTile imageTile){
		
		this.startX = x;
		this.startY = y;
		stopX = x + width;
		stopY = y + height;
		
		this.buttonImageTile = imageTile;
		
	}
	
	public abstract void buttonClicked();
	
	
	public void render(Renderer r) {
		if(clicked) {
			r.drawImageTile(buttonImageTile, startX + r.getCamX(), startY + r.getCamY(), 1, 0);
		} else {
			r.drawImageTile(buttonImageTile, startX + r.getCamX(), startY + r.getCamY(), 0, 0);
			
		} 
	}

}
