package com.fdmgroup.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Advent1 {

    public static void main(String[] args) {
        int totalSum = 0;

        try {
            File file = new File("src/com/fdmgroup/resources/Advent1.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
               System.out.println("Line: " + line);
                
                // Convert words to numbers
                line = convertWordToNumber(line);
                System.out.println("Converted Line: " + line);
                
                // Find the first digit
                int firstDigit = findFirstDigit(line);
                // Find the last digit
                int lastDigit = findLastDigit(line);
                // Combine the digits
                int combinedNumber = combineDigits(firstDigit, lastDigit);

                // Add the combined number to the total sum
                totalSum += combinedNumber;

                
                System.out.println("First Digit: " + firstDigit);
                System.out.println("Last Digit: " + lastDigit);
                System.out.println("Combined Number: " + combinedNumber);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Print the total sum of all combined numbers
        System.out.println("Total Sum of Combined Numbers: " + totalSum);
    }

    private static int findFirstDigit(String line) {
        for (char ch : line.toCharArray()) {
            if (Character.isDigit(ch)) {
                return Character.getNumericValue(ch);
            }
        }
        return 0;
    }

    private static int findLastDigit(String line) {
        for (int i = line.length() - 1; i >= 0; i--) {
            if (Character.isDigit(line.charAt(i))) {
                return Character.getNumericValue(line.charAt(i));
            }
        }
        return 0;
    }

    private static int combineDigits(int firstDigit, int lastDigit) {
        String combinedString = String.valueOf(firstDigit) + String.valueOf(lastDigit);
        return Integer.parseInt(combinedString);
    }
    
    private static String convertWordToNumber(String line) {
        HashMap<String, String> numberMap = new HashMap<>();
        numberMap.put("one", "o1e");
        numberMap.put("two", "t2o");
        numberMap.put("three", "t3e");
        numberMap.put("four", "f4r");
        numberMap.put("five", "f5e");
        numberMap.put("six", "s6x");
        numberMap.put("seven", "s7n");
        numberMap.put("eight", "e8t");
        numberMap.put("nine", "n9e");

        // Iterate through the map and replace words with numbers
        for (Map.Entry<String, String> entry : numberMap.entrySet()) {
            line = line.replace(entry.getKey(), entry.getValue());
        }

        return line;
    }
}
