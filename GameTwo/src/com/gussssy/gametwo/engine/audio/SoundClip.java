package com.gussssy.gametwo.engine.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A sound loaded from file that can be played. 
 **/
public class SoundClip{
	
	private Clip clip = null;
	private FloatControl gainControl;
	
	
	public SoundClip(String path){
		
		//System.out.println("Attempting to load audio with path: " + path);
			
		try
		{
			InputStream audioSource = SoundClip.class.getResourceAsStream(path);
			InputStream bufferedAudioInput = new BufferedInputStream(audioSource);
			AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedAudioInput);
			
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
														baseFormat.getSampleRate(),
														16, /*baseFormat.getSampleSizeInBits(),*/
														baseFormat.getChannels(),
														baseFormat.getChannels() * 2, /*baseFormat.getFrameSize(),*/
														baseFormat.getSampleRate(),/*baseFormat.getFrameRate(),*/
														false
														);
			//for PCM encoding, frame rate = sample rate
			// frame size is channels * 2 ...? Why 
			
			//System.out.println("Base format sample rate: " + baseFormat.getSampleRate());
			//System.out.println("Base format sample size in bits: " + baseFormat.getSampleSizeInBits());
			//System.out.println("Base format channels: " + baseFormat.getChannels());
			//System.out.println("Base format frame size: " + baseFormat.getFrameSize());
			//System.out.println("Base format frame rate: " + baseFormat.getFrameRate());
			
			//System.out.println("Decode format sample rate: " + decodeFormat.getSampleRate());
			//System.out.println("Decode format sample size in bits: " + decodeFormat.getSampleSizeInBits());
			//System.out.println("Decode format channels: " + decodeFormat.getChannels());
			//System.out.println("Decode format frame size: " + decodeFormat.getFrameSize());
			//System.out.println("Decode format frame rate: " + decodeFormat.getFrameRate() + "\n\n\n");
			
			
			
			
			AudioInputStream decodedAis = AudioSystem.getAudioInputStream(decodeFormat, ais);
			
			clip = AudioSystem.getClip();
			clip.open(decodedAis);
			
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		} 
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			e.printStackTrace();
			System.out.println("Path of sound that falied to load: " + path);
		}
		
		
		System.out.println("Loaded audio file: " + path);
		//System.out.println("Duration: " + clip.);
		
		
	}
	
	public void play(){
		
		if(clip == null) return;
		
		stop();
		
		clip.setFramePosition(0);
		
		// apprently this while loop stop the sound from not playing under some situations, I will need to test this myself
		while(!clip.isRunning()){
			clip.start();
		}
	}
	
	public void stop(){
		
		if(clip.isRunning())clip.stop();
		
	}
	
	public void close(){
		
		stop();
		clip.drain(); 	//empty the stream
		clip.close();	//get rid of the stream
	}
	
	/**
	 * This doesnt always behave as intended. If this method is spammed, it will cancel the previous loop continuous and play the sound once more 
	 */
	public void loop(){
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		play();
	}
	
	/**
	 * I dont think this works properly yet. See tutorial for correction
	 *  Max input value 6.0XXX, 
	 *  
	 */
	public void setVolume(float value){
		gainControl.setValue(value);
	}
	
	public boolean isRunning(){
		return clip.isRunning();
	}

}
