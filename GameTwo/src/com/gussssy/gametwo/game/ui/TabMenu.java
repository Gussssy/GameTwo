package com.gussssy.gametwo.game.ui;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;

public class TabMenu {
	
	private Image img = new Image("/tabMenu2.png");
	//private Image img = new Image("/tabMenu.png");
	
	public void update(GameContainer gc, GameManager gm, float dt){}
	
	public void render(GameContainer gc, Renderer r){
		
		r.drawImage(img, (int)r.getCamX(), (int)r.getCamY());
		//System.out.println("Rendering tab menu");
		
	}

}
