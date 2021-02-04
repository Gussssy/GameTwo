package com.gussssy.gametwo.game.objects.environment;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.Image;
import com.gussssy.gametwo.engine.gfx.Light;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

public class LampPost extends GameObject {
	
	private Image image = new Image("/lamp_post.png");
	private Light light = new Light(50, 0xffaa9955);
	
	// pixel location of the lamp post. no need to use posX/posY float. Lamp post wont move.
	private int x,y;
	
	
	
	public LampPost(int x, int y){
		
		this.x = x;
		this.y = y;
		
	}
	
	

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.drawImage(image, x, y);
		r.drawLight(light, x + 3, y + 6);

	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub

	}

}
