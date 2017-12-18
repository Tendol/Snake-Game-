// Snake.java
//
// Written by Guanghan Pan, Tenzin Dolma Gyalpo

import java.util.*;
import java.awt.*;

public class Snake{

	// instance variables
	protected Vector<Integer> x = new Vector<Integer>();
	protected Vector<Integer> y = new Vector<Integer>();
	protected int direction;
	protected int length;
	protected Color color;
	protected boolean alive;
	protected boolean forwarded;
	

	// construct a new Snake
	public Snake(int initialX, int initialY, Color inicolor){
		for (int i=0; i<3;i++) {
			x.add(initialX-i);
			y.add(initialY);
		}		
		direction = 0;
		length = 3;
		color = inicolor;
		alive = true;
		forwarded = false;
	}
	
	// move forward
	public void forward(){
		for (int i=length-1;i>0;i--){
			x.set(i, x.get(i-1));
			y.set(i, y.get(i-1));
		}
		if (direction == 0){
			x.set(0, x.get(0)+1);
		}
		else if(direction == 1){
			y.set(0, y.get(0)-1);
		}
		else if(direction == 2){
			x.set(0, x.get(0)-1);
		}
		else y.set(0, y.get(0)+1);
		forwarded = true;
	}

	// return the direction of the snake 
	public int getDirection(){
		return direction;
	}
	
	// return the length of the snake 
	public int getLength(){
		return length;
	}
	
	// return the x coordinate of the head of the snake
	public int getHeadX(){
		return x.get(0);
	}
	
	// return the x coordinate of the head of the snake
	public int getHeadY(){
		return y.get(0);
	}
	// return the color of the snake
	public Color getColor(){
		return color;
	}
	
	// set the direction of the snake to n
	public void setDirection(int n){
		direction = n;
	}
	
	// the snake grows one more block
	public void grow() {
		x.add(x.get(length-1));
		y.add(y.get(length-1));
		length++;
	}


}
