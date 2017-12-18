// snakePanel.java
//
// Written by Guanghan Pan, Tenzin Dolma Gyalpo 

import java.applet.*;
import java.awt.*;
import java.awt.event.*;



public class snakePanel extends Applet implements ActionListener {

	private static final long serialVersionUID = 1L; // to avoid Eclipse warning

	// instance variables
	protected Panel gamePanel;
	Button onePlayerButton, twoPlayersButton,startButton,
	cheatButton,resumeButton,pauseButton;
	Choice difficultyChoice;
	gameBoard a;
	int selectedDifficulty;
	int selectedGameMode = 0;

	// initializes the applet
	public void init() {
		Image apple = getImage(getDocumentBase(), "apple.png");
        Image mango = getImage(getDocumentBase(), "mango.png");
        Image banana = getImage(getDocumentBase(), "banana.png");
        Image strawberry = getImage(getDocumentBase(), "strawberry.png");
        Image watermelon = getImage(getDocumentBase(), "watermelon.png");
        Image grass = getImage(getDocumentBase(), "grass.jpg");
		a = new gameBoard(apple, mango, banana, strawberry, watermelon, grass);
		setLayout(new BorderLayout()); 
		add("Center", makeGamePanel());
		add("South", makeBottomPanel());
	}

	// Create the bottom Panel
	private Panel makeBottomPanel() {
		setFont(new Font("TimesRoman", Font.BOLD, 12));
		Panel bottomPanel = new Panel();
		bottomPanel.setLayout(new GridLayout(2,1));
		Panel UpperPanel = new Panel();
		UpperPanel.setLayout(new BorderLayout());
		UpperPanel.add("West",makePlayerPanel());
		UpperPanel.add("East",makeStartPanel());
		bottomPanel.add(UpperPanel);
		bottomPanel.add(makeLowerPanel());
		return bottomPanel;
	}

	// Create the start panel in the bottom Panel
	public Panel makeStartPanel() {
		Panel startPanel = new Panel();
		startPanel.setLayout(new GridLayout());
		difficultyChoice = new Choice();
		difficultyChoice.addItem("Easy");
		difficultyChoice.addItem("Medium");
		difficultyChoice.addItem("Hard");
		startPanel.add(difficultyChoice);
		startButton = new Button("Start!");
		startPanel.add(startButton);
		startButton.addActionListener(this);
		return startPanel;
	}

	// Create the Lower Panel in the Bottom Panel
	public Panel makeLowerPanel() {
		Panel lowerPanel = new Panel();
		lowerPanel.setLayout(new BorderLayout());
		Panel pausePanel = new Panel();
		pauseButton = new Button("Pause");
		pausePanel.add(pauseButton);
		pauseButton.addActionListener(this);
		resumeButton = new Button("Resume");
		pausePanel.add(resumeButton);
		resumeButton.addActionListener(this);
		lowerPanel.add("East",pausePanel);
		return lowerPanel;
	}

	// Create the Player Panel in the bottom panel
	private Panel makePlayerPanel() {
		Panel playerPanel = new Panel();
		playerPanel.setLayout(new GridLayout());
		onePlayerButton = new Button("one player");
		twoPlayersButton = new Button("two players");
		playerPanel.add(onePlayerButton);
		onePlayerButton.addActionListener(this);
		playerPanel.add(twoPlayersButton);
		twoPlayersButton.addActionListener(this);
		return playerPanel;
	}

	// creates the game panel at the center
	public Panel makeGamePanel() {
		gamePanel = new Panel();
		gamePanel.setBackground(Color.white);
		gamePanel.setLayout(new GridLayout(1, 1));
		gamePanel.add(a);
		return gamePanel;
	}


	// handle button clicks
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {
			
			System.out.println("Start Clicked!");
			a.pauseCount = 0;
			getDifficulty();
			if (a.inGame) a.timer.stop();	
			a.difficulty = selectedDifficulty;
			a.gameMode = selectedGameMode;	
			a.newGame();
		} else if (e.getSource() == onePlayerButton) {
			selectedGameMode = 0;
		} else if (e.getSource() == twoPlayersButton) {
			selectedGameMode = 1;
		} else if (e.getSource() == pauseButton) {
			a.pause();
			if (a.pause) a.pauseCount++;
			if (a.pauseCount == 10 && a.gameMode == 0) {
				a.cheat = true;
				a.resume();
				a.pauseCount = 0;
			}
		} else if (e.getSource() == resumeButton) {
			a.resume();
			a.pauseCount = 0;
		}
	}

	// get the difficulty selected by the user
	private void getDifficulty() {
		if (difficultyChoice.getSelectedItem().equals("Easy"))
            selectedDifficulty=0;
		else if (difficultyChoice.getSelectedItem().equals("Medium"))
            selectedDifficulty=1;
		else
            selectedDifficulty=2;
	}



}



