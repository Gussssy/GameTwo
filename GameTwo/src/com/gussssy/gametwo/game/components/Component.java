package com.gussssy.gametwo.game.components;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.objects.GameObject;

/**
 * Parent class for all Components such as AABBComponents, HealthBars, Weapons and Spells. 
 */
public abstract class Component{
	
	/**
	 * String identifying this component
	 * */
	protected String tag;
	
	/**
	 * The Parent GameObject 
	 **/
	protected GameObject parent;
	
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	public abstract void render(GameContainer gc, Renderer r);
	
	
	public String getTag(){
		return tag;
	}
	public void setTag(String tag){
		this.tag = tag;
	}
	public GameObject getParent(){
		return parent;
	}
	public void setParent(GameObject parent){
		this.parent = parent;
	}

}
