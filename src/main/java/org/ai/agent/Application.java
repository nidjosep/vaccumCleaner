package org.ai.agent;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);
    private static final Pattern INPUT_PATTERN = Pattern.compile("\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+\\.?\\d*)\\s*,\\s*(\\d+\\.?\\d*)\\s*");

    public static void main(String[] args) {
        int totalTimeSteps = 100000;
        int totalCost = 0;

        int R, cS, cM = 8;
        double p, q;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter (R, cS, cM, p, q) [in comma-separated form] : ");
        String input = scanner.nextLine();

        Matcher matcher = INPUT_PATTERN.matcher(input);
        if (matcher.matches()) {
            R = Integer.parseInt(matcher.group(1).trim());
            cS = Integer.parseInt(matcher.group(2).trim());
            cM = Integer.parseInt(matcher.group(3).trim());
            p = Double.parseDouble(matcher.group(4).trim());
            q = Double.parseDouble(matcher.group(5).trim());
        } else {
            logger.error("Invalid input {}. Program exiting.", input);
            System.out.println("Invalid Input. Program exiting.");
            return;
        }

        System.out.printf("\nRunning with (R, cS, cM, p, q) => (%d, %d, %d, %.1f, %.1f)\n", R, cS, cM, p, q);

        Square squareA = new Square("A");
        Square squareB = new Square("B");
        VacuumCleaner vacuumCleaner = new VacuumCleaner(new Square[]{squareA, squareB});

        for (int index = 1; index <= totalTimeSteps; index++) {
            logger.info("*********** ITERATION {} ***********", index);
            initSquaresWithProbability(p, squareA, squareB);
            Square currentSquare = vacuumCleaner.getCurrentSquare();
            logger.info("Current Vacuum Cleaner location: {}", currentSquare.getName());
            if (currentSquare.isDirty()) {
                currentSquare.suckUpTheDirt();
                totalCost += cS;
                logger.info("The square {} has been cleaned", vacuumCleaner.getCurrentSquare().getName());
            } else {
                if (changeSquareWithProbability(q, currentSquare, vacuumCleaner)) {
                    totalCost += cM;
                }
            }
            int rewardPoints = 0;
            if (squareA.isClean()) {
                rewardPoints++;
            }
            if (squareB.isClean()) {
                rewardPoints++;
            }
            vacuumCleaner.addRewardPoints(R * rewardPoints);
            logger.info("The reward points earned in the current iteration: {}, Total reward points earned: {}\n", rewardPoints, vacuumCleaner.getRewardPoints());
        }
        double totalScore = vacuumCleaner.getRewardPoints() - totalCost;

        String averageCost = String.format("%.3f", totalScore / totalTimeSteps);

        logger.info("**********************\nTotal Reward Points = {}\nTotal Cost = {}\nAverage Cost = {}\n**********************", vacuumCleaner.getRewardPoints(), totalCost, averageCost);
        System.out.printf("\n**********************\nTotal Reward Points = %d\nTotal Cost = %d\n", vacuumCleaner.getRewardPoints(), totalCost);
        System.out.printf("The average score earned by the vacuum cleaner agent per time-step is %s\n**********************\n\n", averageCost);
    }

    private static boolean changeSquareWithProbability(double probability, Square currentSquare, VacuumCleaner vacuumCleaner) {
        boolean toBeChanged = Math.random() < probability;
        if (toBeChanged) {
            vacuumCleaner.changeSquare();
            logger.info("The vacuum cleaner has been moved from the square {} to the square {}", currentSquare.getName(), vacuumCleaner.getCurrentSquare().getName());
            return true;
        } else {
            logger.info("The vacuum cleaner will remain in square {}", currentSquare.getName());
        }
        return false;
    }

    private static void initSquaresWithProbability(double probability, Square squareA, Square squareB) {
        initSquare(squareA, probability);
        initSquare(squareB, probability);
    }

    private static void initSquare(Square square, double probability) {
        if (square.isDirty()) {
            logger.info("The square {} has been identified dirty", square.getName());
        } else {
            logger.info("The square {} has been identified clean", square.getName());
            double randomChance = Math.random();
            boolean toBeDirtied = randomChance < probability;
            logger.info("Whether the square {} will be dirtied in this iteration [p = {}, r = {}] ? {}", square.getName(), probability, randomChance, toBeDirtied ? "Yes" : "No");
            if (toBeDirtied) {
                square.makeDirty();
                logger.info("The square {} has been marked dirty", square.getName());
            }
        }
    }
}
