package com.gussssy.gametwo.game.weapon;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.Component;
import com.gussssy.gametwo.game.objects.GameObject;

public abstract class Weapon extends Component {
	
	int damage;
	int cooldown;
	protected boolean onCooldown = false;
	int counter;
	GameObject wielder;
	
	protected ImageTile weaponImage;
	
	int x,y, direction;

	
	// for when the weapon is rendered
	public Weapon(String path, int tileX, int tileY){
		
		weaponImage = new ImageTile(path,tileX, tileY);
		
	}
	
	// for when the weapon is not rendered
	public Weapon(){
		
	}
	
	public abstract void update(GameContainer gc, GameManager gm, float dt);
	public abstract void useWeapon(GameManager gm, int direction);
	public abstract void fireAtTarget(GameManager gm, GameObject target);
	
	
	@Override
	public void render(Renderer r) {
		
		// Diables rendering for npcs, player only
		if(wielder.getTag() != "player"){
			return;
		}
		
		// determine location to draw pistol
		if(wielder.getTag() == "player"){
			direction = wielder.getDirection();
			
			if(direction == 0){
				switch((int)wielder.getAnimationState()){
				case 0:
					x = (int)wielder.getPosX() + 12;
					y = (int)wielder.getPosY() + 12;
					break;
				case 1:
					x = (int)wielder.getPosX() + 14;
					y = (int)wielder.getPosY() + 8;
					break;
				case 2:
					x = (int)wielder.getPosX() + 11;
					y = (int)wielder.getPosY() + 12;
					break;
				case 3:
					x = (int)wielder.getPosX() + 9;
					y = (int)wielder.getPosY() + 8;
					break;
				}
				
			}else{
				switch((int)wielder.getAnimationState()){
				case 0:
					x = (int)wielder.getPosX() - 3; //12 .. 4 but need to -7 so -3
					y = (int)wielder.getPosY() + 12;
					break;
				case 1:
					x = (int)wielder.getPosX() - 6; //14 .. 1 but need -6 so -6
					y = (int)wielder.getPosY() + 8;
					break;
				case 2:
					x = (int)wielder.getPosX() - 3; //11
					y = (int)wielder.getPosY() + 12;
					break;
				case 3:
					x = (int)wielder.getPosX() - 2; //9 
					y = (int)wielder.getPosY() + 8;
					break;
				}
				
			}
			
			
		}else{
			direction = 0;
			x = (int)wielder.getPosX();
			y = (int)wielder.getPosY();
			
		}
		
		
		//r.drawImage(weaponImage, (int)wielder.getPosX() + 5, (int)wielder.getPosY() + 5);
		r.drawImageTile(weaponImage, x, y, 0, direction);
		
		
		
		
	}

	public GameObject getWielder() {
		return wielder;
	}


}
