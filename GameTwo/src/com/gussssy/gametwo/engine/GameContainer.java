package com.gussssy.gametwo.engine;

import java.awt.event.KeyEvent;

/**
 * GameContainer. 
 * 
 * Contains the GameLoop that tells the Game to update and render 60 times per second.   
 **/
public class GameContainer implements Runnable{

	//Game components
	private Thread thread;
	private Window window;
	private Renderer renderer;
	private Input input;
	private AbstractGame game;

	//Testing booleans
	private boolean efficiencyTestMode = false;
	private boolean testUpdates = false; //not sure this actually gives correct updates, I feel it must be the same as frames.... and its not. 
	private boolean printFPS = false;
	
	// Timers for testing render and update time 
	//private Timer renderTimer = new Timer("\t\t\tRender");
	//private Timer updateTimer = new Timer("Update");

	//Game loop variables
	private boolean running = false;
	private final double UPDATE_CAP = 1.0/60.0; // will update 60 times per second
	private boolean render = false;
	private boolean paused = false;

	//Frame variables
	private int width = 1000, height = 500;
	private float scale = 2f;
	private String title = "Game Two alpha";

	/**
	 * GameContainer Constructor 
	 * 
	 **/
	public GameContainer(AbstractGame game){

		this.game = game;
	}

	/**
	 * Starts the game intitialising key components. Size of the window depends on width, height and scale variales of this class.
	 **/
	public void start(){
		window = new Window(this);
		renderer = new Renderer(this);
		input = new Input(this);
		thread = new Thread(this);
		thread.run();

	}

	/**
	 * Starts the game loop which will continue to run untill the game terminates. 
	 * 
	 * This method will continue to run untill the game terminates. 
	 */
	public void run(){
		
		// Set running to true to start the game loop
		running = true;

		// time variables
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;

		//fps variables
		double frameTime = 0;
		int frames = 0;
		int fps = 0;

		int updates = 0;
		int accumulatedFrames = 0;
		int numSeconds = 0;
		
		int testCounter = 0;
		
		// Perform any setup on the game that had to occur after the game has finished construction
		game.init(this);


		
		// GAME LOOP 
		while(running){


			if(!efficiencyTestMode)
			{
				render = false;
			}
			

			firstTime = System.nanoTime() / 1000000000.0; 	//get the current time 
			passedTime = firstTime - lastTime;				//determine time passed since last iteration
			lastTime = firstTime; 							//set lastTime for the next iteration

			unprocessedTime += passedTime;					//accumulate unprocessedTime
			frameTime += passedTime;						//accumulate time since last fps count


			while(unprocessedTime >= UPDATE_CAP)			//when unprocessedTime exceeds the update cap, execute inner loop
			{			
				

				unprocessedTime -= UPDATE_CAP;				//if the thread freezes for some reason, and we miss two updates, as we don't reset unprocessed time to 0, it will update again until condition is not met
				render = true;

				//UPDATE GAME
				if(!paused){
					
					// update the game
					game.update(this, (float)UPDATE_CAP);
					
				// If the game is paused, execute a single frame when '1' is pressed	
				}else if(input.isKeyDown(KeyEvent.VK_1)){
					
					// Game is currently paused, perform a single update
					game.update(this, (float)UPDATE_CAP);
				}
				
				
				// Increment updates if testing max updates possible
				if(testUpdates)updates++;
				
				
				// Toggle Pause if user presses 'P'
				if(getInput().isKeyDown(KeyEvent.VK_P)){
					if(paused == false) {
						paused = true;
					} else {
						paused = false;
					}
				}
				
				
				// Testing occasional missed input bug
				// there could be an issue here. sometimes input registers 'w' pushed but a player jump does not happen
				input.update(testCounter);
				
				

				//Count FPS after 1 second
				if(frameTime >= 1.0)
				{
					
					//Determines max possible frame renders for efficiency testing purposes
					if(efficiencyTestMode)
					{
						accumulatedFrames += frames;
						numSeconds++;
						getAverageMaxFrames(accumulatedFrames, numSeconds);
					}
					
					// Record frames that have occured in the last second
					frameTime = 0;
					fps = frames;
					frames = 0;
					
					// print fps to console
					if(printFPS)System.out.println("fps: "+fps);
					
					
					if(testUpdates)
					{
						System.out.println("updates: "+updates);
						updates = 0;
					}
				}
			}

			if(render){
				//RENDER GAME
				//renderTimer.startTime();
				renderer.clear();
				
				game.render(this, renderer);
				
				renderer.process();
				//renderTimer.endTime();
				
				// draws the fps count in the top left of the screen
				renderer.drawText("FPS: "+ fps, renderer.getCamX() , renderer.getCamY(), 0xffffffff);
				
				window.update();
				
				frames++;

			}else{
				//System.out.println("I didnt render");

				try{
					Thread.sleep(1);
				} catch (InterruptedException e){e.printStackTrace();}
			}	


			dispose();
		}

	}

	public void stop(){

	}

	public void dispose(){

	}
	
	private void getAverageMaxFrames(int framesTotal, int seconds){
		double averageMaxFrames = (double)framesTotal / (double)seconds;
		System.out.println("Average Max FPS: " + averageMaxFrames);
	}


	public int getWidth(){
		return width;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getHeight(){
		return height;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public float getScale(){
		return scale;
	}

	public void setScale(float scale){
		this.scale = scale;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public Window getWindow(){
		return window;
	}

	public Input getInput(){
		return input;
	}

	public Renderer getRenderer(){
		return renderer;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}


}
