package com.gussssy.gametwo.game;


import com.gussssy.gametwo.engine.audio.SoundClip;
import com.gussssy.gametwo.engine.audio.SoundClipSet;

public class SoundManager{
	
	public static SoundClip bulletFire = new SoundClip("/audio/LaserGun.wav");
	public static SoundClip sizzle = new SoundClip("/audio/sizzle.wav");
	public static SoundClip jump = new SoundClip("/audio/jump_grunt.wav");
	public static SoundClip jetpack = new SoundClip("/audio/JetPackWhoosh.wav");
	public static SoundClip hurt = new SoundClip("/audio/hurt1.wav");
	public static SoundClip dead = new SoundClip("/audio/dead1.wav");
	public static SoundClip ohbum = new SoundClip("/audio/ohbum.wav");
	public static SoundClip gold = new SoundClip("/audio/pick_up_gold.wav");
	public static SoundClip teleport = new SoundClip("/audio/teleport.wav");
	public static SoundClip goose = new SoundClip("/audio/goose.wav");
	public static SoundClip goose2 = new SoundClip("/audio/goose2.wav");
	public static SoundClip goose3 = new SoundClip("/audio/goose3.wav");
	public static SoundClip error1 = new SoundClip("/audio/error1.wav");
	public static SoundClip error2 = new SoundClip("/audio/goose2.wav");
	public static SoundClip enable = new SoundClip("/audio/enable.wav");
	public static SoundClip turnOff = new SoundClip("/audio/turnOff.wav");
	public static SoundClip beep1 = new SoundClip("/audio/beep1.wav");
	public static SoundClip doing1 = new SoundClip("/audio/doing1.wav");
	public static SoundClip engineIdle1 = new SoundClip("/audio/engine_idle_1.wav");
	public static SoundClip whoosh1 = new SoundClip("/audio/whoosh_1.wav");
	public static SoundClip arrowRelease = new SoundClip("/audio/arrow_release.wav");
	public static SoundClip wind1 = new SoundClip("/audio/wind1.wav");
	
	
	// Magic / spells
	public static SoundClip spellCast1 = new SoundClip("/audio/spellCast1.wav");
	public static SoundClip iceSpell1 = new SoundClip("/audio/ice_spell_1.wav");
	public static SoundClip iceBreak1 = new SoundClip("/audio/ice_break_1.wav"); // almost glass like
		// these three below are similar, not like iceBreak1 ------------------
	public static SoundClip iceBreak2 = new SoundClip("/audio/ice_break_2.wav"); // sound like ice cracking
	public static SoundClip iceBreak3 = new SoundClip("/audio/ice_break_3.wav");
	public static SoundClip iceBreak4 = new SoundClip("/audio/ice_break_4.wav");
		// ---------------------------------------------------------------------
	public static SoundClip spellCharging1 = new SoundClip("/audio/spell_charging_1.wav");
	public static SoundClip portalOpen = new SoundClip("/audio/portal_open.wav");
	
	// Ambient noise
	public static SoundClip marsSound1 = new SoundClip("/audio/mars_sound_1.wav");
	
	
	// Electric noise
	public static SoundClip lightBuzz = new SoundClip("/audio/light_buzz.wav");
	public static SoundClip electricSpark1 = new SoundClip("/audio/electric_spark_1.wav");
	public static SoundClip electricSpark2 = new SoundClip("/audio/electric_spark_2.wav");
	public static SoundClip electricSpark3 = new SoundClip("/audio/electric_spark_3.wav");
	
	// Alerted Sounds for NPCs
	public static SoundClip alerted1 = new SoundClip("/audio/alert_1_quiet.wav");
	public static SoundClip alerted2 = new SoundClip("/audio/alert_2.wav");
	public static SoundClip alerted3 = new SoundClip("/audio/alert_3.wav");
	public static SoundClip alerted4 = new SoundClip("/audio/alert_4.wav");
	
	// IceWizrd Greetings and various other lines
	public static SoundClip wizardGreeting1 = new SoundClip("/audio/wizard_greeting_1.wav");
	public static SoundClip wizardOhMy = new SoundClip("/audio/wizard_oh_my.wav");
	public static SoundClip wizardOhYes = new SoundClip("/audio/wizard_oh_yes.wav");
	public static SoundClip wizardGottem = new SoundClip("/audio/wizard_gottem.wav");
	public static SoundClip wizardAhhShot = new SoundClip("/audio/wizard_ah_shit.wav");
	
	// Laser gun sounds
	public static SoundClip laserGun1 = new SoundClip("/audio/laser_gun_1.wav");
	public static SoundClip laserGun2 = new SoundClip("/audio/laser_gun_2.wav");
	public static SoundClip laserGun3 = new SoundClip("/audio/laser_gun_3.wav");
	public static SoundClip laserGun4 = new SoundClip("/audio/laser_gun_4.wav");
	public static SoundClip laserGun5 = new SoundClip("/audio/laser_gun_5.wav");
	public static SoundClip laserGun6 = new SoundClip("/audio/laser_gun_6.wav");
	public static SoundClip laserGun7 = new SoundClip("/audio/laser_gun_7.wav");
	
	// Snow foot steps
	public static SoundClip snowFootStep1 = new SoundClip("/audio/snow_step_a_1.wav");
	public static SoundClip snowFootStep2 = new SoundClip("/audio/snow_step_a_2.wav");
	public static SoundClip snowFootStep3 = new SoundClip("/audio/snow_step_a_3.wav");
	public static SoundClip snowFootStep4 = new SoundClip("/audio/snow_step_a_4.wav");
	public static SoundClip snowFootStep5 = new SoundClip("/audio/snow_step_a_5.wav");
	public static SoundClip snowFootStep6 = new SoundClip("/audio/snow_step_a_6.wav");
	public static SoundClip snowFootStep7 = new SoundClip("/audio/snow_step_a_7.wav");
	public static SoundClip snowFootStep8 = new SoundClip("/audio/snow_step_a_8.wav");
	public static SoundClip snowFootStep9 = new SoundClip("/audio/snow_step_a_9.wav");
	
	

	// Tracks - Removed 
	//public static SoundClip introChromeSparks = new SoundClip("/audio/copywrited/intro_chromesparks.wav");
	//public static SoundClip onlyTime = new SoundClip("/audio/copywrited/Enya_OnlyTime.wav");
	//public static SoundClip growingOfTheWorld = new SoundClip("/audio/copywrited/growing_of_the_world.wav");

	
	//public static SoundClip x = new SoundClip("/audio/x.wav");
	
	// SoundClipSets
	public static SoundClipSet electricSparks;
	public static SoundClipSet alertSounds;
	public static SoundClipSet wizardGreetings;
	public static SoundClipSet iceSpawnSounds;
	public static SoundClipSet laserGunSounds;
	public static SoundClipSet snowFootSteps;
	
	public SoundManager(){
		
	}
	
	/**
	 * Want this to be an interface method for GameInit 
	 */
	public static void init(){
		
		// set up electric sparks set
		electricSparks = new SoundClipSet();
		electricSparks.addSound(electricSpark1);
		electricSparks.addSound(electricSpark2);
		electricSparks.addSound(electricSpark3);
		
		alertSounds = new SoundClipSet();
		alertSounds.addSound(alerted1);
		alertSounds.addSound(alerted2);
		alertSounds.addSound(alerted3);
		alertSounds.addSound(alerted4);
		
		wizardGreetings = new SoundClipSet();
		wizardGreetings.addSound(wizardGreeting1);
		wizardGreetings.addSound(wizardOhMy);
		wizardGreetings.addSound(wizardOhYes);
		wizardGreetings.addSound(wizardGottem);
		wizardGreetings.addSound(wizardAhhShot);
		
		iceSpawnSounds = new SoundClipSet();
		iceSpawnSounds.addSound(SoundManager.iceBreak2);
		iceSpawnSounds.addSound(SoundManager.iceBreak3);
		iceSpawnSounds.addSound(SoundManager.iceBreak4);
		
		laserGunSounds = new SoundClipSet();
		laserGunSounds.addSound(laserGun1);
		laserGunSounds.addSound(laserGun2);
		laserGunSounds.addSound(laserGun3);
		laserGunSounds.addSound(laserGun4);
		laserGunSounds.addSound(laserGun5);
		laserGunSounds.addSound(laserGun6);
		//laserGunSounds.addSound(laserGun7); // thought this was a good idea but it is not
		
		
		snowFootSteps = new SoundClipSet();
		snowFootSteps.addSound(snowFootStep1);
		snowFootSteps.addSound(snowFootStep2);
		snowFootSteps.addSound(snowFootStep3);
		snowFootSteps.addSound(snowFootStep4);
		snowFootSteps.addSound(snowFootStep5);
		snowFootSteps.addSound(snowFootStep6);
		snowFootSteps.addSound(snowFootStep7);
		snowFootSteps.addSound(snowFootStep8);
		snowFootSteps.addSound(snowFootStep9);
		
		
	}
	
	
}
