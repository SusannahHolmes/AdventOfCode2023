package com.fdmgroup.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class Advent4 {

	public static void main(String[] args) {
		double totalScore  = 0d;
		try {
			File file = new File("src/com/fdmgroup/resources/Advent4.txt");
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] splitNumbers = line.split("[:|]");
				
				String[] winningNumbersString = splitNumbers[1].trim().split("\\s+");
				int[] winningNumbers = new int[winningNumbersString.length];
				
				String[] yourNumbersString = splitNumbers[2].trim().split("\\s+");
				int[] yourNumbers = new int[yourNumbersString.length];
				
				int wIndex = 0;
				for(String winningNumberString: winningNumbersString) {
					winningNumbers[wIndex++] = Integer.parseInt(winningNumberString);
				}
				int yIndex = 0;
				for(String yourNumberString: yourNumbersString) {
					yourNumbers[yIndex++] = Integer.parseInt(yourNumberString);
				}
				
				// Convert the first array to a HashSet
		        HashSet<Integer> set = new HashSet<>();
		        for (int num : yourNumbers) {
		            set.add(num);
		        }

		        // Count the common elements
		        int commonCount = 0;
		        for (int num : winningNumbers) {
		            if (set.contains(num)) {
		                commonCount++;
		            }
		        }
		        
		        double score = 0;
		        if (commonCount > 0) {
		            score = Math.pow(2, commonCount - 1);
		            totalScore += score;
		        }

		        System.out.println("Number of winning numbers: " + commonCount + " Score: " + score);
			}
			System.out.println("Total score: " + totalScore);
			
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}
}
