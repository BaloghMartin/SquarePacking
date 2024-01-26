package com.company;

import java.util.List;

public class Spiral {
    public static double placeSquares(List<Integer> guideSequence) {
        int currentGuideIndex = 0;
        int meretHelp = guideSequence.size();
        int hivnum = 1;

        int optV2 = (int) Math.sqrt(guideSequence.size() * (guideSequence.size() + 1) * (2 * guideSequence.size() + 1) / 6);

        int[][] solution;

        while (true) {
            solution = new int[optV2 + hivnum - 1][optV2 + hivnum - 1];

            while (currentGuideIndex <= guideSequence.size()) {
                meretHelp = guideSequence.get(currentGuideIndex);
                int[] position = findFirstZeroPosition(solution, meretHelp);
                if (position != null) {
                    solution = placer(solution, meretHelp, position[0], position[1]);
                    currentGuideIndex++;

                    if (currentGuideIndex >= guideSequence.size()) {
                        //printSolution(solution);
                        return (double) solution.length / optV2;
                    }
                } else {
                    break;
                }
            }

            currentGuideIndex = 0;
            hivnum++;
        }
    }

    static int[][] placer(int[][] solution, int meret, int n, int m) {
        for (int i = n; i < n + meret; i++) {
            for (int j = m; j < m + meret; j++) {
                solution[i][j] = meret;
            }
        }
        return solution;
    }

    public static int[] findFirstZeroPosition(int[][] matrix, int integer) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return null;
        }
        int top = 0;
        int bottom = matrix.length - integer;
        int left = 0;
        int right = matrix[0].length - integer;

        while (top <= bottom && left <= right) {
            for (int i = left; i <= right; i++) {
                if (matrix[top][i] == 0 && checkSquareSubarray(matrix, top, i, integer)) {
                    return new int[]{top, i};
                }
            }
            top++;

            for (int i = top; i <= bottom; i++) {
                if (matrix[i][right] == 0 && checkSquareSubarray(matrix, i, right, integer)) {
                    return new int[]{i, right};
                }
            }
            right--;

            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    if (matrix[bottom][i] == 0 && checkSquareSubarray(matrix, bottom, i, integer)) {
                        return new int[]{bottom, i};
                    }
                }
                bottom--;
            }

            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    if (matrix[i][left] == 0 && checkSquareSubarray(matrix, i, left, integer)) {
                        return new int[]{i, left};
                    }
                }
                left++;
            }
        }

        return null;
    }

    private static boolean checkSquareSubarray(int[][] matrix, int row, int col, int integer) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        if (row + integer > rows || col + integer > cols) {
            return false;
        }
        for (int i = row; i < row + integer; i++) {
            for (int j = col; j < col + integer; j++) {
                if (matrix[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printSolution(int[][] solution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[0].length; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[][] placeSquaresAndReturnArray(List<Integer> guideSequence) {
        int currentGuideIndex = 0;
        int meretHelp = guideSequence.size();
        int hivnum = 1;
        int optV2 = (int) Math.ceil(Math.sqrt(guideSequence.size() * (guideSequence.size() + 1) * (2 * guideSequence.size() + 1) / 6));
        int[][] solution;

        while (true) {
            solution = new int[optV2 + hivnum - 1][optV2 + hivnum - 1];

            while (currentGuideIndex <= guideSequence.size()) {
                meretHelp = guideSequence.get(currentGuideIndex);
                int[] position = findFirstZeroPosition(solution, meretHelp);
                if (position != null) {
                    solution = placer(solution, meretHelp, position[0], position[1]);
                    currentGuideIndex++;

                    if (currentGuideIndex >= guideSequence.size()) {
                        //printSolution(solution);
                        return solution;
                    }
                } else {
                    break;
                }
            }

            currentGuideIndex = 0;
            hivnum++;
        }
    }

    public static int placeSquaresAndReturnSize(List<Integer> guideSequence,int N, int MAX) {
        int currentGuideIndex = 0;
        int meretHelp = N;
        int hivnum = 1;
        int optV2 = (int) Math.ceil(Math.sqrt(guideSequence.size() * (guideSequence.size() + 1) * (2 * guideSequence.size() + 1) / 6));
        int[][] solution;

        while (true) {
            solution = new int[optV2 + hivnum - 1][optV2 + hivnum - 1];

            if((optV2 + hivnum - 1)>=MAX){
                //
                return solution.length+1;}

            while (currentGuideIndex <= guideSequence.size()) {
                meretHelp = guideSequence.get(currentGuideIndex);
                //ezt kell majd átvinni
                int[] position = findFirstZeroPosition(solution, meretHelp);
                if (position != null) {
                    //meg ezt
                    solution = placer(solution, meretHelp, position[0], position[1]);
                    currentGuideIndex++;

                    if (currentGuideIndex >= guideSequence.size()) {
                        //printSolution(solution);
                        return solution.length;
                    }
                } else {
                    break;
                }
            }

            currentGuideIndex = 0;
            hivnum++;
        }
    }


    public static double placeSquaresAndReturnFitness(List<Integer> guideSequence) {
        double fitness=1;
        int currentGuideIndex = 0;
        int meretHelp = guideSequence.size();
        int hivnum = 1;
        int optV2 = (int) Math.sqrt(guideSequence.size() * (guideSequence.size() + 1) * (2 * guideSequence.size() + 1) / 6);
        int[][] solution;

        while (true) {
            solution = new int[optV2 + hivnum - 1][optV2 + hivnum - 1];

            while (currentGuideIndex <= guideSequence.size()) {
                meretHelp = guideSequence.get(currentGuideIndex);
                int[] position = findFirstZeroPosition(solution, meretHelp);
                if (position != null) {
                    solution = placer(solution, meretHelp, position[0], position[1]);
                    currentGuideIndex++;

                    if (currentGuideIndex >= guideSequence.size()) {
                        //fitness=fitness*zeroRatio(solution);
                        //printSolution(solution);
                        return fitness;
                    }
                } else {
                    fitness=fitness*zeroRatio(solution);
                    break;
                }
            }

            currentGuideIndex = 0;
            hivnum++;
        }
    }

    private static double zeroRatio(int[][] solution) {
        int totalCells = solution.length * solution[0].length; // Calculate the total number of cells in the 2D array
        int zeroCount = 0; // Initialize a counter for zeros

        // Iterate through the 2D array to count zeros
        for (int row = 0; row < solution.length; row++) {
            for (int col = 0; col < solution[0].length; col++) {
                if (solution[row][col] == 0) {
                    zeroCount++;
                }
            }
        }

        // Calculate the zero ratio (number of zeros / total number of cells)
        double ratio = (double)   totalCells/zeroCount;

        return ratio;
    }
}
