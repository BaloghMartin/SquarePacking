package com.company;

import java.util.List;

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
            //System.out.println("Entering the outer loop");
            //tömblétrehozás
            meretHelp = maxMeret;
            solution = new int[optV2 + hivnum - 1][optV2 + hivnum - 1];

            //elemetetés
            while (currentGuideIndex < guideSequence.size()) {
                System.out.println("Entering the inner loop");

                //elbánás a tömbbel case-ek szerint
                int currentGuide = guideSequence.get(currentGuideIndex); // Change this line
                //helykeresés
                int[] position = finder(solution, meretHelp);
                if (position != null) {
                    //ha van hely beírja
                    solution = placer(solution, meretHelp, position[0], position[1]);
                    System.out.println("Placed square of size " + meretHelp + " at position (" + position[0] + ", " + position[1] + ")");
                    currentGuideIndex++;
                    meretHelp--;
                    if (meretHelp == 0) {
                        return solution;
                    }
                } else {
                    //ha nem akkor ugrik és növeli a méretet
                    System.out.println("No suitable place found. Increasing size.");
                    break;
                }
            }

            currentGuideIndex=0;
            hivnum++;
        }
    }

    private static int[] finder(int[][] solution, int meret) {
        int n = 0;
        int m = 0;
        int[] place;
        int sor = 0;

        while (true) {
            System.out.println("Entering the outer loop");
            //sorléptetés
            while (true) {
                System.out.println("Entering the row loop");

                //oszlopléptetés
                //vizsgált hely nem jó
                if (solution[n][m] != 0) {
                    System.out.println("Cell not empty: (" + n + ", " + m + ")");
                    m += solution[n][m];
                    if (m + meret > solution.length) {
                        System.out.println("m + meret >= solution.length");
                        break;
                    }
                }

                if (solution[n][m] == 0) {
                    System.out.println("Cell is empty: (" + n + ", " + m + ")");
                    //a többi négy sarok nem jó
                    if (m + meret >= solution.length) {
                        System.out.println("m + meret >= solution.length");
                        break;
                    }
                    if (solution[n + meret][m + meret] != 0 || solution[n][m + meret] != 0 || solution[n + meret][m] != 0) {
                        System.out.println("Adjacent cells not empty");
                        m++;
                        if (m + meret > solution.length) {
                            System.out.println("m + meret >= solution.length");
                            break;
                        }
                    }

                    //jó a hely
                    else {
                        System.out.println("Good place found: (" + n + ", " + m + ")");
                        return place = new int[]{n, m};
                    }
                }
            }
            n++;
            if (n + meret > solution.length) {
                System.out.println("n + meret >= solution.length");
                break;
            }
        }

        System.out.println("Exiting the finder function");
        return null;
    }


    private static int[][] placer(int[][] solution, int meret, int n, int m) {
        for (int i = n; i < n + meret; i++) {
            for (int j = m; j < m + meret; j++) {
                solution[i][j] = meret;
                System.out.println("Placing square of size " + meret + " at position (" + i + ", " + j + ")");
            }
        }
        System.out.println("Exiting the placer function");
        return solution;
    }}
