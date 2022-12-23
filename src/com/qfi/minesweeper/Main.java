package com.qfi.minesweeper;

import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * Main class which runs a command line instance of the Minesweeper game.
 */
public class Main
{
	private static final int MAX_SIZE = 9;
	private static final int BOMB_COUNT = 10;

	/**
	 * The main run method.
	 *
	 * @param args - Command line arguments.
	 */
	public static void main(String[] args)
	{
		int row;
		int col;
		long startTime = 0;
		boolean gameplay = true;
		boolean firsttime = true;

		Scanner scan = new Scanner(System.in);

		Board b = new Board(MAX_SIZE, MAX_SIZE, BOMB_COUNT);
		b.printBoard();

		do
		{
			System.out.print("Input row: ");
			row = selectionChecker(scan);

			System.out.print("Input column: ");
			col = selectionChecker(scan);

			if (firsttime)
			{
				startTime = System.nanoTime();
				firsttime = false;
			}

			b.submitMove(row, col);
			b.printBoard();

			if (b.lostGame(row, col))
			{
				gameplay = false;
				b.printEndBoard();
				System.out.println("Sorry, you lost.");
				calculateEndTime(startTime);
			}
			if (b.wonGame())
			{
				gameplay = false;
				System.out.println("Congratulations, you won!");
				calculateEndTime(startTime);
			}
		} while(gameplay);
	}

	/**
	 * Attempts to get some valid integer selection from the user & returns that value.
	 *
	 * @param scan - A reference to a Scanner object to receive input from user.
	 * @return int - An valid integer between 0 and MAX_SIZE.
	 */
	private static int selectionChecker(Scanner scan)
	{
		int value = 0;
		boolean check = true;

		while (check)
		{
			value = getSelection(scan);
			value--;

			if (value <= -1 || value > (MAX_SIZE - 1))
			{
				System.out.println("This value is not in the range of the game try again");
				continue;
			}

			check = false;
		}

		return value;
	}

	/**
	 * Uses the provided scanner to receive some input as an integer.
	 *
	 * @param scan - A reference to a Scanner object to receive input from user.
	 * @return int - An integer received from the user.
	 */
	private static int getSelection(Scanner scan)
	{
		int value = -99;

		try
		{
			value = scan.nextInt();
		}
		catch (Exception e)
		{
			System.out.println("This value is not in range, please try again.");
		}

		return value;
	}

	/**
	 * Calculates the total time taken to complete the game.
	 * 
	 * @param startTime - The start time of the game.
	 */
	private static void calculateEndTime(long startTime)
	{
		DecimalFormat df = new DecimalFormat("###.##");
		long endTime = System.nanoTime();
		double seconds = (endTime - startTime) / 1000000000.0;
		int minutes = (int) (seconds / 60);
		seconds = seconds - (minutes * 60);
		System.out.println("Your time was: " + minutes + " Minute(s), " +  df.format(seconds) + " second(s).");
	}
}
