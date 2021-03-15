package com.gussssy.gametwo.game.objects.tempobjects;

import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;

public class TempLine extends TempObject {
	
	int x0, y0, x1, y1;

	public TempLine(int lifeTime, int x0, int y0, int x1, int y1, int color) {
		super(lifeTime);
		
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.color = color;
		
	}

	

	@Override
	public void render(Renderer r) {
		
		r.drawLine(x0, y0, x1, y1, color);
		
	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub
		
	}

}
