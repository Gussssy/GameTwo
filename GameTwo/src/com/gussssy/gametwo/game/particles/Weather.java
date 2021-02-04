package com.gussssy.gametwo.game.particles;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.audio.SoundClip;
import com.gussssy.gametwo.game.GameManager;
import com.gussssy.gametwo.game.debug.DebugPanel;

public class Weather {
	
	ArrayList<ParticleEmitter> emitters = new ArrayList<ParticleEmitter>();
	SoundClip activeWeatherSound = new SoundClip("/audio/teleport.wav");
	SoundClip blizzardSound = new SoundClip("/audio/blizzard_smaller.wav");
	SoundClip rainSound = new SoundClip("/audio/rainAndThunder.wav");
	
	// Snow Emitters
	SnowEmitter blizzard = new SnowEmitter();
	SnowEmitter lightSnow = new SnowEmitter();
	
	// Rain Emitters
	RainEmitter lightRain = new RainEmitter();
	RainEmitter heavyRain = new RainEmitter();
	
	int counter = 0;
	
	
	public Weather(GameManager gm){
		
		blizzard.active = false;
		lightSnow.active = false;
		lightRain.active = false;
		heavyRain.active = false;
		
		emitters.add(blizzard);
		emitters.add(lightSnow);
		emitters.add(heavyRain);
		emitters.add(lightRain);
		
		gm.addObject(blizzard);
		gm.addObject(lightSnow);
		gm.addObject(lightRain);
		gm.addObject(heavyRain);
		
		// heavy rain
		heavyRain.maxParticlesPerEmission = 300;
		heavyRain.minParticlesPerEmission = 150;
		heavyRain.minVx = 0;
		heavyRain.maxVx = 30;
		heavyRain.minVy = 150;
		heavyRain.maxVy = 200;
		
		// light rain
		lightRain.maxParticlesPerEmission = 80;
		lightRain.minParticlesPerEmission = 60;
		lightRain.minVx = 0;
		lightRain.maxVx = 5;
		lightRain.minVy = 150;
		lightRain.maxVy = 200;
		
		// blizzard
		blizzard.maxParticlesPerEmission = 200;
		blizzard.minParticlesPerEmission = 150;
		blizzard.minVx = 200;
		blizzard.maxVx = 250;
		blizzard.minVy = 150;
		blizzard.maxVy = 200;
		blizzard.cooldown = 1;
			
	}
	
	
	
	public void update(GameContainer gc, GameManager gm, float dt){
		
		DebugPanel.message3 = "Weather Counter: " + counter;
		
		counter--;
		
		// time for new weather? 
		if(counter <= 0){
			// yes new weather pls
			setNewWeatherRandom();
			
		}
		
		/*for(ParticleEmitter e : emitters){
			e.update(gc, gm, dt);
		}*/
		
	}
	
	
	
	
	private void setNewWeatherRandom(){
		
		//System.out.println("Setting New Weather");
		
		int nextWeather = ThreadLocalRandom.current().nextInt(0,5);
		//System.out.println("Next Weather Int: " + nextWeather);
		int weatherDuration = ThreadLocalRandom.current().nextInt(600,660);
		counter = weatherDuration;
		//System.out.println("Next Weather duration: " + weatherDuration);
		
		switch(nextWeather){
		case 0:  
			//System.out.println("Next Weather: Fine ");
			blizzard.active = false;
			lightSnow.active = false;
			lightRain.active = false;
			heavyRain.active = false;
			//activeWeatherSound.close();
			break;
		case 1: 
			//System.out.println("Next Weather: Light Rain ");
			blizzard.active = false;
			lightSnow.active = false;
			lightRain.active = true;
			heavyRain.active = false;
			activeWeatherSound.stop();
			// should i be closing...? but then it wont work again ? I need to leanr about this
			activeWeatherSound = rainSound;
			activeWeatherSound.play();
			break;
		case 2: 
			//System.out.println("Next Weather: Heavy Rain ");
			blizzard.active = false;
			lightSnow.active = false;
			lightRain.active = false;
			heavyRain.active = true;
			activeWeatherSound.stop();
			activeWeatherSound = rainSound;
			activeWeatherSound.play();
			break;
		case 3:
			//System.out.println("Next Weather: Light Snow ");
			blizzard.active = false;
			lightSnow.active = true;
			lightRain.active = false;
			heavyRain.active = false;
			activeWeatherSound.stop();
			break;
		case 4:
			//System.out.println("Next Weather: Blizzard ");
			blizzard.active = true;
			lightSnow.active = false;
			lightRain.active = false;
			heavyRain.active = false;
			activeWeatherSound.stop();
			activeWeatherSound = blizzardSound;
			activeWeatherSound.play();
			break;
			
			
		}
	}

}
