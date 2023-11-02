package com.company;

import java.util.List;
import java.util.Arrays;

public class FirstFit {
    public static int[][] placeSquares(int maxMeret, List<Integer> guideSequence) throws Except.Tulkicsi {
        int currentGuideIndex = 0;
        int meretHelp = maxMeret;
        int hivnum = 1;
        double opt = 0;
        int optSeg = maxMeret;
        while (true) {
            opt = opt + (optSeg * optSeg);
            optSeg--;
            if (optSeg == 0) {
                break;
            }
        }
        double optV = Math.sqrt(opt);
        int optV2 = (int) Math.round(optV);
        int[][] solution;

        while (true) {
            //tömblétrehozás
            meretHelp = maxMeret;
            solution = new int[optV2 + hivnum - 1][optV2 + hivnum - 1];

            //elemetetés
            while (currentGuideIndex < guideSequence.size()) {

                //elbánás a tömbbel case-ek szerint
                int currentGuide = guideSequence.get(currentGuideIndex); // Change this line
                solution=matrixTransform(currentGuide, solution);
                //helykeresés
                int[] position = finder(solution, meretHelp);
                if (position != null) {
                    //ha van hely beírja
                    solution = placer(solution, meretHelp, position[0], position[1]);
                    currentGuideIndex++;
                    meretHelp--;
                    if (meretHelp == 0) {
                        return solution;
                    }
                } else {
                    //ha nem akkor ugrik és növeli a méretet
                    break;
                }
            }

            currentGuideIndex = 0;
            hivnum++;
        }
    }

    private static int[][] matrixTransform(int currentGuide, int[][] solution) {
        int numRows = solution.length;
        int numCols = solution[0].length;

        if (currentGuide >= 1 && currentGuide <= 8) {
            switch (currentGuide) {
                case 1:
                    // Case 1: No transformation, return the original matrix
                    return solution;

                case 2:
                    // Case 2: Mirror sideways
                    int[][] mirrorSideways = new int[numRows][numCols];
                    for (int i = 0; i < numRows; i++) {
                        for (int j = 0; j < numCols; j++) {
                            mirrorSideways[i][j] = solution[i][numCols - 1 - j];
                        }
                    }
                    return mirrorSideways;

                case 3:
                    // Case 3: Mirror lengthways
                    int[][] mirrorLengthways = new int[numRows][numCols];
                    for (int i = 0; i < numRows; i++) {
                        for (int j = 0; j < numCols; j++) {
                            mirrorLengthways[i][j] = solution[numRows - 1 - i][j];
                        }
                    }
                    return mirrorLengthways;

                case 4:
                    // Case 4: Transpose (swap rows and columns)
                    int[][] transpose = new int[numCols][numRows];
                    for (int i = 0; i < numRows; i++) {
                        for (int j = 0; j < numCols; j++) {
                            transpose[j][i] = solution[i][j];
                        }
                    }
                    return transpose;

                case 5:
                    // Case 5: Mirror sideways and lengthways
                    int[][] mirrorSidewaysLengthways = matrixTransform(2, matrixTransform(3, solution));
                    return mirrorSidewaysLengthways;

                case 6:
                    // Case 6: Mirror sideways and transpose
                    int[][] mirrorSidewaysTranspose = matrixTransform(2, matrixTransform(4, solution));
                    return mirrorSidewaysTranspose;

                case 7:
                    // Case 7: Mirror lengthways and transpose
                    int[][] mirrorLengthwaysTranspose = matrixTransform(3, matrixTransform(4, solution));
                    return mirrorLengthwaysTranspose;

                case 8:
                    // Case 8: Mirror sideways, lengthways, and transpose
                    int[][] mirrorAll = matrixTransform(2, matrixTransform(3, matrixTransform(4, solution)));
                    return mirrorAll;

                default:
                    // Invalid case, return the original matrix
                    return solution;
            }
        } else {
            // Invalid input, return the original matrix
            return solution;
        }
    }


    private static int[] finder(int[][] solution, int meret) {
        int n = 0;
        int m = 0;
        int[] place;

        while (n < solution.length) {
            while (m < solution[n].length) {
                // Check if the cell is not empty
                if (solution[n][m] != 0) {
                    m++;
                } else {
                    // Check if adjacent cells are not empty
                    if (m + meret <= solution[n].length && n + meret <= solution.length) {
                        boolean adjacentCellsNotEmpty = false;
                        for (int i = n; i < n + meret && !adjacentCellsNotEmpty; i++) {
                            for (int j = m; j < m + meret; j++) {
                                if (solution[i][j] != 0) {
                                    adjacentCellsNotEmpty = true;
                                    break;
                                }
                            }
                        }

                        if (!adjacentCellsNotEmpty) {
                            return place = new int[]{n, m};
                        }
                    }
                    m++;
                }
            }
            n++;
            m = 0;
        }

        return null;
    }

    private static int[][] placer(int[][] solution, int meret, int n, int m) {
        for (int i = n; i < n + meret; i++) {
            for (int j = m; j < m + meret; j++) {
                solution[i][j] = meret;
            }
        }
        return solution;
    }
}