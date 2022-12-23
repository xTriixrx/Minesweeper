package com.qfi.minesweeper;

import java.util.Scanner;
import java.text.DecimalFormat;

/**
 *
 */
public class Main
{
	private static final int MAX_SIZE = 9;
	private static final int BOMB_COUNT = 10;

	public static void main(String[] args)
	{
		int row;
		int col;
		boolean gameplay = true;
		boolean firsttime = true;
		long startTime = 0, endTime = 0;

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
				DecimalFormat df = new DecimalFormat("###.##");
				endTime = System.nanoTime();
				gameplay = false;
				double seconds = (endTime-startTime) / 1000000000.0;
				int minutes = (int) (seconds / 60);
				seconds = seconds - (minutes * 60);
				b.printEndBoard();
				System.out.println("Your time was: " + minutes + " Minute(s), " +  df.format(seconds) + " second(s).");
				System.out.println("Sorry, you lost");
			}
			if (b.wonGame())
			{
				DecimalFormat df = new DecimalFormat("###.##");
				endTime = System.nanoTime();
				gameplay = false;
				double seconds = (endTime-startTime) / 1000000000.0;
				int minutes = (int) (seconds / 60);
				seconds = seconds - (minutes * 60);
				System.out.println("Congratulations, you won!");
				System.out.println("Your time was: " + minutes + " Minute(s), " +  df.format(seconds) + " second(s).");
			}
		} while(gameplay);
	}

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
}
