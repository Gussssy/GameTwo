package com.gussssy.gametwo.engine.audio;

/**
 * An extension of SoundClip. 
 * 
 *  Havent done anything just planning some more  features lacking in SoundClip without breaking the games sounds
 * 
 * [PLANNED] Adds the ability to send a notification to an object when the sound has finished playing.
 * [PLANNED] Counts the time the sound hhas been playing so that whgen its getting close to finishing it can fade out
 * 
 * 
 **/
public class NotifyingSoundClip extends SoundClip {

	//LineListener listener = new LineListener();
	
	public NotifyingSoundClip(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

}
