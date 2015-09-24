package hw3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import boardAPI.BattleshipBoard;
import boardAPI.Ship;
import boardAPI.battleshipInterface.BattleshipEvent;
import boardAPI.battleshipInterface.BattleshipListener;
import boardGUI.BoardPanel;
import boardGUI.BoardSquare;
import boardGUI.ColorScheme;
import boardGUI.StatusPanel;
import sound.WavPlayer;


@SuppressWarnings("serial")
public class BattleshipApplet extends JApplet implements MouseListener, BattleshipListener{
	

	private final WavPlayer hitPlay = new WavPlayer("/ship_hit.wav");
	private final WavPlayer sinkPlay = new WavPlayer("/ship_sunk.wav");
	private BattleshipBoard logicalBoard;
	private BoardPanel guiBoard;
	private StatusPanel sPanel;
	
	
	/** To start a new game. */
	private final JButton playButton = new JButton("RESET");
	
	/** Message bar to display the number of shots and the outcome. */
	private final JLabel msgBar = new JLabel("");
	
	private boolean isGameOver;
	
	public BattleshipApplet() {
		super();
		logicalBoard = new BattleshipBoard(10, 5);
		placeShips();
		logicalBoard.lockBoard();
		guiBoard = new BoardPanel(logicalBoard.getSize(), 20, new ColorScheme(new Color(50, 50, 255), Color.RED, Color.WHITE, Color.BLACK));
		sPanel = new StatusPanel(logicalBoard.getShipNames());
		playButton.addActionListener(this::resetButtonClicked);
		isGameOver = false;
		

		logicalBoard.addBattleshipListener(guiBoard);
		logicalBoard.addBattleshipListener(sPanel);
		logicalBoard.addBattleshipListener(this);
		
		
	}
	
	@Override
	public void init() {
		configureGui();
	}
	
	/** Configure UI consisting of a play button, a board panel, a status panel, and a message Bar. */
	private void configureGui() {
		setLayout(new BorderLayout());
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttons.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
		buttons.add(playButton);
		
		add(buttons, BorderLayout.NORTH);
		guiBoard.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(guiBoard, BorderLayout.CENTER);
		guiBoard.addMouseListener(this);
		sPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(sPanel, BorderLayout.WEST);
		msgBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(msgBar, BorderLayout.SOUTH);
	}
	
	/**Display the given message to the message bar. */
	private void displayMessage(String msg) {
		msgBar.setText(msg);
	}
	
	private void resetButtonClicked(ActionEvent event) {
		
		logicalBoard = new BattleshipBoard(10, 5);
		placeShips();
		logicalBoard.lockBoard();
		
		guiBoard.reset();
				
		sPanel.reset();
		
		isGameOver = false;
		
		logicalBoard.addBattleshipListener(guiBoard);
		logicalBoard.addBattleshipListener(sPanel);
		logicalBoard.addBattleshipListener(this);
						
		displayMessage("");
				
		repaint();
	}
	
	private void placeShips() {
		// create the ships

		Ship[] fleet = new Ship[5];

		fleet[0] = new Ship("Aircraft Carrier", 5, 0, 0, false);
		fleet[1] = new Ship("Battleship", 4, 0, 0, false);
		fleet[2] = new Ship("Frigate", 3, 0, 0, false);
		fleet[3] = new Ship("Submarine", 3, 0, 0, false);
		fleet[4] = new Ship("Minesweeper", 2, 0, 0, false);
		
		// randomly place the ships on the board
		Random rng = new Random();
		int n = logicalBoard.getSize();
		for (int i = 0; i < fleet.length; i++) {

			boolean validShip = false;
			
			while (!validShip) {

				fleet[i].move(rng.nextInt(n), rng.nextInt(n), rng.nextBoolean());
				validShip = logicalBoard.addShip(fleet[i]);
			}			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && !isGameOver) {
			BoardSquare s = guiBoard.clickAt(e.getX(), e.getY());
			logicalBoard.fireShot(s.ROW, s.COL);
		}
			
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shotFired(BattleshipEvent e) {
		
		
		char status = ' ';
		
		if ((e.getEvent() & BattleshipEvent.HIT) == BattleshipEvent.HIT)
			status = 'H';

		if ((e.getEvent() & BattleshipEvent.MISS) == BattleshipEvent.MISS)
			status = 'M';
		
		if ((e.getEvent() & BattleshipEvent.INVALID) == BattleshipEvent.INVALID)
			status = ' ';
		
					
		if(status == 'H') {
			String dispMsg = "SHOT FIRED! IT'S A HIT!!! ";
			boolean playHit = true;
			if ((e.getEvent() & BattleshipEvent.SHIP_SUNK) == BattleshipEvent.SHIP_SUNK) {
				dispMsg += "You sunk the " + e.getShip().getName() + "!!! ";
				playHit = false;
			}
			displayMessage(dispMsg);
			
			// play sounds
			if (playHit) {
				new Thread(hitPlay).start();
			}
			else
				new Thread(sinkPlay).start();
			
		}
		if(status == 'M') displayMessage("SHOT FIRED! MISS!");
		if(status == ' ') displayMessage("Can't shoot there...");
		
	}

	@Override
	public void gameOver(BattleshipEvent e) {
		isGameOver = true;
		displayMessage("GAME OVER.");		
	}

	@Override
	public void shipMoved(BattleshipEvent e) {
		// TODO Auto-generated method stub
		
	}
		
}
