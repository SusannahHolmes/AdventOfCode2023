package com.fdmgroup.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Advent2 {

	private static final int MAX_RED = 12;
	private static final int MAX_GREEN = 13;
	private static final int MAX_BLUE = 14;

	public static void main(String[] args) {
		int sumOfCorrectGames = 0;
		int productSum = 0;
		try {
			File file = new File("src/com/fdmgroup/resources/Advent2.txt");
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				boolean allColorsWithinLimits = true;

				// Initialize maxColorValues to track maximum values for each color
				Map<String, Integer> maxColorValues = new HashMap<>();
				maxColorValues.put("red", Integer.MIN_VALUE);
				maxColorValues.put("green", Integer.MIN_VALUE);
				maxColorValues.put("blue", Integer.MIN_VALUE);

				String[] parts = line.split("[,;:]");
				for (String part : parts) {
					part = part.trim();
					String[] numberAndColor = part.split(" ");
					if (numberAndColor.length == 2) {
						try {
							int number = Integer.parseInt(numberAndColor[0]);
							String color = numberAndColor[1];

							// Update the maximum value for the color
							maxColorValues.put(color, Math.max(maxColorValues.get(color), number));

							if (!isWithinLimit(color, number)) {
								allColorsWithinLimits = false;
							}
						} catch (NumberFormatException e) {
							System.out.println("Invalid number format in part: " + part);
						}
					}
				}

				// Calculate and print the product of the maximum values for each color
				int maxRed = maxColorValues.get("red");
				int maxGreen = maxColorValues.get("green");
				int maxBlue = maxColorValues.get("blue");
				int product = maxRed * maxGreen * maxBlue;
				productSum += product;
				System.out.println("Game " + extractGameNumber(line) + " - Maximum values: Red: " + maxRed + 
				                   ", Green: " + maxGreen + ", Blue: " + maxBlue);
				System.out.println("Product of maximum values: " + product);

				if (allColorsWithinLimits) {
					String gameString = extractGameNumber(line);
					System.out.println("Game " + gameString + " matches the criteria.");
					int gameInteger = Integer.parseInt(gameString);
					sumOfCorrectGames += gameInteger;
				}
			}
			System.out.println("Sum of prodcuts of all games: " + productSum);
			scanner.close();
			System.out.println("Total sum of correct game numbers: " + sumOfCorrectGames);
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static boolean isWithinLimit(String color, int number) {
		switch (color) {
			case "red":
				return number <= MAX_RED;
			case "green":
				return number <= MAX_GREEN;
			case "blue":
				return number <= MAX_BLUE;
			default:
				return false;
		}
	}

	private static String extractGameNumber(String line) {
	    String[] parts = line.split(":");
	    for (String part : parts) {
	        String[] subParts = part.split("\\s+");
	        if (subParts.length > 1) {
	            return subParts[1].trim();
	        }
	    }
	    return "didn't work";
	}
}
