package com.gussssy.gametwo.game.ui;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;

public interface UIElement {
	
	public void update(GameContainer gc, GameManager gm, float dt);
	public void render(Renderer r);

}
