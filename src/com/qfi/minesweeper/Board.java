package com.qfi.minesweeper;

public class Board
{
	int m_size = 0;
	private int[][] m_board = null;// = new int[8][8];//main array
	private int[][] m_selects = null;// = new int[8][8];//array of chosen positions

	public Board (int size)
	{
		m_size = size;
		m_board = new int[m_size][m_size];
		m_selects = new int[m_size][m_size];
	}

	/*
	 * This method was made to check bomb counts on cells and used to test game
	 */
	public void PrintBoard(){

		System.out.print("  ");

		for(int i = 0; i < m_size; i++){
			System.out.print("|" + (i+1) );

		}
		System.out.print("|");
		System.out.println();
		System.out.println("-------------------");

		for(int i = 0; i < m_size; i++){
			System.out.print((i+m_size + " "));
			for(int j = 0; j < m_size; j++){
				if(m_board[i][j] == 0){
					System.out.print("|-");
				}
				else{
					System.out.print("|" + m_board[i][j]);
				}
			}
			System.out.print("|");
			System.out.println();
		}

		System.out.print("-------------------");
		System.out.println();
	}

	/*
	 * Places ten "bombs" in both arrays randomly sets values for bombs in each array
	 */
	public void PlaceBombs(){
		int bomb = 9;
		int hold, count = 0;

		while(count < 10){
			for(int i = 0; i < m_board.length; i++){
				for(int j = 0; j < m_board[0].length; j++){
					hold = (int)(Math.random() * 25) + 1;
					if(hold == bomb && count < 10){
						m_board[i][j] = 9;
						m_selects[i][j] = 11;
						count++;
					}

				}
			}
		}

	}
	
	/*
	 * Counts bombs surrounding ith position throughout array 
	 */
	public void BombCount(){
		int count = 0;

		for(int i = 0; i < m_board.length; i++){
			count = 0;
			for(int j = 0; j < m_board[0].length; j++){
				count = 0;

				if(m_board[i][j] == 9){
					try{
						if(m_board[i][j] == 9)
							m_board[i][j] = 9;
					}catch(Exception e){

					}
				}

				else{
					try{
						if(m_board[i+1][j] == 9)
							count++;
					}catch(Exception e){

					}

					try{
						if(m_board[i-1][j] == 9)
							count++;
					}catch(Exception e){

					}

					try{		
						if(m_board[i][j+1] == 9)
							count++;
					}catch(Exception e){

					}

					try{
						if(m_board[i][j-1] == 9)
							count++;
					}catch(Exception e){

					}

					try{
						if(m_board[i-1][j-1] == 9)
							count++;
					}catch(Exception e){

					}

					try{
						if(m_board[i-1][j+1] == 9)
							count++;
					}catch(Exception e){

					}

					try{
						if(m_board[i+1][j+1] == 9)
							count++;
					}catch(Exception e){

					}

					try{
						if(m_board[i+1][j-1] == 9)
							count++;
					}catch(Exception e){

					}

					m_board[i][j] = count;
				}
			}
		}

	}

	/*
	 * Prints "empty" array board for user
	 */
	public void PrintGame(){
		System.out.print("  ");

		for(int i = 0; i < m_size; i++){
			System.out.print("|" + (i+1) );

		}
		System.out.print("|");
		System.out.println();
		System.out.println("-------------------");

		for(int i = 0; i < m_size; i++){
			System.out.print((i+1 + " "));
			for(int j = 0; j < m_size; j++){
				if(m_board[i][j] == 0 || m_board[i][j] != 0)
					System.out.print("|-");
			}
			System.out.print("|");
			System.out.println();
		}

		System.out.print("-------------------");
		System.out.println();
	}

	/*
	 * Determines what to do when player chooses a specific position
	 */
	public void UpdateOnMove(int row, int col){
		if(m_board[row][col] != 9 && m_board[row][col] != 0){
			UpdateBoard(row, col);
		}
		else{
			Spread(row, col, m_board);
		}

	}

	/*
	 * Updates board to user , outlines all possibilities that could be 
	 * shown throughout game
	 */
	public void UpdateBoard(int row, int col){

		System.out.print("  ");

		for(int i = 0; i < m_size; i++){
			System.out.print("|" + (i+1) );

		}
		System.out.print("|");
		System.out.println();
		System.out.println("-------------------");

		for(int i = 0; i < m_size; i++){
			System.out.print((i+1 + " "));
			for(int j = 0; j < m_size; j++){
				if(m_selects[i][j] == 10){
					System.out.print("| ");
				}
				else if(m_selects[i][j] == 11){
					System.out.print("|-");
				}
				else if(m_selects[i][j] != 0){
					System.out.print("|" + m_selects[i][j]);
				}
				else if(m_board[row][col] == 0 && i == row && j == col){
					System.out.print("| ");
				}
				else if(m_board[i][j] != m_board[row][col]){
					System.out.print("|-");
				}
				else if(m_board[i][j] == m_board[row][col]){
					if(i == row && j == col){
						System.out.print("|" + m_board[row][col]);
						m_selects[i][j] = m_board[row][col];
					}
					else{
						System.out.print("|-");
					}
				}
				else{
					System.out.print("|-");
				}
			}
			System.out.print("|");
			System.out.println();

		}


		System.out.print("-------------------");
		System.out.println();

		/* was used to test selects array inputs
		for(int i = 0; i < selects.length; i++){
			for(int j = 0; j < selects[0].length; j++){
				System.out.print(selects[i][j]);
			}
			System.out.println();
		}
		 */
	}

	/*
	 * Algorithm for if a spread is called on position, checks 8 surrounding positions to
	 * one called on
	 */
	public void Spread(int row, int col, int[][] arr){
		if(arr[row][col] == 0){
			try{
				if(arr[row][col] != 9){
					if(arr[row][col] != 0){
						m_selects[row][col] = arr[row][col];
					}
					if(arr[row][col] == 0){
						m_selects[row][col] = 10;
					}
				}
			}catch(Exception e){

			}

			try{
				if(arr[row + 1][col] != 9){
					if(arr[row + 1][col] != 0){
						m_selects[row + 1][col] = arr[row + 1][col];
					}
					if(arr[row + 1][col] == 0){
						m_selects[row + 1][col] = 10;
					}
				}
			}catch(Exception e){

			}

			try{
				if(arr[row + 1][col + 1] != 9){
					if(arr[row + 1][col + 1] != 0){
						m_selects[row + 1][col + 1] = arr[row + 1][col + 1];
					}
					if(arr[row + 1][col + 1] == 0){
						m_selects[row + 1][col + 1] = 10;
					}
				}
			}catch(Exception e){

			}

			try{
				if(arr[row +1][col - 1] != 9){
					if(arr[row +1][col - 1] != 0){
						m_selects[row + 1][col - 1] = arr[row +1][col - 1];
					}
					if(arr[row +1][col - 1] == 0){
						m_selects[row + 1][col - 1] = 10;
					}
				}
			}catch(Exception e){

			}

			try{
				if(arr[row - 1][col] != 9){
					if(arr[row - 1][col] != 0){
						m_selects[row - 1][col] = arr[row - 1][col];
					}
					if(arr[row - 1][col] == 0){
						m_selects[row - 1][col] = 10;
					}
				}
			}catch(Exception e){

			}

			try{
				if(arr[row - 1][col + 1] != 9){
					if(arr[row - 1][col + 1] != 0){
						m_selects[row - 1][col + 1] = arr[row - 1][col + 1];
					}
					if(arr[row - 1][col + 1] == 0){
						m_selects[row - 1][col + 1] = 10;
					}
				}
			}catch(Exception e){

			}

			try{
				if(arr[row - 1][col - 1] != 9){
					if(arr[row - 1][col - 1] != 0){
						m_selects[row - 1][col - 1] = arr[row - 1][col - 1];
					}
					if(arr[row - 1][col - 1] == 0){
						m_selects[row - 1][col - 1] = 10;
					}
				}
			}catch(Exception e){

			}

			try{
				if(arr[row][col - 1] != 9){
					if(arr[row][col - 1] != 0){
						m_selects[row][col - 1] = arr[row][col - 1];
					}
					if(arr[row][col - 1] == 0){
						m_selects[row][col - 1] = 10;
					}
				}
			}catch(Exception e){

			}

			try{
				if(arr[row][col + 1] != 9){
					if(arr[row][col + 1] != 0){
						m_selects[row][col + 1] = arr[row][col + 1];
					}
					if(arr[row][col + 1] == 0){
						m_selects[row][col + 1] = 10;
					}
				}
			}catch(Exception e){

			}

		}
		UpdateBoard(row, col);
	}
	
	/*
	 * If game is lost, outputs boolean in main class  
	 */
	public boolean LoseGame(int row, int col){
		if(m_board[row][col] == 9){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * If is true when only bombs are left, outputs boolean in main class
	 */
	public boolean WinGame(int row, int col){
		for(int i = 0; i < m_size; i++){
			for(int j = 0; j < m_size; j++){
				if(m_selects[i][j] == 0){
					return false;
				}
			}
		}
		return true;
	}
	/*
	 * Is called at the end of game (if game is lost) displays bombs as Qs and all selected 
	 * positions
	 */
	public void EndGameBoard(){
		System.out.print("  ");

		for(int i = 0; i < m_size; i++){
			System.out.print("|" + (i+1) );

		}
		System.out.print("|");
		System.out.println();
		System.out.println("-------------------");

		for(int i = 0; i < m_size; i++){
			System.out.print((i+1 + " "));
			for(int j = 0; j < m_size; j++){
				if(m_board[i][j] == 9){
					System.out.print("|Q");
				}
				else if(m_selects[i][j] == 0){
					System.out.print("|-");
				}
				else if(m_selects[i][j] == 10){
					System.out.print("| ");
				}
				else if(m_board[i][j] == 0){
					System.out.print("|-");
				}
				else{
					System.out.print("|" + m_board[i][j]);
				}
			}
			System.out.print("|");
			System.out.println();
		}

		System.out.print("-------------------");
		System.out.println();
	}

}
