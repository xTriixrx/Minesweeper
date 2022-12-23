package com.qfi.minesweeper;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

/**
 * The Board object wraps the board management & updating functionality from the associated program. This board
 * can be used to support different output mechanisms, but has been enhanced to support a JavaFx GUI variation. The
 * initial implementation was written for a command line operation for a college project.
 *
 * @author Vincent.Nigro
 * @version 0.0.1
 */
public class Board
{
	private final int m_rowSize;
	private final int m_colSize;
	private final int m_bombCount;
	private final Random m_random;
	private final int[][] m_board; // main board
	private final int[][] m_selects; // board of chosen positions
	private static final int BOMB_FLAG = 9;
	private static final String SEPARATOR = "--";
	private static final String SPACE_REGEX = "\\s+";
	private static final int UNSELECTED_BOMB_FLAG = 11;
	private static final int SELECTED_EMPTY_POSITION = 10;
	private static final Logger m_logger = LogManager.getLogger(Board.class);

	/**
	 * Board constructor.
	 *
	 * @param rowSize - The row size of the board to be instantiated & managed.
	 * @param colSize - The column size of the board to be instantiated & managed.
	 * @param bombCount - The total bombs to be instantiated for the board.
	 */
	public Board(int rowSize, int colSize, int bombCount)
	{
		m_rowSize = rowSize;
		m_colSize = colSize;
		m_bombCount = bombCount;

		m_random = new Random();
		m_board = new int[m_rowSize][m_colSize];
		m_selects = new int[m_rowSize][m_colSize];

		// Populate bombs & generate neighboring bomb counts for each position
		placeBombs();
		countNeighborBombs();
	}

	/**
	 * Places bombs within the board & selects array up to what bombCount is set to based on
	 * the Board object's instantiation.
	 */
	private void placeBombs()
	{
		int count = 0;

		// While placed bombs is less than expected bomb count for this board
		while (count < m_bombCount)
		{
			int randRow = m_random.nextInt(m_rowSize);
			int randCol = m_random.nextInt(m_colSize);

			// If random row/col position hasn't already placed a bomb, place the bomb
			if (m_board[randRow][randCol] != BOMB_FLAG)
			{
				m_board[randRow][randCol] = BOMB_FLAG;
				m_selects[randRow][randCol] = UNSELECTED_BOMB_FLAG;
				count++;

				m_logger.trace("Placed bomb at position: (" + randRow + "," + randCol + ").");
			}
		}

		m_logger.debug("Bombs have been placed onto the board.");
	}

	/**
	 * This method is called during the instantiation of the Board object in order to count each positions
	 * neighboring bomb count such that it can be used throughout the gameplay.
	 */
	private void countNeighborBombs()
	{
		// Iterate through each position, & check surrounding neighbors for bombs
		for (int row = 0; row < m_rowSize; row++)
		{
			for (int col = 0; col < m_colSize; col++)
			{
				// If position is a bomb, it can't have a count
				if (m_board[row][col] == BOMB_FLAG)
				{
					continue;
				}

				int neighboringBombs = neighborBombCount(row, col);
				m_board[row][col] =  neighboringBombs;
				m_logger.trace("Position " + "(" + row + "," + col + ") has " + neighboringBombs +
					" total neighboring bombs.");
			}
		}
	}

	/**
	 * This method will check a position's neighbors in order to get the count of bombs that the position
	 * is surrounded by. The value of the neighboring bombs will be returned.
	 *
	 * @param row - The associated row of the corresponding position.
	 * @param col - The associated column of the corresponding position.
	 * @return int - An integer representing number of neighboring bombs for queried position.
	 */
	private int neighborBombCount(int row, int col)
	{
		int count = 0;

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
				if (m_board[rowNum][colNum] == BOMB_FLAG)
				{
					count++;
					m_logger.trace("Position " + "(" + row + "," + col + ") has a neighboring bomb at position: "
						+ "(" + rowNum + "," + colNum + ").");
				}
			}
		}

		return count;
	}

	/**
	 * The public interface to submit a move to the board controller. Once a position has been
	 * submitted, the board will be updated accordingly. The external controller can query the
	 * board controller for game status & if needed, can get read access to the associated internal
	 * board & selection arrays to update external graphical displays.
	 *
	 * @param row - The associated row of the corresponding position.
	 * @param col - The associated column of the corresponding position.
	 */
	public void submitMove(int row, int col)
	{
		// If the selected position is not an empty position, just update the selection.
		if (m_board[row][col] != BOMB_FLAG && m_board[row][col] != 0)
		{
			update(row, col);
		}
		else // If an empty position, perform the spread algorithm and then update.
		{
			spread(row, col);
			update(row, col);
		}
	}

	/**
	 * An internal update method which will update the selections map for the selected position.
	 *
	 * @param row - The associated row of the corresponding position.
	 * @param col - The associated column of the corresponding position.
	 */
	private void update(int row, int col)
	{
		// If position is not empty, enter board representation into selections
		if (m_board[row][col] != 0)
		{
			m_selects[row][col] = m_board[row][col];
		}
		else // If empty, enter a selected empty position representation flag into selections
		{
			m_selects[row][col] = SELECTED_EMPTY_POSITION;
		}
	}

	/**
	 * A recursive algorithm for if a spread is called on position, checks 8 surrounding positions to
	 * perform spread & 'select' all positions up to the edges of the spread.
	 *
	 * @param row - The starting position's associated row.
	 * @param col - The starting position's associated column.
	 */
	private void spread(int row, int col)
	{
		// If position is empty but wasn't selected, perform spread
		if (m_board[row][col] == 0 && m_selects[row][col] != SELECTED_EMPTY_POSITION)
		{
			// Get list of additional spreads
			List<String> spreads = spreadProtocol(row, col);

			// Iterate over each spread position found & continue fanning out
			for (String pos : spreads)
			{
				String[] rowCol = pos.split(SPACE_REGEX);
				int posRow = Integer.parseInt(rowCol[0]);
				int posCol = Integer.parseInt(rowCol[1]);
				spread(posRow, posCol);
			}
		}
	}

	/**
	 * Performs a spread protocol for a given position, checking each neighbor and selecting all
	 * positions until an edge has been hit. If another empty position is found as a neighbor, it
	 * is added to the spreads list and is used later to perform a spread operation.
	 *
	 * @param row - The starting spread position's associated row.
	 * @param col - The starting spread position's associated column.
	 * @return {@code List<String>} - A list of other positions that need to iterated on a spread.
	 */
	private List<String> spreadProtocol(int row, int col)
	{
		List<String> spreads = new ArrayList<>();

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
				spreads.add(spreadCalculation(row, col, rowNum, colNum));
			}
		}

		// Removes all empty strings from list
		spreads.removeAll(List.of(""));

		return spreads;
	}

	/**
	 * Performs a spread calculation for a single neighbor based on the starting position.
	 *
	 * @param row - The starting spread position's associated row.
	 * @param col - The starting spread position's associated column.
	 * @param neighborRow - The neighboring position's associated row.
	 * @param neighborCol - The neighboring position's associated column.
	 * @return String - A string which may contain another position that needs a spread performed.
	 */
	private String spreadCalculation(int row, int col, int neighborRow, int neighborCol)
	{
		String spread = "";

		// Only perform actions if current queried position is not a bomb
		if (m_board[neighborRow][neighborCol] != BOMB_FLAG)
		{
			// If a position is found that is not a spread block, add to selects array
			if (m_board[neighborRow][neighborCol] != 0)
			{
				m_selects[neighborRow][neighborCol] = m_board[neighborRow][neighborCol];
			}

			// If the current spread block is found, tag in select array
			if (neighborRow == row && neighborCol == col)
			{
				if (m_board[neighborRow][neighborCol] == 0)
				{
					m_selects[neighborRow][neighborCol] = SELECTED_EMPTY_POSITION;
				}
			}
			else
			{
				// Add additional spread blocks into list for additional processing
				if (m_board[neighborRow][neighborCol] == 0)
				{
					spread = neighborRow + " " + neighborCol;
				}
			}
		}

		return spread;
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
				if (m_selects[i][j] == SELECTED_EMPTY_POSITION)
				{
					System.out.print("| ");
				}
				else if (m_selects[i][j] == UNSELECTED_BOMB_FLAG)
				{
					System.out.print("|-");
				}
				else if (m_selects[i][j] == BOMB_FLAG)
				{
					System.out.print("|Q");
				}
				else if (m_selects[i][j] != 0)
				{
					System.out.print("|" + m_selects[i][j]);
				}
				else
				{
					System.out.print("|-");
				}
			}
			System.out.print("|");
			System.out.println();

		}

		System.out.print(SEPARATOR.repeat(m_colSize + (m_colSize / 4)));
		System.out.println();
	}

	/**
	 *
	 */
	public void printEndBoard()
	{
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
				if(m_board[i][j] == BOMB_FLAG){
					System.out.print("|Q");
				}
				else if(m_board[i][j] == 0){
					System.out.print("| ");
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

	/**
	 * A boolish method to determine if the game has been won based on if there are no more
	 * remaining selections that need to be selected.
	 *
	 * @return boolean - A boolean representing whether the game has been won.
	 */
	public boolean wonGame()
	{
		for (int i = 0; i < m_rowSize; i++)
		{
			for (int j = 0; j < m_colSize; j++)
			{
				// If a selection doesn't exist, then the game is not finished.
				if (m_selects[i][j] == 0)
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * A boolish method to determine if the game is lost based on some row/col position. If the
	 * position contains a bomb, then the game has been lost.
	 *
	 * @return boolean - A boolean representing whether the game has been lost.
	 */
	public boolean lostGame(int row, int col)
	{
		return m_board[row][col] == BOMB_FLAG;
	}

	/**
	 * Returns a reference to the board array for read access only. Should only be used for
	 * applications where the game is being displayed other than the command line.
	 *
	 * @return int[][] - A reference to the board array to be used for read access only.
	 */
	public int[][] getBoardArray()
	{
		return m_board;
	}

	/**
	 * Returns a reference to the selections array for read access only. Should only be used for
	 * applications where the game is being displayed other than the command line.
	 *
	 * @return int[][] - A reference to the selections array to be used for read access only.
	 */
	public int[][] getSelectionArray()
	{
		return m_selects;
	}
}
