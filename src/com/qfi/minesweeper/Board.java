package com.qfi.minesweeper;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Board
{
	private int m_rowSize = 0;
	private int m_colSize = 0;
	private Random m_random = null;
	private int[][] m_board = null;// = new int[8][8];//main m_boarday
	private int[][] m_selects = null;// = new int[8][8];//m_boarday of chosen positions
	private static final int BOMB_FLAG = 9;
	private static final String SEPARATOR = "--";

	public Board(int rowSize, int colSize)
	{
		m_rowSize = rowSize;
		m_colSize = colSize;
		m_random = new Random();
		m_board = new int[m_rowSize][m_colSize];
		m_selects = new int[m_rowSize][m_colSize];

		placeBombs();
		bombCount();
	}

	/*
	 * Places ten "bombs" in both m_boardays randomly sets values for bombs in each m_boarday
	 */
	private void placeBombs()
	{
		int hold, count = 0;

		while(count < 10){
			for(int i = 0; i < m_rowSize; i++){
				for(int j = 0; j < m_colSize; j++){
					hold = m_random.nextInt(25) + 1;
					if(hold == BOMB_FLAG && count < 10){
						m_board[i][j] = 9;
						m_selects[i][j] = 11;
						count++;
					}

				}
			}
		}

	}
	
	/*
	 * Counts bombs surrounding ith position throughout m_boarday
	 */
	private void bombCount()
	{
		int count = 0;

		for (int row = 0; row < m_rowSize; row++)
		{
			for (int col = 0; col < m_colSize; col++)
			{
				count = 0;

				if (m_board[row][col] == 9)
				{
					continue;
				}

				int startRow = (row - 1 < 0) ? row : row - 1;
				int startCol = (col - 1 < 0) ? col : col - 1;
				int endRow = (row + 1 == m_rowSize) ? row : row + 1;
				int endCol = (col + 1 == m_colSize) ? col : col + 1;

				// Iterate through each possible row neighbor
				for (int rowNum = startRow; rowNum <= endRow; rowNum++)
				{
					// Iterate through each possible column neighbor
					for (int colNum = startCol; colNum <= endCol; colNum++)
					{
						if (m_board[rowNum][colNum] == 9)
						{
							count++;
						}
					}
				}

				m_board[row][col] = count;
			}
		}

	}

	/*
	 * Determines what to do when player chooses a specific position
	 */
	public void submitMove(int row, int col)
	{
		if(m_board[row][col] != 9 && m_board[row][col] != 0)
		{
			update(row, col);
		}
		else{
			spread(row, col);
			update(row, col);
		}

	}

	/*
	 * Updates board to user , outlines all possibilities that could be 
	 * shown throughout game
	 */
	private void update(int row, int col)
	{
		if (m_board[row][col] != 0)
		{
			m_selects[row][col] = m_board[row][col];
		}
		else
		{
			m_selects[row][col] = 10;
		}
	}

	/*
	 * Algorithm for if a spread is called on position, checks 8 surrounding positions to
	 * one called on
	 */
	private void spread(int row, int col)
	{
		List<String> spreads = new ArrayList<>();

		if (m_board[row][col] == 0 && m_selects[row][col] != 10)
		{
			int startRow = (row - 1 < 0) ? row : row - 1;
			int startCol = (col - 1 < 0) ? col : col - 1;
			int endRow = (row + 1 == m_rowSize) ? row : row + 1;
			int endCol = (col + 1 == m_colSize) ? col : col + 1;

			// Iterate through each possible row neighbor
			for (int rowNum = startRow; rowNum <= endRow; rowNum++)
			{
				// Iterate through each possible column neighbor
				for (int colNum = startCol; colNum <= endCol; colNum++)
				{
					// Only perform actions if current queried position is not a bomb
					if (m_board[rowNum][colNum] != 9)
					{
						// If a position is found that is not a spread block, add to selects array
						if (m_board[rowNum][colNum] != 0)
						{
							m_selects[rowNum][colNum] = m_board[rowNum][colNum];
						}

						// If the current spread block is found, tag in select array
						if (rowNum == row && colNum == col)
						{
							if (m_board[rowNum][colNum] == 0)
							{
								m_selects[rowNum][colNum] = 10;
							}
						}
						else
						{
							// Add additional spread blocks into list for additional processing
							if (m_board[rowNum][colNum] == 0)
							{
								spreads.add(rowNum + " " + colNum);
							}
						}
					}
				}
			}

			// Iterate over each spread position found & continue fanning out
			for (String pos : spreads)
			{
				String[] rowCol = pos.split("\\s+");
				int r = Integer.parseInt(rowCol[0]);
				int c = Integer.parseInt(rowCol[1]);
				spread(r, c);
			}
		}
	}
	
	/*
	 * If game is lost, outputs boolean in main class  
	 */
	public boolean lostGame(int row, int col)
	{
		return m_board[row][col] == 9;
	}
	
	/*
	 * If is true when only bombs are left, outputs boolean in main class
	 */
	public boolean wonGame()
	{
		for (int i = 0; i < m_rowSize; i++)
		{
			for (int j = 0; j < m_colSize; j++)
			{
				if (m_selects[i][j] == 0)
				{
					return false;
				}
			}
		}

		return true;
	}

	/*
	 * This method was made to check bomb counts on cells and used to test game
	 */
	public void printBoard()
	{
		System.out.print("  ");

		for(int i = 0; i < m_colSize; i++){
			System.out.print("|" + (i+1) );

		}
		System.out.print("|");
		System.out.println();
		System.out.println(SEPARATOR.repeat(m_colSize + (m_colSize / 4)));

		for (int i = 0; i < m_rowSize; i++)
		{
			System.out.print((i+1 + " "));
			for (int j = 0; j < m_colSize; j++)
			{
				if (m_selects[i][j] == 10)
				{
					System.out.print("| ");
				}
				else if (m_selects[i][j] != 0)
				{
					System.out.print("|" + m_selects[i][j]);
				}
				else
				{
					System.out.print("| ");
				}
			}
			System.out.print("|");
			System.out.println();

		}


		System.out.print(SEPARATOR.repeat(m_colSize + (m_colSize / 4)));
		System.out.println();
	}

	/*
	 * Is called at the end of game (if game is lost) displays bombs as Qs and all selected
	 * positions
	 */
	public void printEndBoard(){
		System.out.print("  ");

		for(int i = 0; i < m_colSize; i++){
			System.out.print("|" + (i+1) );

		}
		System.out.print("|");
		System.out.println();
		System.out.println(SEPARATOR.repeat(m_colSize + (m_colSize / 4)));

		for(int i = 0; i < m_rowSize; i++){
			System.out.print((i+1 + " "));
			for(int j = 0; j < m_colSize; j++){
				if(m_board[i][j] == 9){
					System.out.print("|Q");
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

		System.out.print(SEPARATOR.repeat(m_colSize + (m_colSize / 4)));
		System.out.println();
	}

	public int[][] getBoardArray()
	{
		return m_board;
	}

	public int[][] getSelectionArray()
	{
		return m_selects;
	}
}
