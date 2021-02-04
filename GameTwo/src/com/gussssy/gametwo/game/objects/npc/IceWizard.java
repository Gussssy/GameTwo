package com.gussssy.gametwo.game.objects.npc;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.engine.gfx.ImageTile;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.components.AABBComponent;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.spell.IceShardSpell;

public class IceWizard extends NPC {
	
	// The IceWizards image
	private ImageTile iceWizardImage = new ImageTile("/character/ice_wizard_1.png", 16, 20);
	
	// The wizards image is 20 pixels high. The game is desinged around 16*16 objects. This is a work around for this problem.
	private int imageOffY = 4;
	
	//private Spell2 spell = new Spell2(this);
	private IceShardSpell spell = new IceShardSpell(this);
	
	
	public IceWizard(int tileX, int tileY){
		
		System.out.println("IceWizard Constructor");
		
		this.tag = "iceWizard";
		this.tileX = tileX;
		this.tileY = tileY;
		posX = tileX * GameManager.TS;
		posY = tileY * GameManager.TS;
		offX = 0;
		offY = 0;
		
		/*this.width = 16;
		this.height = 20;*/
		
		// taken from botbot constructor
		this.topPadding = 2;
		this.leftRightPadding = 3;
		this.width = 16 - 2*leftRightPadding;
		this.height = 16 - topPadding;
		
		team = 0;
		
		
		// hitbox
		hitBox = new AABBComponent(this);
		addComponent(hitBox);
		
		// health bar
		healthBar.setHealthBarOffY(-2);
		
		// spell 
		addComponent(spell);
		
	}

	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		// Vision / Awareness
		// ...
		
		
		// Decsion Making
		switch(actionType){
		case IDLE:
			idle(gm);
			break;
		case PATH: 
			break;
		case ATTACK:
			aggroIdle(gm);
			break;
		case FOLLOW:
			break;
		case WAIT:
			break;
		default:
			break;
		}
		
		
		// Movement
		npcMovement(gc, gm, dt);
		npcWalkingAnimation(dt);
		
		// General NPC update
		npcUpdate(gc, gm, dt);
		
		// cast spell if the npc is waiting
		if(waiting){
			
			//spell.castSpell(gc);
			
		}

	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.drawImageTile(iceWizardImage, (int)posX, (int)posY-imageOffY, (int)animationState, direction);
		
		renderComponents(gc,r);

	}

	@Override
	public void collision(GameObject other, AABBComponent otherHitBox) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void attack(GameManager gm, int direction) {
		// TODO Auto-generated method stub
		
	}

}
