package com.gussssy.gametwo.game.spell;


import java.util.ArrayList;

import com.gussssy.gametwo.game.components.Component;
import com.gussssy.gametwo.game.objects.GameObject;
import com.gussssy.gametwo.game.particles.ClusterParticle;
import com.gussssy.gametwo.game.particles.Particle;

public abstract class Spell extends Component{
	
	ArrayList<Particle> particles = new ArrayList<Particle>();
	
	GameObject caster;
	
	float spellPosX, spellPosY;

	
	
	public Spell(GameObject caster){
		
		this.caster = caster;
		this.tag = "spell";
		
	}
	
	ClusterParticle cp;
	Particle p;

	protected void removeDeadParticles(){
		
		for(int i = particles.size()-1; i >= 0;  i--){
			
			p = particles.get(i);
			
			// remove the particle if it is now dead
			if(p.dead == true){
				particles.remove(i);
				continue;
			}
		}
		
	}

	public float getSpellPosY() {
		return spellPosY;
	}

	public void setSpellPosY(float spellPosY) {
		this.spellPosY = spellPosY;
	}
	
	
	/**
	
	@Override
	public void update(GameContainer gc, GameManager gm, float dt) {
		
		GameManager.debugMessage1 = "Particles: " + particles.size();
		
		
		
		for(int i = particles.size()-1; i >= 0;  i--){
			
			cp = particles.get(i);
			
			// remove the particle if it is now dead
			if(cp.dead == true){
				particles.remove(i);
				continue;
			}
			
			cp.update(gm, dt);
			
			
			
		}
		
		if(gc.getInput().isKey(KeyEvent.VK_C) || gc.getInput().isButton(3)){
			System.out.println("Cast Spell");
			
			if(count <= 0){
				for(int i = 0; i < 100; i++){
					double x = ThreadLocalRandom.current().nextDouble(wielder.getPosX()-5, wielder.getPosX()+5);
					double y = ThreadLocalRandom.current().nextDouble(wielder.getPosY()-5, wielder.getPosY()+5);
					particles.add(new ClusterParticle((float)x, (float)y));
				}
				
				count = cooldown;
			}else{
				count--;
			}
			
		}
		
		// Spell sound
		if(particles.size() != 0 && !soundPlaying ){
			soundPlaying = true;
			SoundManager.wind1.loop();
		}
		
		if(soundPlaying && particles.size() == 0){
			soundPlaying = false;
			SoundManager.wind1.stop();
		}
		
		
		
	}*/

	/*
	@Override
	public void useWeapon(GameManager gm, int direction) {
		// TODO Auto-generated method stub
		
	}*/

	
	/*
	@Override
	public void render(GameContainer gc, Renderer r) {
		
		for(ClusterParticle p : particles){
			
			r.drawImage(p.getClusterImage(), (int)p.posX, (int)p.posY);
		}
		
		
	}*/
	
}
