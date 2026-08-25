package squarepacking.algorithm;


import squarepacking.algorithm.*;
import squarepacking.ui.*;

import java.util.List;

public class SpiralOptimized {
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

    private static int[] findFirstZeroPosition(int[][] matrix, int integer) {
        int n = matrix.length;
        int top = 0;
        int bottom = n - 1;
        int left = 0;
        int right = n - 1;

        while (top <= bottom && left <= right) {
            // top row (left to right)
            for (int i = left; i <= right; ) {
                int val = matrix[top][i];
                if (val == 0) {
                    int conflict = findConflictLeftToRight(matrix, top, i, integer);
                    if (conflict == -1) return new int[]{top, i};
                    i = conflict + 1;
                } else {
                    boolean isNumberChange = (i > left) && (val != matrix[top][i - 1]);
                    if (isNumberChange) i += val;
                    else i++;
                }
            }
            top++;

            // right column (top to bottom)
            for (int i = top; i <= bottom; ) {
                int val = matrix[i][right];
                if (val == 0) {
                    int conflict = findConflictTopToBottom(matrix, i, right, integer);
                    if (conflict == -1) return new int[]{i, right - integer + 1};
                    i = conflict + 1;
                } else {
                    boolean isNumberChange = (i > top) && (val != matrix[i - 1][right]);
                    if (isNumberChange) i += val;
                    else i++;
                }
            }
            right--;

            // bottom row (right to left)
            for (int i = right; i >= left; ) {
                int val = matrix[bottom][i];
                if (val == 0) {
                    int conflict = findConflictRightToLeft(matrix, bottom, i, integer);
                    if (conflict == -1) return new int[]{bottom - integer + 1, i - integer + 1};
                    i = conflict - 1; // FIXED: jump to conflict - 1
                } else {
                    boolean isNumberChange = (i < right) && (val != matrix[bottom][i + 1]);
                    if (isNumberChange) i -= val;
                    else i--;
                }
            }
            bottom--;

            // left column (bottom to top)
            for (int i = bottom; i >= top; ) {
                int val = matrix[i][left];
                if (val == 0) {
                    int conflict = findConflictBottomToTop(matrix, i, left, integer);
                    if (conflict == -1) return new int[]{i - integer + 1, left};
                    i = conflict - 1; // FIXED: jump to conflict - 1
                } else {
                    boolean isNumberChange = (i < bottom) && (val != matrix[i + 1][left]);
                    if (isNumberChange) i -= val;
                    else i--;
                }
            }
            left++;
        }

        return null;
    }

    private static int findConflictLeftToRight(int[][] matrix, int row, int col, int size) {
        if (row + size > matrix.length || col + size > matrix[0].length) return col;
        // Scan columns left to right to short-circuit early if conflict is near the start
        for (int c = col; c < col + size; c++) {
            for (int r = row; r < row + size; r++) {
                if (matrix[r][c] != 0) return c;
            }
        }
        return -1;
    }

    private static int findConflictTopToBottom(int[][] matrix, int row, int col, int size) {
        int startCol = col - size + 1;
        if (row + size > matrix.length || startCol < 0) return row;
        // Scan rows top to bottom to short-circuit early
        for (int r = row; r < row + size; r++) {
            for (int c = startCol; c <= col; c++) {
                if (matrix[r][c] != 0) return r;
            }
        }
        return -1;
    }

    private static int findConflictRightToLeft(int[][] matrix, int row, int col, int size) {
        int startRow = row - size + 1;
        int startCol = col - size + 1;
        if (startRow < 0 || startCol < 0) return col;
        // Scan columns right to left to short-circuit early
        for (int c = col; c >= startCol; c--) {
            for (int r = startRow; r <= row; r++) {
                if (matrix[r][c] != 0) return c;
            }
        }
        return -1;
    }

    private static int findConflictBottomToTop(int[][] matrix, int row, int col, int size) {
        int startRow = row - size + 1;
        if (startRow < 0 || col + size > matrix[0].length) return row;
        // Scan rows bottom to top to short-circuit early
        for (int r = row; r >= startRow; r--) {
            for (int c = col; c < col + size; c++) {
                if (matrix[r][c] != 0) return r;
            }
        }
        return -1;
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
                //ezt kell majd Ä‚Ë‡tvinni
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

