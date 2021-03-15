package com.gussssy.gametwo.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * 
 */
public class Window{
	
	private JFrame frame;
	private BufferedImage image;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	
	/**
	 *  Window constructor. 
	 *  
	 *  Sets up the canvas, the containing frame, the buffer strategy and replaces the default cursor with nothing so a custom cursor can be used.  
	 */
	public Window(GameContainer gc){
		
		
		// The canvas - the game display
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);	//Buffered = in RAM
		Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()), (int)(gc.getHeight() * gc.getScale()));
		canvas = new Canvas();
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
		
		
		// The frame containing the canvas
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		
		
		
		
		// Cursor
		// 	- 	Replace the cursor with a blank image so a custom cursor can be used.
		
		// Transparent 16 x 16 cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		
		// Create a new blank cursor using the above image
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		
		// Set the blank cursor to the JFrame.
		frame.getContentPane().setCursor(blankCursor);
		
		
		
		
	
		// allow the tab key to be used for game key events rather then the default focus traversal
		canvas.setFocusTraversalKeysEnabled(false);
		
		
		
		// Buffer strategy
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
		
	}
	
	/**
	 * Draw the next frame onto the canvas
	 */
	public void update(){
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}

	public BufferedImage getImage(){
		return image;
	}

	public Canvas getCanvas(){
		return canvas;
	}

	public JFrame getFrame(){
		return frame;
	}

}
