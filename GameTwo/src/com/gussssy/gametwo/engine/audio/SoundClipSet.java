package com.gussssy.gametwo.engine.audio;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class to hold a collection of related sounds to simplify playing a variety of different sounds for a recurring event. 
 **/
public class SoundClipSet {
	
	ArrayList<SoundClip> sounds = new ArrayList<SoundClip>();
	
	// holds the index position of the last played sound
	private int count = 0;

	
	
	/**
	 *  Creates a new empty SoundClipSet
	 */
	public SoundClipSet(){
		
	}
	
	
	
	/**
	 * Add a sound to the set 
	 **/
	public void addSound(SoundClip clip){
		
		sounds.add(clip);
		
	}
	
	/**
	 * Add a new sound to the set by loading in a new sound from file.  
	 **/
	public void addSound(String path){
		
		sounds.add(new SoundClip(path));
		
	}
	
	/**
	 * Play the next sound in the set. Plays sounds in the order they were added.  
	 **/
	public void playNext(){
		
		if(sounds.size() == 0){
			System.out.println("No sounds in this set. Cannot play a sound");
			return;
		}
		
		
		
		if(sounds.size() > count){
			//System.out.println("Playing sound at postion: " + count);
			sounds.get(count).play();
			
			
		}else{
			
			// return count to 0 and play the first sound
			count = 0;
			sounds.get(count).play();
			//System.out.println("Playing sound at postion: " + count);
		}
		
		//increment count to play next sound in the list, next time this method is called
		count++;
		
	}
	
	/**
	 * Play a random sound from the set 
	 **/
	public void playRandom(){
		
		int x  = ThreadLocalRandom.current().nextInt(0, sounds.size());
		//System.out.println("Random Index for sound: " + x + "\n\t(should be 0, 1, 2)");
		
		sounds.get(x).stop();
		sounds.get(x).play();
		
	}
	
}
