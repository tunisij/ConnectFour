package package1;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**********************************************************************
 * Displays the GUI for the ConnectFourGame 
 * 
 * @author John Tunisi
 * @version 9/24/13
 *********************************************************************/


public class ConnectFourPanel extends JPanel {

	/**A 2D array to hold squares for connect four*/
	private JLabel[][] board;
	
	/**An array of buttons to select which column to play*/
	private JButton[] selection;
	
	/**An array to count the number of wins each player has*/
	private int[] wins;

	/**The first player of the game*/
	private int player;

	/**The top panel to hold the select buttons*/
	private JPanel top;
	
	/**The bottom panel to hold the squares*/
	private JPanel bottom;

	/**A button to exit and close the game*/
	private JButton exit;
	
	/**A button to reset the current game*/
	private JButton reset;
	
	/**A button to undo the last move*/
	private JButton undo;
	
	/**Displays the number of games played*/
	private JLabel gamesPlayed;

	/**The connect four game to play*/
	private ConnectFourGame game;
	
	/**A menu item to see game statistics*/
	private JMenuItem statsItem;
	
	/**A menu item to exit and close the game*/
	private JMenuItem quitItem;
	
	/**A menu item to open a new game*/
	private JMenuItem newGameItem;

	/**The size of the connect four board*/
	private int BDSIZE;
	
	/**The number of connections needed to win*/
	private int connections;
	
	/**The number of players playing the board*/
	private int numPlayers;
	
	/**An array list of colors for each player*/
	private ArrayList<Color> cList = new ArrayList<Color>();	
	
	/**A counter for the number of games played*/
	private int totalGamesPlayed;
	
	/**An arraylist of rows that have been played*/
	private ArrayList<Integer> undoRow = new ArrayList<Integer>();
	
	/**An arraylist of columns that have been played*/
	private ArrayList<Integer> undoCol = new ArrayList<Integer>();
	
	/******************************************************************
	 * A constructor to initialize variables
	 * and ask user for parameters
	 * 
	 * @param quitItem A menu item to quit the game and close
	 * @param newGameItem A menu item to open a new game
	 * @param statsItem A menu item to show the game's statistics
	 *****************************************************************/
	public ConnectFourPanel(JMenuItem quitItem,
			JMenuItem newGameItem, JMenuItem statsItem){	

		//array of colors for each player, in order
		cList.add(Color.WHITE);
		cList.add(Color.BLACK);
		cList.add(Color.RED);
		cList.add(Color.BLUE);
		cList.add(Color.GREEN);
		cList.add(Color.CYAN);
		cList.add(Color.PINK);
		cList.add(Color.LIGHT_GRAY);
		cList.add(Color.ORANGE);
		cList.add(Color.GRAY);
		cList.add(Color.YELLOW);
		cList.add(Color.MAGENTA);	
		
		
		
	
		this.newGameItem = newGameItem;
		this.quitItem = quitItem;
		this.statsItem = statsItem;
//		newGame();
		//board size between 4 and 19	
		while(BDSIZE < 4 || BDSIZE >= 20){
			String strBdSize = JOptionPane.showInputDialog (null,
					"Enter in the size of the board:"+
							"\nMin: 4    Max: 19", 10);
			
			if(checkInput(strBdSize))
				BDSIZE = Integer.parseInt(strBdSize);
			else
				BDSIZE = 0;
		}

		//determines maximum players
		int maxPlay = BDSIZE-1;
		if(maxPlay > 11)
			maxPlay = 11;
		
		//number of players between 2 and 11
		while(numPlayers > maxPlay || numPlayers < 2){
			String numP = JOptionPane.showInputDialog(null, 
					"Enter number of players\nMin: 2    Max: " 
					+ maxPlay, 2);
			
			if(checkInput(numP))
				numPlayers = Integer.parseInt(numP);
			else
				numPlayers = 0;
			
			wins = new int[numPlayers+1];
			if(numPlayers == 1)
				wins = new int[numPlayers+2];
		}
		
		//determines maximum connections
		int maxConn = maxPlay-1;
		String strCons = "";

		//number of connections between 2 and 10
		while(connections > maxConn || connections < 2){
			strCons = JOptionPane.showInputDialog (null, 
					"Enter the number of connections needed to win" + 
					"\nMin: " + 2 + "    Max: " + maxConn, 4);
			
			if(checkInput(strCons))
				connections = Integer.parseInt(strCons);
			else
				connections = 0;
		}

		//starting player
		while(player < 1 || player > numPlayers){
			String firstPlayer = JOptionPane.showInputDialog (null, 
					"Which player starts first?", 1);
			
			if(checkInput(firstPlayer))
				player = Integer.parseInt(firstPlayer);
			else
				player = 0;
		
		//sets all player wins to 0
		for(int i = 0; i < numPlayers; i++){
			wins[i] = 0;
		}
		
		//creates GUI
		top = new JPanel();
		bottom = new JPanel();			

		selection = new JButton[BDSIZE];
		undo = new JButton ("Undo");
		top.add(undo);
		reset = new JButton ("Reset");
		top.add(reset);
		exit = new JButton ("Exit");
		top.add(exit);
		gamesPlayed = new JLabel ("Games Played: " + totalGamesPlayed);
		top.add(gamesPlayed);

		game = new ConnectFourGame(BDSIZE, connections, 
				player, numPlayers);
		
		bottom.setLayout(new GridLayout(BDSIZE+1,BDSIZE,1,1));
		bottom.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		//adds listeners for each button
		ButtonListener listener = new ButtonListener();
		exit.addActionListener(listener);
		undo.addActionListener(listener);
		reset.addActionListener(listener);
		newGameItem.addActionListener(listener);
		quitItem.addActionListener(listener);
		statsItem.addActionListener(listener);
		
		for (int col = 0; col < BDSIZE; col++) {
			selection[col] = new JButton ("Select");
			selection[col].addActionListener(listener);
			bottom.add(selection[col]);
		}

		//sets up game board
		board = new JLabel[BDSIZE][BDSIZE];

		for (int row = 0; row < BDSIZE; row++) {
			for (int col = 0; col < BDSIZE; col++) {
				board[row][col] = new JLabel();
				board[row][col].setBorder(BorderFactory.
						createLineBorder(Color.BLACK));
				
				board[row][col].setOpaque(true);
			    board[row][col].setBackground(Color.WHITE);
				bottom.add(board[row][col]);					
			}
		}

		setLayout(new BorderLayout());
		add (BorderLayout.NORTH,top);
		add (BorderLayout.CENTER,bottom);
		}
	}
	
	public void newGame(){
		//board size between 4 and 19	
				while(BDSIZE < 4 || BDSIZE >= 20){
					String strBdSize = JOptionPane.showInputDialog (null,
							"Enter in the size of the board:"+
									"\nMin: 4    Max: 19", 10);
					
					if(checkInput(strBdSize))
						BDSIZE = Integer.parseInt(strBdSize);
					else
						BDSIZE = 0;
				}

				//determines maximum players
				int maxPlay = BDSIZE-1;
				if(maxPlay > 11)
					maxPlay = 11;
				
				//number of players between 2 and 11
				while(numPlayers > maxPlay || numPlayers < 2){
					String numP = JOptionPane.showInputDialog(null, 
							"Enter number of players\nMin: 2    Max: " 
							+ maxPlay, 2);
					
					if(checkInput(numP))
						numPlayers = Integer.parseInt(numP);
					else
						numPlayers = 0;
					
					wins = new int[numPlayers+1];
					if(numPlayers == 1)
						wins = new int[numPlayers+2];
				}
				
				//determines maximum connections
				int maxConn = maxPlay-1;
				String strCons = "";

				//number of connections between 2 and 10
				while(connections > maxConn || connections < 2){
					strCons = JOptionPane.showInputDialog (null, 
							"Enter the number of connections needed to win" + 
							"\nMin: " + 2 + "    Max: " + maxConn, 4);
					
					if(checkInput(strCons))
						connections = Integer.parseInt(strCons);
					else
						connections = 0;
				}

				//starting player
				while(player < 1 || player > numPlayers){
					String firstPlayer = JOptionPane.showInputDialog (null, 
							"Which player starts first?", 1);
					
					if(checkInput(firstPlayer))
						player = Integer.parseInt(firstPlayer);
					else
						player = 0;
					
					top.removeAll();
					top.revalidate();
//					bottom.removeAll();
					
					//creates GUI
					top = new JPanel();
					bottom = new JPanel();			

					selection = new JButton[BDSIZE];
					undo = new JButton ("Undo");
					top.add(undo);
					reset = new JButton ("Reset");
					top.add(reset);
					exit = new JButton ("Exit");
					top.add(exit);
					gamesPlayed = new JLabel ("Games Played: " + totalGamesPlayed);
					top.add(gamesPlayed);

					game = new ConnectFourGame(BDSIZE, connections, 
							player, numPlayers);
					
					bottom.setLayout(new GridLayout(BDSIZE+1,BDSIZE,1,1));
					bottom.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
					
					//adds listeners for each button
					ButtonListener listener = new ButtonListener();
					exit.addActionListener(listener);
					undo.addActionListener(listener);
					reset.addActionListener(listener);
					newGameItem.addActionListener(listener);
					quitItem.addActionListener(listener);
					statsItem.addActionListener(listener);
					
					for (int col = 0; col < BDSIZE; col++) {
						selection[col] = new JButton ("Select");
						selection[col].addActionListener(listener);
						bottom.add(selection[col]);
					}

					//sets up game board
					board = new JLabel[BDSIZE][BDSIZE];

					for (int row = 0; row < BDSIZE; row++) {
						for (int col = 0; col < BDSIZE; col++) {
							board[row][col] = new JLabel();
							board[row][col].setBorder(BorderFactory.
									createLineBorder(Color.BLACK));
							
							board[row][col].setOpaque(true);
						    board[row][col].setBackground(Color.WHITE);
							bottom.add(board[row][col]);					
						}
					}

					setLayout(new BorderLayout());
					add (BorderLayout.NORTH,top);
					add (BorderLayout.CENTER,bottom);
					
					top.repaint();
					bottom.revalidate();
				}
	}
	
	/******************************************************************
	 * Checks that the input is only digits
	 * 
	 * @param s A string the user enters
	 * @return true if the string only has digits
	 * @return false if the string has other characters
	 *****************************************************************/
	public boolean checkInput(String s){
			if(s.matches(".*[1234567890]"))
				return true;
			return false;
		}

	/******************************************************************
	 * Represents a listener for button push (action) events.
	 * @author John Tunisi
	 *****************************************************************/
	private class ButtonListener implements ActionListener
	{
		/**************************************************************
		 * Updates the counter and label when the button is pushed.
		 *************************************************************/
		
		public void actionPerformed (ActionEvent event)
		{			
			JComponent comp = (JComponent) event.getSource();

			//places a piece down where selected
			for (int col = 0; col < BDSIZE; col++){
				if (comp == selection[col]) {
					int row = game.selectCol(col);
					undoRow.add(row);
					undoCol.add(col);
					
					//if the column is not full
					if (row != -1) {
						board[row][col].setBackground(cList.get
								(game.getCurrentPlayer()));
						
//						board[row][col].setText(""+game.
//						getCurrentPlayer());
						
						game.nextPlayer();
					}
					else
						//if the column is full
						JOptionPane.showMessageDialog(null,
								"Col is full!");
				}
			}
			
//			if(game.onePlayer && game.getCurrentPlayer() == 2){
//				selection[game.playAI()].doClick(250);
//			}
					
			//opens in new window, old window still open :(
			//resets the game completely
			if(comp == newGameItem){
				BDSIZE = 0;
				numPlayers = 0;
				player = 0;
				connections = 0;
				top.removeAll();
				bottom.removeAll();
				newGame();
				top.repaint();
				bottom.repaint();
				//				String[] args = null;
//				ConnectFour.main(args);
			}
			
		    //if the reset button is pressed
			if(comp == reset){
		    	
				//updates number of games played
				totalGamesPlayed++;
		    	top.remove(gamesPlayed);
		    	gamesPlayed = new JLabel ("Games Played: " + 
		    			totalGamesPlayed);
		    	top.add(gamesPlayed);
				
				for (int col = 0; col < BDSIZE; col++)
					selection[col].setEnabled(true);
				undo.setEnabled(true);
				top.revalidate();
				bottom.revalidate();
				game.reset();
		    	
		    	for (int row = 0; row < BDSIZE; row++) {
					for (int col = 0; col < BDSIZE; col++) {
						bottom.remove(board[row][col]);

						board[row][col] = new JLabel();
						board[row][col].setBorder(BorderFactory.
								createLineBorder(Color.BLACK));
						
						board[row][col].setOpaque(true);
					    board[row][col].setBackground(Color.WHITE);
						bottom.add(board[row][col]);
					}
		    	}
		    		
		    	game = new ConnectFourGame(BDSIZE, connections,
		    			player, numPlayers); 
		    }
			
			//close window if button or menu item is clicked
		    if ((comp == exit) || (quitItem == comp))
		    	System.exit(1);

		    //stats list for each player
		    String str = "";
		    int z;
		    if(numPlayers == 1)
		    	z = numPlayers++;
		    z = numPlayers;
		    for (int i = 1; i <= z; i++){
		    	str += ("\nPlayer " + i + " wins: " + wins[i]);
		    }
		    
		    //stats menu item
		    if(comp == statsItem){
		    	JOptionPane.showMessageDialog(null, "Games Played: " +
		    			totalGamesPlayed + "\n" + str);
		    	return;
		    }

		    int count = game.isWinner();

		    //displays winner message
		    if(game.isWinner() > 0){
		    	wins[count]++;
		    	JOptionPane.showMessageDialog(null, "Player " + count
		    			+ " wins!");
		    	
		    	for (int col = 0; col < BDSIZE; col++)
					selection[col].setEnabled(false);
		    	
		    	undo.setEnabled(false);
		    }

		    //displays cats game message
		    if(game.isWinner() == -2){
		    	//totalCatsGames++;
		    	JOptionPane.showMessageDialog(null, "Cats game -_-");
		    	
		    	for (int col = 0; col < BDSIZE; col++) 
					selection[col].setEnabled(false);
		    
		    	undo.setEnabled(false);
		    }
		    
			//if the undo button is pressed
			if(comp == undo){
				
				if(undoRow.size() > 0 && undoCol.size() > 0){
					board[undoRow.get(undoRow.size()-1)]
						 [undoCol.get(undoCol.size()-1)].setBackground
						 (cList.get(0));
					
					undoRow.remove(undoRow.size()-1);
					undoCol.remove(undoCol.size()-1);
				}
				
				game.undo();
				bottom.revalidate();
			}
		}
	}
}