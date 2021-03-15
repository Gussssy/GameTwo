package com.gussssy.gametwo.game;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;

public interface Event {
	
	public void update(GameContainer gc, GameManager gm, float dt);
	public void render(Renderer r);
	public void endEvent();

}
