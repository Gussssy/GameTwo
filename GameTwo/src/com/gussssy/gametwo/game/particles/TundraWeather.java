package com.gussssy.gametwo.game.particles;

import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.audio.SoundClip;
import com.gussssy.gametwo.game.GameManager;

public class TundraWeather {
	
	// Snow Emitters
	SnowEmitter blizzard = new SnowEmitter();
	SnowEmitter lightSnow = new SnowEmitter();
	
	// sounds 
	SoundClip blizzardSound = new SoundClip("/audio/blizzard_smaller.wav");
	SoundClip rainSound = new SoundClip("/audio/rainAndThunder.wav");
	
	// timing controls
	int counter = 0;
	int nextWeather;
	
	
	public TundraWeather(GameManager gm){
		
		blizzard.active = false;
		lightSnow.active = false;
		
		// add the snow emmitters to the gameobjects
		gm.addObject(blizzard);
		gm.addObject(lightSnow);
		

		// Configure blizzard snow emitter
		blizzard.maxParticlesPerEmission = 200;
		blizzard.minParticlesPerEmission = 150;
		blizzard.minVx = 200;
		blizzard.maxVx = 250;
		blizzard.minVy = 150;
		blizzard.maxVy = 200;
		blizzard.cooldown = 1;
		blizzard.emissionRangeFromPlayer = 1200;
		
		// Light snow follows the snow emitter defaults
		
		
	}
	
	
	
	public void update(GameContainer gc, GameManager gm, float dt){
		
		if(counter <= 0){
			setNextWeather();
		}

		counter--;
		
	}
	
	
	public void setNextWeather(){
		
		counter = ThreadLocalRandom.current().nextInt(600,6000);
		//counter = ThreadLocalRandom.current().nextInt(60,61);
		nextWeather = ThreadLocalRandom.current().nextInt(0,2);
		System.out.println("Next Weather: " + nextWeather);
		
		// TEMP FORCE LGUHT SNOW
		//nextWeather = 0;
		
		if(nextWeather == 0){
			// light snow
			blizzard.active = false;
			lightSnow.active = true;
			
			blizzardSound.stop();
			
		}else {
			// blizzard
			
			blizzardSound.play();
			blizzard.active = true;
			lightSnow.active = false;
		}
		
		
	}

}
