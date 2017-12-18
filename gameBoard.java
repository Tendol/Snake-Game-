// gameBoard.java
//
// Written by Guanghan Pan, Tenzin Dolma Gyalpo

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class gameBoard extends Canvas implements ActionListener,KeyListener {

	private static final long serialVersionUID = 1L;

	// Instance variables 	
	static final Color purple = new Color(110, 0, 220);
	public Snake s1 = new Snake(3,1,Color.red);
	public Snake s2;
	private Food food = new Food();
	public int gameMode = 0;
	public boolean inGame = false;
	public int difficulty;
	public boolean cheat = false;
	public int pauseCount = 0;
	private int delay[] = {150,100,50};
	public boolean gameOver = false;
	public boolean pause = false;
	public int highScore[] = {0,0,0};
	public Timer timer;
	protected Image foodImage;
	protected Image[] images = new Image[5];
	protected Image grass;
	Image offscreen;
	Dimension offscreensize;
	Graphics g2;
	int t = 0;

	// initialize the gameBoard, input the images from the panel
	public gameBoard(Image f0,Image f1,Image f2,Image f3, Image f4, Image g) {
		addKeyListener(this);
		setBackground(Color.white);
		images[0] = f0;
		images[1] = f1;
		images[2] = f2;
		images[3] = f3;
		images[4] = f4;
		grass = g;
		setFocusable(true);
		newFood();
	}

	// starts the game  
	public void newGame() {
		cheat = false;
		inGame = true;
		gameOver = false;
		pause = false;
		s1 = new Snake(3,1,Color.red);
		if (gameMode == 1) s2 = new Snake(3,30,Color.green);
		newFood();
		repaint();
		timer = new Timer(delay[difficulty], this);
		timer.setInitialDelay(500); // a short delay before the game has started 
		timer.start();
	}

	// This method is called by Java when the window is changed (e.g.,
	// uncovered or resized), or when "repaint()" is called.
	public void paint (Graphics g) {
		update(g);
	}

	public void update(Graphics g) {
		Dimension d = getSize();

		// initially (or when size changes) creates new image
		if ((offscreen == null)
				|| (d.width != offscreensize.width)
				|| (d.height != offscreensize.height)) {
			offscreen = createImage(d.width, d.height);
			offscreensize = d;
			g2 = offscreen.getGraphics();
			g2.setFont(getFont());
		}

		// erase old contents:
		g2.setColor(getBackground());
		g2.fillRect(0, 0, d.width, d.height);

		// draw new contents:
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.drawImage(grass, 0, 0, getWidth(), getHeight(), this);
		g2.setColor(Color.black);
		drawBorder(g2);
		drawSnake(g2,s1);
		if (gameMode == 1) drawSnake(g2,s2);
		drawFood(g2,food.getFoodX(),food.getFoodY());
		if (pause) showPause(g2);
		if (gameOver) showGameOver(g2);
		showCurrentLength(g2);
		showHighestScore(g2);


		// draws the image on top of the old one
		g.drawImage(offscreen, 0, 0, null);     
	}

	// records the final length of the snake every time it dies. 
	// then prints it on the screen. 
	public void showHighestScore(Graphics g) {
		g.setFont(new Font("Arial", Font.BOLD, 18));
		centerString(g, "Single Player Highest Score:             Easy:" + highScore[0] 
				+ "             Medium:" + highScore[1]
						+ "             Hard:" + highScore[2],
						getWidth()/2, getHeight()-20, Color.BLACK);
	}

	// records the current length of the snake and update it every time the snake grows
	public void showCurrentLength(Graphics g) {
		g.setFont(new Font("Arial", Font.BOLD, 18));
		if (gameMode == 0) {
			centerString(g, "Current Length:" + s1.length,
					getWidth()/2, 10, Color.BLACK);
		} else {
			centerString(g, "Red Current Length:" + s1.length +
					"         Green Current Length:" + s2.length,
					getWidth()/2, 10, Color.BLACK);
		}
	}

	// when the snake(s) dies it prints game Over on the screen 
	private void showGameOver(Graphics g) {

		g.setFont(new Font("Arial Bold", Font.BOLD, 100));
		centerString(g, "Game Over", getWidth()/2, getHeight()/2, Color.red);
		if (gameMode == 0) {
			g.setFont(new Font("Arial Bold", Font.BOLD, 40));
			centerString(g, "Length: " + s1.length, getWidth()/2, getHeight()/2+60, Color.blue);
		}
		if (gameMode == 1) {
			g.setFont(new Font("Arial Bold", Font.BOLD, 40));
			if (s1.alive && !s2.alive) 
				centerString(g, "Red Snake wins!", getWidth()/2, getHeight()/2+60, Color.blue);
			else if (!s1.alive && s2.alive)
				centerString(g, "Green Snake wins!", getWidth()/2, getHeight()/2+60, Color.blue);
			else {
				if (s1.getLength()>s2.getLength()) 
					centerString(g, "Red Snake wins!", getWidth()/2, getHeight()/2+60, Color.blue);
				else if(s1.getLength()<s2.getLength())
					centerString(g, "Green Snake wins!", getWidth()/2, getHeight()/2+60, Color.blue);
				else centerString(g, "Draw!", getWidth()/2, getHeight()/2+60, Color.blue);
			}

		}

	}

	// draws the snake s 
	private void drawSnake(Graphics g, Snake s) {
		for (int i=0; i<s.getLength();i++) {
			if (cheat) p(g,s.x.get(i),s.y.get(i),randomColor());
			else p(g,s.x.get(i),s.y.get(i),s.getColor());
		}
		p(g,s.getHeadX(),s.getHeadY(),purple);
	}

	// draws the food according to the random x and y coordinates. 
	public void drawFood(Graphics g,int x,int y) {
		int width = (getWidth())/34;
		int height = (getHeight())/34;
		g.drawImage(foodImage, (x+1)*width, (y+1)*height, width, height, this);

	}

	// draws the border tiles of the game, depending on the difficulty level
	// of the game. 
	public void drawBorder(Graphics g) {
		for (int i=0;i<=31;i++){
			if (i<=31){
				p(g,0,i,Color.BLACK);
				p(g,31,i,Color.BLACK);
			}
			p(g,i,0,Color.BLACK);
			p(g,i,31,Color.BLACK);
		}

		if (difficulty==2) {
			for (int i=6;i<=25;i++){
				if (i<13 || i>18) {
					p(g,6,i,Color.BLACK);
					p(g,25,i,Color.BLACK);
				}
				p(g,i,6,Color.BLACK);
				p(g,i,25,Color.BLACK);
			}
		}

		if (difficulty==1) {
			drawBlock(g,6,6);
			drawBlock(g,22,6);
			drawBlock(g,6,22);
			drawBlock(g,22,22);
		}
	}

	// draws the respective blocks in the snake 
	public void drawBlock(Graphics g, int x, int y) {
		for (int i = 0;i<4;i++) {
			for (int j = 0;j<4;j++)
				p(g,x+i,y+j, Color.black);
		}
	}

	// paints each block at their respective coordinates
	// with specific color 
	public void p(Graphics g,int x,int y, Color block) {
		int width = (getWidth())/34;
		int height = (getHeight())/34;
		g.setColor(block);
		g.fillRect((x+1)*width, (y+1)*height, width, height);
		g.setColor(Color.WHITE);
		g.drawRect((x+1)*width, (y+1)*height, width, height);
	}

	@Override
	// if the game is still going then it refocuses on the game and moves the snake
	// also checks whether it ate food or collided or died. 
	public void actionPerformed(ActionEvent e) {
		if (inGame) {
			requestFocus();
			s1.forward();
			if (gameMode == 1) {
				s2.forward();
				checkCollision(s2);
				checkFood(s2);
			}
			if (!cheat) checkCollision(s1);
			checkFood(s1);
			checkGameOver();
		}
		repaint();
	}
	
	// Checks whether the snake collided with the walls or itself or with the other snake. 
	public void checkCollision(Snake s){
		if (s.getHeadX()<1||s.getHeadX()>30||
				s.getHeadY()<1||s.getHeadY()>30) 	gameOver(s);
		checkSnakeCollision(s1,s1);		
		if (gameMode == 1) {
			checkSnakeCollision(s1,s2);
			checkSnakeCollision(s2,s2);
			checkSnakeCollision(s2,s1);
			if (s1.getHeadX() == s2.getHeadX() &&s1.getHeadY() == s2.getHeadY()) {
				gameOver(s1);
				gameOver(s2);
			}
		}
		if (difficulty == 1) {
			if((s.getHeadX()>=6 && s.getHeadX()<=9) && (s.getHeadY()>=6 && s.getHeadY()<=9))
				gameOver(s);
			if((s.getHeadY()>=6 && s.getHeadY()<=9) && (s.getHeadX()>=22&&s.getHeadX()<=25))
				gameOver(s);
			if((s.getHeadX()>=6 && s.getHeadX()<=9) && (s.getHeadY()>=22&&s.getHeadY()<=25))
				gameOver(s);
			if((s.getHeadX()>=22&&s.getHeadX()<=25) && (s.getHeadY()>=22&&s.getHeadY()<=25))
				gameOver(s);
		}
		if (difficulty == 2) {
			if((s.getHeadY() == 6 || s.getHeadY() == 25) 
					&& s.getHeadX()>=6 &&s.getHeadX()<=25)
				gameOver(s);
			if ((s.getHeadX() == 6 || s.getHeadX() == 25) 
					&& ((s.getHeadY()>=6 &&s.getHeadY()<=12) 
							|| (s.getHeadY()>=19 &&s.getHeadY()<=25)))
				gameOver(s);
		}
	}
	
	// checks if the two snakes collided with each other  
	public void checkSnakeCollision(Snake first, Snake second){
		for (int i = 1; i < second.getLength(); i++){
			if(first.getHeadX()==second.x.get(i)
					&& first.getHeadY() == second.y.get(i)) {
				gameOver(first);
			}
		}
	}
	
	// game is over for snake s 
	public void gameOver(Snake s) {
		gameOver = true;
		s.alive = false;
	}
	
	// checks if the food is eaten 
	// if so, then grows and makes new food 
	public void checkFood(Snake s){
		if(s.getHeadX() == food.foodX && s.getHeadY() == food.foodY) {
			s.grow();
			newFood();
		}
	}
	
	// create new food
	// find the position and image of the new food 
	public void newFood(){
		food.relocateFood();
		changeFood(); 
		for (int i=0;i<s1.getLength();i++){
			while(food.getFoodX()==s1.x.get(i)
					&&food.getFoodY()==s1.y.get(i)) {
				newFood();
			}
		}

		if (gameMode == 1) {
			for (int i=0;i<s2.getLength();i++){
				while(food.getFoodX()==s2.x.get(i)
						&&food.getFoodY()==s2.y.get(i)) {
					newFood();
				}
			}
		}

		if (difficulty == 1) {
			if((food.getFoodX()>=6 && food.getFoodX()<=9) && (food.getFoodY()>=6 && food.getFoodY()<=9))
				newFood();
			if((food.getFoodY()>=6 && food.getFoodY() <=9) &&(food.getFoodX()>=22&& food.getFoodX()<=25))
				newFood();
			if((food.getFoodX()>=6 && food.getFoodX()<=9) && (food.getFoodY()>=22&& food.getFoodY()<=25))
				newFood();
			if((food.getFoodX()>=22&& food.getFoodX()<=25) &&(food.getFoodY()>=22&& food.getFoodY()<=25))
				newFood();
		}

		if (difficulty == 2) {
			if((food.getFoodY() == 6 || food.getFoodY() == 25) 
					&& food.getFoodX()>=6 &&food.getFoodX()<=25)
				newFood();
			if ((food.getFoodX() == 6 || food.getFoodX() == 25) 
					&& ((food.getFoodY()>=6 && food.getFoodY() <= 12) 
							|| (food.getFoodY()>=19 && food.getFoodY() <= 25)))
				newFood();
		}
	}
	
	// change the image of the food 
	public void changeFood() {	 

		int imageNum = (int) (Math.random()*5);
		foodImage = images[imageNum];
	}

	// check if the game is over or not
	// if so, update the highest score. 
	public void checkGameOver(){
		if (!gameOver) return;
		inGame = false;
		timer.stop();
		if (gameMode == 0) {
			if (s1.getLength() > highScore[difficulty]) 
				highScore[difficulty] = s1.getLength();			
		}
		repaint();
	}

	// respond to the key pressed 
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();
		if (inGame) {
			keyDirection(s1, key, KeyEvent.VK_LEFT, 2);
			keyDirection(s1, key, KeyEvent.VK_RIGHT, 0);
			keyDirection(s1, key, KeyEvent.VK_UP, 1);
			keyDirection(s1, key, KeyEvent.VK_DOWN, 3);			
			if (gameMode == 1) {
				keyDirection(s2, key, KeyEvent.VK_A, 2);
				keyDirection(s2, key, KeyEvent.VK_D, 0);
				keyDirection(s2, key, KeyEvent.VK_W, 1);
				keyDirection(s2, key, KeyEvent.VK_S, 3);
			}
		}
	}
	
	// change the direction of the snake according to key pressed 
	public void keyDirection(Snake s, int input, int Dkey, int direction) {
		if ((input == Dkey) && (s.getDirection()!=direction) 
				&& (s.getDirection()!=((direction+2)%4)) && s.forwarded) {
			s.setDirection(direction);
			s.forwarded = false;
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	// pause the game when pause is pressed 
	public void pause() {
		if (inGame){
			timer.stop();
			inGame = false;
			pause = true;
			repaint();
		}		
	}

	// print pause on the screen 
	public void showPause(Graphics g) {
		g.setFont(new Font("Arial Bold", Font.BOLD, 80));
		centerString(g, "Paused", getWidth()/2, getHeight()/2, Color.red);
	}

	// resume the game when resume is pressed 
	public void resume() {
		if(pause) {
			timer.start();
			inGame = true;
			pause = false;
			repaint();
		}
	}

	// get random color for the snake blocks used during cheat mode 
	public static Color randomColor(){
		int a = (int)(Math.random()*225+1);
		int b = (int)(Math.random()*225+1);
		int c = (int)(Math.random()*225+1);
		Color random = new Color(a,b,c);
		return random;
	}

	// prints the string 's' on the screen at a given position with a given color. 
	public static void centerString(Graphics g, String s, int x, int y, Color c) {
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int xs = x - fm.stringWidth(s)/2 + 1;
		int ys = y + fm.getAscent()/3 + 1;
		g.setColor(c);
		g.drawString(s, xs, ys);
	}

}


