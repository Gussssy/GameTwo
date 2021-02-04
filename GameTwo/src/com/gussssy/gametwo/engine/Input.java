package com.gussssy.gametwo.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Recieves Input from mouse and keyboard. Updated each frame.  
 **/
public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	
	private GameContainer gc;
	
	boolean debug = false;
	
	private final int NUM_KEYS = 256;
	private boolean[] keys = new boolean[NUM_KEYS];
	private boolean[] keysLastFrame = new boolean[NUM_KEYS];
	
	private final int NUM_BUTTONS = 5;
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	private boolean[] buttonsLastFrame = new boolean[NUM_BUTTONS];
	private int mouseX, mouseY;
	private int scroll;
	int testCounter;
	
	public Input(GameContainer gc){
		this.gc = gc;
		mouseX = 0;
		mouseY = 0;
		scroll = 0;
		
		gc.getWindow().getCanvas().addKeyListener(this);
		gc.getWindow().getCanvas().addMouseListener(this);
		gc.getWindow().getCanvas().addMouseMotionListener(this);
		gc.getWindow().getCanvas().addMouseWheelListener(this);
	}
	
	public void update(int testCounter){
		
		if(debug)System.out.println("Input Update. Counter:" + testCounter + " The thread that updates input: " + Thread.currentThread().getId());
		
		if(debug)this.testCounter = testCounter;
		
		//reset scroll
		scroll = 0;
		
		//set key state for next frame
		for(int i = 0; i < NUM_KEYS; i++){
			keysLastFrame[i] = keys[i];
		}
		
		//set mouse state for next frame
		for(int i = 0; i < NUM_BUTTONS; i++){
			buttonsLastFrame[i] = buttons[i];
		}
	}
	
	
	
	
	// KEYBOARD INPUT
	
	/**
	 * Is the key currently pressed 
	 **/
	public boolean isKey(int keyCode){
		return keys[keyCode];
	}
	
	/**
	 * Was the key pressed in the last frame and released in this frame
	 **/
	public boolean isKeyUp(int keyCode){
		return !keys[keyCode] && keysLastFrame[keyCode];
	}
	
	/**
	 * Was the key pressed in this frame 
	 **/
	public boolean isKeyDown(int keyCode){
		
		if(debug){if(keyCode == KeyEvent.VK_W){
			System.out.println("(Looking to see w) The thread that gets the key: " + Thread.currentThread().getId());
			System.out.println("\t Counter: " + testCounter);
		}
	}
		
		return keys[keyCode] && !keysLastFrame[keyCode];
	}
	
	
	
	
	// MOUSE BUTTON INPUT
	
	/**
	 * Is the mouse button currently pressed 
	 **/
	public boolean isButton(int button){
		return buttons[button];
	}
	
	/**
	 * Was the mouse button in the last frame and released in this frame
	 **/
	public boolean isButtonUp(int button){
		return !buttons[button] && buttonsLastFrame[button];
	}
	
	/**
	 * Was the mouse button pressed in this frame 
	 **/
	public boolean isButtonDown(int button){
		return buttons[button] && !buttonsLastFrame[button];
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e){
		
		scroll = e.getWheelRotation();
		
		// 1 = scrolling down
		// -1 = scrolling up
	}

	@Override
	public void mouseDragged(MouseEvent e){
		
		mouseX = (int)(e.getX() / gc.getScale());
		mouseY = (int)(e.getY() / gc.getScale());
	}

	@Override
	public void mouseMoved(MouseEvent e){
		
		mouseX = (int)(e.getX() / gc.getScale());
		mouseY = (int)(e.getY() / gc.getScale());
	}

	@Override
	public void mouseClicked(MouseEvent e){
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e){
		
		
	}

	@Override
	public void mouseExited(MouseEvent e){
		
		
	}

	@Override
	public void mousePressed(MouseEvent e){
		
		buttons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e){
		
		buttons[e.getButton()] = false;
	}

	@Override
	public void keyPressed(KeyEvent e){
		
		keys[e.getKeyCode()] = true;
		
		if(debug) {
			System.out.println("keyPressed: The thread that registers key typed: " + Thread.currentThread().getId());
			if(e.getKeyCode() == KeyEvent.VK_W){
				System.out.println(" X X X X Thread: " + Thread.currentThread().getId() + ", registers press of 'w' at counter: " + testCounter);
			}
			//System.out.println("KEY PRESSED");
			//System.out.println(e.getKeyChar());
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e){
		
		keys[e.getKeyCode()] = false;
		//System.out.println("KEY RELEASED");
		// could I do this manually, e.g force a key to be down for mimimum one frame...
	}

	@Override
	public void keyTyped(KeyEvent e){
		
		
	}

	public int getMouseX(){
		return mouseX;
	}

	public int getMouseY(){
		return mouseY;
	}

	public int getScroll(){
		return scroll;
	}

}
