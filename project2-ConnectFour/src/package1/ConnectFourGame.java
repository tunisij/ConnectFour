package package1;

import java.util.ArrayList;

/**********************************************************************
 * Creates rules and actions for a game of connect four
 * 
 * @author John
 * @version 9/24/13
 *********************************************************************/

public class ConnectFourGame {

	/**A 2D array creates the board and tells who played in a space*/
	private int[][] board;
	
	/**The first player of the game*/
	private int player;
	
	/**An array of players used to tell which player won*/
	private int[] numPlayers;
	
	/**The number of connections needed to win*/
	private int connections;
	
	/**The size of the board*/
	private int BDSIZE;
	
	/**Increment for number of connections present in isWinner*/
	private int cnt;
	
	/**Increment for each player in game in isWinner*/
	private int count;
	
	/**An arraylist of columns that have been played*/
	private ArrayList<Integer> undoCol = new ArrayList<Integer>();
	
	/**An arraylist of rows that have been played*/
	private ArrayList<Integer> undoRow = new ArrayList<Integer>();
	
	/**Checks if the player is playing against AI*/
	public boolean onePlayer = false;
	
	/**The last row played in the game*/
	public int lastRow;
	
	/**The last column played in the game*/
	public int lastCol;
	
	/******************************************************************
	 * A constructor to initialize variables
	 * 
	 * @param psize the size of the board
	 * @param pconnections the number of connections needed to win
	 * @param fPlayer the first player to start the game
	 * @param pnumPlayers the number of players on the board
	 *****************************************************************/
	public ConnectFourGame (int psize, int pconnections,
			int fPlayer, int pnumPlayers) {
		
		BDSIZE = psize;
		player = fPlayer;
		board = new int[BDSIZE][BDSIZE];
		connections = pconnections;
		
		if(pnumPlayers == 1){
			pnumPlayers = 2;
			onePlayer = true;
		}
		numPlayers = new int[pnumPlayers+1];
		while(count <= pnumPlayers){
			numPlayers[count] = count;
			count++;
		}
		
		reset();
	}

	/******************************************************************
	 * Resets the board
	 *****************************************************************/
	public void reset() {
		for (int r = 0; r < BDSIZE; r++)
			for (int c = 0; c < BDSIZE; c++)
				board[r][c] = -1;
	}

	/******************************************************************
	 * Selects the column to place the piece
	 * 
	 * @param pCol the selected column
	 * @return r the row the piece was placed
	 * @return -1 the piece was not placed
	 *****************************************************************/
	public int selectCol (int pCol) {
		for (int r = BDSIZE-1; r >= 0; r--)
			if (board[r][pCol] == -1) {
				board[r][pCol] = player;
				undoRow.add(r);
				undoCol.add(pCol);
				lastRow = r;
				lastCol = pCol;
				
				return r;
			}
		return -1;
	}

	/******************************************************************
	 * Gets the next player to take a turn
	 * 
	 * @return player that will play next
	 *****************************************************************/
	public int nextPlayer() {
		if(player == numPlayers.length-1)
			return player = 1;
		else
			return player++;
	}
	
	/******************************************************************
	 * Gets the last player to take a turn
	 * 
	 * @return player that played last
	 *****************************************************************/
	public int lastPlayer() {
		if(player == 1)
			return player = numPlayers.length-1;
		else
			return player--;
	}

	/******************************************************************
	 * Gets the current player
	 * 
	 * @return player the current player
	 *****************************************************************/
	public int getCurrentPlayer () {
		return player;
	}
	
	/******************************************************************
	 * Undoes the last move made
	 *****************************************************************/
	public void undo(){
		if(undoRow.size() > 0 && undoCol.size() > 0){
			board[undoRow.get(undoRow.size()-1)]
				 [undoCol.get(undoCol.size()-1)] = -1;
			
			undoRow.remove(undoRow.size()-1);
			undoCol.remove(undoCol.size()-1);
			lastPlayer();
		}
	}
	
	/******************************************************************
	 * Determines the winner of the game
	 * 
	 * @return x the player who won the game
	 * @return -1 the game is not over
	 * @return -2 if the game is a cats game
	 *****************************************************************/
	public int isWinner() {	
		count = 1;
		cnt = 0;
		
		//checks every player for win conditions
		while(count <= numPlayers.length){
			
			//Horizontal win condition
			//goes through every row
			for (int r = 0; r < BDSIZE; r++)
				
				//goes through every column
				for (int c = 0; c < BDSIZE-(connections-1); c++){

					//adds if statements based on number of connections
					for(int i = 0; i < connections; i++){
						
						//checks for horizontal win
						if(board[r][c+i] == count){
							cnt++;
							
						//resets cnt and ends for loop
						}else{
							cnt = 0;
							i = connections;
						}

						//checks if cnt equals 
						//number of connections needed
						if(cnt == connections){
							
							//returns player number of winner
							for (int x = 0; x < numPlayers.length; x++){
								if(numPlayers[x] == count){
									return x;
								}
							}
						}
					}
				}
			
			//Vertical win condition
			//goes through every row
			for (int r = 0; r < BDSIZE-(connections-1); r++)
				
				//goes through every column
				for (int c = 0; c < BDSIZE; c++){

					//adds if statements based on number of connections
					for(int i = 0; i < connections; i++){
						
						//checks for vertical win
						if(board[r+i][c] == count){
							cnt++;
							
						//resets cnt and ends for loop
						}else{
							cnt = 0;
							i = connections;
						}
						
						//checks if cnt equals number of connections needed
						if(cnt == connections){
							
							//returns player number of winner
							for (int x = 0; x < numPlayers.length; x++){
								if(numPlayers[x] == count){
									return x;
								}
							}
						}
					}
				}

			//Diagonal win condition (forward slash)
			//goes through every row
			for (int r = (BDSIZE-connections); r > 0; r--)
				
				//goes through every column
				for (int c = (connections-1); c < BDSIZE-1; c++){

					//adds if statements based on number of connections
					for(int i = 0; i < connections; i++){
						
						//checks for diagonal win
						if(board[r+i][c-i] == count){
							cnt++;
							
						//resets cnt and ends for loop
						}else{
							cnt = 0;
							i = connections;
						}
					
						//checks if cnt equals number of connections needed
						if(cnt == connections){
							
							//returns player number of winner
							for (int x = 0; x < numPlayers.length; x++){
								if(numPlayers[x] == count){
									return x;
								}
							}
						}
					}
				}
		
			//Diagonal win condition (backslash)
			//goes through every row
			for (int r = 0; r < BDSIZE-(connections-1); r++)
				
				//goes through every column
				for (int c = 0; c < BDSIZE-(connections-1); c++){

					//adds if statements based on number of connections
					for(int i = 0; i < connections; i++){
						
						//checks for diagonal win
						if(board[r+i][c+i] == count){
							cnt++;
							
						//resets cnt and ends for loop
						}else{
							cnt = 0;
							i = connections;
						}
					
						//checks if cnt equals number of connections needed
						if(cnt == connections){
							
							//returns player number of winner
							for (int x = 0; x < numPlayers.length; x++){
								if(numPlayers[x] == count){
									return x;
								}
							}
						}
					}
				}
	
			//cats game
			int x = 0;
			for(int c = 0; c <= BDSIZE-1; c++){
				if(board[0][c] > -1){
					x++;
				}
				if(x == BDSIZE)
					return -2;
			}
			count++;
		}
		return -1;
	}

//	public int playAI(){
//		int save1;
//		int save2;
//		
//		//horizontal
//		for (int r = 0; r < BDSIZE-1; r++){
//			for (int c = 0; c < BDSIZE-1; c++){
//				save1 = board[r][c];
//				board[r][c] = 2;
//				if(isWinner() == 2){
//					board[r][c] = save1;
//					return c;
//				}
//				board[r][c] = 1;
//				if(isWinner() == 1){
//					board[r][c] = save1;
//					return c;
//				}	
//			}
//		}
//				
//		for(int c = 0; c < BDSIZE-1; c++){
//			int r = 0;
//				save1 = board[r][c];
//				save2 = board[r][c+1];
//				if(isWinner() == 1){
//					board[r][c] = save1;
//					board[r][c+1] = save2;
//					try{
//						return c+2;
//					}catch(IndexOutOfBoundsException p){
//						return c-1;
//					}
//					
//				}
//				
//				board[r][c] = save1;
//				board[r][c+1] = save2;
//				
//		}
//				
//				if(board[r][c] == 2 )
//					return c;
//				
//				if(board[r][c] == 1 )
//					return c+1;
//				
//		
//		move++;
//		if(move == 1)
//			return BDSIZE/2;
//		if(move == 2)
//			return BDSIZE/2+1;
//		return move;
//		return 0;
//	}
}