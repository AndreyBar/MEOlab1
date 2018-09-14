package com.kpi;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        PrettyPrinter printer = new PrettyPrinter(System.out);
        Integer altQty = getQuantity("Enter quantity of alternatives: ", s);
        Integer expQty = getQuantity("Enter quantity of experts: ", s);
        Integer system = getQuantity("Enter point system: ", s);
        String[][] data = new String[expQty + 2][altQty + 2];

        // Write alternatives to data
        String[] altNames = getNames("Please, enter the alternative names:", altQty, s);
        data[0][0] = null;
        if (altQty - 1 >= 0) System.arraycopy(altNames, 0, data[0], 1, altQty - 1);

        // Write headers
        data[0][data[0].length - 1] = "SUM";
        data[data.length - 1][0] = "NORMALIZED";

        // Write experts to data
        String[] expNames = getNames("Please, enter experts names:", expQty, s);
        for (int i = 0; i < expQty; i++) {
            data[i + 1][0] = expNames[i];
        }

        // Write points of each expert
        setPoints(data, system, s);

        // Calculate and write sum for each expert
        System.out.println("--------------------");
        System.out.println("Calculating sums for each expert");
        calculateAndSetSums(data);

        // Calculate normalized estimates
        System.out.println("--------------------");
        System.out.println("Calculating normalized estimates of comparative selectivity of alternatives");
        calculateAndSetNormalizedEstimates(data);

        System.out.println("\n\t\tFINAL TABLE");
        printer.print(data);
        //System.out.println(Arrays.deepToString(data));


    }

    private static Integer getQuantity(String message, Scanner scanner) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Please, enter the number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static Integer getQuantity(String message, Scanner scanner, Integer pointSystem) {
        System.out.print(message);
        String input = scanner.next();
        while (!isInteger(input) || (isInteger(input) && Integer.parseInt(input) > pointSystem)) {
            System.out.print(String.format("Please, enter the number less than %d: ", pointSystem));
            input = scanner.next();
        }
        return Integer.parseInt(input);
    }

    private static String[] getNames(String message, Integer length, Scanner scanner) {
        System.out.println(message);
        String[] arr = new String[length];
        for (int i = 0; i < length; i++) {
            arr[i] = scanner.next();
        }
        return arr;
    }

    private static void setPoints(String[][] target, Integer pointSystem, Scanner s) {
        for (int i = 1; i < target.length - 1; i++) {
            System.out.println(String.format("Please enter points of %s expert", target[i][0]));
            for (int j = 1; j < target[0].length - 1; j++) {
                Integer point = getQuantity("", s, pointSystem);
                target[i][j] = point.toString();
            }
        }
    }

    private static void calculateAndSetNormalizedEstimates(String[][] input) {
        for (int i = 1; i < input[0].length - 1; i++) {
            System.out.print(String.format("Calculating normalized estimate for %s alternative: ", input[0][i]));
            float normalizedEstimate = 0;
            for (int j = 1; j < input.length - 1; j++) {
                float forExpert = Float.parseFloat(input[j][i]) / Float.parseFloat(input[j][input[0].length - 1]);
                normalizedEstimate += forExpert;
            }
            normalizedEstimate /= input.length - 2;
            input[input.length - 1][i] = Float.toString(normalizedEstimate);
            System.out.println(normalizedEstimate);
        }
    }

    private static void calculateAndSetSums(String[][] input) {
        for (int i = 1; i < input.length - 1; i++) {
            System.out.print(String.format("Calculating sum for expert \"%s\": ", input[i][0]));
            int sum = 0;
            for (int j = 1; j < input[0].length - 1; j++) {
                sum += Integer.parseInt(input[i][j]);
            }
            System.out.println(sum);
            input[i][input[0].length - 1] = Integer.toString(sum);
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(Exception e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
