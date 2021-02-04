package com.gussssy.gametwo.game;


import com.gussssy.gametwo.engine.audio.SoundClip;

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
	

	// Tracks - Removed 
	//public static SoundClip introChromeSparks = new SoundClip("/audio/copywrited/intro_chromesparks.wav");
	//public static SoundClip onlyTime = new SoundClip("/audio/copywrited/Enya_OnlyTime.wav");
	//public static SoundClip growingOfTheWorld = new SoundClip("/audio/copywrited/growing_of_the_world.wav");
	
	//Sacrilegium III
	
	
	//public static SoundClip x = new SoundClip("/audio/x.wav");
	
	public SoundManager(){
		
	}
	
	/**
	 * Want this to be an interface method for GameInit 
	 */
	public static void init(){
		
		
		
	}
	
	
}
