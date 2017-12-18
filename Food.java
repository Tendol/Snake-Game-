// Food.java
// 
// Written by Guanghan Pan, Tenzin Dolma Gyalpo


public class Food{
	
	// instance variables
	protected int foodX;
	protected int foodY;
	
	// construct the food at random coordinate
	public Food(){
		foodX = (int)(Math.random()*30+1);
		foodY = (int)(Math.random()*30+1);
	}
	
	// relocate the coordinate of the food
	public void relocateFood(){		
		
		foodX = (int)(Math.random()*30+1);
		foodY = (int)(Math.random()*30+1);
		
	}

	// get the x coordinate of the food 
	public int getFoodX(){
		return foodX;
	}
	
	// get the y coordinate of the food
	public int getFoodY(){
		return foodY;
	}
	

}

