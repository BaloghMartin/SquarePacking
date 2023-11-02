package com.company;

import java.util.List;

public class FirstFit {
    public static int[][] placeSquares(int maxMeret, List<Integer> guideSequence) throws Except.Tulkicsi {
        int currentGuideIndex = 0;
        int meretHelp=maxMeret;
        int hivnum = 0;
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

        while(true){
            //tömblétrehozás
            meretHelp=maxMeret;
            solution = new int[optV2+hivnum-1][optV2+hivnum-1];

                //elemetetés
                while (currentGuideIndex < guideSequence.size()) {
                    //elbánás a tömbbel case-ek szerint
                    int currentGuide = guideSequence.get(hivnum - 1);
                    //helykeresés
                    int[] position = finder(solution, meretHelp);
                    if (position != null) {
                        //ha van hely beírja
                        solution = placer(solution, meretHelp, position[0], position[1]);
                        currentGuideIndex++;
                        meretHelp--;
                        if (meretHelp==0){return solution;}
                    } else {
                        //ha nem akkor ugrik és növeli a méretet
                        break;
                    }
                }
                hivnum++;


        }

    }

    private static int[] finder(int[][] solution, int meret) {
        int n = 0;
        int m = 0;
        int[] place;
        int sor = 0;
        /*while (sor + meret <= solution.length - 1) {
            n = sor;
            m = 0;
            while (n + meret <= solution.length) {
                boolean canPlace = true;
                for (int i = n; i < n + meret; i++) {
                    for (int j = m; j < m + meret; j++) {
                        if (i >= solution.length || j >= solution[0].length || solution[i][j] != 0) {
                            canPlace = false;
                            break;
                        }
                    }
                    if (!canPlace) {
                        break;
                    }
                }
                if (canPlace) {
                    return new int[]{n, m};
                }
                n++;
            }
            sor++;
        }
*/
        while(true){
            //sorléptetés
            while(true){
                //oszlopléptetés
                //vizsgált hely nem jó
                if (solution[n][m]!=0){
                    m+=solution[n][m];
                }
                if (solution[n][m]==0){
                    //a többi négy sarok nem jó
                    if (solution[n+meret][m+meret]!=0 || solution[n][m+meret]!=0 || solution[n+meret][m]!=0){
                        m++;
                    }
                    //jó a hely
                    else return place= new int[]{n, m};
                }
                if (m>=solution.length){break;}

            }
            n++;
            if (n>= solution.length){break;}
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
