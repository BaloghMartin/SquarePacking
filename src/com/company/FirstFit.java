package com.company;

import java.util.List;

public class FirstFit {
    private static int[][] finder(int[][] solution, int meret, int N, int M, int hivnum, List<Integer> guideSequence) throws Except.Tulkicsi {
        int currentGuideIndex = 0;
        int n = N;
        int m = M;

        while (currentGuideIndex < guideSequence.size()) {
            int currentGuide = guideSequence.get(hivnum - 1);

            if (currentGuide == 1) {
                int sor = 0;
                while (sor + meret <= solution.length - 1) {
                    n = sor;
                    m = 0;
                    while (n + meret <= solution.length) {
                        int[][] result = finderHelper(solution, meret, n, m);
                        if (result != null) {
                            return result;
                        }
                        n++;
                    }
                    sor++;
                }
            }

            throw new Except.Tulkicsi("nem jo a sorba");
        }

        return solution;
    }

    private static int[][] finderHelper(int[][] solution, int meret, int N, int M) {
        int n = N;
        int m = M;

        while (true) {
            if (isAvailable(solution, n, m, meret)) {
                return writer(solution, n, m, meret);
            } else {
                m += 1;
            }
        }
    }

    private static boolean isAvailable(int[][] solution, int n, int m, int meret) {
        for (int i = n; i < n + meret; i++) {
            for (int j = m; j < m + meret; j++) {
                if (i >= solution.length || j >= solution[0].length || solution[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[][] writer(int[][] solution, int n, int m, int meret) {
        for (int i = n; i < n + meret; i++) {
            for (int j = m; j < m + meret; j++) {
                solution[i][j] = meret;
            }
        }
        return solution;
    }
}
