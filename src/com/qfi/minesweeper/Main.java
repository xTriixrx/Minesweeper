package com.qfi.minesweeper;

import java.util.Scanner;
import java.text.DecimalFormat;

public class Main
{
	public static void main(String[] args)
	{
		int maxSize = 9;
		int row = 0, col = 0;
		boolean rowCheck = true, colCheck = true, Gameplay = true, firsttime = true;
		long StartTime = 0, EndTime = 0;

		Scanner scan = new Scanner(System.in);

		Board b = new Board(maxSize, maxSize);
		b.printBoard();

		do
		{
			System.out.print("Input row: ");
			while(rowCheck){
				try{
					row = scan.nextInt();
					row = row - 1;
					if(row <= -1 || row > (maxSize - 1)){
						rowCheck = true;
						System.out.print("This value is not in the range of the game try again");
						System.out.println();
					}
					else{
					rowCheck = false;
					}
				}catch(Exception e){
					System.out.print("This value is not in the range of the game try again");
				}
			}

			System.out.print("Input column: ");
			while(colCheck)
			{
				try
				{
					col = scan.nextInt();
					col = col - 1;
					if(col <= -1 || col > (maxSize - 1)){
						colCheck = true;
						System.out.print("This value is not in the range of the game try again");
						System.out.println();
					}
					else{
					colCheck = false;
					}
				}catch(Exception e){
					System.out.print("This value is not in the range of the game try again");
				}
			}
			if (firsttime){
			StartTime = System.nanoTime();
			firsttime = false;
			}

			b.submitMove(row, col);
			b.printBoard();

			rowCheck = true;
			colCheck = true;

			if (b.lostGame(row, col))
			{
				DecimalFormat df = new DecimalFormat("###.##");
				EndTime = System.nanoTime();
				Gameplay = false;
				double seconds = (EndTime-StartTime) / 1000000000.0;
				int minutes = (int) (seconds / 60);
				seconds = seconds - (minutes * 60);
				b.printEndBoard();
				System.out.println("Your time was: " + minutes + " Minute(s), " +  df.format(seconds) + " second(s).");
				System.out.println("Sorry, you lost");
			}
			if (b.wonGame())
			{
				DecimalFormat df = new DecimalFormat("###.##");
				EndTime = System.nanoTime();
				Gameplay = false;
				double seconds = (EndTime-StartTime) / 1000000000.0;
				int minutes = (int) (seconds / 60);
				seconds = seconds - (minutes * 60);
				System.out.println("Congratulations, you won!");
				System.out.println("Your time was: " + minutes + " Minute(s), " +  df.format(seconds) + " second(s).");
			}
		}while(Gameplay);
	}

}
