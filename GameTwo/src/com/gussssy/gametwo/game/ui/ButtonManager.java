package com.gussssy.gametwo.game.ui;

import java.util.ArrayList;

import com.gussssy.gametwo.engine.GameContainer;
import com.gussssy.gametwo.engine.Renderer;
import com.gussssy.gametwo.game.GameManager;

public class ButtonManager implements UIElement {
	
	// the buttons this button manager is looking after
	public ArrayList<Button> buttons = new ArrayList<Button>();
	
	int mouseX;
	int mouseY;
	
	
	// each frame checks to see if any of its buttons has been clicked
	public void update(GameContainer gc, GameManager gm, float dt){
		
		// if the mouse has been clicked, loop through buttons to see if 
		if(gc.getInput().isButton(1)){
			
			System.out.println("CLICKED\n\tMouseX: " + mouseX + "\n\tMouseY: " + mouseY);
			
			// Left Mouse Button is down, get the coords
			mouseX = gc.getInput().getMouseX();
			mouseY = gc.getInput().getMouseY();
			
			// loop though buttons and see if click was on a button
			for(Button b : buttons ){
				
				if(mouseX > b.startX && mouseX < b.stopX && mouseY > b.startY && mouseY < b.stopY ){
					
					System.out.println("Button startX: " + b.startX);
					
					// the button was clicked
					b.buttonClicked();
					b.clicked = true;
					
				}
				
			}
			
			
		}else{
			for(Button b : buttons){
				b.clicked = false;
			}
		}
		
	}
	
	
	public void addButton(Button b){
		buttons.add(b);
		
	}


	@Override
	public void render(Renderer r) {
		
		for(Button b : buttons){
			b.render(r);
		}
		
	}

}
