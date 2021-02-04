package com.gussssy.gametwo.game.objects.items;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.SoundManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.objects.ObjectType;

// shouyld items be game objects... they wont do very much
public abstract class Item extends GameObject{
	
	protected Image itemImage;
	
	
	
	/**
	 *  @param path . A String containing the path to the image file for this item. 
	 **/
	public Item(String path){
		itemImage = new Image(path);
		width = itemImage.getWidth();
		height = itemImage.getHeight();
		this.tag = "donut";
		components.add(new AABBComponent(this));
		
		objectType = ObjectType.ITEM;
			
	}
	
	/*public Item(int tileX, int tileY){
		
		itemImage = new Image("/donut.png");
		posX = GameManager.TS * tileX;
		posY = GameManager.TS * tileY;
		width = itemImage.getWidth();
		height = itemImage.getHeight();
		this.tag = "donut";
		components.add(new AABBComponent(this));
		
	}*/

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		updateComponents(gc, gm, dt);
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		r.drawImage(itemImage, (int)posX, (int)posY);
		
		this.renderComponents(gc, r);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// player will pick up item by colliding with it
		if(other.getTag() == "player"){
			System.out.println("player picking up item");
			SoundManager.gold.play();
			dead = true;
		}
		
	}

}
