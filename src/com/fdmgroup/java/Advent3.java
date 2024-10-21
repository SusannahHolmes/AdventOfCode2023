package com.fdmgroup.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Advent3 {

    public static void main(String[] args) {
        List<String[]> array = new ArrayList<>();

        try {
            File file = new File("src/com/fdmgroup/resources/Advent3.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] columns = parseLine(line);
                array.add(columns);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Add a row of "." at the start and end
        int colsCount = array.get(0).length;
        String[] emptyRow = new String[colsCount + 2];
        for (int i = 0; i < emptyRow.length; i++) {
            emptyRow[i] = ".";
        }
        array.add(0, emptyRow);
        array.add(emptyRow);

        // Add a column of "." at the start and end for each row
        for (int i = 1; i < array.size() - 1; i++) {
            String[] newRow = new String[colsCount + 2];
            newRow[0] = ".";
            newRow[newRow.length - 1] = ".";
            System.arraycopy(array.get(i), 0, newRow, 1, colsCount);
            array.set(i, newRow);
        }

        // Print the 2D array
        for (String[] row : array) {
            for (String col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
        System.out.println("Array size: " + array.size() + " rows, " + array.get(0).length + " columns");

        // Define special characters
        String specialChars = "@#$%&*+-/=";

        // Find and sum the part numbers
        int sumOfPartNumbers = getSumOfPartNumbers(array, specialChars);
        System.out.println("Sum of part numbers: " + sumOfPartNumbers);
        
        // Get combined numbers for * only
        List<String[]> combinedNumbers = getCombinedNumbers(array, "*");
        Map<String, List<Integer>> locationToNumbersMap = new HashMap<>();

        // Populate the map with combined numbers and their special character locations
        for (String[] combinedNumberWithLocation : combinedNumbers) {
            String location = combinedNumberWithLocation[1];
            int number = Integer.parseInt(combinedNumberWithLocation[0]);
            locationToNumbersMap.computeIfAbsent(location, k -> new ArrayList<>()).add(number);
        }

        // Filter out locations that occur only once and multiply numbers at the same location
        int sumOfStarProducts = 0;
        for (Map.Entry<String, List<Integer>> entry : locationToNumbersMap.entrySet()) {
            List<Integer> numbers = entry.getValue();
            if (numbers.size() > 1) {
                int product = numbers.stream().reduce(1, (a, b) -> a * b);
                System.out.println("Location: " + entry.getKey() + ", Product of combined numbers: " + product);
                sumOfStarProducts += product;
            }
        }
        System.out.println("PartTwoAnswer: " + sumOfStarProducts);
    }

    private static String[] parseLine(String line) {
        List<String> entries = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d|\\.|[^\\d\\s.]");
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            entries.add(matcher.group());
        }

        return entries.toArray(new String[0]);
    }

    private static int getSumOfPartNumbers(List<String[]> array, String specialChars) {
        int sum = 0;
        int rowsCount = array.size();
        int colsCount = array.get(0).length;

        for (int x = 0; x < rowsCount; x++) {
            for (int y = 0; y < colsCount; y++) {
                if (isNumber(array.get(x)[y])) {
                    // Combine consecutive digits
                    StringBuilder numberBuilder = new StringBuilder(array.get(x)[y]);
                    int tempY = y + 1;
                    while (tempY < colsCount && isNumber(array.get(x)[tempY])) {
                        numberBuilder.append(array.get(x)[tempY]);
                        tempY++;
                    }
                    String combinedNumber = numberBuilder.toString();
                    int startY = y; // Store the start position
                    y = tempY - 1; // Adjust y to skip the combined digits

                    // Check for special characters around each position in the combined number
                    boolean hasSpecialChar = false;
                    for (int i = 0; i < combinedNumber.length(); i++) {
                        if (hasSpecialCharAround(array, x, startY + i, specialChars)) {
                            hasSpecialChar = true;
                            break;
                        }
                    }

                    if (hasSpecialChar) {
                        System.out.println("Summing number: " + combinedNumber);
                        sum += Integer.parseInt(combinedNumber);
                    }
                }
            }
        }
        return sum;
    }

    private static List<String[]> getCombinedNumbers(List<String[]> array, String specialChars) {
        List<String[]> combinedNumbersWithLocations = new ArrayList<>();
        int rowsCount = array.size();
        int colsCount = array.get(0).length;

        for (int x = 0; x < rowsCount; x++) {
            for (int y = 0; y < colsCount; y++) {
                if (isNumber(array.get(x)[y])) {
                    // Combine consecutive digits
                    StringBuilder numberBuilder = new StringBuilder(array.get(x)[y]);
                    int tempY = y + 1;
                    while (tempY < colsCount && isNumber(array.get(x)[tempY])) {
                        numberBuilder.append(array.get(x)[tempY]);
                        tempY++;
                    }
                    String combinedNumber = numberBuilder.toString();
                    int startY = y; // Store the start position
                    y = tempY - 1; // Adjust y to skip the combined digits

                    // Check for special characters around each position in the combined number
                    boolean hasSpecialChar = false;
                    int specialCharX = -1;
                    int specialCharY = -1;
                    for (int i = 0; i < combinedNumber.length(); i++) {
                        int[] specialCharLocation = getSpecialCharLocation(array, x, startY + i, specialChars);
                        if (specialCharLocation != null) {
                            hasSpecialChar = true;
                            specialCharX = specialCharLocation[0];
                            specialCharY = specialCharLocation[1];
                            break;
                        }
                    }

                    if (hasSpecialChar) {
                        combinedNumbersWithLocations.add(new String[]{combinedNumber, "(" + specialCharX + ", " + specialCharY + ")"});
                    }
                }
            }
        }
        return combinedNumbersWithLocations;
    }

    private static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int[] getSpecialCharLocation(List<String[]> array, int x, int y, String specialChars) {
        int rowsCount = array.size();
        int colsCount = array.get(0).length;

        int[][] directions = {
            {0, 1}, {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}, {1, 0}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            // Skip if out of bounds
            if (newX < 0 || newX >= rowsCount || newY < 0 || newY >= colsCount) {
                continue;
            }

            if (specialChars.contains(array.get(newX)[newY])) {
                return new int[]{newX, newY};
            }
        }
        return null;
    }

    private static boolean hasSpecialCharAround(List<String[]> array, int x, int y, String specialChars) {
        return getSpecialCharLocation(array, x, y, specialChars) != null;
    }
}
